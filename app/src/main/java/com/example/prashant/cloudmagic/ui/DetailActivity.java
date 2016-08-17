package com.example.prashant.cloudmagic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prashant.cloudmagic.R;
import com.example.prashant.cloudmagic.models.EmailDetail;
import com.example.prashant.cloudmagic.models.Participants;
import com.example.prashant.cloudmagic.task.DeleteEmailTask;
import com.example.prashant.cloudmagic.task.DetailEmailTask;
import com.example.prashant.cloudmagic.util.EmailUtils;

public class DetailActivity extends AppCompatActivity
        implements DetailEmailTask.DetailEmailResponseListener,
        DeleteEmailTask.DeleteEmailTaskListener {

    public static final String KEY_INDEX = "index";
    public static final String KEY_ID = "id";
    private LinearLayout mContainer;
    private TextView mBody;
    private TextView mSubject;
    private ImageView mStar;
    private TextView mDate;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        id = i.getStringExtra(KEY_ID);
        setContentView(R.layout.activity_detail);
        mContainer = (LinearLayout) findViewById(R.id.details_participant_container);
        mBody = (TextView) findViewById(R.id.details_body);
        mSubject = (TextView) findViewById(R.id.details_subject);
        mStar = (ImageView) findViewById(R.id.details_star);
        mDate = (TextView) findViewById(R.id.details_date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new DetailEmailTask(this).execute(id);
    }

    @Override
    public void onEmailDetailResponse(EmailDetail detail) {
        setData(detail);
    }

    private void setData(final EmailDetail detail) {
        if (detail == null) {
            Toast.makeText(getBaseContext(), "Unable to retreive message", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            for (Participants p : detail.getParticipants()) {
                View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_participant, mContainer, false);
                ((TextView) v.findViewById(R.id.participant_name)).setText(p.getName());
                ((TextView) v.findViewById(R.id.participant_email)).setText(p.getEmail());
                mContainer.addView(v);
            }
            mStar.setVisibility(View.VISIBLE);
            if (detail.isStarred()) {
                mStar.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                mStar.setImageResource(android.R.drawable.btn_star_big_off);
            }
            mStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView v = (ImageView) view;
                    if (detail.isStarred()) {
                        detail.setStarred(false);
                        v.setImageResource(android.R.drawable.btn_star_big_off);
                    } else {
                        detail.setStarred(true);
                        v.setImageResource(android.R.drawable.btn_star_big_on);
                    }
                }
            });
            mDate.setText(EmailUtils.getFullDateTime(detail.getTs()));
            mSubject.setText(detail.getSubject());
            mBody.setText(detail.getBody());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete) {
            new DeleteEmailTask(this).execute(id);
            return true;
        } else if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteEmail(boolean success) {
        if (success == true) {
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Unable to delete message", Toast.LENGTH_SHORT).show();
        }
    }
}
