package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Models.Headlines;
import com.example.newsapp.Models.SavedNewsHeadlines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsHolder> {

    private Context context;
    private List<SavedNewsHeadlines> headlinesList;
    private SelectListener listener;
    DBHelper DB;
    SessionManagement sessionManagement;

    public SavedNewsAdapter(Context context, List<SavedNewsHeadlines> headlinesList, SelectListener listener) {
        this.context = context;
        this.headlinesList = headlinesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SavedNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedNewsHolder(LayoutInflater.from(context).inflate(R.layout.saved_news_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text_title.setText(headlinesList.get(position).getTitle());
        holder.text_source.setText(headlinesList.get(position).getSource());

        if(headlinesList.get(position).getUrlToImage()!=null) {
            Picasso.get().load(headlinesList.get(position).getUrlToImage()).into(holder.img_headline);
        }

        holder.text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSavedNewsClicked(headlinesList.get(position));
            }
        });

        holder.img_headline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSavedNewsClicked(headlinesList.get(position));
            }
        });

        holder.share_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = holder.text_title.getText().toString() + " For more such news install "+ "BreakingNews.com";
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                context.startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        holder.unsave_news.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                sessionManagement = new SessionManagement(context);
                DB = new DBHelper(context.getApplicationContext());
                Integer user_id = DB.getUserId(sessionManagement.getUserDetails().get(SessionManagement.EMAIL));
                String title = headlinesList.get(position).getTitle();
                int deleteNews = DB.deleteNews(user_id,title);
                if(deleteNews == 1){
                    Toast.makeText(view.getContext(), "Unsaved News Successfully", Toast.LENGTH_SHORT).show();
                    if(DB.getNewsList(user_id).size() <=0 ){
                        Intent home = new Intent(context,MainActivity.class);
                        context.startActivity(home);
                    }else{
                        Intent refresh = new Intent(context, SavedNewsActivity.class);
                        context.startActivity(refresh);
                    }
                }else{
                    Toast.makeText(view.getContext(), "Failed to Unsave News", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return headlinesList.size();
    }
}
