package org.techtown.dahan_ble;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;


public class SettingFragment extends Fragment {

    public String mode_temp;
    public String autoCompressure_temp;
    public String flow1_temp;
    public String flow2_temp;
    public String flow3_temp;
    Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);




        //프래그먼트의 위젯
        TextView tv_connect = super.getActivity().findViewById(R.id.tv_connect);
        ImageView iv_bluetoothstate2 = (ImageView) rootView.findViewById(R.id.iv_bluetoothstate2);
        Switch fr_sw_autocomp = (Switch) rootView.findViewById(R.id.fr_sw_autocomp);
        Switch fr_sw_flow1 = (Switch) rootView.findViewById(R.id.fr_sw_flow1);
        Switch fr_sw_flow2 = (Switch) rootView.findViewById(R.id.fr_sw_flow2);
        Switch fr_sw_flow3 = (Switch) rootView.findViewById(R.id.fr_sw_flow3);
        EditText fr_et_flow1 = (EditText) rootView.findViewById(R.id.fr_et_flow1);
        EditText fr_et_flow2 = (EditText) rootView.findViewById(R.id.fr_et_flow2);
        EditText fr_et_flow3 = (EditText) rootView.findViewById(R.id.fr_et_flow3);
        EditText et_cleanpower = (EditText) rootView.findViewById(R.id.et_cleanpower);
        EditText et_cleanpower2 = (EditText) rootView.findViewById(R.id.et_cleanpower2);
        EditText et_cleanpower3 = (EditText) rootView.findViewById(R.id.et_cleanpower3);
        EditText et_cleanpower4 = (EditText) rootView.findViewById(R.id.et_cleanpower4);
        Button fr_btn_save = (Button) rootView.findViewById(R.id.fr_btn_save);
        Button fr_btn_cancel = (Button) rootView.findViewById(R.id.fr_btn_cancel);
        Button fr_btn_analog = (Button) rootView.findViewById(R.id.fr_btn_analog);
        Button fr_btn_digital = (Button) rootView.findViewById(R.id.fr_btn_digital);
        Button btn_back = rootView.findViewById(R.id.btn_back);
        Button fr_btn_flowreset = (Button) rootView.findViewById(R.id.fr_btn_flowreset);
        Button fr_btn_appreset = (Button) rootView.findViewById(R.id.fr_btn_appreset);
        Button fr_btn_exit = (Button) rootView.findViewById(R.id.fr_btn_exit);
        TextView fr_setting_devName = (TextView) rootView.findViewById(R.id.fr_setting_devName);
        TextView fr_setting_macAdd = (TextView) rootView.findViewById(R.id.fr_setting_macAdd);

        fr_et_flow1.clearFocus();

        //이니셜라이징
        mode_temp = ((MainActivity)getActivity()).mode;
        autoCompressure_temp = ((MainActivity)getActivity()).autoCompressure;
        flow1_temp = ((MainActivity)getActivity()).flow1;
        flow2_temp = ((MainActivity)getActivity()).flow2;
        flow3_temp = ((MainActivity)getActivity()).flow3;
        fr_setting_devName.setText("기기명 : " + ((MainActivity)getActivity()).devName);
        fr_setting_macAdd.setText("맥주소 : " + ((MainActivity)getActivity()).devMacadd);
        et_cleanpower.setText(((MainActivity)getActivity()).cleanPower);
        et_cleanpower2.setText(((MainActivity)getActivity()).cleanPower2);
        et_cleanpower3.setText(((MainActivity)getActivity()).cleanPower3);
        et_cleanpower4.setText(((MainActivity)getActivity()).cleanPower4);
        if(tv_connect.getText().toString().equals("true")) {
            iv_bluetoothstate2.setImageResource(R.drawable.ble_on);
        } else {
            iv_bluetoothstate2.setImageResource(R.drawable.ble_off);
        }
        ((MainActivity)getActivity()).showToast(flow1_temp+ " " + flow2_temp + " " + flow3_temp );

        // 설정파일값을 확인하여 UI 초기화
        // <메인화면 테마설정>
        if( ((MainActivity)getActivity()).mode.equals("0") ){
            // mode 값이 0일 경우 디지털 방식
            fr_btn_analog.setBackgroundResource(R.drawable.analog_button_off);
            fr_btn_digital.setBackgroundResource(R.drawable.digital_button_on);

        } else if ( ((MainActivity)getActivity()).mode.equals("1") ) {
            // mode 값이 1일 경우 아날로그 방식
            fr_btn_analog.setBackgroundResource(R.drawable.analog_button_on);
            fr_btn_digital.setBackgroundResource(R.drawable.digital_button_off);
        }
        // <컴프레셔 자동 동작 설정>
        if( ((MainActivity)getActivity()).autoCompressure.equals("0") ){
            fr_sw_autocomp.setChecked(false);
        }else if( ((MainActivity)getActivity()).autoCompressure.equals("1") ){
            fr_sw_autocomp.setChecked(true);
        }

        // <알람설정>
        String[] flow1 = ((MainActivity)getActivity()).flow1.split(",");
        String[] flow2 = ((MainActivity)getActivity()).flow2.split(",");
        String[] flow3 = ((MainActivity)getActivity()).flow3.split(",");
        if( flow1[0].equals("0") ) {
            fr_sw_flow1.setChecked(false);
            fr_et_flow1.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_et_flow1.setText(flow1[1]);
            fr_et_flow1.setEnabled(false);

            fr_sw_flow2.setChecked(false);
            fr_sw_flow2.setEnabled(false);
            fr_et_flow2.setEnabled(false);

            fr_sw_flow3.setChecked(false);
            fr_sw_flow3.setEnabled(false);
            fr_et_flow3.setEnabled(false);

        } else {
            fr_sw_flow1.setChecked(true);
            fr_et_flow1.setBackgroundResource(R.drawable.time_button_pressed);
            fr_et_flow1.setText(flow1[1]);
            fr_et_flow1.setEnabled(true);


        }

        if( flow2[0].equals("0") ) {
            fr_sw_flow2.setChecked(false);
            fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_et_flow2.setText(flow2[1]);
            fr_et_flow2.setEnabled(false);
        } else {
            fr_sw_flow2.setChecked(true);
            fr_et_flow2.setBackgroundResource(R.drawable.time_button_pressed);
            fr_et_flow2.setText(flow2[1]);
            fr_et_flow2.setEnabled(true);
        }

        if( flow3[0].equals("0") ) {
            fr_sw_flow3.setChecked(false);
            fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_et_flow3.setText(flow3[1]);
            fr_et_flow3.setEnabled(false);
        } else {
            fr_sw_flow3.setChecked(true);
            fr_et_flow3.setBackgroundResource(R.drawable.time_button_pressed);
            fr_et_flow3.setText(flow3[1]);
            fr_et_flow3.setEnabled(true);
        }



        fr_btn_analog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr_btn_analog.setBackgroundResource(R.drawable.analog_button_on);
                fr_btn_digital.setBackgroundResource(R.drawable.digital_button_off);
                mode_temp = "1";
            }
        });
        fr_btn_digital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr_btn_analog.setBackgroundResource(R.drawable.analog_button_off);
                fr_btn_digital.setBackgroundResource(R.drawable.digital_button_on);
                mode_temp = "0";
            }
        });

        fr_sw_autocomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fr_sw_autocomp.isChecked()) {
                    autoCompressure_temp = "1";
                } else {
                    autoCompressure_temp = "0";
                }
            }
        });
        /*
        fr_sw_flow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fr_sw_flow1.isChecked()){
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow1.setEnabled(true);
                    fr_sw_flow2.setEnabled(true);
                    fr_sw_flow3.setEnabled(true);
                    flow1_temp = "1," + fr_et_flow1.getText().toString().trim();
                }else{
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);
                    flow1_temp = "0," + fr_et_flow1.getText().toString().trim();
                    flow2_temp = "0," + fr_et_flow1.getText().toString().trim();
                    flow3_temp = "0," + fr_et_flow1.getText().toString().trim();
                    fr_et_flow1.setEnabled(false);
                    fr_sw_flow2.setChecked(false);
                    fr_sw_flow2.setEnabled(false);
                    fr_sw_flow3.setChecked(false);
                    fr_sw_flow3.setEnabled(false);
                }
            }
        });
        */
        fr_sw_flow1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //on일때 함수
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow1.setEnabled(true);

                    flow1_temp = "1," + fr_et_flow1.getText().toString().trim();

                    fr_sw_flow2.setEnabled(true);
                    fr_sw_flow2.setChecked(false);
                    fr_et_flow2.setEnabled(false);

                    fr_sw_flow3.setEnabled(true);
                    fr_sw_flow3.setChecked(false);
                    fr_et_flow3.setEnabled(false);


                } else {
                    //off일때 함수
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);

                    flow1_temp = "0," + fr_et_flow1.getText().toString().trim();
                    flow2_temp = "0," + fr_et_flow2.getText().toString().trim();
                    flow3_temp = "0," + fr_et_flow3.getText().toString().trim();

                    fr_et_flow1.setEnabled(false);

                    fr_sw_flow2.setChecked(false);
                    fr_sw_flow2.setEnabled(false);
                    fr_et_flow2.setEnabled(false);

                    fr_sw_flow3.setChecked(false);
                    fr_sw_flow3.setEnabled(false);
                    fr_et_flow3.setEnabled(false);
                }
            }
        });


        fr_et_flow1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] flow1 = ((MainActivity)getActivity()).flow1.split(",");
                if(fr_sw_flow1.isChecked()) {
                    flow1_temp = "1," + fr_et_flow1.getText().toString().trim();
                } else {
                    flow1_temp = "0," + fr_et_flow1.getText().toString().trim();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        /*
        fr_sw_flow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fr_sw_flow2.isChecked()){
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow2.setEnabled(true);
                    flow2_temp = "1," + fr_et_flow2.getText().toString().trim();
                }else{
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow2.setEnabled(false);
                    flow2_temp = "0," + fr_et_flow2.getText().toString().trim();
                }
            }
        });
        */
        fr_sw_flow2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //on일때 함수
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow2.setEnabled(true);
                    flow2_temp = "1," + fr_et_flow2.getText().toString().trim();
                } else {
                    //off일때 함수
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow2.setEnabled(false);
                    flow2_temp = "0," + fr_et_flow2.getText().toString().trim();
                }
            }
        });
        fr_et_flow2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] flow2 = ((MainActivity)getActivity()).flow2.split(",");

                if(fr_sw_flow2.isChecked()){
                    flow2_temp = "1," + fr_et_flow2.getText().toString().trim();
                }
                else {
                    flow2_temp = "0," + fr_et_flow2.getText().toString().trim();
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*
        fr_sw_flow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fr_sw_flow3.isChecked()){
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow3.setEnabled(true);
                    flow3_temp = "1," + fr_et_flow3.getText().toString().trim();
                }else{
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow3.setEnabled(false);
                    flow3_temp = "0," + fr_et_flow3.getText().toString().trim();
                }
            }
        });
        */
        fr_sw_flow3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //on일때 함수
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow3.setEnabled(true);
                    flow3_temp = "1," + fr_et_flow3.getText().toString().trim();
                } else {
                    //off일때 함수
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow3.setEnabled(false);
                    flow3_temp = "0," + fr_et_flow3.getText().toString().trim();
                }
            }
        });
        fr_et_flow3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] flow3 = ((MainActivity)getActivity()).flow3.split(",");

                if(fr_sw_flow3.isChecked()) {
                    flow3_temp = "1," + fr_et_flow3.getText().toString().trim();
                }else {
                    flow3_temp = "0," + fr_et_flow3.getText().toString().trim();
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        fr_btn_flowreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).flowresetflag=true;
                ((MainActivity)getActivity()).showToast("누적시간 초기화 요청");
            }
        });

        //리셋버튼 클릭 이벤트
        fr_btn_appreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //팝업창 띄우기
                dialog = new Dialog(getContext(), android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.setting_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                Button btn_reset_yes = dialog.findViewById(R.id.btn_reset_yes);
                Button btn_reset_no = dialog.findViewById(R.id.btn_reset_no);

                btn_reset_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).mode = ((MainActivity)getActivity()).mode_default;
                        ((MainActivity)getActivity()).autoCompressure = ((MainActivity)getActivity()).autoCompressure_default;
                        ((MainActivity)getActivity()).flow1 = ((MainActivity)getActivity()).flow1_default;
                        ((MainActivity)getActivity()).flow2 = ((MainActivity)getActivity()).flow2_default;
                        ((MainActivity)getActivity()).flow3 = ((MainActivity)getActivity()).flow3_default;
                        ((MainActivity)getActivity()).cleanPower = ((MainActivity)getActivity()).cleanPower_default;
                        ((MainActivity)getActivity()).cleanPower2 = ((MainActivity)getActivity()).cleanPower2_default;
                        ((MainActivity)getActivity()).cleanPower3 = ((MainActivity)getActivity()).cleanPower3_default;
                        ((MainActivity)getActivity()).cleanPower4 = ((MainActivity)getActivity()).cleanPower4_default;
                        ((MainActivity)getActivity()).save();

                        // UI 초기화
                        // 설정파일값을 확인하여 UI 초기화
                        // <메인화면 테마설정>
                        if( ((MainActivity)getActivity()).mode.equals("0") ){
                            // mode 값이 0일 경우 디지털 방식
                            fr_btn_analog.setBackgroundResource(R.drawable.analog_button_off);
                            fr_btn_digital.setBackgroundResource(R.drawable.digital_button_on);

                        } else if ( ((MainActivity)getActivity()).mode.equals("1") ) {
                            // mode 값이 1일 경우 아날로그 방식
                            fr_btn_analog.setBackgroundResource(R.drawable.analog_button_on);
                            fr_btn_digital.setBackgroundResource(R.drawable.digital_button_off);
                        }
                        // <컴프레셔 자동 동작 설정>
                        if( ((MainActivity)getActivity()).autoCompressure.equals("0") ){
                            fr_sw_autocomp.setChecked(false);
                        }else if( ((MainActivity)getActivity()).autoCompressure.equals("1") ){
                            fr_sw_autocomp.setChecked(true);
                        }
                        // <알람설정>
                        String[] flow1 = ((MainActivity)getActivity()).flow1.split(",");
                        String[] flow2 = ((MainActivity)getActivity()).flow2.split(",");
                        String[] flow3 = ((MainActivity)getActivity()).flow3.split(",");
                        if( flow1[0].equals("0") ) {
                            fr_sw_flow1.setChecked(false);
                            fr_et_flow1.setBackgroundResource(R.drawable.time_button_unpressed);
                            fr_et_flow1.setText(flow1[1]);
                            fr_et_flow1.setEnabled(false);
                        } else {
                            fr_sw_flow1.setChecked(true);
                            fr_et_flow1.setBackgroundResource(R.drawable.time_button_pressed);
                            fr_et_flow1.setText(flow1[1]);
                            fr_et_flow1.setEnabled(true);
                        }

                        if( flow2[0].equals("0") ) {
                            fr_sw_flow2.setChecked(false);
                            fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
                            fr_et_flow2.setText(flow2[1]);
                            fr_et_flow2.setEnabled(false);
                        } else {
                            fr_sw_flow2.setChecked(true);
                            fr_et_flow2.setBackgroundResource(R.drawable.time_button_pressed);
                            fr_et_flow2.setText(flow2[1]);
                            fr_et_flow2.setEnabled(true);
                        }

                        if( flow3[0].equals("0") ) {
                            fr_sw_flow3.setChecked(false);
                            fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);
                            fr_et_flow3.setText(flow3[1]);
                            fr_et_flow3.setEnabled(false);
                        } else {
                            fr_sw_flow3.setChecked(true);
                            fr_et_flow3.setBackgroundResource(R.drawable.time_button_pressed);
                            fr_et_flow3.setText(flow3[1]);
                            fr_et_flow3.setEnabled(true);
                        }
                        et_cleanpower.setText(((MainActivity)getActivity()).cleanPower);
                        et_cleanpower2.setText(((MainActivity)getActivity()).cleanPower2);
                        et_cleanpower3.setText(((MainActivity)getActivity()).cleanPower3);
                        et_cleanpower4.setText(((MainActivity)getActivity()).cleanPower4);

                        ((MainActivity)getActivity()).showToast("초기화 되었습니다.");

                        dialog.dismiss();
                    }
                });

                btn_reset_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();

                // 설정파일값을 확인하여 UI 초기화
                // <메인화면 테마설정>
                if( ((MainActivity)getActivity()).mode.equals("0") ){
                    // mode 값이 0일 경우 디지털 방식
                    fr_btn_analog.setBackgroundResource(R.drawable.analog_button_off);
                    fr_btn_digital.setBackgroundResource(R.drawable.digital_button_on);

                } else if ( ((MainActivity)getActivity()).mode.equals("1") ) {
                    // mode 값이 1일 경우 아날로그 방식
                    fr_btn_analog.setBackgroundResource(R.drawable.analog_button_on);
                    fr_btn_digital.setBackgroundResource(R.drawable.digital_button_off);
                }
                // <컴프레셔 자동 동작 설정>
                if( ((MainActivity)getActivity()).autoCompressure.equals("0") ){
                    fr_sw_autocomp.setChecked(false);
                }else if( ((MainActivity)getActivity()).autoCompressure.equals("1") ){
                    fr_sw_autocomp.setChecked(true);
                }
                // <알람설정>
                String[] flow1 = ((MainActivity)getActivity()).flow1.split(",");
                String[] flow2 = ((MainActivity)getActivity()).flow2.split(",");
                String[] flow3 = ((MainActivity)getActivity()).flow3.split(",");
                if( flow1[0].equals("0") ) {
                    fr_sw_flow1.setChecked(false);
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow1.setText(flow1[1]);
                    fr_et_flow1.setEnabled(false);
                } else {
                    fr_sw_flow1.setChecked(true);
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow1.setText(flow1[1]);
                    fr_et_flow1.setEnabled(true);
                }

                if( flow2[0].equals("0") ) {
                    fr_sw_flow2.setChecked(false);
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow2.setText(flow2[1]);
                    fr_et_flow2.setEnabled(false);
                } else {
                    fr_sw_flow2.setChecked(true);
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow2.setText(flow2[1]);
                    fr_et_flow2.setEnabled(true);
                }

                if( flow3[0].equals("0") ) {
                    fr_sw_flow3.setChecked(false);
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow3.setText(flow3[1]);
                    fr_et_flow3.setEnabled(false);
                } else {
                    fr_sw_flow3.setChecked(true);
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow3.setText(flow3[1]);
                    fr_et_flow3.setEnabled(true);
                }
                et_cleanpower.setText(((MainActivity)getActivity()).cleanPower);
                et_cleanpower2.setText(((MainActivity)getActivity()).cleanPower2);
                et_cleanpower3.setText(((MainActivity)getActivity()).cleanPower3);
                et_cleanpower4.setText(((MainActivity)getActivity()).cleanPower4);

                mainActivity.onFragmentChanged(1);
            }
        });

        tv_connect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 텍스트 변경 전
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트 변경중일 때
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트 변경 후
                if(tv_connect.getText().toString().equals("true"))
                {
                    iv_bluetoothstate2.setImageResource(R.drawable.ble_on);
                } else {
                    iv_bluetoothstate2.setImageResource(R.drawable.ble_off);
                }
            }
        });

        fr_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).mode = mode_temp;
                ((MainActivity)getActivity()).autoCompressure = autoCompressure_temp;
                ((MainActivity)getActivity()).flow1 = flow1_temp;
                ((MainActivity)getActivity()).flow2 = flow2_temp;
                ((MainActivity)getActivity()).flow3 = flow3_temp;
                ((MainActivity)getActivity()).cleanPower = et_cleanpower.getText().toString();
                ((MainActivity)getActivity()).cleanPower2 = et_cleanpower2.getText().toString();
                ((MainActivity)getActivity()).cleanPower3 = et_cleanpower3.getText().toString();
                ((MainActivity)getActivity()).cleanPower4 = et_cleanpower4.getText().toString();

                ((MainActivity)getActivity()).save();


                /*
                // 설정파일값을 확인하여 UI 초기화
                // <메인화면 테마설정>
                if( ((MainActivity)getActivity()).mode.equals("0") ){
                    // mode 값이 0일 경우 디지털 방식
                    fr_btn_analog.setBackgroundResource(R.drawable.analog_button_off);
                    fr_btn_digital.setBackgroundResource(R.drawable.digital_button_on);

                } else if ( ((MainActivity)getActivity()).mode.equals("1") ) {
                    // mode 값이 1일 경우 아날로그 방식
                    fr_btn_analog.setBackgroundResource(R.drawable.analog_button_on);
                    fr_btn_digital.setBackgroundResource(R.drawable.digital_button_off);
                }

                // <알람설정>
                String[] flow1 = ((MainActivity)getActivity()).flow1.split(",");
                String[] flow2 = ((MainActivity)getActivity()).flow2.split(",");
                String[] flow3 = ((MainActivity)getActivity()).flow3.split(",");
                if( flow1[0].equals("0") ) {
                    fr_sw_flow1.setChecked(false);
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow1.setText(flow1[1]);
                    fr_et_flow1.setEnabled(false);
                } else {
                    fr_sw_flow1.setChecked(true);
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow1.setText(flow1[1]);
                    fr_et_flow1.setEnabled(true);
                }

                if( flow2[0].equals("0") ) {
                    fr_sw_flow2.setChecked(false);
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow2.setText(flow2[1]);
                    fr_et_flow2.setEnabled(false);
                } else {
                    fr_sw_flow2.setChecked(true);
                    fr_et_flow2.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow2.setText(flow2[1]);
                    fr_et_flow2.setEnabled(true);
                }

                if( flow3[0].equals("0") ) {
                    fr_sw_flow3.setChecked(false);
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow3.setText(flow3[1]);
                    fr_et_flow3.setEnabled(false);
                } else {
                    fr_sw_flow3.setChecked(true);
                    fr_et_flow3.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow3.setText(flow3[1]);
                    fr_et_flow3.setEnabled(true);
                }
                et_cleanpower.setText(((MainActivity)getActivity()).cleanPower);
                et_cleanpower2.setText(((MainActivity)getActivity()).cleanPower2);
                et_cleanpower3.setText(((MainActivity)getActivity()).cleanPower3);
                et_cleanpower4.setText(((MainActivity)getActivity()).cleanPower4);
                */

                //((MainActivity)getActivity()).showToast( flow1_temp + " " + flow2_temp + " " + flow3_temp);
                ((MainActivity)getActivity()).showToast( "저장 되었습니다.");
            }
        });


        fr_btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //팝업창 띄우기
                dialog = new Dialog(getContext(), android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.setting_dialog_reset);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                Button btn_reset_yes = dialog.findViewById(R.id.btn_reset_yes);
                Button btn_reset_no = dialog.findViewById(R.id.btn_reset_no);

                btn_reset_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).DisconnectBLE();
                        dialog.dismiss();
                    }
                });

                btn_reset_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("연결해제").setMessage("연결을 해제하시겠습니까?");

                builder.setPositiveButton("해제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int id) {
                        ((MainActivity)getActivity()).DisconnectBLE();
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
                */
            }
        });

        return rootView;
    }


}