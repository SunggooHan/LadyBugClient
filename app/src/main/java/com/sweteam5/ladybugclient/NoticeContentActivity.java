package com.sweteam5.ladybugclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NoticeContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_content);

        Intent intent = getIntent();
        Bundle noticeBundle = intent.getBundleExtra("notice");
        String title = noticeBundle.getString("title");
        String content = noticeBundle.getString("content");
        String date = noticeBundle.getString("date");

        TextView titleTextView = findViewById(R.id.noticeTitleTextView);
        TextView contentTextView = findViewById(R.id.noticeContentTextView);
        TextView dateTextView = findViewById(R.id.noticeDateTextView);

        titleTextView.setText(title);
        contentTextView.setText(content);
        dateTextView.setText(date);
    }
}