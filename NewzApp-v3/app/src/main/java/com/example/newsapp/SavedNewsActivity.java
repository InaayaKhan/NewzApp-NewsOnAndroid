package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.Models.Headlines;
import com.example.newsapp.Models.NewsAPIResponse;
import com.example.newsapp.Models.SavedNewsHeadlines;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class SavedNewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SelectListener,View.OnClickListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView recyclerView;
    SavedNewsAdapter savedNewsAdapter;
    SessionManagement sessionManagement;
    TextView userNameText,userEmailText;
    ProgressDialog dialog;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);

        dialog=new ProgressDialog(this);
        dialog.setTitle("Fetching saved news articles...");
        dialog.show();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool);
        View headerview = navigationView.getHeaderView(0);
        userNameText = (TextView) headerview.findViewById(R.id.userNameText);
        userEmailText = (TextView) headerview.findViewById(R.id.userEmailText);
        DB = new DBHelper(getApplicationContext());

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open_drawer,R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.bringToFront();
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        sessionManagement = new SessionManagement(this);

        if( sessionManagement.isLoggedIn()){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_register).setVisible(false);
            menu.findItem(R.id.nav_saved_news).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(true);
            HashMap<String, String> user = sessionManagement.getUserDetails();
            String userName = user.get(SessionManagement.NAME);
            String userEmail = user.get(SessionManagement.EMAIL);

            userNameText.setText(userName);
            userEmailText.setText(userEmail);
        }else{
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_register).setVisible(true);
            menu.findItem(R.id.nav_saved_news).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
            userNameText.setText("BreakingNews.com");
            userEmailText.setText("A News App by HMS");
        }
        Integer user_id = DB.getUserId(sessionManagement.getUserDetails().get(SessionManagement.EMAIL));
        List<SavedNewsHeadlines> list = DB.getNewsList(user_id);
        showNews(list);
        dialog.dismiss();
    }




    private void showNews(List<SavedNewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        savedNewsAdapter = new SavedNewsAdapter(SavedNewsActivity.this,list,this);
        recyclerView.setAdapter(savedNewsAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.nav_login:
            case R.id.nav_register:
                intent = new Intent(SavedNewsActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                intent = new Intent(SavedNewsActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_bus:
                break;
            case R.id.nav_sports:
                intent = new Intent(SavedNewsActivity.this,SportsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_health:
                break;
            case R.id.nav_tech:
                intent = new Intent(SavedNewsActivity.this,TechnologyActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sci:
                intent = new Intent(SavedNewsActivity.this,ScienceActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_saved_news:
                break;
            case R.id.nav_logout:
                sessionManagement.logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnNewsClicked(Headlines headlines) {

    }

    @Override
    public void onSavedNewsClicked(SavedNewsHeadlines headlines) {
        startActivity(new Intent(SavedNewsActivity.this, DetailSavedNewsActivity.class)
                .putExtra("data",headlines));
    }

    @Override
    public void onClick(View view) {

    }
}