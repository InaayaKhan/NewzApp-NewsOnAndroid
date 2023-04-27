package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.Models.Headlines;
import com.example.newsapp.Models.SavedNewsHeadlines;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

public class DetailSavedNewsActivity extends AppCompatActivity {
    SavedNewsHeadlines headlines;
    TextView txt_title,txt_author,txt_time,txt_detail,txt_content,txt_url;
    ImageView img_news;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        txt_title=findViewById(R.id.text_detail_title);
        txt_author=findViewById(R.id.text_detail_author);
        txt_detail=findViewById(R.id.text_detail_detail);
        txt_content=findViewById(R.id.text_detail_content);
        txt_time=findViewById(R.id.text_detail_time);
        img_news=findViewById(R.id.img_detail_news);
        txt_url=findViewById(R.id.text_detail_url);
        toolbar = findViewById(R.id.tool);

        headlines = (SavedNewsHeadlines) getIntent().getSerializableExtra("data");

        Instant instant = Instant.parse(headlines.getPublishedAt());
        ZoneId zid = ZoneId.of("UTC");
        ZonedDateTime zdt = instant.atZone(zid) ;
        Locale locale = Locale.ENGLISH ;
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.MEDIUM ).withLocale( locale ) ;
        String output = zdt.format( f ) ;

        txt_title.setText(headlines.getTitle());
        if(headlines.getAuthor() == null){
            txt_author.setText("Source: Anonymous");
        }else{
            txt_author.setText("Source: "+headlines.getAuthor());
        }
        txt_time.setText(output);
        txt_detail.setText(headlines.getDescription());
        txt_content.setText(headlines.getContent());
        if(headlines.getUrlToImage()!=null){
            Picasso.get().load(headlines.getUrlToImage()).into(img_news);
        }else{
            img_news.setImageAlpha(R.drawable.ic_baseline_image_not_supported_24);
        }

        if(!headlines.getUrl().equals("")){
            txt_url.setText("Click for More Information");
            txt_url.setTextColor(Color.BLUE);

            txt_url.setOnClickListener(new View.OnClickListener() {
                Intent browserIntent;
                @Override
                public void onClick(View view) {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(headlines.getUrl()));
                    startActivity(browserIntent);
                }
            });
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}