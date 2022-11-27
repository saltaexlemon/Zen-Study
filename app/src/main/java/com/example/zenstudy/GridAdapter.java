package com.example.zenstudy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GridAdapter extends ArrayAdapter {
    ArrayList<Date> dates;
    Calendar currentDate;
    ArrayList<Event> events;
    LayoutInflater inflater;
    private String calendarDate;
    private int displayMonth, displayYear, DayNo;

    public GridAdapter(@NonNull Context context, ArrayList<Date>dates, Calendar currentDate, ArrayList<Event> events) {
        super(context, R.layout.calendar_cell);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.calendar_cell,parent,false);
        }

        if (displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.ashrose));
        }

        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_Number = view.findViewById(R.id.calendar_day);
        RelativeLayout calendarCellArea = view.findViewById(R.id.calendarCellArea);

        Day_Number.setText(String.valueOf(DayNo));
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();

        calendarDate = String.valueOf(displayYear) + "-" + String.valueOf(displayMonth) + "-" + String.valueOf(DayNo);

        /*for (int i = 0; i < events.size(); i++){
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getFirstDATE())); //getFirstDATE getSecondDATE
            if(DayNo == eventCalendar.get(Calendar.DAY_OF_MONTH)&& displayMonth == eventCalendar.get(Calendar.MONTH)+1 && displayYear == eventCalendar.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENT());
                EventNumber.setText(arrayList.size()+"Events");
            }
        }*/

        if (events.size() > 0){

            for (Event event: events){

                if (event.getFirstDATE().equals(calendarDate)){
                    calendarCellArea.setBackgroundResource(R.drawable.planner_calendar_cell_event);
                    Day_Number.setTextColor(Color.parseColor("#000000"));
                }
            }
        }

        //if (event.firstDATE == )

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);
            System.out.println("Conversion successful.");
        }catch(ParseException e){
            e.printStackTrace();
        }

        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    public String getDay(int position){
        return dates.get(position).toString();
    }

    /*
    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }*/
}
