package org.techtown.dahan_ble;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.gatt.callback.BleCallback;
import com.ficat.easyble.gatt.callback.BleConnectCallback;
import com.ficat.easyble.gatt.callback.BleNotifyCallback;
import com.ficat.easyble.gatt.callback.BleWriteCallback;
import com.ficat.easyble.scan.BleScanCallback;


import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static FragmentManager fm;
    private static ItemAdapter adapter;
    private ListView listView;
    private List<BleDevice> deviceList = new ArrayList<>();

    ProgressDialog pd;


    // 어플리케이션 설정파일 관련 (_default 가 붙은 값은 초기화시 초기값으로 이용)
    private SharedPreferences appData;

    public static String mode;  // 메인화면 테마 설정(0 = 디지털 모드, 1 = 아날로그 모드)
    public static String mode_default = "0"; //초기값(초기화시 이용)
    public static String autoCompressure;   // 컴프레셔 자동동작(0 = 자동동작 x, 1 = 자동동작 o)
    public static String autoCompressure_default = "0"; //초기값(초기화시 이용)
    public static String flow1;    // 유량계 오동작 알람설정(0 = off, 1 = on, 뒤에 숫자는 주기[초])
    public static String flow1_default = "1,10";    //초기값(초기화시 이용)
    public static String flow2;    // 유량계 최소값 모니터링(0 = off, 1 = on, 뒤에 숫자는 주기[초])
    public static String flow2_default = "1,1"; //초기값(초기화시 이용)
    public static String flow3;    // 유량계 최댓값 모니터링(0 = off, 1 = on, 뒤에 숫자는 주기[초])
    public static String flow3_default = "1,60";    //초기값(초기화시 이용)
    public static String cleanPower; // 세척강도
    public static String cleanPower2; // 세척강도
    public static String cleanPower3; // 세척강도
    public static String cleanPower4; // 세척강도
    public static String cleanPower_default = "0.5";    //초기값(초기화시 이용)
    public static String cleanPower2_default = "1.0";    //초기값(초기화시 이용)
    public static String cleanPower3_default = "2.0";    //초기값(초기화시 이용)
    public static String cleanPower4_default = "3.0";    //초기값(초기화시 이용)
    public static String radioButtoncheck = "0";

    public static boolean flowresetflag = false; //누적시간 리셋요청여부
    public static int resetcount = 10;
    public static boolean comp_control = false; //컴프레셔 제어
    public static boolean wash_control = false; //배관세척 제어
    public static boolean actionflag = false;
    private String connectedDeviceMacAddress;

    BleManager bleManager;

    static ConnectFragment connectFragment;
    static SettingFragment settingFragment;
    static MainFragment mainFragment;

    Dialog dialog;

    ImageView btn_NewDeviceSearch, bluetoothstate1 ;
    TextView tv_connect, tv_flow, tv_comp_state, tv_washstate, tv_washpower, tv_acctime, tv_resetcheck;
    TextView tv_runningtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get BLE manager and initialization
        BleManager.ScanOptions scanOptions = BleManager.ScanOptions
                .newInstance()
                .scanPeriod(3000)
                .scanDeviceName(null);

        BleManager.ConnectOptions connectOptions = BleManager.ConnectOptions
                .newInstance()
                .connectTimeout(12000);

        bleManager = BleManager
                .getInstance()
                .setScanOptions(scanOptions)//it is not necessary
                .setConnectionOptions(connectOptions)//like scan options
                .setLog(true, "TAG")
                .init(this.getApplication());//Context is needed here,do not use Activity,which can cause Activity leak


        //프래그먼트 인플레이트
        connectFragment = (ConnectFragment)getSupportFragmentManager().findFragmentById(R.id.connectFragment);
        //connectFragment = new ConnectFragment();
        //settingFragment = (SettingFragment)getSupportFragmentManager().findFragmentById(R.id.settingFragment);
        settingFragment = new SettingFragment();
        //mainFragment = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        mainFragment = new MainFragment();






        /*상태바 제거*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //블루투스 항목 관련
        adapter = new ItemAdapter();


        //설정값 가져오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();


        //각종 위젯 초기화
        btn_NewDeviceSearch = findViewById(R.id.btn_NewDeviceSearch);
        tv_connect = findViewById(R.id.tv_connect);
        tv_flow = findViewById(R.id.tv_flow);
        tv_comp_state = findViewById(R.id.tv_comp_state);
        tv_washstate = findViewById(R.id.tv_washstate);
        tv_washpower = findViewById(R.id.tv_washpower);
        tv_acctime = findViewById(R.id.tv_acctime);
        tv_resetcheck = findViewById(R.id.tv_resetcheck);
        tv_runningtime = findViewById(R.id.tv_runningtime);


        // BLE connect Callback
        BleConnectCallback bleConnectCallback = new BleConnectCallback() {
            @Override
            public void onStart(boolean startConnectSuccess, String info, BleDevice device) {
                if (startConnectSuccess) {
                    //start to connect successfully
                } else {
                    //fail to start connection, see details from 'info'
                    String failReason = info;
                }
            }

            @Override
            public void onFailure(int failCode, String info, BleDevice device) {
                if(failCode == BleConnectCallback.FAIL_CONNECT_TIMEOUT){
                    //connection timeout
                }else{
                    //connection fail due to other reasons
                }

            }

            @Override
            public void onConnected(BleDevice device) {

                showToast("연결되었습니다.");
                onFragmentChanged(1);
                tv_connect.setText("true");

                if(pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                if(dialog.isShowing()){
                    dialog.dismiss();
                }


                // ※※※ UUID of HC-42: Search UUID: FFF0, service UUID: FFE0, transparent transmission data UUID: FFE1.
                bleManager.notify(device, "0000FFE0-0000-1000-8000-00805F9B34FB", "0000FFE1-0000-1000-8000-00805F9B34FB", new BleNotifyCallback() {
                    @Override
                    public void onCharacteristicChanged(byte[] data, BleDevice device) {
                        String Data = null;
                        try {
                            Data = new String(data, StandardCharsets.UTF_8);
                            Data.trim();
                            Log.d("Rece Data : ", Data);// Receive 데이터 확인

                            // 데이터 파싱 시작
                            String[] Ddata = Data.split("_");   // ex) Ddata[0] = "$--DAHAN-MCU", Ddata[1] = "29.40,1,1,1.50,15:30:28,0*25"
                            Ddata[1].trim();
                            String[] DDdata = Ddata[1].split("\\*");    // ex) DDdata[0] = "29.40,1,1,1.50,15:30:28,0", DDdata[1] = "25"
                            //Log.d("DDdata : ", DDdata[0]);
                            DDdata[0].trim();
                            DDdata[1] = DDdata[1].replaceAll("(\r\n|\r|\n|\n\r)", "");  // \r\n 또는 \n\r 또는 \n 또는 \r을 찾아 공백으로 치환
                            DDdata[1].trim();
                            //Log.d("DDdata : ", DDdata[1]);
                            int lenth = Integer.parseInt(DDdata[1]);

                            if(DDdata[0].length() == lenth){
                                //Log.d("판별 : ", "O 정상데이터");
                                String[] DDDdata = DDdata[0].split(",");    // ex) DDDdata[0]="29.40" DDDdata[1]="1" DDDdata[2]="1" DDDdata[3]="1.50" DDDdata[4]="15:30:28" DDDdata[5]="0"
                                double flow = Double.parseDouble(DDDdata[0]);

                                //일단 받은 데이터는 메인액티비티의 텍스트뷰형식으로 전부 취합해둠
                                //유량 데이터
                                tv_flow.setText(DDDdata[0]);
                                //컴프레셔전원상태
                                tv_comp_state.setText(DDDdata[1]);
                                //배관세척진행상태
                                tv_washstate.setText(DDDdata[2]);
                                //솔벨브온오프주기(배관세척강도)
                                tv_washpower.setText(DDDdata[3]);
                                //배관 누적시간
                                tv_acctime.setText(DDDdata[4]);
                                //누적시간 리셋확인
                                tv_resetcheck.setText(DDDdata[5]);
                                if(DDDdata[5].equals("1")) {
                                    flowresetflag = false;
                                    tv_runningtime.setText("00:00:00");
                                }



                            } else {
                                //Log.d("판별 : ", "X 비정상 데이터");
                            }


                            // 데이터를 재취합하여 보내보자
                            // 컴프레셔전원명령,배관세척명령,솔벨브온오프주기설정값,배관세척 동작시간, 누적시간리셋여부 순이다.
                            //String send_Data = "$--DAHAN-AND_1,1,0.5,35:12,1*15\r\n";
                            String send_Data = "$--DAHAN-AND_";


                            //컴프레셔 전원명령 체크
                            if(comp_control){
                                send_Data = send_Data + "1,";
                            }else {
                                send_Data = send_Data + "0,";
                            }
                            //배관 세척명령 체크
                            if(wash_control){
                                send_Data = send_Data + "1,";
                            }else {
                                send_Data = send_Data + "0,";
                            }
                            //솔벨브온오프주기설정값 체크(cleanPower)
                            if(radioButtoncheck.equals("0")) {
                                send_Data = send_Data + cleanPower + ",";
                            } else if(radioButtoncheck.equals("1")) {
                                send_Data = send_Data + cleanPower2 + ",";
                            } else if(radioButtoncheck.equals("2")) {
                                send_Data = send_Data + cleanPower3 + ",";
                            } else {
                                send_Data = send_Data + cleanPower4 + ",";
                            }

                            //배관세척 동작시간 추가
                            send_Data = send_Data + tv_runningtime.getText().toString() + ",";

                            //누적시간 리셋   셋팅프래그먼트에서 초기화 요청시에 flowresetflag가 true로 변한다.
                            if(flowresetflag) {
                                send_Data = send_Data + "1";
                            }else {
                                send_Data = send_Data + "0";
                            }

                            send_Data = send_Data + "\r\n";


                            byte[] send = send_Data.getBytes();

                            bleManager.write(device, "0000FFE0-0000-1000-8000-00805F9B34FB", "0000FFE1-0000-1000-8000-00805F9B34FB", send, new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(byte[] data, BleDevice device) {
                                    String temp = new String(data);
                                    Log.d("Send Data : " , temp);
                                }

                                @Override
                                public void onFailure(int failCode, String info, BleDevice device) {

                                }
                            });

                        } catch (Exception e) {
                            Log.d("Error : ", e.getMessage());
                        }

                    }

                    @Override
                    public void onNotifySuccess(String notifySuccessUuid, BleDevice device) {

                    }

                    @Override
                    public void onFailure(int failCode, String info, BleDevice device) {
                        switch (failCode) {
                            case BleCallback.FAIL_DISCONNECTED://connection has disconnected
                                break;
                            case BleCallback.FAIL_OTHER://other reason
                                break;
                            default:
                                break;
                        }

                    }
                });

            }

            @Override
            public void onDisconnected(String info, int status, BleDevice device) {
                showToast("연결이 해제되었습니다.");
                tv_connect.setText("false");
            }
        };

        tv_connect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if (tv_connect.getText().toString().equals("false")) {
                    pd = new ProgressDialog(MainActivity.this);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.show();
                    //showToast("1차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }

                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("2차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("3차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("4차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("5차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("6차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("7차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("8차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("9차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }
                if (tv_connect.getText().toString().equals("false")) {
                    //showToast("10차 시도");
                    bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                }

            }
        });




        // 새로운 기기 검색 버튼 클릭 이벤트(다이얼로그의 시작)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        btn_NewDeviceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //팝업창 띄우기
                dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.newdevice_connect_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                // 다이얼로그상의 위젯 초기화 findViewById 메서드 앞에 다이얼로그 객체(dialog.)을 꼭 붙여줄것! 안그럼 Null pointer exception 발생
                Button btn_dialog_refresh = dialog.findViewById(R.id.btn_dialog_refresh);
                Button btn_dialog_exit = dialog.findViewById(R.id.btn_dialog_exit);
                TextView tv_newdevicesearchstate = dialog.findViewById(R.id.tv_newdevicesearchstate);
                listView = dialog.findViewById(R.id.listView);

                bleManager.startScan(new BleScanCallback() {
                    @Override
                    public void onLeScan(BleDevice device, int rssi, byte[] scanRecord) {
                        for (BleDevice d : deviceList) {
                            if (device.address.equals(d.address)) {
                                return;
                            }
                        }
                        deviceList.add(device);
                        adapter.addItem(new Item(device.name, device.address));
                    }

                    @Override
                    public void onStart(boolean startScanSuccess, String info) {
                        if (startScanSuccess) {
                            //start scan successfully
                            tv_newdevicesearchstate.setText("제품을 찾는중입니다.");
                        } else {
                            //fail to start scan, you can see details from 'info'
                            String failReason = info;
                            tv_newdevicesearchstate.setText(failReason);
                        }
                    }

                    @Override
                    public void onFinish() {
                        listView.setAdapter(adapter);
                        tv_newdevicesearchstate.setText("제품을 선택해주세요.");
                        bleManager.stopScan();
                        //Toast.makeText(MainActivity.this, String.valueOf(adapter.getCount()), Toast.LENGTH_SHORT).show();
                    }
                });


                // 다이얼로그 새로고침 버튼 클릭 시 발생 이벤트
                btn_dialog_refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.setAdapter(null);

                        bleManager.startScan(new BleScanCallback() {
                            @Override
                            public void onLeScan(BleDevice device, int rssi, byte[] scanRecord) {
                                for (BleDevice d : deviceList) {
                                    if (device.address.equals(d.address)) {
                                        return;
                                    }
                                }
                                deviceList.add(device);
                                adapter.addItem(new Item(device.name, device.address));
                            }

                            @Override
                            public void onStart(boolean startScanSuccess, String info) {
                                if (startScanSuccess) {
                                    //start scan successfully
                                    tv_newdevicesearchstate.setText("제품을 찾는중입니다.");
                                } else {
                                    //fail to start scan, you can see details from 'info'
                                    String failReason = info;
                                    tv_newdevicesearchstate.setText(failReason);
                                }
                            }

                            @Override
                            public void onFinish() {
                                listView.setAdapter(adapter);
                                tv_newdevicesearchstate.setText("제품을 선택해주세요.");
                                bleManager.stopScan();
                                //Toast.makeText(MainActivity.this, String.valueOf(adapter.getCount()), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

                //  다이얼로그 블루투스 장비 선택 시 발생 이벤트
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        final Item item = (Item) adapter.getItem(position);

                        //Toast.makeText(getApplicationContext(), item.getName() + " " + item.getMac(), Toast.LENGTH_SHORT).show();

                        //다이얼로그 이벤트를 만들어줍니다.
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("블루투스 연결").setMessage(item.getName() + " 에 연결하시겠습니까?");



                        builder.setPositiveButton("연결", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int id) {
                                /*
                                //임시 가연결
                                onFragmentChanged(1);
                                dialog.dismiss();
                                */

                                //실제 연결
                                connectedDeviceMacAddress = item.getMac();
                                bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);

                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int id) {
                                //Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });



                btn_dialog_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                /*
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
                */
                dialog.show();
            }
        }); // (다이얼로그의 끝)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~








    }//=========================================================================================================================================

    public void showToast(String para) {
        Toast.makeText(getApplicationContext(), para, Toast.LENGTH_SHORT).show();
    }

    //설정파일 정장 함수
    public void save() {
        // SharedPreferences 객체만으로는 저장 불가능, Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입(저장시킬 이름, 저장시킬 값)
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putString("mode", mode);
        editor.putString("autoCompressure", autoCompressure);
        editor.putString("flow1", flow1);
        editor.putString("flow2", flow2);
        editor.putString("flow3", flow3);
        editor.putString("cleanPower", cleanPower);
        editor.putString("cleanPower2", cleanPower2);
        editor.putString("cleanPower3", cleanPower3);
        editor.putString("cleanPower4", cleanPower4);

        // apply, commit을 안하면 병경된 내용이 저장되지 않음
        editor.apply();
    }

    // 저장된 설정파일값 불러오는함수
    private void load() {
        // SHaredPreferences 객체.get타입(저장된 이름, 기본값)
        // 저장된 이름이 존재하지 않을 시 기본값

        mode = appData.getString("mode","0");  // 메인화면 테마 설정(0 = 디지털 모드, 1 = 아날로그 모드)
        autoCompressure = appData.getString("autoCompressure","0");   // 컴프레셔 자동동작(0 = 자동동작 x, 1 = 자동동작 o, default = 0)
        flow1 = appData.getString("flow1","1,10");    // 유량계 오동작 알람설정(0 = off, 1 = on, 뒤에 숫자는 주기[초])
        flow2 = appData.getString("flow2","1,1");    // 유량계 최소값 모니터링(0 = off, 1 = on, 뒤에 숫자는 주기[초])
        flow3 = appData.getString("flow3","1,60");    // 유량계 최댓값 모니터링(0 = off, 1 = on, 뒤에 숫자는 주기[초])
        cleanPower = appData.getString("cleanPower","0.5");   // 세척강도
        cleanPower2 = appData.getString("cleanPower2","1.0");   // 세척강도2
        cleanPower3 = appData.getString("cleanPower3","2.0");   // 세척강도3
        cleanPower4 = appData.getString("cleanPower4","3.0");   // 세척강도3


    }

    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, connectFragment).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
        }
    }

    class ItemAdapter extends BaseAdapter {
        //데이터가 들어가있지 않고, 어떻게 담을지만 정의해둠
        ArrayList<Item> items = new ArrayList<Item>();

        @Override
        public int getCount() { return items.size(); }

        public void addItem(Item item) { items.add(item);}

        @Override
        public Object getItem(int position) { return items.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        //어댑터가 데이터를 관리하고 뷰도 만듬
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemView itemView = null;
            if(convertView == null) {
                itemView = new ItemView(getApplicationContext());
            }else {
                itemView = (ItemView)convertView;
            }
            Item item = items.get(position);
            itemView.setName(item.getName());
            itemView.setMacadd(item.getMac());

            return itemView;
        }
    }

    public void ShowTimeMethod(String actionTime) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                SimpleDateFormat simple = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String time = simple.format(date);

                try {
                    Date ActionStartTime = simple.parse(actionTime);
                    Date CurrentTime = simple.parse(time);

                    // 한국시간은 9시간 보정을 받기때문에 9시간을 밀리세크로 계산해서 빼준다.
                    long seconds = (CurrentTime.getTime()-ActionStartTime.getTime()) - (long)(32400000);

                    String runtime = (String) simple.format(new Timestamp(seconds));

                    tv_runningtime.setText(runtime);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(actionflag) {
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                    handler.sendEmptyMessage(1); //핸들러 호출 = 시간 갱신
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void onBackPressed() {
        // 백버튼 기능 제거
    }
}