package com.example.zenstudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskView extends AppCompatActivity {

    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    final DatabaseReference reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child(HomeMain.userID).child("Task");

    private ArrayList<Task> taskArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        ImageButton addButton = findViewById(R.id.addButton);
        Button backButton = findViewById(R.id.backButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskView.this, TaskAdd.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showTasks();
    }

    @Override
    public void onResume(){
        super.onResume();

        showTasks();
    }

    public void showTasks(){

        GridView taskGridView = findViewById(R.id.taskGridView);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                taskArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if (dataSnapshot.hasChild("SubTask")){
                        String taskMainTitle = dataSnapshot.child("TaskName").getValue(String.class);

                        Task task = new Task(taskMainTitle);

                        for (DataSnapshot data : dataSnapshot.child("SubTask").getChildren()){
                            String subTaskDueDate = data.child("SubTaskDueDate").getValue(String.class);
                            String subTaskTitle = data.child("SubTaskName").getValue(String.class);
                            String subTaskPriority = data.child("SubTaskPriority").getValue(String.class);

                            task.addSubTask(subTaskTitle, subTaskDueDate, subTaskPriority);
                        }

                        taskArrayList.add(task);
                    }

                    else {
                        String dueDate = dataSnapshot.child("TaskDueDate").getValue(String.class);
                        String taskMainTitle = dataSnapshot.child("TaskName").getValue(String.class);
                        String priority = dataSnapshot.child("TaskPriority").getValue(String.class);

                        Task task = new Task(taskMainTitle, dueDate, priority);
                        taskArrayList.add(task);
                    }
                }

                TaskGVAdapter taskAdapter = new TaskGVAdapter(TaskView.this, taskArrayList);
                taskGridView.setAdapter(taskAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskView.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
