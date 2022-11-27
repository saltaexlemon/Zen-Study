package com.example.zenstudy;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventGVAdapter extends ArrayAdapter<Event> {

    private String eventStartDate;
    private String eventEndDate;
    private String notify;
    AlertDialog alertDialog;
    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child(HomeMain.userID).child("Event");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(HomeMain.userID).child("Event");
    DatabaseReference userReference = FirebaseDatabase.getInstance(firebaseURL).getReference().child(HomeMain.userID).child("Event");

    public EventGVAdapter(@NonNull Context context, ArrayList<Event> eventArrayList){
        super(context, 0, eventArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listitemView = convertView;
        if (listitemView == null){
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.planner_card_event, parent, false);
        }

        Event event = getItem(position);

        TextView eventTitle = listitemView.findViewById(R.id.eventTitle);
        TextView eventStartTime = listitemView.findViewById(R.id.eventStartTime);
        TextView eventEndTime = listitemView.findViewById(R.id.eventEndTime);
        TextView firstDate = listitemView.findViewById(R.id.firstDate);
        TextView secondDate = listitemView.findViewById(R.id.secondDate);
        ImageButton eventEditButton = listitemView.findViewById(R.id.eventEditButton);

        //splitting the event first date
        String eventFirstDate = event.getFirstDATE();
        String[] ThreePart = eventFirstDate.split("-");
        String event_first_day = ThreePart[1] + "/" + ThreePart[2];

        //splitting the event second date
        String eventSecondDate = event.getSecondDATE();
        String[] ThreePartz = eventSecondDate.split("-");
        String event_second_day = ThreePartz[1] + "/" + ThreePartz[2];

        eventTitle.setText(event.getEVENT());
        eventStartTime.setText(event.getStartTIME());
        eventEndTime.setText(event.getEndTIME());
        firstDate.setText(event_first_day);
        secondDate.setText(event_second_day);

        eventEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                View editView = LayoutInflater.from(getContext()).inflate(R.layout.event_edit, parent, false);
                EditText EventTitle = editView.findViewById(R.id.eventTitle);
                EditText EventDetails = editView.findViewById(R.id.details);

                TextView EventStartTime = editView.findViewById(R.id.setStartTimeText);
                TextView EventEndTime = editView.findViewById(R.id.setEndTimeText);
                TextView EventDate1 = editView.findViewById(R.id.setDateText1);
                TextView EventDate2 = editView.findViewById(R.id.setDateText2);

                ImageButton SetStartTime = editView.findViewById(R.id.selectStartTime);
                ImageButton SetEndTime = editView.findViewById(R.id.selectEndTime);
                ImageButton SetDate1 = editView.findViewById(R.id.selectDate1);
                ImageButton SetDate2 = editView.findViewById(R.id.selectDate2);

                CheckBox SetReminder1 = editView.findViewById(R.id.setReminder1);
                CheckBox SetReminder2 = editView.findViewById(R.id.setReminder2);

                Button deleteEvent = editView.findViewById(R.id.deleteButton);
                Button saveEvent = editView.findViewById(R.id.saveButton);
                Button backButton = editView.findViewById(R.id.backButton);

                EventTitle.setText(event.getEVENT());
                EventDetails.setText(event.getDETAILS());
                EventStartTime.setText(event.getStartTIME());
                EventEndTime.setText(event.getEndTIME());
                EventDate1.setText(event.getFirstDATE());
                EventDate2.setText(event.getSecondDATE());

                String Notify = "1day";
                if(Notify == event.getNOTIFY()){
                    SetReminder1.setChecked(true);
                }
                else if(Notify != event.getNOTIFY()){
                    SetReminder2.setChecked(true);
                }

                SetStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(editView.getContext(), R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                                String event_Time = hformat.format(c.getTime());
                                EventStartTime.setText(event_Time);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });

                SetEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(editView.getContext(), R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                                String event_Time = hformat.format(c.getTime());
                                EventEndTime.setText(event_Time);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });

                SetDate1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(editView.getContext(), R.style.Theme_AppCompat_DayNight_Dialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(Calendar.YEAR, i);
                                newDate.set(Calendar.MONTH, i1);
                                newDate.set(Calendar.DAY_OF_MONTH, i2);
                                SimpleDateFormat d1format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                String event_Date1 = d1format.format(newDate.getTime());
                                EventDate1.setText(event_Date1);
                            }
                        }, year, month, dayOfMonth);
                        datePickerDialog.show();
                    }
                });

                SetDate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int years = calendar.get(Calendar.YEAR);
                        int months = calendar.get(Calendar.MONTH);
                        int dayOfMonths = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(editView.getContext(), R.style.Theme_AppCompat_DayNight_Dialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                Calendar newDates = Calendar.getInstance();
                                newDates.set(Calendar.YEAR, i);
                                newDates.set(Calendar.MONTH, i1);
                                newDates.set(Calendar.DAY_OF_MONTH, i2);
                                SimpleDateFormat d2format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                String event_Date2 = d2format.format(newDates.getTime());
                                EventDate2.setText(event_Date2);
                            }
                        }, years, months, dayOfMonths);
                        datePickerDialog.show();
                    }
                });

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                deleteEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Event_Title = event.getEVENT();

                        userReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                                    if(childSnapshot.child("event").getValue() == Event_Title){
                                        String parentkey = childSnapshot.getKey();
                                        Log.i(TAG, parentkey);
                                        reff.child(parentkey).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        alertDialog.dismiss();
                    }
                });

                saveEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String event_title = event.getEVENT();

                        String eventTitle = EventTitle.getText().toString();
                        String eventDetails = EventDetails.getText().toString();
                        String eventStartTime = EventStartTime.getText().toString();
                        String eventEndTime = EventEndTime.getText().toString();
                        String firstDate = EventDate1.getText().toString();
                        String secondDate = EventDate2.getText().toString();

                        if(SetReminder1.isChecked()){
                            notify = "1day";
                        }
                        else if(SetReminder2.isChecked()){
                            notify = "1hr";
                        }

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                                    if(childSnapshot.child("event").getValue() == event_title){
                                        String parentKey = childSnapshot.getKey();
                                        Log.i(TAG, parentKey);

                                        ref.child(parentKey).removeValue();
                                        Event EditedEvents = new Event(eventTitle, eventDetails, eventStartTime, eventEndTime, firstDate, secondDate, notify);
                                        ref.push().setValue(EditedEvents);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        alertDialog.dismiss();

                    }
                });

                builder.setView(editView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return listitemView;
    }
}

