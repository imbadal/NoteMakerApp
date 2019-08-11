package com.example.noteapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "note_table")
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long notificationid;
    private String title;
    private String desc;
    private String timeStamp;
    @ColumnInfo(name = "priority")
    private String priority;
    private boolean notify;
    private String notify_date;
    private String notify_time;

    public Note(long notificationid, String title, String desc, String timeStamp, String priority, boolean notify, String notify_date, String notify_time) {
        this.notificationid = notificationid;
        this.title = title;
        this.desc = desc;
        this.timeStamp = timeStamp;
        this.priority = priority;
        this.notify = notify;
        this.notify_date = notify_date;
        this.notify_time = notify_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(long notificationid) {
        this.notificationid = notificationid;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public String getNotify_date() {
        return notify_date;
    }

    public void setNotify_date(String notify_date) {
        this.notify_date = notify_date;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.notificationid);
        dest.writeString(this.title);
        dest.writeString(this.desc);
        dest.writeString(this.timeStamp);
        dest.writeString(this.priority);
        dest.writeByte(this.notify ? (byte) 1 : (byte) 0);
        dest.writeString(this.notify_date);
        dest.writeString(this.notify_time);
    }

    protected Note(Parcel in) {
        this.id = in.readInt();
        this.notificationid = in.readLong();
        this.title = in.readString();
        this.desc = in.readString();
        this.timeStamp = in.readString();
        this.priority = in.readString();
        this.notify = in.readByte() != 0;
        this.notify_date = in.readString();
        this.notify_time = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
