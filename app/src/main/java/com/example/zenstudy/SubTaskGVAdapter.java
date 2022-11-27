package com.example.zenstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SubTaskGVAdapter extends ArrayAdapter<SubTask> {

    //Constructor
    public SubTaskGVAdapter(@NonNull Context context, ArrayList<SubTask> subTaskArrayList){
        super(context, 0, subTaskArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Display the card view
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.task_subtask_card, parent, false);
        }

        SubTask subTask = getItem(position);
        String priority = subTask.getPriority();

        TextView subTaskTitleText = listitemView.findViewById(R.id.subTaskTitle);
        TextView dueDateText = listitemView.findViewById(R.id.dueDate);

        subTaskTitleText.setText(subTask.getSubTitle());
        dueDateText.setText("Due Date: " + subTask.getDueDate());

        if (priority.equals("High"))
        {
            subTaskTitleText.setBackgroundResource(R.color.ashrose);
        }

        else if (priority.equals("Medium"))
        {
            subTaskTitleText.setBackgroundResource(R.color.pale_yellow);
        }

        else {
            subTaskTitleText.setBackgroundResource(R.color.light_blue);
        }

        return listitemView;
    }
}
