package com.example.prashant.cloudmagic.models;

/**
 * Created by Prashant on 17/08/2016.
 */
public class EmailDetail {
    private String id;
    private String subject;
    private Participants[] participants;
    private String body;
    private boolean isRead;
    private boolean isStarred;
    private long ts;

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setParticipants(Participants[] participants) {
        this.participants = participants;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getSubject() {
        return subject;
    }

    public Participants[] getParticipants() {
        return participants;
    }

    public String getBody() {
        return body;
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

}
