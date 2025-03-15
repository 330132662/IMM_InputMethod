package com.example.administrator.imm;

import android.content.ActivityNotFoundException;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.imm.adapter.GridAdapter;
import com.example.administrator.imm.adapter.RecyTabAdapter;
import com.example.administrator.imm.common.AppConfig;
import com.example.administrator.imm.http.EventClick;
import com.example.administrator.imm.http.EventTypeChoose;
import com.example.administrator.imm.http.ListApi;
import com.example.administrator.imm.http.TypeApi;
import com.example.administrator.imm.model.ListResp;
import com.example.administrator.imm.model.TypeResp;
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

import okhttp3.Call;
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
    private List<ListResp.DataDTO> expList;

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
    /**
     * 表情名称
     */
    private RecyclerView recy_tab;
    private int typeIdChoosed = -1;
    private int fixedHeight = 240;// dp值
    private RecyTabAdapter adapter;

    /**
     * 键盘 第一次现实的时候调用
     *
     * @return
     */
    @Override
    public View onCreateInputView() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float heightF = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, fixedHeight, metrics);
//        int heightPx = Integer.parseInt(heightF + "");
        int heightPx = (int) heightF;
        Timber.i("heightPx = " + heightPx);
        // 设置窗口参数
        /*Window window = getWindow().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = heightPx;
            params.verticalMargin = 0f;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }*/


        // keyboard被创建后，将调用onCreateInputView函数
        /*keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);  // 此处使用了keyboard.xml
        keyboard = new Keyboard(this, R.xml.qwerty); // 此处使用了qwerty.xml
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this); *///注册键盘事件监听
        recyRoot = getLayoutInflater().inflate(R.layout.layout_recyclerview, null);
//        tab_layout = recyRoot.findViewById(R.id.tab_layout);
        recy_tab = recyRoot.findViewById(R.id.recy_tab);
        recy_tab.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 1;
                outRect.bottom = 1;
                outRect.left = 20;
                outRect.right = 2;
            }
        });
        adapter = new RecyTabAdapter(this);

        recy_tab.setAdapter(adapter);
//        dsl_layout = recyRoot.findViewById(R.id.dsl_layout);
//        vp2 = recyRoot.findViewById(R.id.vp2);
        initView();
        initView1();


        //            注意  虽然没用到  但是删了之后 其他空间就显示不出来的了！
        /*tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                  方案1  新请求  刷新recyclerview里的数据

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
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
    public void expressionTypeClick(EventTypeChoose click) {
//   表情包切换 背景对应切换
        int size = tabData.size();
        int choosedIndex = click.getPos();
        for (int i = 0; i < size; i++) {
            TypeResp.DataDTO item = tabData.get(i);
            item.setSelected(choosedIndex == i);
        }
        adapter.setDataList(tabData);

        typeIdChoosed = tabData.get(choosedIndex).getId();
        reqList();
    }

    private Bitmap chooseBitmap = null;

    @Subscribe
    public void expressionClick(EventClick click) {
        final int pos = click.getPos();
        chooseBitmap = click.getImg();
//        Drawable choosed = biaoqing.get(pos);
        send(pos);
    }

    private void send(int pos) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            /*Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();*/
            File cacheDir = getCacheDir();
            File file = new File(cacheDir, "temp_image.jpg");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                chooseBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
          /*  String imageUrl = expList.get(pos).getIcon();
            if (!imageUrl.startsWith("http")) {
                imageUrl = AppConfig.Companion.getHostUrl() + imageUrl;
            }
            if (ic != null) {
                // 方式1：发送图片URL（通用方案）
                ic.commitText(imageUrl, 1);
            }*/


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
        Log.d(TAG, "onStartInputView 应用包名 " + currentUsingPkg);
//        updateInputViewHeight();
    }

    private void updateInputViewHeight() {
        Window window = getWindow().getWindow();
        if (window == null) return;

//        View rootView = window.getDecorView().findViewById(R.id.root_view);
        if (recyRoot == null) return;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, fixedHeight, metrics);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) recyRoot.getLayoutParams();
        params.height = heightPx;
        Timber.i("高度2 = " + heightPx);
        recyRoot.setLayoutParams(params);
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


    /**
     * 获取 表情包的 组
     */
    private void reqType() {
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(new TypeApi()).request(new OnHttpListener<TypeResp>() {
            @Override
            public void onSucceed(TypeResp result, boolean cache) {
                OnHttpListener.super.onSucceed(result, cache);
                Timber.d("onSucceed  cache ");
                tabData = result.getData();

            }

            @Override
            public void onSucceed(TypeResp typeResp) {
                tabData = typeResp.getData();
                Timber.d("onSucceed  ");

            }

            @Override
            public void onEnd(Call call) {
                OnHttpListener.super.onEnd(call);
//                loadFragment();
                loadTab();
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void loadTab() {
        adapter.setDataList(tabData);
        recy_tab.setAdapter(adapter);
        if (tabData != null) {
            typeIdChoosed = tabData.get(0).getId();
            reqList();
        } else {
            toast("No Data");
        }

    }

    /**
     * 表情包下的表情列表
     */
    private void reqList() {
        ListApi api = new ListApi();
//        api.setLast_id(0);
        api.setType_id(typeIdChoosed);
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(api).request(new OnHttpListener<ListResp>() {
            @Override
            public void onSucceed(ListResp result, boolean cache) {
                OnHttpListener.super.onSucceed(result, cache);
                expList = result.getData();
            }

            @Override
            public void onSucceed(ListResp typeResp) {
                expList = typeResp.getData();
            }

            @Override
            public void onEnd(Call call) {
                OnHttpListener.super.onEnd(call);
                refreshExpList();
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void refreshExpList() {
        gridAdapter.setDataList(expList);
        recyclerView.setAdapter(gridAdapter);
        adapter.setDataList(tabData);
        recy_tab.setAdapter(adapter);

    }

    /*private TabLayout tab_layout;
    private DslTabLayout dsl_layout;
    private ViewPager2 vp2;
    private List<Fragment> fragments;*/

    /**
     * 按照类型 数量 加载多个fragment
     */
    private void loadFragment() {
//        fragments = new ArrayList<>();
        /*for (TypeResp.DataDTO dataDTO : tabData) {
            String name = dataDTO.getName();
            TabLayout.Tab t = tab_layout.newTab().setText(name);
            tab_layout.addTab(t);
//            fragments.add(new ExpFrag(dataDTO.getId()));


        }*/

//        vp2.setAdapter(new MyFragmentAdapter( this, fragments));
        /*ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter();
        viewPager2Adapter.setDataList(fragments);
        vp2.setAdapter(viewPager2Adapter);*/

    }

    /**
     * 表情包 下的表情列表
     */
    private RecyclerView recyclerView;

    private void initView1() {
        recyclerView = recyRoot.findViewById(R.id.list);
//        recyclerView.addItemDecoration(new GridSpaceDecoration1());
        gridAdapter = new GridAdapter(this);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        recyclerView.setAdapter(gridAdapter);
        reqList();
    }

    /**
     * plan A: 用tab+ 单个recyclerview的方案
     */
    private void resulting1() {

    }

    //↓↓↓↓↓↓↓OnKeyboardActionListener接口对应的方法↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

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
}
