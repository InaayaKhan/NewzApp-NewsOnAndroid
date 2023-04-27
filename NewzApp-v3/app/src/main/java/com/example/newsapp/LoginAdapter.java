package com.example.newsapp;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class LoginAdapter extends FragmentPagerAdapter {
    int totalTabs;
    private Context context;

    public LoginAdapter(FragmentManager fm,Context context, int totalTabs){
        super(fm);
        this.totalTabs = totalTabs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int pos){
        switch (pos){
            case 0:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            case 1:
                RegisterTabFragment registerTabFragment = new RegisterTabFragment();
                return registerTabFragment;
            default:
                return null;
        }
    }
}
