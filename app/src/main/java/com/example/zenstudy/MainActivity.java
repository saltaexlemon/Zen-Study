package com.example.zenstudy;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    private FirebaseUser user;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText emailEditText,passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //get the current logged in user information
        user = FirebaseAuth.getInstance().getCurrentUser();

        // if user already login
        if (user != null) {
            //go to main page
            Intent toHome = new Intent(this, HomeMain.class);
            toHome.putExtra("userID", user.getUid());
            startActivity(toHome);
        }
    }

    /**
     * Initialize user interface binding for login button
     * login email edittext and login password edittext
     *
     * this method is private because it is only callable
     * within this class
     */
    private void initializeUI(){

        loginButton = findViewById(R.id.loginButton);
        //disable login button click if both email and password field is empty
        loginButton.setEnabled(false);
        //set click listener for login button
        loginButton.setOnClickListener(this);

        Button registerBtn = findViewById(R.id.registerUserBtn);
        registerBtn.setOnClickListener(this);

        TextView clickResetLink = findViewById(R.id.clickResetLink);
        clickResetLink.setOnClickListener(this);

        emailEditText = findViewById(R.id.loginEmail);
        emailEditText.addTextChangedListener(this);

        passwordEditText = findViewById(R.id.loginPassword);
        passwordEditText.addTextChangedListener(this);
    }


    /**
     * Sign in user with email and password
     *
     * pre-condition: email and password must no be empty
     *
     * If the task is successful, redirect the user to main page
     * else the error will display at the edit text
     */
    private void login(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // if the email edit text is empty
        // display the error, else no error
        emailEditText.setError((email.isEmpty())? "Please input your email" : null);

        // if the password edit text is empty
        // display the error, else no error
        passwordEditText.setError((password.isEmpty())? "Please input your password" : null);


        // if both email field and password field is not empty
        // login user into firebase and redirect to main page
        if(!email.isEmpty() && !password.isEmpty()){

            //start process of sign in here
            auth.signInWithEmailAndPassword(email,password)
                    //shorthand anonymous function called lambda function in java
                    //this shorthand function only can be used when the interface
                    //has only one method in it
                    .addOnCompleteListener(task -> {

                        //check is login successful
                        if(task.isSuccessful()){
                            user = auth.getCurrentUser(); //get user info
                            System.out.println(user.getUid());
                            Intent toHome = new Intent(MainActivity.this, HomeMain.class);
                            toHome.putExtra("userID", user.getUid());
                            startActivity(toHome); // go to main page
                        }else{
                            //display error
                            Toast.makeText(MainActivity.this,
                                    Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    /**
     * Allows the user to switch to different activities
     * @param view a view or button that is triggered with user click
     */
    @Override
    public void onClick(View view){


        if (view.getId() == R.id.loginButton) {
            login(); //run the login logic above
        }else if(view.getId() == R.id.registerUserBtn){
            // redirect user to register page
            Intent toRegister = new Intent(this, Register.class);
            startActivity(toRegister);
        }else if(view.getId() == R.id.clickResetLink){
            // redirect user to reset page
            Intent toReset = new Intent(this, Reset.class);
            startActivity(toReset);
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //if the text field is empty
        //disable the login button
        if(s.length()==0){
            loginButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //enable the login button if both text field are not empty
        loginButton.setEnabled(!email.isEmpty() && !password.isEmpty());
    }
}