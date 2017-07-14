package com.demo.songmeiling.view.recyclerview;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.demo.songmeiling.view.R;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewActivity extends Activity implements GroupListener {

    private static RecyclerView recyclerview;
    private RecycleViewAdapter mAdapter;
    private List<String> textItems = new ArrayList<String>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private int titleIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initView();//初始化布局
        setListener();//设置监听事件
        new GetData().execute(10);  //执行加载数据

    }

    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.grid_recycler);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerview.setAdapter(new RecycleViewAdapter(RecycleViewActivity.this, textItems));
        recyclerview.addItemDecoration(new StickyDecoration());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.grid_swipe_refresh);
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    private void setListener() {
        //swipeRefreshLayout刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.w("sml", "onRefresh");
                titleIndex++;
                new GetData().execute(10);
            }
        });
    }

    @Override
    public String getGroupName(int position) {
        return "TITLE" + position / 10;
    }


    private class GetData extends AsyncTask<Integer, Integer, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置swipeRefreshLayout为刷新状态
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<String> doInBackground(Integer... params) {
            for (int i = 0; i < params[0]; i++) {
                textItems.add(String.valueOf(i));
            }
            return textItems;
        }

        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            if (result.size() > 0) {
                Log.d("sml", result.toString());
                if (mAdapter == null) {
                    recyclerview.setAdapter(mAdapter = new RecycleViewAdapter(RecycleViewActivity.this, textItems));//recyclerview设置适配器
                    //实现适配器自定义的点击监听
                    mAdapter.setOnItemClickListener(new RecycleViewAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position = recyclerview.getChildAdapterPosition(view);
                            Log.w("SML", "click position = " + position);
                        }

                        @Override
                        public void onItemLongClick(View view) {
                            int position = recyclerview.getChildAdapterPosition(view);
                            Log.w("SML", "long click position = " + position);
                        }
                    });
                } else {
                    //让适配器刷新数据
                    mAdapter.notifyDataSetChanged();
                }
            }
            //停止swipeRefreshLayout加载动画
            Log.w("sml", "setRefreshing false");
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public class RecycleDecoration extends RecyclerView.ItemDecoration {

        private int mHeight = 50;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if (position % 10 == 0) {
                outRect.set(0, mHeight, 0, 0);
            }
        }

    }


    public class StickyDecoration extends RecyclerView.ItemDecoration {

        private int mGroupHeight = 50;

        private boolean isFirstInGroup(int pos) {

            if (pos % 10 != 0) {
                return false;
            } else {
                return true;
            }
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            String groupId = getGroupName(pos);
            if (groupId == null) return;
            if (pos == 0 || isFirstInGroup(pos)) {
                outRect.top = mGroupHeight;
            }
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int itemCount = state.getItemCount();
            final int childCount = parent.getChildCount();
            final int left = parent.getLeft() + parent.getPaddingLeft();
            final int right = parent.getRight() - parent.getPaddingRight();
            String preGroupName;
            String currentGroupName = null;
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(view);
                preGroupName = currentGroupName;
                currentGroupName = getGroupName(position);
                if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName)) {
                    continue;
                }
                int viewBottom = view.getBottom();
                float top = Math.max(mGroupHeight, view.getTop());
                if (position + 1 < itemCount) {
                    String nextGroupName = getGroupName(position + 1);
                    if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                        top = viewBottom;
                    }
                }
                Paint mGroupPaint = new Paint();
                mGroupPaint.setColor(Color.CYAN);
                c.drawRect(left, top - mGroupHeight, right, top, mGroupPaint);
                Paint mTextPaint = new Paint();
                mTextPaint.setARGB(120, 120, 99, 120);
                Paint.FontMetrics fm = mTextPaint.getFontMetrics();

                float baseLine = top - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;
                c.drawText(currentGroupName, left, baseLine, mTextPaint);
            }
        }
    }

}