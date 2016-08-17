package com.example.prashant.cloudmagic.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.prashant.cloudmagic.R;
import com.example.prashant.cloudmagic.adapters.EmailListAdapter;
import com.example.prashant.cloudmagic.models.Email;
import com.example.prashant.cloudmagic.task.FetchEmailTask;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchEmailTask.EmailResponseListener {
    private RecyclerView mRecyclerView;
    private EmailListAdapter mAdapter;
    public SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new EmailListAdapter(getBaseContext());
        mRecyclerView.setAdapter(mAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                fetchEmails();
            }
        });
        fetchEmails();
    }

    private void fetchEmails() {
        new FetchEmailTask(this).execute();
    }

    @Override
    public void onEmailResponse(boolean isSuccess, List<Email> emailList) {
        swipeContainer.setRefreshing(false);
        if (isSuccess) {
            mAdapter.updateData(emailList);
        }
    }
}
