package com.example.zenstudy;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class HomeOngoingTimer extends Fragment {

    //Initialization of constant variables for time values
    final int SECONDS_TO_MILLISECONDS = 1000;
    final int SECONDS_MAX = 59;

    //Initialization of variables to control the pause timer
    private int pauseSeconds = 0;
    private int pauseMinutes = 0;
    private int pauseDurationRemainder = 0;
    private int timerCountdownRemainder = 0;
    private boolean isResumed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.home_ongoing_timer, container, false);

        //Initialization of elements in ongoingTimer.xml
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button pauseButton = view.findViewById(R.id.pauseButton);

        //Cancels the timer countdown
        //When users click the CANCEL button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
            }
        });

        //Pauses the timer countdown
        //When users click the PAUSE button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });
        return view;
    }

    /**
     * Cancels the timer countdown
     */
    public void cancelTimer(){

        //Creates the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.timer_cancel, null));
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Display the alert dialog
        dialog.show();

        //Initialization of elements in timer_cancel.xml
        Button cancelYes = dialog.findViewById(R.id.cancelYes);
        Button cancelNo = dialog.findViewById(R.id.cancelNo);

        //Closes the alert dialog box, cancels the timer and returns to the home page
        //When users click the YES button
        cancelYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((HomeMain)getActivity()).cancelTimer();
                ((HomeMain)getActivity()).fragmentToStartTimer();
            }
        });

        //Closes the alert dialog box
        //When users click the NO button
        cancelNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Pauses the timer countdown
     */
    public void pauseTimer(){

        //Retrieve the remaining duration of the timer countdown
        timerCountdownRemainder = ((HomeMain) getActivity()).pauseTimer();

        //Retrieve the duration that users can pause the timer countdown
        pauseDurationRemainder = ((HomeMain)getActivity()).calcMaxPauseDuration();

        pauseSeconds = pauseDurationRemainder;

        //Calculates the minutes that can be paused
        while (pauseSeconds >= 60){
            pauseMinutes++;
            pauseSeconds -= 60;
        }

        //Creates the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.timer_pause, null));
        dialog = dialogBuilder.create();

        //Displays the alert dialog box
        dialog.show();

        //Initialization of elements in timer_pause.xml
        TextView pauseTimer = dialog.findViewById(R.id.pauseTimer);
        ImageButton resumeButton = dialog.findViewById(R.id.resumeButton);

        //Start the pause timer
        new CountDownTimer((long) pauseDurationRemainder * SECONDS_TO_MILLISECONDS, 1000){
            public void onTick(long milisUntilFinished){

                //Closes the alert dialog box, cancels the pause timer and resumes the timer countdown
                //If the timer countdown is resumed
                if (isResumed){
                    dialog.dismiss();
                    cancel();
                    resumeMainTimer();
                }

                pauseSeconds--;
                pauseDurationRemainder--;

                //If the seconds < 0, convert a minute to seconds
                if (pauseSeconds < 0 && pauseMinutes > 0){
                    pauseSeconds = SECONDS_MAX;
                    pauseMinutes--;
                }

                //Display the pause timer duration
                pauseTimer.setText(timerCountdownFormat(pauseMinutes) + ":" + timerCountdownFormat(pauseSeconds));
            }

            //When the pause timer finishes
            public void onFinish(){
                dialog.dismiss();
                ((HomeMain)getActivity()).failedTimer();
            }
        }.start();

        //Resumes the timer countdown and stops the pause timer
        //When users click the resume icon
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isResumed = true;
            }
        });
    }

    /**
     * Resumes the timer countdown
     */
    public void resumeMainTimer(){
        int temp = timerCountdownRemainder;

        //Sets the remaining pause duration
        ((HomeMain)getActivity()).setPauseDurationRemainder(pauseDurationRemainder);

        //Reset the pause timer
        reset();

        //Starts the timer countdown
        ((HomeMain)getActivity()).startTimer(temp);
    }

    /**
     * Changes the time value to String and formats it to double digits
     * @param time the time value to be formatted
     * @return the string value of the time value in double digits
     */
    public String timerCountdownFormat(int time){

        //If the time is in single digit, add a '0' to format it to double digits
        if (time <= 9){
            return "0" + time;
        }

        return String.valueOf(time);
    }

    /**
     * Resets the pause timer
     */
    public void reset(){
        pauseSeconds = 0;
        pauseMinutes = 0;
        pauseDurationRemainder = 0;
    }
}