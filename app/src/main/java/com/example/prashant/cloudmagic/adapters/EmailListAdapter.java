package com.example.prashant.cloudmagic.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prashant.cloudmagic.R;
import com.example.prashant.cloudmagic.models.Email;
import com.example.prashant.cloudmagic.ui.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 17/08/2016.
 */
public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.EmailViewHolder> {
    private List<Email> mList;

    public EmailListAdapter(Context ctx) {
        mList = new ArrayList<Email>();
    }

    public void updateData(List<Email> list) {
        if (list != null) {
            this.mList.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_item, null);
        EmailViewHolder emailViewHolder = new EmailViewHolder(view);
        return emailViewHolder;
    }

    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        holder.bindHolder(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Email delete(final int pos) {
        Email email = mList.get(pos);
        mList.remove(pos);
        notifyItemRemoved(pos);
        return email;
    }

    public boolean add(Email message, int pos) {
        if (pos > mList.size())
            return false;
        mList.add(pos, message);
        notifyItemInserted(pos);
        return true;
    }

    public class EmailViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView star;
        private Email mEmail;
        private TextView subject;
        private TextView participants;
        private TextView time;
        private TextView preview;


        public EmailViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.item);
            star = (ImageView) itemView.findViewById(R.id.item_star);
            subject = (TextView) itemView.findViewById(R.id.item_subject);
            participants = (TextView) itemView.findViewById(R.id.item_participants);
            time = (TextView) itemView.findViewById(R.id.item_time);
            preview = (TextView) itemView.findViewById(R.id.item_preview);
            view.setOnClickListener(new ViewClickListener());
            star.setOnClickListener(new StarClickListener());
        }

        public void bindHolder(final int pos) {
            mEmail = mList.get(pos);
            subject.setText(mEmail.getSubject());
            shortlist(mEmail.isStarred(), star);
            setRead(mEmail.isRead(), view);
            time.setText(mEmail.getDateString());
            participants.setText(mEmail.getParticipantsString());
            preview.setText(mEmail.getPreview());
        }

        public void shortlist(boolean shortlist, ImageView view) {
            int pos = getAdapterPosition();
            if (shortlist) {
                mList.get(pos).setStarred(true);
                view.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                mList.get(pos).setStarred(false);
                view.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }

        public void setRead(boolean isRead, View view) {
            int pos = getAdapterPosition();
            if (isRead) {
                view.setSelected(true);
                mList.get(pos).setRead(true);
            } else {
                view.setSelected(false);
            }
        }

        private class StarClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                ImageView view = (ImageView) v;
                shortlist(!mEmail.isStarred(), view);
            }
        }

        private class ViewClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                setRead(true, v);
                Intent i = new Intent(v.getContext(), DetailActivity.class);
                i.putExtra(DetailActivity.KEY_INDEX, getAdapterPosition());
                i.putExtra(DetailActivity.KEY_ID, mList.get(getAdapterPosition()).getId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(i);
            }
        }

    }
}

