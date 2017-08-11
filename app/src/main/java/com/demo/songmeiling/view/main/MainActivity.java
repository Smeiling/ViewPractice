package com.demo.songmeiling.view.main;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.demo.songmeiling.view.utils.DensityUtil;
import com.demo.songmeiling.view.utils.LogUtil;
import com.demo.songmeiling.view.utils.SystemBarTintManager;
import com.demo.songmeiling.view.R;
import com.demo.songmeiling.view.views.SlideLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    public static final String TAG = "SML";

    private SystemBarTintManager tintManager;
    private ListView lv;
    public List<String> alphabet = new ArrayList<String>();
    private List<ViewItem> views;

    private LinearLayout header;

    private SlideLayout layout;
    float dX, dY, uX, uY;
    private int validDistance;

    private EditText et_search;
    private TextView tv_cancel;

    private int screenWidth;
    private int screenHeight;

    private boolean show = true;

    private GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        getScreenParams();
        setData();
        validDistance = ViewConfiguration.get(this).getScaledTouchSlop();
        header = (LinearLayout) findViewById(R.id.ll_header);
        TextView title = (TextView) header.findViewById(R.id.tv_header);

        title.setText("View List");


        lv = (ListView) findViewById(R.id.lv);
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtil.w(TAG, "LV DOWN");
                        dX = ev.getX();
                        dY = ev.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //LogUtil.w(TAG, "LV MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        LogUtil.w(TAG, "LV UP");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public Object getItem(int i) {
                return views.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = getLayoutInflater().inflate(R.layout.textview, null);
                TextView tv = (TextView) view.findViewById(R.id.tv);
                tv.setText(views.get(i).getTitle());
                tv.setBackgroundColor(Color.parseColor(views.get(i).getColor()));
                return view;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.w(TAG, i + " Item clicked");
                Intent intent = new Intent();
                intent.setAction(views.get(i).getAction());
                startActivity(intent);
            }
        });
        lv.setVisibility(View.GONE);

        header.setVisibility(View.GONE);

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lv.getVisibility() == View.VISIBLE) {
                    hideSearchPannel();
                }
            }
        });
        et_search = (EditText) findViewById(R.id.btn_anim);
        et_search.getLayoutParams().width = screenWidth * 2 - 40;
        et_search.setPadding((screenWidth * 2 - 40 - 80) / 2, 10, 10, 10);
        et_search.setFocusable(false);
        et_search.setOnTouchListener(this);


        parentLayout();

        initWindow();
    }

    private void getScreenParams() {
        WindowManager wm = this.getWindowManager();

        screenWidth = DensityUtil.px2dip(this, wm.getDefaultDisplay().getWidth());
        screenHeight = DensityUtil.px2dip(this, wm.getDefaultDisplay().getHeight());
        Log.w(TAG, "screenWidth = " + screenWidth);
    }

    private void parentLayout() {
        layout = (SlideLayout) findViewById(R.id.ll);
        layout.setOnSlideListener(new SlideLayout.OnSlideListener() {
            @Override
            public void onSlide(int slideMode) {
                if (slideMode == SlideLayout.SLIDE_DOWN) {
                    Log.w(TAG, "down slide");
                    header.setVisibility(View.VISIBLE);
                } else if (slideMode == SlideLayout.SLIDE_UP) {
                    Log.w(TAG, "up slide");
                    header.setVisibility(View.GONE);
                } else if (slideMode == SlideLayout.SLIDE_LEFT) {
                    Log.w(TAG, "left slide");
                } else if (slideMode == SlideLayout.SLIDE_RIGHT) {
                    Log.w(TAG, "right slide");
                }
            }

            @Override
            public void onMoving(int distance) {

            }
        });
    }

    private void setData() {
        try {
            Gson gson = new GsonBuilder().create();
            InputStream is = getResources().openRawResource(R.raw.views);
            InputStreamReader isReader = null;
            isReader = new InputStreamReader(is, "UTF-8");
            views = gson.fromJson(isReader, new TypeToken<List<ViewItem>>() {
            }.getType());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            views = new ArrayList<ViewItem>();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            //tintManager.setStatusBarTintColor(getColor(0x00991199));
            tintManager.setStatusBarAlpha(1);
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    private void showSearchPannel() {
        Log.w(TAG, "show");
        lv.setVisibility(View.VISIBLE);
        lv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_from_bottom));
        tv_cancel.setVisibility(View.VISIBLE);
        tv_cancel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_from_right));
        performAnimate(true);
    }

    private void hideSearchPannel() {
        Log.w(TAG, "hide");
        lv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_to_bottom));
        lv.setVisibility(View.GONE);
        tv_cancel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_to_right));
        tv_cancel.setVisibility(View.GONE);

        performAnimate(false);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.btn_anim && lv.getVisibility() == View.GONE) {
            showSearchPannel();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) et_search.getLayoutParams();
            Log.w(TAG, "params = " + params.weight + "," + params.width + "," + params.height);
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainService.class);
        switch (view.getId()) {
            case R.id.btn_start:
                Log.d("MainService", "onStart clicked");
                startService(intent);
                break;
            case R.id.btn_stop:
                Log.d("MainService", "onStop clicked");
                stopService(intent);
                break;
            default:
                break;
        }

    }

    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            Log.w(TAG, "setWidth = " + width);
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

    }

    private void performAnimate(boolean expand) {
        ViewWrapper wrapper = new ViewWrapper(et_search);
        Log.w(TAG, "screenWidth = " + screenWidth);
        if (expand) {
            ObjectAnimator.ofInt(wrapper, "width", screenWidth * 2 - 40 - 80).setDuration(500).start();
        } else {
            ObjectAnimator.ofInt(wrapper, "width", screenWidth * 2 - 40).setDuration(500).start();
        }
    }
}
