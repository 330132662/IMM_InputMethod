package com.example.administrator.imm;

import android.content.ActivityNotFoundException;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.administrator.imm.adapter.GridAdapter;
import com.example.administrator.imm.common.AppConfig;
import com.example.administrator.imm.http.EventClick;
import com.example.administrator.imm.http.ListApi;
import com.example.administrator.imm.http.TypeApi;
import com.example.administrator.imm.model.TypeResp;
import com.google.android.material.tabs.TabLayout;
import com.hjq.http.EasyHttp;
import com.hjq.http.lifecycle.ApplicationLifecycle;
import com.hjq.http.listener.OnHttpListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by yang.jianan on 2017/04/19 14:37.
 * 开发参考：http://blog.csdn.net/le_go/article/details/9264831
 * 输入法服务的生命周期图：http://img.blog.csdn.net/20130707203833640
 */

public class AndroidInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private String TAG = AndroidInputMethodService.class.getName();

    //    private KeyboardView keyboardView; // 对应keyboard.xml中定义的KeyboardView
    private Keyboard keyboard; // 对应qwerty.xml中定义的Keyboard
    private List<TypeResp.DataDTO> tabData;
    private GridAdapter gridAdapter;

    // 做了一些非UI方面的初始化，即字符串变量词汇分隔符的初始化
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        Log.d(TAG, "onCreate()");
    }

    private void initView() {

        reqType();
    }


    private List<String> tabList = new ArrayList<>();

    private View recyRoot;
    private int typeIdChoosed = -1;

    /**
     * 键盘 第一次现实的时候调用
     *
     * @return
     */
    @Override
    public View onCreateInputView() {
        // keyboard被创建后，将调用onCreateInputView函数
        /*keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);  // 此处使用了keyboard.xml
        keyboard = new Keyboard(this, R.xml.qwerty); // 此处使用了qwerty.xml
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this); *///注册键盘事件监听
        recyRoot = getLayoutInflater().inflate(R.layout.layout_recyclerview, null);
        tab_layout = recyRoot.findViewById(R.id.tab_layout);
        vp2 = recyRoot.findViewById(R.id.vp2);
        initView();
        initView1();


        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                  方案1  新请求  刷新recyclerview里的数据
                typeIdChoosed = tabData.get(tab.getPosition()).getId();
                toast("" + typeIdChoosed);
                reqList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Log.d(TAG, "onCreateInputView()");
        return recyRoot;
    }

    /*    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_hide) {
            // 隐藏软键盘
            hideWindow();
        } else {
            Button button = (Button) v;
            // 获得InputConnection对象, InputConnection由客户端控件创建，并传递给输入法应用，由输入法应用调用，进行信息反馈
            InputConnection inputConnection = getCurrentInputConnection();
            if (button.getId() == R.id.btn1) {
                // 设置预输入文本
                // setComposingText方法的第2个参数值为1，表示在当前位置预输入文本
                inputConnection.setComposingText(button.getText(), 1);
            } else {
                // 向当前获得焦点的EditText控件输出文本
                // commitText方法第2个参数值为1，表示在当前位置插入文本
                inputConnection.commitText(button.getText(), 1);
            }
        }
    }*/
    @Subscribe
    public void expressionClick(EventClick click) {
        final int pos = click.getPos();
//        Drawable choosed = biaoqing.get(pos);
//        send(choosed);
    }

    private void send(Drawable expPic) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            File cacheDir = getCacheDir();
            File file = new File(cacheDir, "temp_image.png");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri = FileProvider.getUriForFile(this, AppConfig.Companion.getPackageName() + ".provider", file);
// 结果示例：content://com.example.app.fileprovider/cache/temp_image.png
            InputConnection ic = getCurrentInputConnection();
            Bundle params = new Bundle();
            String mimeType = "image/png";
            ic.commitContent(new InputContentInfo(uri,        // 表情包的 Content URI
                    new ClipDescription(mimeType, new String[]{})            // 可选，用于标识来源
            ), InputConnection.INPUT_CONTENT_GRANT_READ_URI_PERMISSION, params);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");  // 根据实际图片类型调整 MIME
            intent.putExtra(Intent.EXTRA_STREAM, uri);

