package com.example.udacity_6_news_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    ImageView article_image;


    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_news_item, parent, false);
        }

        final News currentNews = getItem(position);

        TextView articleTitleView = listItemView.findViewById(R.id.article_title_textview);
        TextView articleAuthorView = listItemView.findViewById(R.id.article_author_textview);
        TextView articleSectionView = listItemView.findViewById(R.id.article_category_textview);
        TextView articlePublishDateView = listItemView.findViewById(R.id.article_date_textview);
        article_image = listItemView.findViewById(R.id.article_image);

        if(currentNews != null){
            articleTitleView.setText(currentNews.getArticle_title());
            articleSectionView.setText(currentNews.getArticle_section());
            articlePublishDateView.setText(formatDate(currentNews.getArticle_date()));
            articleAuthorView.setText(formatDate(currentNews.getArticle_authors()));
            loadImageFromUrl(currentNews.getArticle_thumbnail(), article_image);

            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News currentNews = getItem(position);
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(currentNews.getArticle_url())));
                    getContext().startActivity(websiteIntent);
                }
            });
        } else{
            Toast.makeText(getContext(), "Issue retrieving news", Toast.LENGTH_SHORT).show();
        }

        return listItemView;
    }

    private String formatDate(String str) {
        String[] parts = str.split("T");
        return parts[0];
    }


    private void loadImageFromUrl(String url, ImageView contentImage) {
        if(url != null) {
            Picasso.with(getContext()).load(url).placeholder(R.drawable.news_image_placeholder).into(article_image);
        }else{
            contentImage.setImageResource(R.drawable.news_image_placeholder);
        }
    }

}



