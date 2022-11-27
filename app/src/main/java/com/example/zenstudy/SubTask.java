package com.example.zenstudy;

import android.os.Parcel;
import android.os.Parcelable;

public class SubTask implements Parcelable {

    private String subTitle;
    private String dueDate;
    private String priority;

    //Default Constructor
    public SubTask(){
        this.subTitle ="";
        this.dueDate = "";
        this.priority = "";
    }

    //Overloaded Constructor
    public SubTask(String subTitle, String dueDate, String priority){
        this.subTitle = subTitle;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    protected SubTask(Parcel in) {
        subTitle = in.readString();
        dueDate = in.readString();
        priority = in.readString();
    }

    public static final Creator<SubTask> CREATOR = new Creator<SubTask>() {
        @Override
        public SubTask createFromParcel(Parcel in) {
            return new SubTask(in);
        }

        @Override
        public SubTask[] newArray(int size) {
            return new SubTask[size];
        }
    };

    //Getters
    public String getSubTitle(){return subTitle;}
    public String getDueDate(){return dueDate;}
    public String getPriority(){return priority;}

    //Setters
    public void setSubTitle(String subTitle){this.subTitle = subTitle;}
    public void setDueDate(String dueDate){this.dueDate = dueDate;}
    public void setPriority(String priority){this.priority = priority;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subTitle);
        dest.writeString(dueDate);
        dest.writeString(priority);
    }
}
