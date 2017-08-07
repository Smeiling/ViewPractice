package com.demo.songmeiling.view.branchviewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.songmeiling.view.R;
import com.demo.songmeiling.view.branchviewpager.model.Article;

import java.util.List;

/**
 * Created by songmeiling on 2017/8/7.
 */

public class RankAdapter extends BaseAdapter {

    private Context mContext;
    private List<Article> articleList;
    private LayoutInflater inflater;

    public RankAdapter(Context context, List<Article> list) {
        mContext = context;
        articleList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int i) {
        return articleList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        Article article = articleList.get(i);
        if (view == null) {
            view = inflater.inflate(R.layout.rank_item_view, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvTitle.setText(article.getTitle());
        holder.tvTheme.setText(article.getTheme());
        holder.tvAuthor.setText(article.getAuthor());

        return view;
    }

    private class ViewHolder {
        private TextView tvTitle;
        private TextView tvTheme;
        private TextView tvAuthor;
        private ImageView ivImg;

        ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvTheme = (TextView) view.findViewById(R.id.tv_theme);
            tvAuthor = (TextView) view.findViewById(R.id.tv_author);
            ivImg = (ImageView) view.findViewById(R.id.iv_img);
        }

    }
}
