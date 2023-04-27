package com.example.newsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView text_title,text_source,text_publishedAt;
    ImageView img_headline,save_news,share_news;
    CardView cardView;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        text_title = itemView.findViewById(R.id.text_title);
        text_source = itemView.findViewById(R.id.text_source);
        text_publishedAt = itemView.findViewById(R.id.headline_publishedAt);
        img_headline = itemView.findViewById(R.id.headline_image);
        cardView = itemView.findViewById(R.id.main_container);
        save_news = itemView.findViewById(R.id.save_news);
        share_news = itemView.findViewById(R.id.share_news);
    }
}
