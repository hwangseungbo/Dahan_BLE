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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

    // ?????????????????? ???????????? ?????? (_default ??? ?????? ?????? ???????????? ??????????????? ??????)
    private SharedPreferences appData;
    public static String devName;
    public static String devMacadd;

    public static String mode;  // ???????????? ?????? ??????(0 = ????????? ??????, 1 = ???????????? ??????)
    public static String mode_default = "0"; //?????????(???????????? ??????)
    public static String autoCompressure;   // ???????????? ????????????(0 = ???????????? x, 1 = ???????????? o)
    public static String autoCompressure_default = "0"; //?????????(???????????? ??????)
    public static String flow1;    // ????????? ????????? ????????????(0 = off, 1 = on, ?????? ????????? ??????[???])
    public static String flow1_default = "0,10";    //?????????(???????????? ??????)
    public static String flow2;    // ????????? ????????? ????????????(0 = off, 1 = on, ?????? ????????? ??????????????? ??????)
    public static String flow2_default = "0,1"; //?????????(???????????? ??????)
    public static String flow3;    // ????????? ????????? ????????????(0 = off, 1 = on, ?????? ????????? ??????????????? ??????)
    public static String flow3_default = "0,60";    //?????????(???????????? ??????)
    public static String cleanPower; // ????????????
    public static String cleanPower2; // ????????????
    public static String cleanPower3; // ????????????
    public static String cleanPower4; // ????????????
    public static String cleanPower_default = "0.5";    //?????????(???????????? ??????)
    public static String cleanPower2_default = "1.0";    //?????????(???????????? ??????)
    public static String cleanPower3_default = "2.0";    //?????????(???????????? ??????)
    public static String cleanPower4_default = "3.0";    //?????????(???????????? ??????)
    public static String radioButtoncheck = "0";
    public static int heartbeat1 = 0;
    public static int heartbeat2 = 0;
    public static boolean sounds = false;

    public static boolean IwantDisconnect = false;
    public static boolean flowresetflag = false; //???????????? ??????????????????
    public static int resetcount = 10;
    public static boolean comp_control = false; //???????????? ??????
    public static boolean wash_control = false; //???????????? ??????
    public static boolean actionflag = false;
    private String connectedDeviceMacAddress;

    BleManager bleManager;

    static ConnectFragment connectFragment;
    static SettingFragment settingFragment;
    static MainFragment mainFragment;

    Dialog dialog;

    ImageView btn_NewDeviceSearch, bluetoothstate1;
    TextView tv_connect, tv_flow, tv_comp_state, tv_washstate, tv_washpower, tv_acctime, tv_resetcheck, tv_autoCompressure;
    TextView tv_runningtime, tv_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySoundPlayer.initSounds(getApplicationContext());

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


        //??????????????? ???????????????
        connectFragment = (ConnectFragment) getSupportFragmentManager().findFragmentById(R.id.connectFragment);
        connectFragment = new ConnectFragment();
        //settingFragment = (SettingFragment)getSupportFragmentManager().findFragmentById(R.id.settingFragment);
        settingFragment = new SettingFragment();
        //mainFragment = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        mainFragment = new MainFragment();



        /*????????? ??????*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //???????????? ?????? ??????
        adapter = new ItemAdapter();


        //????????? ????????????
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        //showToast(flow1 + " " + flow2 + " " + flow3);

        //?????? ?????? ?????????
        btn_NewDeviceSearch = findViewById(R.id.btn_NewDeviceSearch);   // ????????? ????????????????????? ??????
        tv_connect = findViewById(R.id.tv_connect);
        tv_flow = findViewById(R.id.tv_flow);
        tv_comp_state = findViewById(R.id.tv_comp_state);
        tv_washstate = findViewById(R.id.tv_washstate);
        tv_washpower = findViewById(R.id.tv_washpower);
        tv_acctime = findViewById(R.id.tv_acctime);
        tv_resetcheck = findViewById(R.id.tv_resetcheck);
        tv_runningtime = findViewById(R.id.tv_runningtime);
        tv_alarm = findViewById(R.id.tv_alarm);


        //????????? ??????????????????
        pd = new ProgressDialog(MainActivity.this);

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
                if (failCode == BleConnectCallback.FAIL_CONNECT_TIMEOUT) {
                    //connection timeout
                } else {
                    //connection fail due to other reasons
                }

            }

            @Override
            public void onConnected(BleDevice device) {

                showToast("?????????????????????.");

                onFragmentChanged(1);
                tv_connect.setText("true");
                IwantDisconnect = false;

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                if (dialog!= null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (pd.isShowing()) {
                    pd.dismiss();
                }



                // ????????? UUID of HC-42: Search UUID: FFF0, service UUID: FFE0, transparent transmission data UUID: FFE1.
                bleManager.notify(device, "0000FFE0-0000-1000-8000-00805F9B34FB", "0000FFE1-0000-1000-8000-00805F9B34FB", new BleNotifyCallback() {
                    @Override
                    public void onCharacteristicChanged(byte[] data, BleDevice device) {
                        String Data = null;
                        try {
                            Data = new String(data, StandardCharsets.UTF_8);
                            Data = Data.replaceAll("(\r\n|\r|\n|\n\r)", "");    // \r\n ?????? \n\r ?????? \n ?????? \r??? ?????? ???????????? ??????
                            Data.trim();

                            int in = Data.indexOf("$");

                            //int out = Data.length();
                            Data = Data.substring(in);

                            Log.d("Receive Data : ", Data);// Receive ????????? ??????

                            String[] Completely = Data.split("\\$");

                            /*
                            for (int i = 0 ; i < Completely.length - 1 ; i++) {
                                Log.d("Several Data : ", Completely[i] + "  index : " + i);
                            }
                            */

                            // ????????? ?????? ??????
                            String[] Ddata = Completely[1].split("_");   // ex) Ddata[0] = "--DAHAN-MCU", Ddata[1] = "29.40,1,1,1.50,15:30:28,0*25"
                            Ddata[1].trim();
                            String[] DDdata = Ddata[1].split("\\*");    // ex) DDdata[0] = "29.40,1,1,1.50,15:30:28,0", DDdata[1] = "25"

                            //Log.d("DDdata : ", DDdata[0]);
                            DDdata[0].trim();
                            DDdata[1] = DDdata[1].replaceAll("(\r\n|\r|\n|\n\r)", "");  // \r\n ?????? \n\r ?????? \n ?????? \r??? ?????? ???????????? ??????
                            DDdata[1].trim();
                            //Log.d("DDdata : ", DDdata[1]);
                            int lenth = Integer.parseInt(DDdata[1]);

                            if (DDdata[0].length() == lenth) {  // DDdata[0] = "29.40,1,1,1.50,15:30:28,0"
                                Log.d("?????? : ", "O ???????????????");
                                String[] DDDdata = DDdata[0].split(",");    // ex) DDDdata[0]="29.40" DDDdata[1]="1" DDDdata[2]="1" DDDdata[3]="1.50" DDDdata[4]="15:30:28" DDDdata[5]="0"
                                double flow = Double.parseDouble(DDDdata[0]);

                                //?????? ?????? ???????????? ????????????????????? ???????????????????????? ?????? ????????????
                                //?????? ?????????
                                tv_flow.setText(DDDdata[0]);

                                //????????????????????????
                                tv_comp_state.setText(DDDdata[1]);

                                //????????????????????????
                                tv_washstate.setText(DDDdata[2]);

                                //????????????????????????(??????????????????)
                                tv_washpower.setText(DDDdata[3]);

                                //?????? ????????????
                                tv_acctime.setText(DDDdata[4]);

                                //???????????? ????????????
                                tv_resetcheck.setText(DDDdata[5]);
                                if (DDDdata[5].equals("1")) {
                                    flowresetflag = false;
                                    tv_runningtime.setText("00:00:00");
                                }

                                if (sounds) {

                                    int flowvalue = (int) (Float.parseFloat(tv_flow.getText().toString()));

                                    String[] lmt2 = flow2.split(",");
                                    String[] lmt3 = flow3.split(",");

                                    if (Integer.parseInt(lmt2[1]) < flowvalue && Integer.parseInt(lmt3[1]) > flowvalue) {
                                        tv_alarm.setText("0");
                                    }

                                }


                            } else {
                                //Log.d("?????? : ", "X ????????? ?????????");
                            }


                            // ???????????? ??????????????? ????????????
                            // ????????????????????????,??????????????????,?????????????????????????????????,???????????? ????????????, ???????????????????????? ?????????.
                            //String send_Data = "$--DAHAN-AND_1,1,0.5,35:12,1*15\r\n";
                            String send_Data = "$--DAHAN-AND_";


                            //???????????? ???????????? ??????
                            if (comp_control) {
                                send_Data = send_Data + "1,";
                            } else {
                                send_Data = send_Data + "0,";
                            }
                            //?????? ???????????? ??????
                            if (wash_control) {
                                send_Data = send_Data + "1,";
                            } else {
                                send_Data = send_Data + "0,";
                            }
                            //????????????????????????????????? ??????(cleanPower)
                            if (radioButtoncheck.equals("0")) {
                                send_Data = send_Data + cleanPower + ",";
                            } else if (radioButtoncheck.equals("1")) {
                                send_Data = send_Data + cleanPower2 + ",";
                            } else if (radioButtoncheck.equals("2")) {
                                send_Data = send_Data + cleanPower3 + ",";
                            } else {
                                send_Data = send_Data + cleanPower4 + ",";
                            }

                            //???????????? ???????????? ??????
                            send_Data = send_Data + tv_runningtime.getText().toString() + ",";

                            //???????????? ??????   ??????????????????????????? ????????? ???????????? flowresetflag??? true??? ?????????.
                            if (flowresetflag) {
                                send_Data = send_Data + "1";
                            } else {
                                send_Data = send_Data + "0";
                            }

                            send_Data = send_Data + "\r\n";


                            byte[] send = send_Data.getBytes();

                            bleManager.write(device, "0000FFE0-0000-1000-8000-00805F9B34FB", "0000FFE1-0000-1000-8000-00805F9B34FB", send, new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(byte[] data, BleDevice device) {
                                    String temp = "";
                                    temp = new String(data);
                                    Log.d("Send Data : ", temp);
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
                showToast("????????? ?????????????????????.");

                tv_connect.setText("false");
                if (MainActivity.sounds) {
                    //sounds??? true??? ????????? ??????
                    soundStop();
                }

                if (!IwantDisconnect) {
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.setCancelable(false);
                    pd.show();
                }

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


                if (!IwantDisconnect) {

                    //1??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //2??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //3??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //4??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //5??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //6??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //7??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //8??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    //9??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }

                    //10??? ??????
                    if (tv_connect.getText().toString().equals("false")) {
                        bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            while (tv_connect.getText().toString().equals("false")) {
                                bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);
                            }
                        }
                    }).start();


                }
            }
        });


        // ????????? ?????? ?????? ?????? ?????? ?????????(?????????????????? ??????)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        btn_NewDeviceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????? ?????????
                dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.newdevice_connect_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                // ????????????????????? ?????? ????????? findViewById ????????? ?????? ??????????????? ??????(dialog.)??? ??? ????????????! ????????? Null pointer exception ??????
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
                            if (device.name.equals("unknown")) {
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
                            tv_newdevicesearchstate.setText("????????? ??????????????????.");
                        } else {
                            //fail to start scan, you can see details from 'info'
                            String failReason = info;
                            tv_newdevicesearchstate.setText(failReason);
                        }
                    }

                    @Override
                    public void onFinish() {
                        listView.setAdapter(adapter);
                        tv_newdevicesearchstate.setText("????????? ??????????????????.");
                        bleManager.stopScan();
                        //Toast.makeText(MainActivity.this, String.valueOf(adapter.getCount()), Toast.LENGTH_SHORT).show();
                    }
                });


                // ??????????????? ???????????? ?????? ?????? ??? ?????? ?????????
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
                                    if (device.name.equals("unknown")) {
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
                                    tv_newdevicesearchstate.setText("????????? ??????????????????.");
                                } else {
                                    //fail to start scan, you can see details from 'info'
                                    String failReason = info;
                                    tv_newdevicesearchstate.setText(failReason);
                                }
                            }

                            @Override
                            public void onFinish() {
                                listView.setAdapter(adapter);
                                tv_newdevicesearchstate.setText("????????? ??????????????????.");
                                bleManager.stopScan();
                                //Toast.makeText(MainActivity.this, String.valueOf(adapter.getCount()), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

                //  ??????????????? ???????????? ?????? ?????? ??? ?????? ?????????
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        final Item item = (Item) adapter.getItem(position);

                        //Toast.makeText(getApplicationContext(), item.getName() + " " + item.getMac(), Toast.LENGTH_SHORT).show();

                        //??????????????? ???????????? ??????????????????.
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("???????????? ??????").setMessage(item.getName() + " ??? ?????????????????????????");


                        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int id) {
                                /*
                                //?????? ?????????
                                onFragmentChanged(1);
                                dialog.dismiss();
                                */

                                //?????? ??????
                                connectedDeviceMacAddress = item.getMac();
                                bleManager.connect(connectedDeviceMacAddress, bleConnectCallback);

                                devName = item.getName();
                                devMacadd = item.getMac();
                            }
                        });

                        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
        }); // (?????????????????? ???)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~




    }//=========================================================================================================================================

    public void showToast(String para) {
        Toast.makeText(getApplicationContext(), para, Toast.LENGTH_SHORT).show();
    }

    //???????????? ?????? ??????
    public void save() {
        // SharedPreferences ?????????????????? ?????? ?????????, Editor ??????
        SharedPreferences.Editor editor = appData.edit();

        // ???????????????.put??????(???????????? ??????, ???????????? ???)
        // ???????????? ????????? ?????? ???????????? ????????????
        editor.putString("mode", mode);
        editor.putString("autoCompressure", autoCompressure);
        editor.putString("flow1", flow1);
        editor.putString("flow2", flow2);
        editor.putString("flow3", flow3);
        editor.putString("cleanPower", cleanPower);
        editor.putString("cleanPower2", cleanPower2);
        editor.putString("cleanPower3", cleanPower3);
        editor.putString("cleanPower4", cleanPower4);

        // apply, commit??? ????????? ????????? ????????? ???????????? ??????
        editor.apply();
    }

    // ????????? ??????????????? ??????????????????
    private void load() {
        // SHaredPreferences ??????.get??????(????????? ??????, ?????????)
        // ????????? ????????? ???????????? ?????? ??? ?????????

        mode = appData.getString("mode", "0");  // ???????????? ?????? ??????(0 = ????????? ??????, 1 = ???????????? ??????)
        autoCompressure = appData.getString("autoCompressure", "0");   // ???????????? ????????????(0 = ???????????? x, 1 = ???????????? o, default = 0)
        flow1 = appData.getString("flow1", "0,10");    // ????????? ????????? ????????????(0 = off, 1 = on, ?????? ????????? ??????[???])
        flow2 = appData.getString("flow2", "0,1");    // ????????? ????????? ????????????(0 = off, 1 = on, ?????? ????????? ??????[???])
        flow3 = appData.getString("flow3", "0,60");    // ????????? ????????? ????????????(0 = off, 1 = on, ?????? ????????? ??????[???])
        cleanPower = appData.getString("cleanPower", "0.5");     // ????????????
        cleanPower2 = appData.getString("cleanPower2", "1.0");   // ????????????2
        cleanPower3 = appData.getString("cleanPower3", "2.0");   // ????????????3
        cleanPower4 = appData.getString("cleanPower4", "3.0");   // ????????????3

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
        //???????????? ??????????????? ??????, ????????? ???????????? ????????????
        ArrayList<Item> items = new ArrayList<Item>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Item item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //???????????? ???????????? ???????????? ?????? ??????
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemView itemView = null;
            if (convertView == null) {
                itemView = new ItemView(getApplicationContext());
            } else {
                itemView = (ItemView) convertView;
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

                    // ??????????????? 9?????? ????????? ??????????????? 9????????? ??????????????? ???????????? ?????????.
                    long seconds = (CurrentTime.getTime() - ActionStartTime.getTime()) - (long) (32400000);

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
                while (actionflag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    handler.sendEmptyMessage(1); //????????? ?????? = ?????? ??????
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }


    // ????????? ????????? ??????
    public void Alarm1() {
        final Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {
                    String[] limit = flow1.split(",");
                    int lmt = Integer.parseInt(limit[1]);

                    int flowvalue = (int) (Float.parseFloat(tv_flow.getText().toString()));

                    if (flowvalue < 1) { //  Alarm ??????1 : Flowmeter Fault Alarm, ?????? ?????? On ?????? X??? ?????? ????????? ????????? 0 ?????? ?????? ????????? ??????
                        heartbeat1++;

                    } else {
                        heartbeat1 = 0;
                    }

                    if (lmt < heartbeat1) {
                        // Alarm ??????1 : Flowmeter Fault Alarm
                        tv_alarm.setText("1");      // tv_alarm??? "0"??? ?????? ?????? X,    "1"??? ?????? ?????? O.
                    } else {
                        String[] lmt2 = flow2.split(",");
                        String[] lmt3 = flow3.split(",");
                        if (Integer.parseInt(lmt2[1]) < flowvalue && Integer.parseInt(lmt3[1]) > flowvalue) {
                            if (sounds) {
                                tv_alarm.setText("0");
                            }
                        }
                    }

                    //showToast(limit[1] + "    " +String.valueOf(heartbeat1));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                while (actionflag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    handler1.sendEmptyMessage(1); //????????? ?????? = ?????? ??????
                }
            }
        };
        Thread thread = new Thread(task1);
        thread.start();
    }

    // ????????? ????????? ????????????
    public void Alarm2() {
        final Handler handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {
                    String[] limit = flow2.split(",");
                    int lmt = Integer.parseInt(limit[1]);

                    int flowvalue = (int) (Float.parseFloat(tv_flow.getText().toString()));

                    if (heartbeat2 > 10) { //  Alarm ??????2 : Flowmeter Low Alarm, ?????? ?????? On ?????? ????????? ????????? ????????? ??? ????????? ???????????? ??? ??????
                        // ???????????? ?????? 10??? ????????? ????????? ????????? ????????? ?????? ????????? ??????
                        if (flowvalue < lmt) {
                            tv_alarm.setText("1");      // tv_alarm??? "0"??? ?????? ?????? X,    "1"??? ?????? ?????? O.
                        }
                    }

                    heartbeat2++;

                    //showToast(limit[1] + "    " +String.valueOf(heartbeat2));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                while (actionflag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    handler2.sendEmptyMessage(1); //????????? ?????? = ?????? ??????
                }
            }
        };
        Thread thread = new Thread(task2);
        thread.start();
    }

    // ????????? ????????? ????????????
    public void Alarm3() {
        final Handler handler3 = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {
                    String[] limit = flow3.split(",");
                    int lmt3 = Integer.parseInt(limit[1]);

                    int flowvalue = (int) (Float.parseFloat(tv_flow.getText().toString()));

                    if (lmt3 < flowvalue) { //  Alarm ??????3 : Flowmeter High Alarm , ?????? ?????? On ?????? ????????? ????????? ????????? ?????? ?????? ????????? ??? ??????
                        // ???????????? ?????? ?????? ????????? ????????? ??????
                        tv_alarm.setText("1");      // tv_alarm??? "0"??? ?????? ?????? X,    "1"??? ?????? ?????? O.
                    }

                    //showToast(limit[1]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable task3 = new Runnable() {
            @Override
            public void run() {
                while (actionflag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    handler3.sendEmptyMessage(1); //????????? ?????? = ?????? ??????
                }
            }
        };
        Thread thread = new Thread(task3);
        thread.start();
    }


    public void DisconnectBLE() {
        if (!connectedDeviceMacAddress.equals(null)) {
            IwantDisconnect = true;
            onFragmentChanged(0);
            bleManager.disconnectAll();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }


    public void soundOn() {
        MySoundPlayer.play(MySoundPlayer.Pager_Beeps);
        sounds = true;
    }

    public void soundStop() {
        MySoundPlayer.stop();
        sounds = false;
        //MySoundPlayer.initSounds(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("?????????????????? ??????").setMessage("?????? ?????????????????????????");

        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                moveTaskToBack(true); // ???????????? ?????????????????? ??????
                finishAndRemoveTask(); // ???????????? ?????? + ????????? ??????????????? ?????????
                android.os.Process.killProcess(android.os.Process.myPid()); // ??? ???????????? ??????
            }
        });

        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if(focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if(!rect.contains(x,y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(),0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
