package com.demo.songmeiling.view.branchviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.songmeiling.view.R;
import com.demo.songmeiling.view.branchviewpager.model.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by songmeiling on 2017/8/7.
 */

public class RankListFragment extends Fragment {


    @BindView(R.id.tv_current_hot)
    TextView tvCurrentHot;
    @BindView(R.id.lv_rank)
    ListView lvRank;
    Unbinder unbinder;
    private List<Article> articleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rank_list_view, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTestData();
        if (articleList != null) {
            RankAdapter adapter = new RankAdapter(getContext(), articleList);
            lvRank.setAdapter(adapter);
        }
    }

    private void initTestData() {
        articleList = new ArrayList<>();
        Article article;
        for (int i = 0; i < 5; i++) {
            article = new Article();
            article.setAuthor("Smeiling");
            article.setTitle("My Title");
            article.setTheme("My Theme");
            articleList.add(article);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
