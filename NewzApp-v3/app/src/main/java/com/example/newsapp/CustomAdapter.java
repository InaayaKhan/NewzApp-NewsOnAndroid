package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Models.Headlines;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Headlines> headlinesList;
    private SelectListener listener;
    DBHelper DB;
    SessionManagement sessionManagement;

    public CustomAdapter(Context context, List<Headlines> headlinesList, SelectListener listener) {
        this.context = context;
        this.headlinesList = headlinesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.headlines_list_items,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title = headlinesList.get(position).getTitle();
        holder.text_title.setText(title);
        holder.text_source.setText(headlinesList.get(position).getSource().getName());

        Instant instant = Instant.parse(headlinesList.get(position).getPublishedAt());
        ZoneId zid = ZoneId.of("UTC");
        ZonedDateTime zdt = instant.atZone(zid) ;
        Locale locale = Locale.ENGLISH ;
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT ).withLocale( locale ) ;
        String output = zdt.format( f ) ;
        holder.text_publishedAt.setText(output);

        if(headlinesList.get(position).getUrlToImage()!=null){
            Picasso.get().load(headlinesList.get(position).getUrlToImage()).into(holder.img_headline);
        }

        holder.text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnNewsClicked(headlinesList.get(position));
            }
        });

        holder.img_headline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnNewsClicked(headlinesList.get(position));
            }
        });

        holder.share_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = holder.text_title.getText().toString() + "For more such news install "+ "BreakingNews.com";
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                context.startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        holder.save_news.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                sessionManagement = new SessionManagement(context);
                DB = new DBHelper(context.getApplicationContext());
                Headlines headlines = headlinesList.get(position);
                if(sessionManagement.isLoggedIn())
                {
                    Integer user_id = DB.getUserId(sessionManagement.getUserDetails().get(SessionManagement.EMAIL));
                    String title = headlinesList.get(position).getTitle();
                    Boolean checkNews = DB.checkNews(title, user_id);
                    if (checkNews == false) {
                        Boolean insert = DB.insertNews(user_id, headlines.getTitle(), headlines.getSource().getName(), headlines.getUrlToImage(), headlines.getAuthor(),headlines.getUrl(), headlines.getDescription(), headlines.getContent(), headlines.getPublishedAt());
                        if (insert != null) {
                            Toast.makeText(view.getContext(), "Saved News Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(), "Failed to Save News", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "News Already Saved", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(view.getContext(),"Please login first",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return headlinesList.size();
    }
}
