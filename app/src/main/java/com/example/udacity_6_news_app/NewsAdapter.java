package com.example.udacity_6_news_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    // Declaring Views
    ImageView article_image;
    TextView articleTitleView, articleAuthorView, articleSectionView, articlePublishDateView;

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_news_item, parent, false);
        } // End listItemView == null

        // Initializing views
        articleTitleView = listItemView.findViewById(R.id.article_title_textview);
        articleAuthorView = listItemView.findViewById(R.id.article_author_textview);
        articleSectionView = listItemView.findViewById(R.id.article_category_textview);
        articlePublishDateView = listItemView.findViewById(R.id.article_date_textview);
        article_image = listItemView.findViewById(R.id.article_image);

        final News currentNews = getItem(position);

        if (currentNews != null) {
            articleTitleView.setText(currentNews.getArticle_title());
            articleSectionView.setText(currentNews.getArticle_section());
            articlePublishDateView.setText(format(currentNews.getArticle_date()));
            articleAuthorView.setText(format(currentNews.getArticle_authors()));
            loadImage(currentNews.getArticle_thumbnail(), article_image);

            // Set onClick listener for listItem
            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retrieve item from user clicked position
                    News currentNews = getItem(position);

                    // Open news article in Chrome
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(currentNews.getArticle_url())));
                    getContext().startActivity(websiteIntent);
                }
            });
        } else {
            // If currentNews is null, then inform the user that there is an issue
            Toast.makeText(getContext(), "Issue retrieving news", Toast.LENGTH_SHORT).show();
        }

        return listItemView;
    }

    // Format the date
    private String format(String str) {
        String[] parts = str.split("T");
        return parts[0];
    }

    // Load image and place into article_image ImageView
    private void loadImage(String url, ImageView contentImage) {
        if (url != null) {
            Picasso.with(getContext()).load(url).placeholder(R.drawable.news_image_placeholder).into(article_image);
        } else {
            contentImage.setImageResource(R.drawable.news_image_placeholder);
        }
    }

}



