package com.example.zenstudy;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class HomeMain extends AppCompatActivity{

    private static boolean isOngoingTimer;
    private static boolean isLeavingApp = false;
    public static Context currentContext;

    public static String userID = "";

    //Constant variable for the URL of the database
    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    DatabaseReference reference;

    //Initialization of variables to display the number of bamboo (currency)
    private TextView bambooCurrency;
    private int bambooNum;

    //Initialization of default timer countdown values
    final int DEFAULT_COUNTDOWN_HOURS = 0;
    final int DEFAULT_COUNTDOWN_MINUTES = 0;
    final int DEFAULT_COUNTDOWN_SECONDS = 0;

    //Initialization of constant variables for time values
    final int SECONDS_TO_MILLISECONDS = 1000;
    final int MINUTES_MAX = 59;
    final int SECONDS_MAX = 59;

    //Initialization of constant variables to control the maximum pause duration
    final double PAUSE_TIMER_DURATION_PERCENTAGE = 0.2;

    //Initialization of the TextView for the timer countdown in home_main.xml
    private TextView timerCountdownHours;
    private TextView timerCountdownMinutes;
    private TextView timerCountdownSeconds;

    //Initialization for the timer countdown duration
    private int hours = 0, minutes = 0, seconds = 0;
    private int timerDuration = 0;
    private int totalTimerDuration = 0;

    //Initialization of variables to control the timer (cancel/ pause)
    private boolean isCancelled = false;
    public static boolean isPaused = false;
    private int pauseCounter;
    private int maxPauseDuration = 0;
    private int pauseDurationRemainder = 0;

    //Initialization of variables to play music using the media player
    public static boolean isPlayingMusic = false;
    private String musicAudio = "";
    public static MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child(userID);

        isOngoingTimer = false;
        currentContext = this;
        pauseCounter = 0;

        //Assigning the TextView variables to the TextView in home_main.xml for the timer countdown
        timerCountdownHours = findViewById(R.id.timerCountdownHours);
        timerCountdownMinutes = findViewById(R.id.timerCountdownMinutes);
        timerCountdownSeconds = findViewById(R.id.timerCountdownSeconds);

        //Initialization and assignment of the Button variables to the buttons in the bottom navigation menu of home_main.xml
        Button pandaButton = findViewById(R.id.pandaButton);
        Button shopButton = findViewById(R.id.shopButton);
        Button plannerButton = findViewById(R.id.plannerButton);
        Button tasksButton = findViewById(R.id.tasksButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        ImageButton musicButton = findViewById(R.id.musicButton);

        pandaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeMain.this, Panda.class));
            }
        });
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeMain.this, Shop.class));
            }
        });
        plannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeMain.this, PlannerView.class));
            }
        });
        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeMain.this, TaskView.class));
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeMain.this, Settings.class));
            }
        });

        //Mutes or unmutes the media player when clicked
        musicButton.setOnClickListener(v -> {

            if (isPlayingMusic){
                isPlayingMusic = false;
                mediaPlayer.setVolume(0,0);
                musicButton.setImageResource(R.drawable.music_icon_off);
            }

            else {
                isPlayingMusic = true;
                mediaPlayer.setVolume(1,1);
                musicButton.setImageResource(R.drawable.music_icon_on);
            }
        });

        //Set default timer text for the timer countdown
        timerCountdownHours.setText(timerCountdownFormat(DEFAULT_COUNTDOWN_HOURS));
        timerCountdownMinutes.setText(timerCountdownFormat(DEFAULT_COUNTDOWN_MINUTES));
        timerCountdownSeconds.setText(timerCountdownFormat(DEFAULT_COUNTDOWN_SECONDS));

        //Initialize fragment
        Fragment homeStartTimer = new HomeStartTimer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.homeFrame, homeStartTimer);
        fragmentTransaction.commit();

        //Initialize elements in the home page
        ImageView homeFrameImage = findViewById(R.id.homeFrameImage);
        bambooCurrency = findViewById(R.id.bambooNumber);

        //Retrieve data from Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Retrieve the image for the home page
                String imageURL = snapshot.child("User").child("EquippedItemTimer").getValue(String.class);

                //Retrieve the number of bamboo (currency)
                bambooNum = snapshot.child("User").child("Bamboo").getValue(int.class);

                //Retrieve the audio URL
                musicAudio = snapshot.child("User").child("EquippedMusicAudio").getValue(String.class);

                //Set the text with the number of bamboo
                bambooCurrency.setText(Integer.toString(bambooNum));

                //Load the image for the home page
                Picasso.get().load(imageURL).into(homeFrameImage);
            }

            //Display database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeMain.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Resets the timer countdown
     */
    public void resetTimerCountdown(){
        timerCountdownHours.setText(timerCountdownFormat(DEFAULT_COUNTDOWN_HOURS));
        timerCountdownMinutes.setText(timerCountdownFormat(DEFAULT_COUNTDOWN_MINUTES));
        timerCountdownSeconds.setText(timerCountdownFormat(DEFAULT_COUNTDOWN_SECONDS));
        hours = minutes = seconds = 0;
        isCancelled = isPaused = false;
    }
    /**
     * Start the ongoingTimer fragment and replace the current fragment
     */
    public void fragmentToOngoingTimer(){
        Fragment homeOngoingTimer = new HomeOngoingTimer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrame, homeOngoingTimer);
        fragmentTransaction.commit();
    }

    /**
     * Start the startTimer fragment and replace the current fragment
     */
    public void fragmentToStartTimer(){
        Fragment homeStartTimer = new HomeStartTimer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrame, homeStartTimer);
        fragmentTransaction.commit();
    }

    /**
     * CLose the startTimer fragment
     */
    public void closeFragmentStartTimer(){
        Fragment homeStartTimer = new HomeStartTimer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(homeStartTimer);
        fragmentTransaction.commit();
    }

    /**
     * Close the ongoingTimer fragment
     */
    public void closeFragmentOngoingTimer(){
        Fragment homeStartTimer = new HomeStartTimer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(homeStartTimer);
        fragmentTransaction.commit();
    }

    /**
     * Closes the startTimer fragment and starts the timer countdown
     * @param duration duration of the timer countdown set by the user
     */
    public void toStartTimer(int duration){
        closeFragmentStartTimer();
        totalTimerDuration = duration;
        startTimer(duration);
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
     * Formats the time in hours, minutes and seconds
     * @param seconds the duration of time
     * @return formatted string in hours, minutes and seconds
     */
    public String formatTimeDuration(int seconds){
        int minutes = 0;
        int hours = 0;
        String textFormat = "";

        if (seconds <= 0){
            return "0 seconds";
        }

        //Calculates the total minutes
        while (seconds >= 60){
            minutes++;
            seconds -= 60;
        }

        //Calculates the total hours
        while (minutes >= 60){
            hours++;
            minutes -= 60;
        }

        //Formats the hours
        if (hours > 0){

            if (hours == 1){
                textFormat += hours + " hour ";
            }

            else {
                textFormat += hours + " hours ";
            }
        }

        //Formats the minutes
        if (minutes > 0){

            if (minutes == 1){
                textFormat += minutes + " minute ";
            }

            else {
                textFormat += minutes + " minutes ";
            }
        }

        //Formats the seconds
        if (seconds > 0){

            if (seconds == 1){
                textFormat += seconds + " second";
            }

            else {
                textFormat += seconds + " seconds";
            }
        }

        return textFormat;
    }

    /**
     * Changes the value of isCancelled(boolean) to true
     */
    public void cancelTimer(){
        isCancelled = true;
    }

    /**
     * Changes the value of isPaused(boolean) to true
     * @return the remaining time left on the timer countdown
     */
    public int pauseTimer(){
        isPaused = true;
        pauseCounter++;
        return timerDuration;
    }

    /**
     * Calculate the duration that users are allowed to pause
     * @return the duration that users can pause
     */
    public int calcMaxPauseDuration(){

        System.out.println(pauseCounter);
        //If the user paused the timer for the first time
        if (pauseCounter <= 1){
            maxPauseDuration = (int) (totalTimerDuration * PAUSE_TIMER_DURATION_PERCENTAGE);

            return maxPauseDuration;
        }

        //If the user has paused the timer before
        else {
            return pauseDurationRemainder;
        }
    }

    /**
     * Sets the remaining duration of the pause timer
     * @param remainder the remaining duration the user can pause
     */
    public void setPauseDurationRemainder(int remainder){
        pauseDurationRemainder = remainder;
    }

    /**
     * Calculates the bamboo earned by the user
     */
    public int calcEarnedBamboo (){
        return totalTimerDuration / 60;
    }

    /**
     * Shows a dialog box with details of the timer(succeeded)
     */
    public void succeededTimer(){

        //Close the ongoingTimer fragment
        closeFragmentOngoingTimer();

        //Create the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.timer_success, null));
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Display the alert dialog box
        dialog.show();

        //Initialization of the elements in timer_success.xml
        TextView totalStudyMinutes = dialog.findViewById(R.id.totalStudyMinutes);
        TextView totalPauseMinutes = dialog.findViewById(R.id.totalPauseMinutes);
        TextView totalBambooEarned = dialog.findViewById(R.id.totalBambooEarned);
        ImageButton redoTimerButton = dialog.findViewById(R.id.redoTimerIcon);
        ImageButton homeButton = dialog.findViewById(R.id.homeIcon);

        //Displays the timer details on the alert dialog box
        totalStudyMinutes.setText(formatTimeDuration(totalTimerDuration));
        totalPauseMinutes.setText(formatTimeDuration(maxPauseDuration - pauseDurationRemainder));
        totalBambooEarned.setText(String.valueOf(calcEarnedBamboo()));

        bambooNum += calcEarnedBamboo();
        reference.child("User").child("Bamboo").setValue(bambooNum);

        //Reset the timer countdown
        resetTimerCountdown();

        //Closes the alert dialog box and restarts the timer with the same duration as previously set
        //When users click the Redo Timer icon
        redoTimerButton.setOnClickListener(v -> {
            dialog.dismiss();
            startTimer(totalTimerDuration);
        });

        //Closes the alert dialog box and returns to the home page
        //When users click the Home icon
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fragmentToStartTimer();
            }
        });
    }

    /**
     * Shows a dialog box with details of the timer(failed)
     */
    public void failedTimer(){

        //Closes the ongoingTimer fragment
        closeFragmentOngoingTimer();

        //Creates the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.timer_pause_exceeded, null));
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Displays the alert dialog box
        dialog.show();

        //Initialization of the elements in timer_pause_exceeded.xml
        TextView totalStudyMinutes = dialog.findViewById(R.id.totalStudyMinutes);
        TextView totalPauseMinutes = dialog.findViewById(R.id.totalPauseMinutes);
        ImageButton redoTimerButton = dialog.findViewById(R.id.redoTimerIcon);
        ImageButton homeButton = dialog.findViewById(R.id.homeIcon);

        //Displays the timer details on the alert dialog box
        totalStudyMinutes.setText(formatTimeDuration(totalTimerDuration - timerDuration));
        totalPauseMinutes.setText(formatTimeDuration(maxPauseDuration));

        //Resets the timer countdown
        resetTimerCountdown();

        //Closes the alert dialog box and restarts the timer with the same duration as previously set
        //When users click the Redo Timer icon
        redoTimerButton.setOnClickListener(v -> {
            pauseCounter = 0;
            dialog.dismiss();
            startTimer(totalTimerDuration);
        });

        //Closes the alert dialog box and returns to the home page
        //When users click the Home icon
        homeButton.setOnClickListener(v -> {
            pauseCounter = 0;
            dialog.dismiss();
            fragmentToStartTimer();
        });
    }

    /**
     * Start the countdown timer, display the timer duration and start the media player
     * @param duration the total amount of time
     */
    public void startTimer(int duration){

        //Reset the timer
        resetTimerCountdown();

        //Start the ongoingTimer fragment
        fragmentToOngoingTimer();

        //Assign the timer duration to variables
        seconds = duration;
        timerDuration = duration;
        totalTimerDuration = duration;

        //Calculate the number of hours in the timer duration
        while (seconds >= 60){
            seconds -= 60;
            minutes++;
        }

        //Calculate the number of minutes in the timer duration
        while (minutes >= 60){
            minutes -= 60;
            hours++;
        }

        isOngoingTimer = true;

        try{
            //Pass the audio URL to the Media Player
            mediaPlayer.setDataSource(musicAudio);
            mediaPlayer.setOnPreparedListener(mp -> {
                //Start playing the audio
                mp.start();

                //Loop the audio
                mp.setLooping(true);

                if (isPlayingMusic){
                    mp.setVolume(1,1);
                }

                else {
                    mp.setVolume(0,0);
                }
            });
            mediaPlayer.prepare();
        } catch (IOException e){
            e.printStackTrace();
        }

        //Start the timer countdown
        new CountDownTimer((long) duration * SECONDS_TO_MILLISECONDS, 1000){

            //After every countdown interval
            public void onTick(long milisUntilFinished){

                seconds--;
                timerDuration--;

                //If minutes and seconds < 0, convert an hour to minutes and seconds
                if (seconds < 0 && minutes <= 0 && hours > 0){
                    minutes = MINUTES_MAX;
                    seconds = SECONDS_MAX;
                    hours--;
                }

                //If seconds < 0, convert a minute to seconds
                if (seconds < 0 && minutes > 0){
                    seconds = SECONDS_MAX;
                    minutes--;
                }

                //Display the timer duration
                timerCountdownHours.setText(timerCountdownFormat(hours));
                timerCountdownMinutes.setText(timerCountdownFormat(minutes));
                timerCountdownSeconds.setText(timerCountdownFormat(seconds));

                //If the user cancels the timer countdown
                if (isCancelled){
                    //Cancel and reset timer
                    cancel();
                    resetTimerCountdown();
                    pauseCounter = 0;
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                    isOngoingTimer = false;
                }

                //If the user pauses the timer countdown
                if (isPaused){
                    //Cancel the timer
                    cancel();

                    //Reset the Media Player
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                }
            }

            //When the timer countdown finishes
            public void onFinish(){
                //Reset the Media Player
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                isOngoingTimer = false;

                pauseCounter = 0;
                //Show success dialog box
                succeededTimer();
            }
        }.start();
    }

    public void leavingApp(){
        //Create the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(currentContext);
        LayoutInflater inflater = (LayoutInflater) currentContext.getSystemService(currentContext.LAYOUT_INFLATER_SERVICE);
        dialogBuilder.setView(inflater.inflate(R.layout.timer_cancel, null));
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Display the alert dialog box
        dialog.show();

        Button cancelYes = dialog.findViewById(R.id.cancelYes);
        Button cancelNo = dialog.findViewById(R.id.cancelNo);

        cancelYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeMain.this.finish();
                System.exit(0);
            }
        });

        cancelNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed()
    {

        if (isOngoingTimer){
            leavingApp();
        }

        else{
            HomeMain.this.finish();
            System.exit(0);
        }
    }
}
