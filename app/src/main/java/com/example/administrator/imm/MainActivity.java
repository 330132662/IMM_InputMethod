package com.example.administrator.imm;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.example.administrator.imm.common.AppActivity;
import com.example.administrator.imm.ui.G1Act;

/**
 * Created by yang.jianan on 2017/04/19 14:37.
 * desc：这里可以做很多东西， 比方说 搜狗输入法， 这个负责引导， 以及后续的皮肤，词库，等等功能
 */
public class MainActivity extends AppActivity {
    private AppCompatButton btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(G1Act.class);
            }
        });
    }


}
