package com.example.zenstudy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TaskGVAdapter extends ArrayAdapter<Task>{

    //Constructor
    public TaskGVAdapter(@NonNull Context context, ArrayList<Task> taskArrayList){
        super(context, 0, taskArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Display the card view
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.task_card, parent, false);
        }

        Task task = getItem(position);

        TextView taskMainTitleText = listitemView.findViewById(R.id.taskMainTitle);
        TextView taskDueDateText = listitemView.findViewById(R.id.taskDueDate);
        GridView subTaskGridView = listitemView.findViewById(R.id.subTaskGridView);
        ImageButton editButton = listitemView.findViewById(R.id.editButton);

        if (task.getSubTaskArraySize() > 0){
            taskMainTitleText.setText(task.getMainTitle());
            taskDueDateText.setVisibility(View.GONE);

            LinearLayout subTaskFrame = listitemView.findViewById(R.id.subTaskFrame);
            subTaskFrame.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ArrayList <SubTask> subTaskArrayList = task.getSubTask();

            for (SubTask item: subTaskArrayList) {

                View subTaskView = inflater.inflate(R.layout.task_subtask_card, null);
                TextView subTaskTitle = subTaskView.findViewById(R.id.subTaskTitle);
                TextView subTaskDueDate = subTaskView.findViewById(R.id.dueDate);

                subTaskTitle.setText(item.getSubTitle());
                subTaskDueDate.setText(item.getDueDate());

                if (item.getPriority().equals("High")){
                    subTaskTitle.setBackgroundResource(R.color.ashrose);
                }

                else if (item.getPriority().equals("Medium")){
                    subTaskTitle.setBackgroundResource(R.color.pale_yellow);
                }

                else {
                    subTaskTitle.setBackgroundResource(R.color.light_blue);
                }

                subTaskFrame.addView(subTaskView);
            }

        }

        else {
            taskMainTitleText.setText(task.getMainTitle());
            taskDueDateText.setText("Due Date: " + task.getDueDate());
            String priority = task.getPriority();
            subTaskGridView.setVisibility(View.GONE);


            if (priority.equals("High")){
                taskMainTitleText.setBackgroundResource(R.color.ashrose);
            }

            else if (priority.equals("Medium")){
                taskMainTitleText.setBackgroundResource(R.color.pale_yellow);
            }

            else {
                taskMainTitleText.setBackgroundResource(R.color.light_blue);
            }
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), TaskAdd.class);
                intent.putExtra("task", task);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("subtask", task.getSubTask());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        return listitemView;
    }
}
