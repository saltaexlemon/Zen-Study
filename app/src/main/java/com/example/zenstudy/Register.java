package com.example.zenstudy;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    EditText mPassword,mEmail;
    Button mSignupBtn;
    TextView mSignInBtn;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";

    DatabaseReference reference = FirebaseDatabase.getInstance(firebaseURL).getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //the initializeUI method to set id to the variables
        initializeUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //get the current logged in user information
        user = FirebaseAuth.getInstance().getCurrentUser();

        // if user already login redirects to home page
        if (user != null) {
            Intent toHome = new Intent(this, HomeMain.class);
            startActivity(toHome);
        }
    }

    /**
     * Initialize UI is for configuration the view of
     * the app like edit text, buttons, image, etc
     * and bind with the id set.
     *
     */
    private void initializeUI(){


        mPassword = findViewById(R.id.registerPassword);
        mPassword.addTextChangedListener(this);

        mEmail = findViewById(R.id.registerEmail);
        mEmail.addTextChangedListener(this);

        mSignupBtn = findViewById(R.id.SignupButton);
        mSignupBtn.setEnabled(false);
        mSignupBtn.setOnClickListener(this);

        mSignInBtn = findViewById(R.id.clickSignIn);
        mSignInBtn.setOnClickListener(this);
    }

    /**
     * Sign up or Register user into the firebase.
     * Precondition: the username, email, password must not be empty
     * After the signup is complete, it redirect the user to the
     * login page.
     *
     */
    private void signUp(){

        //get all the input from the user
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        //display error if the edit text field is empty
        mPassword.setError(password.isEmpty()? "Please input your password":null);
        mEmail.setError(email.isEmpty()? "Please input your email":null);

        //if the username, password and email is not empty
        //register the user by email and password
        if(!password.isEmpty() && !email.isEmpty()){

            // register the user with email and password
            fAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            user = fAuth.getCurrentUser();

                            Toast.makeText(Register.this,user.getUid(),Toast.LENGTH_SHORT).show();
                            String userID = user.getUid();

                            reference.child(user.getUid()).child("Item").child("BambooRoom").child("IsOwned").setValue(false);
                            reference.child(user.getUid()).child("Item").child("BambooRoom").child("ItemID").setValue("BambooRoom");
                            reference.child(user.getUid()).child("Item").child("BambooRoom").child("ItemName").setValue("Bamboo Room");
                            reference.child(user.getUid()).child("Item").child("BambooRoom").child("ItemPrice").setValue(1000);
                            reference.child(user.getUid()).child("Item").child("BambooRoom").child("ItemRoomImage").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/bamboo%20fortress%202%20.png?alt=media&token=5cf10b2c-dc0b-4d5e-aabb-eabb7129bb28");
                            reference.child(user.getUid()).child("Item").child("BambooRoom").child("ItemTimerImage").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/bamboo%20fortress%201%20.png?alt=media&token=73719eee-cf53-4469-ac32-e85730ee8225");

                            reference.child(user.getUid()).child("Item").child("BasicRoom").child("IsOwned").setValue(true);
                            reference.child(user.getUid()).child("Item").child("BasicRoom").child("ItemID").setValue("BasicRoom");
                            reference.child(user.getUid()).child("Item").child("BasicRoom").child("ItemName").setValue("Basic Room");
                            reference.child(user.getUid()).child("Item").child("BasicRoom").child("ItemPrice").setValue(1000);
                            reference.child(user.getUid()).child("Item").child("BasicRoom").child("ItemRoomImage").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/basic%20room%202%20.png?alt=media&token=67aa1416-67a4-49e8-bc5f-840a8a301648");
                            reference.child(user.getUid()).child("Item").child("BasicRoom").child("ItemTimerImage").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/basic%20room%201%20.png?alt=media&token=79e17f36-5c6e-492c-a9a1-8316e2ed2cfb");

                            reference.child(user.getUid()).child("Item").child("JapanRoom").child("IsOwned").setValue(false);
                            reference.child(user.getUid()).child("Item").child("JapanRoom").child("ItemID").setValue("JapanRoom");
                            reference.child(user.getUid()).child("Item").child("JapanRoom").child("ItemName").setValue("Japan Room");
                            reference.child(user.getUid()).child("Item").child("JapanRoom").child("ItemPrice").setValue(1000);
                            reference.child(user.getUid()).child("Item").child("JapanRoom").child("ItemRoomImage").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/jap%20inn%202%20.png?alt=media&token=ca7e2d8d-b1a9-46b5-9cb4-9ad2f30d5acc");
                            reference.child(user.getUid()).child("Item").child("JapanRoom").child("ItemTimerImage").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/jap%20inn%201%20.png?alt=media&token=84806082-d901-4c13-809b-536d443990ac");

                            reference.child(user.getUid()).child("User").child("Bamboo").setValue(0);
                            reference.child(user.getUid()).child("User").child("EquippedItemRoom").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/basic%20room%202%20.png?alt=media&token=67aa1416-67a4-49e8-bc5f-840a8a301648");
                            reference.child(user.getUid()).child("User").child("EquippedItemTimer").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/basic%20room%201%20.png?alt=media&token=79e17f36-5c6e-492c-a9a1-8316e2ed2cfb");
                            reference.child(user.getUid()).child("User").child("EquippedMusicAudio").setValue("https://firebasestorage.googleapis.com/v0/b/pandaboodcs.appspot.com/o/John-Bartmann-A-Kwela-Fella.mp3?alt=media&token=037d39e4-0c47-4245-b755-c1263f5038b0");
                            reference.child(user.getUid()).child("User").child("EquippedMusicID").setValue("Jazz1");
                            reference.child(user.getUid()).child("User").child("UserID").setValue(user.getUid());

                            //go to home page
                            Intent toHome = new Intent(this, HomeMain.class);
                            toHome.putExtra("userID", user.getUid());
                            startActivity(toHome);
                        }else{

                            Toast.makeText(Register.this,
                                    Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("Register", task.getException().toString());
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.SignupButton) {
            signUp();
        }else if(v.getId()==R.id.clickSignIn){
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //if the text field is empty, disable sign up button
        if(s.length()==0){
            mSignupBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        //if both text field is not empty, enable signup button
        mSignupBtn.setEnabled(!email.isEmpty() && !password.isEmpty());
    }
}