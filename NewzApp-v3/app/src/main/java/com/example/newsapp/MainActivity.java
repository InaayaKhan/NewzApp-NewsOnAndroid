package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.Models.Headlines;
import com.example.newsapp.Models.NewsAPIResponse;
import com.example.newsapp.Models.SavedNewsHeadlines;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SelectListener,View.OnClickListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SessionManagement sessionManagement;
    TextView userNameText,userEmailText;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        searchView=findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news articles of " + query);
                dialog.show();
                RequestManager requestManager = new RequestManager(MainActivity.this);
                requestManager.getNewsHeadlines(listener,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dialog=new ProgressDialog(this);
        dialog.setTitle("Fetching news articles...");
        dialog.show();

        b1=findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2=findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3=findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4=findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5=findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6=findViewById(R.id.btn_6);
        b6.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open_drawer,R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);



        View headerview = navigationView.getHeaderView(0);
        userNameText = (TextView) headerview.findViewById(R.id.userNameText);
        userEmailText = (TextView) headerview.findViewById(R.id.userEmailText);
        sessionManagement = new SessionManagement(this);
        RequestManager requestManager = new RequestManager(this);
        requestManager.getNewsHeadlines(listener,"general",null);

        if( sessionManagement.isLoggedIn()){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_register).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_saved_news).setVisible(true);
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
    }

    private final OnFetchDataListener<NewsAPIResponse> listener = new OnFetchDataListener<NewsAPIResponse>(){

        @Override
        void onFetchData(List<Headlines> list, String message) {
            if (list.isEmpty()){
                Toast.makeText(MainActivity.this, "No data found!!!", Toast.LENGTH_SHORT).show();
                searchView.setQuery("",false);
                searchView.clearFocus();
                dialog.dismiss();
            }
            else {
                showNews(list);
                dialog.dismiss();
            }

        }

        @Override
        void onError(String message) {
            super.onError(message);
        }
    };

    private void showNews(List<Headlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        customAdapter = new CustomAdapter(this,list,this);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.nav_login:
            case R.id.nav_register:
                intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                break;
            case R.id.nav_bus:
                intent = new Intent(MainActivity.this,BusinessActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sports:
                intent = new Intent(MainActivity.this,SportsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_health:
                intent = new Intent(MainActivity.this,HealthActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_tech:
                intent = new Intent(MainActivity.this,TechnologyActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sci:
                intent = new Intent(MainActivity.this,ScienceActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_saved_news:
                intent = new Intent(MainActivity.this,SavedNewsActivity.class);
                startActivity(intent);
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
        startActivity(new Intent(MainActivity.this, DetailsActivity.class)
        .putExtra("data",headlines));
    }

    @Override
    public void onSavedNewsClicked(SavedNewsHeadlines headlines) {

    }

    @Override
    public void onClick(View v) {
        Button button=(Button) v;
        String category=button.getText().toString();
        dialog.setTitle("Fetching news articles of " + category);
        dialog.show();
        RequestManager requestManager = new RequestManager(this);
        requestManager.getNewsHeadlines(listener,category,null);
    }
}
