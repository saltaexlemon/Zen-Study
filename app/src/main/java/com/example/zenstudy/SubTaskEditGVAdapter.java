package com.example.zenstudy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SubTaskEditGVAdapter extends ArrayAdapter<SubTask> {

    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    final DatabaseReference reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child(HomeMain.userID).child("SubTask");

    private String subTaskTitle = "";
    private String dueDate = "";
    private String priority = "";

    private ArrayList<SubTask> subTaskArrayList = new ArrayList<>();

    //Constructor
    public SubTaskEditGVAdapter(@NonNull Context context, ArrayList<SubTask> arrayList){
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null){
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.task_edit_subtask, parent, false);
        }

        SubTask subTask = getItem(position);

        ImageButton dueDateButton = listitemView.findViewById(R.id.setDueDateButton);
        TextView dueDateText = listitemView.findViewById(R.id.dueDate);
        Spinner prioritySpinner = listitemView.findViewById(R.id.prioritySpinner);
        EditText subTaskTitleText = listitemView.findViewById(R.id.subTaskTitle);
        Button saveSubTaskButton = listitemView.findViewById(R.id.saveSubTaskButton);

        subTaskTitleText.setText(subTask.getSubTitle());
        dueDateText.setText(subTask.getDueDate());

        saveSubTaskButton.setTag(position + "");
        subTaskTitleText.setTag("Text" + position);
        prioritySpinner.setTag("Priority" + position);
        dueDateText.setTag("DueDate" + position);

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

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_text, android.R.layout.simple_spinner_item);
        prioritySpinner.setAdapter(staticAdapter);

        prioritySpinner.setSelection(staticAdapter.getPosition(subTask.getPriority()));

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

                TextView subTaskTitleText = convertView.findViewWithTag("Text" + pos);
                subTaskTitle = subTaskTitleText.getText().toString();

                Spinner prioritySpinner = convertView.findViewWithTag("Priority" + pos);
                priority = prioritySpinner.getSelectedItem().toString();

                TextView dueDateText = convertView.findViewWithTag("DueDate" + pos);
                dueDate = dueDateText.getText().toString();

                SubTask subTask = new SubTask(subTaskTitle, dueDate, priority);
                subTaskArrayList.add(subTask);
            }
        });

        return listitemView;
    }

    public ArrayList<SubTask> getSubTask(){
        System.out.println("Size: " + subTaskArrayList.size());
        return subTaskArrayList;
    }
}

