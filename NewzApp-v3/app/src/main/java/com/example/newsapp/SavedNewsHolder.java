package com.example.newsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SavedNewsHolder extends RecyclerView.ViewHolder {

    TextView text_title,text_source;
    ImageView img_headline,unsave_news,share_news;
    CardView cardView;

    public SavedNewsHolder(@NonNull View itemView) {
        super(itemView);

        text_title = itemView.findViewById(R.id.text_title);
        text_source = itemView.findViewById(R.id.text_source);
        img_headline = itemView.findViewById(R.id.headline_image);
        cardView = itemView.findViewById(R.id.main_container1);
        unsave_news = itemView.findViewById(R.id.unsave_news);
        share_news = itemView.findViewById(R.id.share_news);
    }
}
