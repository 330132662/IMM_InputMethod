package com.example.administrator.imm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.administrator.imm.common.AppActivity;
import com.example.administrator.imm.ui.G1Act;
import com.google.android.material.textview.MaterialTextView;

/**
 * Created by yang.jianan on 2017/04/19 14:37.
 * desc：这里可以做很多东西， 比方说 搜狗输入法， 这个负责引导， 以及后续的皮肤，词库，等等功能
 */
public class MainActivity extends AppActivity {
    private AppCompatButton btn_submit;
    private MaterialTextView tv_inputmng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_submit = findViewById(R.id.btn_submit);
        tv_inputmng = findViewById(R.id.tv_inputmng);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(G1Act.class);
            }
        });
        tv_inputmng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 方式 1：直接打开输入法列表
                Intent generalIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                try {
                    startActivity(generalIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "无法打开输入法设置", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
