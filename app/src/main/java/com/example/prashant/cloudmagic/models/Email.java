package com.example.prashant.cloudmagic.models;

import com.example.prashant.cloudmagic.util.EmailUtils;

/**
 * Created by Prashant on 17/08/2016.
 */
public class Email {
    private String id;
    private String subject;
    private String[] participants;
    private String preview;
    private boolean isRead;
    private boolean isStarred;
    private long ts;
    private String dateString;
    private String participantsString;

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String[] getParticipants() {
        return participants;
    }

    public String getPreview() {
        return preview;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public long getTs() {
        return ts;
    }

    public String getDateString() {
        return dateString;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setParticipantsString(String participantsString) {
        this.participantsString = participantsString;
    }

    public String getParticipantsString() {
        return participantsString;
    }

    public void process() {
        dateString = EmailUtils.getDate(ts);
        preview = preview.replace("\n", " ");
        StringBuilder sb = new StringBuilder();
        sb.append(participants[0]);
        int i = 1;
        while (i < participants.length) {
            sb.append(", ").append(participants[i]);
            i++;
        }
        participantsString = sb.toString();
    }

}
