package org.techtown.dahan_ble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        //이니셜라이징
        if(tv_connect.getText().toString().equals("true")) {
            iv_bluetoothstate.setImageResource(R.drawable.ble_on);
        } else {
            iv_bluetoothstate.setImageResource(R.drawable.ble_off);
        }


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


        return rootView;
    }

}