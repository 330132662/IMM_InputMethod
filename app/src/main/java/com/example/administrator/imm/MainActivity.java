package com.example.administrator.imm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by yang.jianan on 2017/04/19 14:37.
 * desc：这里可以做很多东西， 比方说 搜狗输入法， 这个负责引导， 以及后续的皮肤，词库，等等功能
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toast("已更新");
        finish();
    }

    private void toast(String s) {
        Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();
    }
}
