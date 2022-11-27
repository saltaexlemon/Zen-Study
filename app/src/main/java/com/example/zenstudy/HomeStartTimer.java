package com.example.zenstudy;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class HomeStartTimer extends Fragment {

    //Initialization of constant values
    final int DEFAULT_SEEKBAR_PROGRESS = 30;
    final int MINUTES_TO_SECONDS = 60;

    //Initialization of variable to set the timer countdown duration
    private int timerValue = DEFAULT_SEEKBAR_PROGRESS;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.home_start_timer, container, false);

        //Link the start button to the button in the home_main.xml file
        Button startButton = view.findViewById(R.id.startButton);

        //Open the set timer dialog box when the start button is clicked
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer();
            }
        });

        return view;
    }

    /**
     * Shows a dialog box with a seekbar that allows the user to set the timer countdown duration
     */
    public void setTimer(){
        //Initialization for the alert dialog builder
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.timer_set, null));
        dialog = dialogBuilder.create();

        //Display the alert dialog
        dialog.show();

        //Initialization of elements in the home_main.xml
        TextView timerProgress = dialog.findViewById(R.id.timerProgress);
        SeekBar timerSeekBar = dialog.findViewById(R.id.timerSlider);
        Button startTimerButton = dialog.findViewById(R.id.startTimerButton);

        //Display the default timer duration
        timerProgress.setText(setTimerFormat(DEFAULT_SEEKBAR_PROGRESS));

        //Allow users to change the seekbar value and set the timer countdown duration
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            //Change the timer duration display when the seekbar value changes
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Retrieve the seekbar value (+1 since seekbar value starts at 0)
                //Value retrieved will be in minutes
                progressChangedValue = (progress + 1) * 5;

                //Display the selected timer duration
                timerProgress.setText(setTimerFormat(progressChangedValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                timerValue = progressChangedValue;
            }
        });

        //Closes the alert dialog box and starts the timer countdown
        //When users click the START button
        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((HomeMain)getActivity()).toStartTimer(timerValue * MINUTES_TO_SECONDS);
            }
        });
    }

    /**
     * Formats the seek bar value to a duration (in hours and minutes)
     * @param minutes the number of minutes as determined by the seek bar
     * @return a string of the timer duration to be set (in hours and minutes)
     */
    public String setTimerFormat(int minutes){
        String timeText = "";
        int hours = 0;

        //Calculate the number of hours
        while (minutes >= 60){
            hours++;
            minutes -= 60;
        }

        //Display the number of hours if hours > 0
        if (hours == 1){
            timeText += hours + " hour ";
        }

        else if (hours > 0){
            timeText += hours + " hours ";
        }

        //Display the number of minutes if minutes > 0
        if (minutes > 0){
            timeText += minutes + " minutes";
        }

        return timeText;
    }
}
