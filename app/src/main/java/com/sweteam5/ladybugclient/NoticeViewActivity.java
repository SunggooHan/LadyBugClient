package com.sweteam5.ladybugclient;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
//import com.sweteam5.ladybugclient.R;

import java.util.ArrayList;

public class NoticeViewActivity extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;
    ArrayList<NoticeInfo> noticeArrayList;
    private ArrayList<NoticeGroup> noticeList;
    private LinearLayout noticeContainer;
    NoticeAdapter noticeAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoticeInfo notice = new NoticeInfo();
        setContentView(R.layout.activity_notice_view);




        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("공지 불러오는 중");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        noticeArrayList = new ArrayList<NoticeInfo>();
        noticeAdapter = new NoticeAdapter(NoticeViewActivity.this, noticeArrayList);
        recyclerView.setAdapter(noticeAdapter);

        EventChangeListener();



        //createNoticeGroups();
    }

    private void EventChangeListener(){//서버에서 데이터 가져오는 곳
        db.collection("notice").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc  :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){

                        noticeArrayList.add(dc.getDocument().toObject(NoticeInfo.class));
                    }
                    noticeAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}
