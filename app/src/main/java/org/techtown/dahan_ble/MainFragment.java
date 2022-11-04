package org.techtown.dahan_ble;

import static org.techtown.dahan_ble.MainActivity.comp_control;

import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        //메인액티비티의 위젯
        TextView tv_connect = super.getActivity().findViewById(R.id.tv_connect);
        TextView tv_flow = super.getActivity().findViewById(R.id.tv_flow);
        TextView tv_comp_state = super.getActivity().findViewById(R.id.tv_comp_state);
        TextView tv_runningtime = super.getActivity().findViewById(R.id.tv_runningtime);
        TextView tv_acctime = super.getActivity().findViewById(R.id.tv_acctime);
        TextView tv_washstate = super.getActivity().findViewById(R.id.tv_washstate);
        TextView tv_alarm = super.getActivity().findViewById(R.id.tv_alarm);


        //프레그먼트의 위젯
        TextView fr_tv_flow = rootView.findViewById(R.id.fr_tv_flow);
        TextView fr_tv_flow2 = rootView.findViewById(R.id.fr_tv_flow2);
        TextView fr_tv_action_time = rootView.findViewById(R.id.fr_tv_action_time);
        TextView fr_tv_acc_time = rootView.findViewById(R.id.fr_tv_acc_time);
        Button btn_setting = rootView.findViewById(R.id.btn_setting);
        Switch fr_sw_comp = rootView.findViewById(R.id.fr_sw_comp);
        Switch fr_sw_wash = rootView.findViewById(R.id.fr_sw_wash);
        ImageView mainfrag_background = (ImageView) rootView.findViewById(R.id.mainfrag_background);
        ImageView mainfrag_analog_gauge = (ImageView) rootView.findViewById(R.id.mainfrag_analog_gauge);
        ImageView analog_gauge_needle = (ImageView) rootView.findViewById(R.id.analog_gauge_needle);
        ImageView iv_bluetoothstate = (ImageView) rootView.findViewById(R.id.iv_bluetoothstate);
        ImageView fr_iv_act = (ImageView) rootView.findViewById(R.id.fr_iv_act);
        ImageView fr_iv_stop = (ImageView) rootView.findViewById(R.id.fr_iv_stop);
        ImageView fr_iv_post = (ImageView) rootView.findViewById(R.id.fr_iv_post);
        RadioButton rb_cleanpower = (RadioButton) rootView.findViewById(R.id.rb_cleanpower);
        RadioButton rb_cleanpower2 = (RadioButton) rootView.findViewById(R.id.rb_cleanpower2);
        RadioButton rb_cleanpower3 = (RadioButton) rootView.findViewById(R.id.rb_cleanpower3);
        RadioButton rb_cleanpower4 = (RadioButton) rootView.findViewById(R.id.rb_cleanpower4);

        //이니셜라이징
        if (((MainActivity) getActivity()).mode.equals("0")) {
            // mode 값이 0일 경우 디지털 방식
            mainfrag_background.setImageResource(R.drawable.mainfrag_back_digital);
            fr_tv_flow.setVisibility(fr_tv_flow.VISIBLE);
            fr_tv_flow2.setVisibility(fr_tv_flow2.INVISIBLE);

            mainfrag_analog_gauge.setVisibility(mainfrag_analog_gauge.INVISIBLE);
            analog_gauge_needle.setVisibility(analog_gauge_needle.INVISIBLE);

        } else if (((MainActivity) getActivity()).mode.equals("1")) {
            // mode 값이 1일 경우 아날로그 방식
            mainfrag_background.setImageResource(R.drawable.mainfrag_back_analog);
            fr_tv_flow.setVisibility(fr_tv_flow.INVISIBLE);
            fr_tv_flow2.setVisibility(fr_tv_flow2.VISIBLE);

            mainfrag_analog_gauge.setVisibility(mainfrag_analog_gauge.VISIBLE);
            analog_gauge_needle.setVisibility(analog_gauge_needle.VISIBLE);

            analog_gauge_needle.setRotation(-135);  //테스트용
            /*
            degree      rotation
            0	        -135
            10	        -90
            20	        -45
            30	        -0
            40	        45
            50	        90
            60	        135
            */
        }

        if (tv_connect.getText().toString().equals("true")) {
            iv_bluetoothstate.setImageResource(R.drawable.ble_on);
        } else {
            iv_bluetoothstate.setImageResource(R.drawable.ble_off);
        }

        /*
        if(((MainActivity)getActivity()).comp_control) {
            fr_sw_comp.setChecked(true);
            fr_sw_wash.setEnabled(true);
        } else {
            fr_sw_comp.setChecked(false);
            fr_sw_wash.setChecked((false));
            fr_sw_wash.setEnabled(false);
        }*/

        if (((MainActivity) getActivity()).comp_control) {
            fr_sw_comp.setChecked(true);
            fr_sw_wash.setEnabled(true);
        }

        if (((MainActivity) getActivity()).autoCompressure.equals("1")) {
            ((MainActivity) getActivity()).comp_control = true;
            fr_sw_comp.setChecked(true);
            fr_sw_wash.setEnabled(true);

        } else {
            ((MainActivity) getActivity()).comp_control = false;
            fr_sw_comp.setChecked(false);
            fr_sw_wash.setEnabled(false);
        }

        if (((MainActivity) getActivity()).comp_control) {
            fr_sw_comp.setChecked(true);
        } else {
            fr_sw_comp.setChecked(false);
        }


        if (((MainActivity) getActivity()).wash_control) {
            fr_sw_wash.setChecked(true);
        } else {
            fr_sw_wash.setChecked(false);
        }
        fr_tv_action_time.setText(tv_runningtime.getText().toString());
        fr_tv_acc_time.setText(tv_acctime.getText().toString());
        rb_cleanpower.setText(((MainActivity) getActivity()).cleanPower);
        rb_cleanpower2.setText(((MainActivity) getActivity()).cleanPower2);
        rb_cleanpower3.setText(((MainActivity) getActivity()).cleanPower3);
        rb_cleanpower4.setText(((MainActivity) getActivity()).cleanPower4);

        rb_cleanpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).radioButtoncheck = "0";
            }
        });
        rb_cleanpower2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).radioButtoncheck = "1";
            }
        });
        rb_cleanpower3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).radioButtoncheck = "2";
            }
        });
        rb_cleanpower4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).radioButtoncheck = "3";
            }
        });

