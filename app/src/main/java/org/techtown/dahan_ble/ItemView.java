package org.techtown.dahan_ble;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ItemView extends LinearLayout {
    TextView item_name, item_macadd;

    public ItemView(Context context) {
        super(context);
        init(context);  // 인플레이션하여 붙여주는 역할
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {super(context, attrs);}


    //객체(listview_item.xml 레이아웃)를 인플레이션 하여 붙여줌
    //LayoutInflater를 써서 시스템 서비스를 참조할 수 있음
    //단말이 켜졌을 때 기본적으로 백그라운드에서 실행시키는 것을 시스템 서비스라고 함
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.listview_item, this, true);

        item_name = findViewById(R.id.item_name);
        item_macadd = findViewById(R.id.item_macadd);
    }


    public void setName(String name) { item_name.setText(name); }
    public void setMacadd(String macadd) {
        item_macadd.setText(macadd);
    }
}
