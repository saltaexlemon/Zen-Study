package com.example.zenstudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    //Constant variable for the URL of the database
    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    final DatabaseReference reference = FirebaseDatabase.getInstance(firebaseURL).getReference();

    //Initialization of variables to retrieve data from the database
    private String userName = "";
    private ArrayList<Music> musicArrayList = new ArrayList<>();
    private String equippedMusicID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Initialization of elements in settings
        Button backButton = findViewById(R.id.backButton);
        Button chooseMusicButton = findViewById(R.id.chooseMusicButton);
        Button creditsButton = findViewById(R.id.creditsButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        //Return to the home activity
        backButton.setOnClickListener(v -> finish());

        //Allow users to choose the music played
        chooseMusicButton.setOnClickListener(v -> showMusic());

        //Display the credits
        creditsButton.setVisibility(View.GONE);
        creditsButton.setOnClickListener(v -> showCredits());

        //Logout user
        logoutButton.setOnClickListener(v -> logout());

        //Retrieve data from Firebase
        /*DatabaseReference userReference = reference.child(HomeMain.userID);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


            }

            //Display database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    /**
     * Logout user and redirect user to login page
     */
    private void logout(){
        HomeMain.isPlayingMusic = false;

        FirebaseAuth.getInstance().signOut();
        Intent toLogin = new Intent(this,MainActivity.class);
        startActivity(toLogin);
    }

    /**
     * Show available music and allow user to equip music
     */
    public void showMusic(){

        //Initialization of variables to create a dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.settings_music, null));
        dialog = dialogBuilder.create();

        //Display the dialog box
        dialog.show();

        //Initialization of elements in the dialog box
        GridView musicGridView = dialog.findViewById(R.id.musicGridView);

        //Retrieve data from Firebase
        DatabaseReference userReference = reference.child(HomeMain.userID).child("User");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Retrieve the equipped music ID
                equippedMusicID = snapshot.child("EquippedMusicID").getValue(String.class);
            }

            //Display database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        //Retrieve data from Firebase
        DatabaseReference musicReference = reference.child("Music_Storage");
        musicReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Clear the arraylist
                musicArrayList.clear();

                //Loop to retrieve all the data in Music_Storage
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //Retrieve the data for each music
                    String musicID = dataSnapshot.child("MusicID").getValue(String.class);
                    String musicName = dataSnapshot.child("MusicName").getValue(String.class);
                    String musicAudio = dataSnapshot.child("MusicAudio").getValue(String.class);

                    //Insert the music data into a Music object
                    Music music = new Music(musicID, musicName, musicAudio);

                    //Add the Music object to the arraylist
                    musicArrayList.add(music);
                }

                //Set the gridview for the musicArrayList
                MusicGVAdapter adapter = new MusicGVAdapter(Settings.this, musicArrayList, equippedMusicID);
                musicGridView.setAdapter(adapter);
            }

            //Display database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        //Change the equipped music when an item in the grid is clicked
        musicGridView.setOnItemClickListener((parent, view, position, id) -> {
            //Get the music that was clicked
            Music music = musicArrayList.get(position);

            //Change the equipped music in the database
            userReference.child("EquippedMusicID").setValue(music.getMusicID());
            userReference.child("EquippedMusicAudio").setValue(music.getMusicAudio());
            dialog.dismiss();
        });
    }

    /**
     * Display the credits of the application
     */
    public void showCredits(){
        //Initialization of variables to create a dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.settings_credits, null));
        dialog = dialogBuilder.create();

        //Show the credits dialog box
        dialog.show();
    }
}