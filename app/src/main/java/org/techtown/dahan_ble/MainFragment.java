package org.techtown.dahan_ble;

import static org.techtown.dahan_ble.MainActivity.comp_control;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

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

        //프레그먼트의 위젯
        TextView fr_tv_flow = rootView.findViewById(R.id.fr_tv_flow);
        TextView fr_tv_action_time = rootView.findViewById(R.id.fr_tv_action_time);
        TextView fr_tv_acc_time = rootView.findViewById(R.id.fr_tv_acc_time);
        Button btn_setting = rootView.findViewById(R.id.btn_setting);
        Switch fr_sw_comp = rootView.findViewById(R.id.fr_sw_comp);
        Switch fr_sw_wash = rootView.findViewById(R.id.fr_sw_wash);
        ImageView iv_bluetoothstate = (ImageView) rootView.findViewById(R.id.iv_bluetoothstate);
        ImageView fr_iv_act = (ImageView) rootView.findViewById(R.id.fr_iv_act);
        ImageView fr_iv_stop = (ImageView) rootView.findViewById(R.id.fr_iv_stop);
        ImageView fr_iv_post = (ImageView) rootView.findViewById(R.id.fr_iv_post);
        RadioButton rb_cleanpower = (RadioButton) rootView.findViewById(R.id.rb_cleanpower);
        RadioButton rb_cleanpower2 = (RadioButton) rootView.findViewById(R.id.rb_cleanpower2);
        RadioButton rb_cleanpower3 = (RadioButton) rootView.findViewById(R.id.rb_cleanpower3);
        RadioButton rb_cleanpower4 = (RadioButton) rootView.findViewById(R.id.rb_cleanpower4);

        //이니셜라이징
        if(tv_connect.getText().toString().equals("true")) {
            iv_bluetoothstate.setImageResource(R.drawable.ble_on);
        } else {
            iv_bluetoothstate.setImageResource(R.drawable.ble_off);
        }
        if(((MainActivity)getActivity()).comp_control) {
            fr_sw_comp.setChecked(true);
        } else {
            fr_sw_comp.setChecked(false);
        }
        if(((MainActivity)getActivity()).wash_control) {
            fr_sw_wash.setChecked(true);
        } else {
            fr_sw_wash.setChecked(false);
        }
        fr_tv_action_time.setText(tv_runningtime.getText().toString());
        fr_tv_acc_time.setText(tv_acctime.getText().toString());
        rb_cleanpower.setText(((MainActivity)getActivity()).cleanPower);
        rb_cleanpower2.setText(((MainActivity)getActivity()).cleanPower2);
        rb_cleanpower3.setText(((MainActivity)getActivity()).cleanPower3);
        rb_cleanpower4.setText(((MainActivity)getActivity()).cleanPower4);

        rb_cleanpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).radioButtoncheck = "0";
            }
        });
        rb_cleanpower2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).radioButtoncheck = "1";
            }
        });
        rb_cleanpower3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).radioButtoncheck = "2";
            }
        });
        rb_cleanpower4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).radioButtoncheck = "3";
            }
        });


        fr_sw_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fr_sw_comp.isChecked()){
                    ((MainActivity)getActivity()).comp_control=true;
                } else {
                    ((MainActivity)getActivity()).comp_control=false;
                }
            }
        });

        fr_sw_wash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fr_sw_wash.isChecked()){
                    ((MainActivity)getActivity()).wash_control=true;
                    SimpleDateFormat simple = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String actiontime = simple.format(date);
                    ((MainActivity)getActivity()).actionflag = true;
                    ((MainActivity)getActivity()).ShowTimeMethod(actiontime);
                } else {
                    ((MainActivity)getActivity()).wash_control=false;
                    ((MainActivity)getActivity()).actionflag = false;
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

        tv_connect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트 변경 후
                if(tv_connect.getText().toString().equals("true"))
                {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                fr_tv_flow.setText(tv_flow.getText().toString());
            }
        });

        tv_comp_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(tv_comp_state.getText().toString().equals("0")){

                } else if (tv_comp_state.getText().toString().equals("1")){
                    /*      이거아님
                    fr_iv_act.setImageResource(R.drawable.action_state_act);
                    fr_iv_stop.setImageResource(R.drawable.action_state_nosign);
                    fr_iv_post.setImageResource(R.drawable.action_state_nosign);
                    */
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


        return rootView;
    }


}