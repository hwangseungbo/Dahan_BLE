package org.techtown.dahan_ble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


public class SettingFragment extends Fragment {

    public String mode_temp;
    public String autoCompressure_temp;
    public String flow1_temp;
    public String flow2_temp;
    public String flow3_temp;
    public String cleanPower_temp;


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
        Button fr_btn_save = (Button) rootView.findViewById(R.id.fr_btn_save);
        Button fr_btn_cancel = (Button) rootView.findViewById(R.id.fr_btn_cancel);
        Button fr_btn_analog = (Button) rootView.findViewById(R.id.fr_btn_analog);
        Button fr_btn_digital = (Button) rootView.findViewById(R.id.fr_btn_digital);
        Button btn_back = rootView.findViewById(R.id.btn_back);
        Button fr_btn_power1 = (Button) rootView.findViewById(R.id.fr_btn_power1);
        Button fr_btn_power2 = (Button) rootView.findViewById(R.id.fr_btn_power2);
        Button fr_btn_power3 = (Button) rootView.findViewById(R.id.fr_btn_power3);
        Button fr_btn_power4 = (Button) rootView.findViewById(R.id.fr_btn_power4);
        Button fr_btn_flowreset = (Button) rootView.findViewById(R.id.fr_btn_flowreset);
        Button fr_btn_appreset = (Button) rootView.findViewById(R.id.fr_btn_appreset);



        //이니셜라이징
        mode_temp = ((MainActivity)getActivity()).mode;
        autoCompressure_temp = ((MainActivity)getActivity()).autoCompressure;
        flow1_temp = ((MainActivity)getActivity()).flow1;
        flow2_temp = ((MainActivity)getActivity()).flow2;
        flow3_temp = ((MainActivity)getActivity()).flow3;
        cleanPower_temp = ((MainActivity)getActivity()).cleanPower;
        if(tv_connect.getText().toString().equals("true")) {
            iv_bluetoothstate2.setImageResource(R.drawable.ble_on);
        } else {
            iv_bluetoothstate2.setImageResource(R.drawable.ble_off);
        }

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
        // <배관 세척 강도>
        if(((MainActivity)getActivity()).cleanPower.equals("0.5")){
            fr_btn_power1.setBackgroundResource(R.drawable.time_button_pressed);
            fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
        }else if(((MainActivity)getActivity()).cleanPower.equals("1.0")) {
            fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power2.setBackgroundResource(R.drawable.time_button_pressed);
            fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
        }else if(((MainActivity)getActivity()).cleanPower.equals("2.0")) {
            fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power3.setBackgroundResource(R.drawable.time_button_pressed);
            fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
        }else {
            fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
            fr_btn_power4.setBackgroundResource(R.drawable.time_button_pressed);
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

        fr_sw_flow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fr_sw_flow1.isChecked()){
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_et_flow1.setEnabled(true);
                    flow1_temp = "1," + fr_et_flow1.getText().toString().trim();
                }else{
                    fr_et_flow1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_et_flow1.setEnabled(false);
                    flow1_temp = "0," + fr_et_flow1.getText().toString().trim();
                }
            }
        });

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

        fr_btn_power1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr_btn_power1.setBackgroundResource(R.drawable.time_button_pressed);
                fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                cleanPower_temp = "0.5";
            }
        });
        fr_btn_power2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power2.setBackgroundResource(R.drawable.time_button_pressed);
                fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                cleanPower_temp = "1.0";
            }
        });
        fr_btn_power3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power3.setBackgroundResource(R.drawable.time_button_pressed);
                fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                cleanPower_temp = "2.0";
            }
        });
        fr_btn_power4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                fr_btn_power4.setBackgroundResource(R.drawable.time_button_pressed);
                cleanPower_temp = "3.0";
            }
        });

        fr_btn_flowreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fr_btn_appreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).mode = ((MainActivity)getActivity()).mode_default;
                ((MainActivity)getActivity()).autoCompressure = ((MainActivity)getActivity()).autoCompressure_default;
                ((MainActivity)getActivity()).flow1 = ((MainActivity)getActivity()).flow1_default;
                ((MainActivity)getActivity()).flow2 = ((MainActivity)getActivity()).flow2_default;
                ((MainActivity)getActivity()).flow3 = ((MainActivity)getActivity()).flow3_default;
                ((MainActivity)getActivity()).cleanPower = ((MainActivity)getActivity()).cleanPower_default;
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
                // <배관 세척 강도>
                if(((MainActivity)getActivity()).cleanPower.equals("0.5")){
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else if(((MainActivity)getActivity()).cleanPower.equals("1.0")) {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else if(((MainActivity)getActivity()).cleanPower.equals("2.0")) {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_pressed);
                }



                ((MainActivity)getActivity()).showToast("초기화 되었습니다.");
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
                // <배관 세척 강도>
                if(((MainActivity)getActivity()).cleanPower.equals("0.5")){
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else if(((MainActivity)getActivity()).cleanPower.equals("1.0")) {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else if(((MainActivity)getActivity()).cleanPower.equals("2.0")) {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_pressed);
                }


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
                ((MainActivity)getActivity()).cleanPower = cleanPower_temp;

                ((MainActivity)getActivity()).save();

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
                // <배관 세척 강도>
                if(((MainActivity)getActivity()).cleanPower.equals("0.5")){
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else if(((MainActivity)getActivity()).cleanPower.equals("1.0")) {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else if(((MainActivity)getActivity()).cleanPower.equals("2.0")) {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_pressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_unpressed);
                }else {
                    fr_btn_power1.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power2.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power3.setBackgroundResource(R.drawable.time_button_unpressed);
                    fr_btn_power4.setBackgroundResource(R.drawable.time_button_pressed);
                }

                ((MainActivity)getActivity()).showToast( "저장 되었습니다.");
            }
        });

        return rootView;
    }


}