/*
        fr_sw_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fr_sw_comp.isChecked()) {
                    ((MainActivity) getActivity()).comp_control = true;
                    fr_sw_wash.setEnabled(true);

                } else {
                    ((MainActivity) getActivity()).comp_control = false;
                    fr_sw_wash.setChecked(false);
                    fr_sw_wash.setEnabled(false);
                    ((MainActivity) getActivity()).actionflag = false;   // 동작시간 타이머를 세는 쓰레드를 중지시키는 플래그
                    ((MainActivity) getActivity()).wash_control = false;
                    btn_setting.setEnabled(true);
                    //tv_alarm.setText("0");
                    fr_iv_post.setImageResource(R.drawable.action_state_nosign);

                    if (((MainActivity) getActivity()).sounds) {
                        fr_iv_post.setImageResource(R.drawable.action_state_stop);
                    }
                    //((MainActivity)getActivity()).sounds = false;
                    ((MainActivity) getActivity()).heartbeat1 = 0;
                    ((MainActivity) getActivity()).heartbeat2 = 0;
                }
            }
        });
*/
        fr_sw_comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //on일때 함수
                    ((MainActivity) getActivity()).comp_control = true;
                    fr_sw_wash.setEnabled(true);
                }

                else{
                    //off 일때 함수
                    ((MainActivity) getActivity()).comp_control = false;
                    fr_sw_wash.setChecked(false);
                    fr_sw_wash.setEnabled(false);
                    ((MainActivity) getActivity()).actionflag = false;   // 동작시간 타이머를 세는 쓰레드를 중지시키는 플래그
                    ((MainActivity) getActivity()).wash_control = false;
                    btn_setting.setEnabled(true);
                    //tv_alarm.setText("0");
                    fr_iv_post.setImageResource(R.drawable.action_state_nosign);

                    if (((MainActivity) getActivity()).sounds) {
                        fr_iv_post.setImageResource(R.drawable.action_state_stop);
                    }
                    //((MainActivity)getActivity()).sounds = false;
                    ((MainActivity) getActivity()).heartbeat1 = 0;
                    ((MainActivity) getActivity()).heartbeat2 = 0;
                }
            }
        });


        fr_sw_wash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fr_sw_wash.isChecked()) {
                    fr_tv_action_time.setText("00:00:00");
                    ((MainActivity) getActivity()).wash_control = true;
                    SimpleDateFormat simple = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String actiontime = simple.format(date);
                    ((MainActivity) getActivity()).actionflag = true;
                    ((MainActivity) getActivity()).ShowTimeMethod(actiontime);
                    btn_setting.setEnabled(false);

                    String[] check1 = ((MainActivity) getActivity()).flow1.split(",");
                    if (check1[0].equals("1")) {      //알람이 켜져있을경우 쓰레드 굴림
                        ((MainActivity) getActivity()).Alarm1();
                    }
                    String[] check2 = ((MainActivity) getActivity()).flow2.split(",");
                    if (check2[0].equals("1")) {      //알람이 켜져있을경우 쓰레드 굴림
                        ((MainActivity) getActivity()).Alarm2();
                    }
                    String[] check3 = ((MainActivity) getActivity()).flow3.split(",");
                    if (check3[0].equals("1")) {      //알람이 켜져있을경우 쓰레드 굴림
                        ((MainActivity) getActivity()).Alarm3();
                    }
                } else {
                    ((MainActivity) getActivity()).wash_control = false;
                    ((MainActivity) getActivity()).actionflag = false;
                    //tv_alarm.setText("0");
                    fr_iv_post.setImageResource(R.drawable.action_state_nosign);

                    if (((MainActivity) getActivity()).sounds) {
                        fr_iv_post.setImageResource(R.drawable.action_state_stop);
                    }
                    //((MainActivity)getActivity()).sounds = false;

                    ((MainActivity) getActivity()).heartbeat1 = 0;
                    ((MainActivity) getActivity()).heartbeat2 = 0;
                    btn_setting.setEnabled(true);

                    //fr_tv_action_time.setText("00:00:00");
                }
            }
        });


        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onFragmentChanged(2);
            }
        });

        tv_alarm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // tv_alarm가 "0"일 경우 알람 X,    "1"일 경우 알람 O.
                if (tv_alarm.getText().toString().equals("1")) {
                    fr_iv_post.setImageResource(R.drawable.action_state_stop);
                    if (!((MainActivity) getActivity()).sounds) {
                        // sounds가 false인 경우에 재생
                        ((MainActivity) getActivity()).soundOn();
                    }
                } else if (tv_alarm.getText().toString().equals("0")) {
                    fr_iv_post.setImageResource(R.drawable.action_state_nosign);
                    if (((MainActivity) getActivity()).sounds) {
                        //sounds가 true인 경우에 스탑
                        ((MainActivity) getActivity()).soundStop();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        tv_connect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트 변경 후
                if (tv_connect.getText().toString().equals("true")) {
                    iv_bluetoothstate.setImageResource(R.drawable.ble_on);
                } else {
                    iv_bluetoothstate.setImageResource(R.drawable.ble_off);
                    fr_iv_act.setImageResource(R.drawable.action_state_nosign);
                    fr_iv_stop.setImageResource(R.drawable.action_state_nosign);
                    fr_iv_post.setImageResource(R.drawable.action_state_nosign);
                }
            }
        });


        tv_flow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    fr_tv_flow.setText(tv_flow.getText().toString());
                    fr_tv_flow2.setText(tv_flow.getText().toString());

                    if (((MainActivity) getActivity()).mode.equals("1")) { //아날로그 방식일 경우에만 게이지 바늘 각도계산
                        float flow = Float.parseFloat(tv_flow.getText().toString());
                        flow = (flow * (float) 4.5) - 135;
                        if (flow <= 135) {   //각도가 135도 즉 유량값이 60.0 이하일때
                            analog_gauge_needle.setRotation(flow);
                            Thread.sleep(10);
                        } else {
                            analog_gauge_needle.setRotation(135);
                        }

                    }


                } catch (Exception e) {
                    Log.d("아날로그유량 각도문제 : ", e.getMessage());
                }
            }
        });


        tv_runningtime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fr_tv_action_time.setText(tv_runningtime.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        tv_acctime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fr_tv_acc_time.setText(tv_acctime.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        tv_comp_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_comp_state.getText().toString().equals("0") && tv_washstate.getText().toString().equals("0")) {
                    fr_iv_act.setImageResource(R.drawable.action_state_nosign);
                    fr_iv_stop.setImageResource(R.drawable.action_state_stop);
                } else if (tv_comp_state.getText().toString().equals("1") && tv_washstate.getText().toString().equals("0")) {
                    fr_iv_act.setImageResource(R.drawable.action_state_post);
                    fr_iv_stop.setImageResource(R.drawable.action_state_nosign);
                } else if (tv_comp_state.getText().toString().equals("1") && tv_washstate.getText().toString().equals("1")) {
                    fr_iv_act.setImageResource(R.drawable.action_state_act);
                    fr_iv_stop.setImageResource(R.drawable.action_state_nosign);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tv_washstate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_washstate.getText().toString().equals("0") && tv_comp_state.getText().toString().equals("0")) {
                    fr_iv_act.setImageResource(R.drawable.action_state_nosign);
                    fr_iv_stop.setImageResource(R.drawable.action_state_stop);
                } else if (tv_washstate.getText().toString().equals("1") && tv_comp_state.getText().toString().equals("0")) {
                    fr_iv_act.setImageResource(R.drawable.action_state_act);
                    fr_iv_stop.setImageResource(R.drawable.action_state_nosign);
                } else if (tv_washstate.getText().toString().equals("1") && tv_comp_state.getText().toString().equals("1")) {
                    fr_iv_act.setImageResource(R.drawable.action_state_act);
                    fr_iv_stop.setImageResource(R.drawable.action_state_nosign);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        return rootView;
    }



}
