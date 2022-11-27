package com.example.zenstudy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TaskAdd extends AppCompatActivity {

    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    final DatabaseReference reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child(HomeMain.userID);

    private ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
    private ArrayList<Integer> integerArraylist = new ArrayList<>();
    private ArrayList<SubTask> editSubTaskArrayList = new ArrayList<>();

    private String taskMainTitle = "";
    private String dueDate = "";
    private String priority = "";
    private String uneditedTaskMainTitle = "";
    private EditText taskMainTitleText;
    private Task editTask;
    private boolean isEdit;

    private String subTaskTitle = "";
    private String subTaskDueDate = "";
    private String subTaskPriority = "";

    private int subTaskCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);

        try {
            editTask = getIntent().getParcelableExtra("task");
            editSubTaskArrayList = getIntent().getExtras().getParcelableArrayList("subtask");
            isEdit = true;
        }
        catch (NullPointerException e){
            isEdit = false;
        }

        ImageButton addSubTaskButton = findViewById(R.id.addSubTaskButton);
        GridView subTaskGridView = findViewById(R.id.subTaskGridView);
        taskMainTitleText = findViewById(R.id.taskMainTitle);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button backButton = findViewById(R.id.backButton);
        ImageButton setDueDate = findViewById(R.id.setDueDateButton);
        Spinner prioritySpinner = findViewById(R.id.prioritySpinner);
        RelativeLayout area = findViewById(R.id.taskMainInfoArea);
        TextView setDueDateText = findViewById(R.id.dueDateText);
        TextView setPriorityText = findViewById(R.id.priorityText);
        TextView dueDateText = findViewById(R.id.dueDate);
        LinearLayout subTaskFrame = findViewById(R.id.subTaskFrame);

        addSubTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subTaskCounter++;

                area.setVisibility(View.GONE);
                setDueDate.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.GONE);
                setDueDateText.setVisibility(View.GONE);
                setPriorityText.setVisibility(View.GONE);

                integerArraylist.add(integerArraylist.size() + 1);

                //subTaskGridView.setAdapter(adapter);
                LayoutInflater inflater = (LayoutInflater) (TaskAdd.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View subTaskView = inflater.inflate(R.layout.task_edit_subtask, null);

                ImageButton dueDateButton = subTaskView.findViewById(R.id.setDueDateButton);
                TextView dueDateText = subTaskView.findViewById(R.id.dueDate);
                Spinner prioritySpinner = subTaskView.findViewById(R.id.prioritySpinner);
                EditText subTaskTitleText = subTaskView.findViewById(R.id.subTaskTitle);
                Button saveSubTaskButton = subTaskView.findViewById(R.id.saveSubTaskButton);

                saveSubTaskButton.setTag(integerArraylist.size() + "");
                subTaskTitleText.setTag("Text" + integerArraylist.size());
                prioritySpinner.setTag("Priority" + integerArraylist.size());
                dueDateText.setTag("DueDate" + integerArraylist.size());

                subTaskTitleText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        subTaskTitle = subTaskTitleText.getText().toString();
                    }
                });

                dueDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.Theme_AppCompat_DayNight_Dialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(Calendar.YEAR, i);
                                newDate.set(Calendar.MONTH, i1);
                                newDate.set(Calendar.DAY_OF_MONTH, i2);
                                SimpleDateFormat d1format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                String event_Date1 = d1format.format(newDate.getTime());
                                dueDateText.setText(event_Date1);
                                dueDate = (dueDateText.getText()).toString();
                            }
                        }, year, month, dayOfMonth);
                        datePickerDialog.show();
                    }
                });

                ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(TaskAdd.this, R.array.priority_text, android.R.layout.simple_spinner_item);
                prioritySpinner.setAdapter(staticAdapter);

                prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        priority = (parent.getItemAtPosition(position)).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        priority = "High";
                    }
                });

                saveSubTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String pos = (String)saveSubTaskButton.getTag();

                        TextView subTaskTitleText = subTaskView.findViewWithTag("Text" + pos);
                        subTaskTitle = subTaskTitleText.getText().toString();

                        Spinner prioritySpinner = subTaskView.findViewWithTag("Priority" + pos);
                        priority = prioritySpinner.getSelectedItem().toString();

                        TextView dueDateText = subTaskView.findViewWithTag("DueDate" + pos);
                        dueDate = dueDateText.getText().toString();

                        SubTask subTask = new SubTask(subTaskTitle, dueDate, priority);
                        subTaskArrayList.add(subTask);
                    }
                });

                subTaskFrame.addView(subTaskView);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
                finish();
            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(dueDateText);
            }
        });

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(TaskAdd.this, R.array.priority_text, android.R.layout.simple_spinner_item);
        prioritySpinner.setAdapter(staticAdapter);

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = (parent.getItemAtPosition(position)).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                priority = "High";
            }
        });

        if (isEdit)
        {
            taskMainTitleText.setText(editTask.getMainTitle());
            taskMainTitle = taskMainTitleText.getText().toString();
            uneditedTaskMainTitle = taskMainTitle;
            //System.out.println(editSubTaskArrayList.size());

            if (editSubTaskArrayList.size() > 0){
                area.setVisibility(View.GONE);
                setDueDate.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.GONE);
                setDueDateText.setVisibility(View.GONE);
                setPriorityText.setVisibility(View.GONE);

                //SubTaskEditGVAdapter subTaskEditAdapter = new SubTaskEditGVAdapter(TaskAdd.this, editSubTaskArrayList);
                //subTaskGridView.setAdapter(subTaskEditAdapter);

                for (SubTask subTask : editSubTaskArrayList){
                    LayoutInflater inflater = (LayoutInflater) (TaskAdd.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View subTaskView = inflater.inflate(R.layout.task_edit_subtask, null);

                    ImageButton dueDateButton = subTaskView.findViewById(R.id.setDueDateButton);
                    TextView dueDateEditText = subTaskView.findViewById(R.id.dueDate);
                    Spinner priorityEditSpinner = subTaskView.findViewById(R.id.prioritySpinner);
                    EditText subTaskTitleText = subTaskView.findViewById(R.id.subTaskTitle);
                    Button saveSubTaskButton = subTaskView.findViewById(R.id.saveSubTaskButton);

                    subTaskTitleText.setText(subTask.getSubTitle());
                    dueDateEditText.setText(subTask.getDueDate());

                    saveSubTaskButton.setTag(integerArraylist.size() + "");
                    subTaskTitleText.setTag("Text" + integerArraylist.size());
                    priorityEditSpinner.setTag("Priority" + integerArraylist.size());
                    dueDateEditText.setTag("DueDate" + integerArraylist.size());

                    subTaskTitleText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            subTaskTitle = subTaskTitleText.getText().toString();
                        }
                    });

                    dueDateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.Theme_AppCompat_DayNight_Dialog, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    Calendar newDate = Calendar.getInstance();
                                    newDate.set(Calendar.YEAR, i);
                                    newDate.set(Calendar.MONTH, i1);
                                    newDate.set(Calendar.DAY_OF_MONTH, i2);
                                    SimpleDateFormat d1format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    String event_Date1 = d1format.format(newDate.getTime());
                                    dueDateEditText.setText(event_Date1);
                                    dueDate = (dueDateEditText.getText()).toString();
                                }
                            }, year, month, dayOfMonth);
                            datePickerDialog.show();
                        }
                    });

                    ArrayAdapter<CharSequence> staticEditAdapter = ArrayAdapter.createFromResource(TaskAdd.this, R.array.priority_text, android.R.layout.simple_spinner_item);
                    priorityEditSpinner.setAdapter(staticEditAdapter);

                    priorityEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            priority = (parent.getItemAtPosition(position)).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            priority = "High";
                        }
                    });

                    saveSubTaskButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String pos = (String)saveSubTaskButton.getTag();

                            EditText subTaskTitleText = subTaskView.findViewWithTag("Text" + pos);
                            subTaskTitle = subTaskTitleText.getText().toString();

                            Spinner priorityEditSpinner = subTaskView.findViewWithTag("Priority" + pos);
                            subTaskPriority = priorityEditSpinner.getSelectedItem().toString();

                            TextView dueDateEditText = subTaskView.findViewWithTag("DueDate" + pos);
                            subTaskDueDate = dueDateEditText.getText().toString();

                            SubTask subTask = new SubTask(subTaskTitle, subTaskDueDate, subTaskPriority);
                            subTaskArrayList.add(subTask);
                        }
                    });

                    subTaskFrame.addView(subTaskView);
                }
            }

            else {
                prioritySpinner.setSelection(staticAdapter.getPosition(editTask.getPriority()));
                dueDateText.setText(editTask.getDueDate());
                priority = editTask.getPriority();
                dueDate = editTask.getDueDate();
            }
        }
    }

    public void saveTask(){

        taskMainTitle = taskMainTitleText.getText().toString();

        if (subTaskArrayList.size() > 0){

            reference.child("Task").child(taskMainTitle).child("TaskName").setValue(taskMainTitle);

            for (SubTask subTask : subTaskArrayList){
                System.out.println(subTask.getSubTitle());
                reference.child("Task").child(taskMainTitle).child("SubTask").child(subTask.getSubTitle()).child("SubTaskName").setValue(subTask.getSubTitle());
                reference.child("Task").child(taskMainTitle).child("SubTask").child(subTask.getSubTitle()).child("SubTaskDueDate").setValue(subTask.getDueDate());
                reference.child("Task").child(taskMainTitle).child("SubTask").child(subTask.getSubTitle()).child("SubTaskPriority").setValue(subTask.getPriority());
            }
        }

        else {
            reference.child("Task").child(taskMainTitle).child("TaskName").setValue(taskMainTitle);
            reference.child("Task").child(taskMainTitle).child("TaskDueDate").setValue(dueDate);
            reference.child("Task").child(taskMainTitle).child("TaskPriority").setValue(priority);
        }
    }

    public void showCalendar(TextView dueDateText){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(TaskAdd.this, R.style.Theme_AppCompat_DayNight_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(Calendar.YEAR, i);
                newDate.set(Calendar.MONTH, i1);
                newDate.set(Calendar.DAY_OF_MONTH, i2);
                SimpleDateFormat d1format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String event_Date1 = d1format.format(newDate.getTime());
                dueDateText.setText(event_Date1);
                dueDate = (dueDateText.getText()).toString();
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void deleteTask()
    {
        DatabaseReference taskReference = reference.child("Task");
        taskReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if ((dataSnapshot.child("TaskName").getValue(String.class)).equals(uneditedTaskMainTitle)){
                        String key = dataSnapshot.getKey();
                        taskReference.child(key).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
