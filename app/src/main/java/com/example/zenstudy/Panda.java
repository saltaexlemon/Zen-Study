package com.example.zenstudy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Panda extends AppCompatActivity {

    //Constant variable for the URL of the database
    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    final DatabaseReference reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child(HomeMain.userID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panda);

        //Initialization of elements in the panda page
        ImageView roomSkin = findViewById(R.id.roomSkin);
        Button backButton = findViewById(R.id.backButton);

        //Retrieve data from Firebase
        DatabaseReference userReference = reference;
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Retrieve the skin image
                String roomSkinURL = snapshot.child("User").child("EquippedItemRoom").getValue(String.class);

                //Display the skin image
                Picasso.get().load(roomSkinURL).into(roomSkin);
            }

            //Display the database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Panda.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        //Return to home activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