// 指定目标应用包名（例如微信）
            intent.setPackage(currentUsingPkg);

// 授予临时读取权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

// 启动分享
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // 处理目标应用未安装的情况
                toast("no this app");
            }
        } else {

        }
    }

    /**
     * 联想词条 第一次被现实的时候调用
     * 在要显示侯选词汇的视图时，由框架调用，和onCreateInputView类似，在这个方法中，对candidateview进行初始化
     *
     * @return
     */
    @Override
    public View onCreateCandidatesView() {
        Log.d(TAG, "onCreateCandidatesView()");
        return super.onCreateCandidatesView();
    }

    private String currentUsingPkg = "";

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        currentUsingPkg = info.packageName;
        Log.d(TAG, "onStartInputView 应用名称");
    }


    @Override
    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype);
        Log.d(TAG, "onCurrentInputMethodSubtypeChanged()");
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        Log.d(TAG, "onFinishInput()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //↓↓↓↓↓↓↓OnKeyboardActionListener接口对应的方法↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE: //删除键
                inputConnection.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DONE: //完成键
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                //hideWindow(); //隐藏键盘
                break;
            case -6:
                toast("1123");
                /*Keyboard keyboardE = new Keyboard(this, R.xml.emoji); // 此处使用了qwerty.xml
                keyboardView.setKeyboard(keyboardE);*/
                break;
            default: //普通输入
                char code = (char) primaryCode;
                inputConnection.commitText(String.valueOf(code), 1); //可以对输入的字符串做 加密等等处理
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取 表情包的 组
     */
    private void reqType() {
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(new TypeApi()).request(new OnHttpListener<TypeResp>() {
            @Override
            public void onSucceed(TypeResp result, boolean cache) {
                OnHttpListener.super.onSucceed(result, cache);
                Timber.d("onSucceed  cache ");
            }

            @Override
            public void onSucceed(TypeResp typeResp) {
                tabData = typeResp.getData();
                Timber.d("onSucceed  ");
                loadFragment();
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void reqList() {
        ListApi api = new ListApi();
//        api.setLast_id(0);
        api.setType_id(typeIdChoosed);
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(api).request(new OnHttpListener<TypeResp>() {
            @Override
            public void onSucceed(TypeResp result, boolean cache) {
                OnHttpListener.super.onSucceed(result, cache);
            }

            @Override
            public void onSucceed(TypeResp typeResp) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private TabLayout tab_layout;
    private ViewPager2 vp2;
    private List<Fragment> fragments;

    /**
     * todo 按照类型 数量 加载多个fragment
     */
    private void loadFragment() {
        fragments = new ArrayList<>();
        for (TypeResp.DataDTO dataDTO : tabData) {
            String name = dataDTO.getName();
            TabLayout.Tab t = tab_layout.newTab().setText(name);
            tab_layout.addTab(t);
//            fragments.add(new ExpFrag(dataDTO.getId()));
        }
        /*for (int i = 0; i < tabData.size(); i++) {
            TabLayout.Tab t = tab_layout.getTabAt(i);
            t.setText(tabData.get(i).getName());
        }*/
//        vp2.setAdapter(new MyFragmentAdapter( this, fragments));
        /*ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter();
        viewPager2Adapter.setDataList(fragments);
        vp2.setAdapter(viewPager2Adapter);*/

    }

    private RecyclerView recyclerView;

    private void initView1() {
        recyclerView = recyRoot.findViewById(R.id.list);
//        recyclerView.addItemDecoration(new GridSpaceDecoration1());
        gridAdapter = new GridAdapter();
//        gridAdapter.setDataList(biaoqing);
        recyclerView.setAdapter(gridAdapter);
    }

    /**
     * plan A: 用tab+ 单个recyclerview的方案
     */
    private void resulting1() {

    }
}
