package com.example.newsapp;

import com.example.newsapp.Models.Headlines;
import com.example.newsapp.Models.SavedNewsHeadlines;

public interface SelectListener {
    void OnNewsClicked(Headlines headlines);
    void onSavedNewsClicked(SavedNewsHeadlines headlines);
}