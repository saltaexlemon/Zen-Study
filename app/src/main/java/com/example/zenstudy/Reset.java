package com.example.zenstudy;

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

import java.util.Objects;

public class Reset extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText mEmail;
    TextView clickSignIn;
    Button resetBtn;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        //initialize the important field here
        mEmail = findViewById(R.id.resetEmail);
        mEmail.addTextChangedListener(this);
        clickSignIn = findViewById(R.id.clickSignInLink);

        resetBtn = findViewById(R.id.resetButton);
        resetBtn.setEnabled(false);

        clickSignIn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

    }


    private void resetPassword(){

        String email = mEmail.getText().toString();

        mEmail.setError((email.isEmpty())? "Please input your email":null);

        if(!email.isEmpty()){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task->{
                        if(task.isSuccessful()){
                            Toast.makeText(Reset.this, "Email sent", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Reset.this,
                                    Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button && v.getId() == R.id.resetButton){
            resetPassword();
        }else if(v instanceof TextView && v.getId() == R.id.clickSignInLink){
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        resetBtn.setEnabled(!(s.length()==0));
    }

    @Override
    public void afterTextChanged(Editable s) {
        String email = mEmail.getText().toString();
        resetBtn.setEnabled(!email.isEmpty());

    }
}
