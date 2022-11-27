package com.example.zenstudy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Task implements Parcelable {

    private String mainTitle;
    private String dueDate;
    private String priority;
    private ArrayList<SubTask> subTask = new ArrayList<>();

    //Default Constructor (No subTask added)
    public Task(){
        this.mainTitle = "";
        this.dueDate = "";
        this.priority = "";
    }

    //Overloaded Constructor (No subTask added)
    public Task(String mainTitle, String dueDate, String priority){
        this.mainTitle = mainTitle;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    //Overloaded Constructor (Added subTask)
    public Task(String mainTitle)
    {
        this.mainTitle = mainTitle;
    }

    protected Task(Parcel in) {
        mainTitle = in.readString();
        dueDate = in.readString();
        priority = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    //Getters
    public String getMainTitle(){return mainTitle;}
    public String getDueDate(){return dueDate;}
    public String getPriority(){return priority;}
    public ArrayList<SubTask> getSubTask(){return subTask;}

    //Setters
    public void setMainTitle(String mainTitle){this.mainTitle = mainTitle;}
    public void setDueDate(String dueDate){this.dueDate = dueDate;}
    public void setPriority(String priority){this.priority = priority;}

    public void addSubTask(String subTitle, String dueDate, String priority){
        SubTask newSubTask = new SubTask(subTitle, dueDate, priority);

        subTask.add(newSubTask);
    }

    public int getSubTaskArraySize(){
        return subTask.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mainTitle);
        dest.writeString(dueDate);
        dest.writeString(priority);
    }
}
