/*
Honours Project - PC Part Builder
File: ForgotPasswordActivity class
Author: Conner McGill - B00320975
Date: 2020/08/25

Summary of file:

    This file contains code which allows the user of an account to reset their password if they
    have forgotten it. The user enters their email and presses the reset password button. If the
    email is valid then they will be sent an email with a link to reset their password. If the
    email is invalid or doesn't belong to an account then a toast message will be displayed
    mentioning that an reset email was unable to be sent.

 */
package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Declare instances for user interface elements
    private TextInputLayout textInputEmail;
    private Button sendResetPasswordEmail;
    //Text view that will be used to allow the user to switch to LoginAccountActivity
    private TextView alreadyHaveAnAccountTextView;


    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_reset_password_enter_email);
        sendResetPasswordEmail = findViewById(R.id.button_send_reset_password_email);
        //Text view that will allow the user to switch to the LoginAccountActivity
        alreadyHaveAnAccountTextView = findViewById(R.id.text_password_reset_redirect_to_login_activity);

        //Initialise the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //User will attempt to get password reset email if they press the reset password button
        sendResetPasswordEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPasswordResetEmail();
            }
        });

        /*Create on click listener for text-view which will allow the user to 'click' the text view
        and go to the login form activity. The activity will be opened by calling a function
        which calls an intent to start the LoginAccountActivity. */
        alreadyHaveAnAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginAccountActivity();
            }
        });


    }


    //Validate the users email address
    private Boolean validateEmail() {

        //Gets the text from the email text field and trim any whitespace
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Email field can't be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address!");
            return false;
        } else {
            //Remove the error message
            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);
            return true;
        }

    }

    private void requestPasswordResetEmail() {

        //If the email field has invalid emails then do not send a password reset link to the email
        if (!validateEmail()) {

            return;

        }

        //Get the text in the email field if the details are valid
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        /*
          This method is provided by the Firebase:
          https://firebase.google.com/docs/auth/android/start
          https://stackoverflow.com/questions/42972226/firebase-send-password-reset-email-method-crashes-the-app
         */
        mAuth.sendPasswordResetEmail(emailInput)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //If email is valid then send reset email link and switch to LoginAccountActivity class
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Reset link sent to your email successful",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ForgotPasswordActivity.this,
                                    LoginAccountActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Unable to send reset link email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*This function is called when a user clicks the alreadyHaveAnAccountTextView. This function
    sets up an intent which starts the LoginAccountActivity class. */
    private void openLoginAccountActivity() {
        Intent openLoginAccountActivityIntent = new Intent
                (this, LoginAccountActivity.class);
        startActivity(openLoginAccountActivityIntent);
    }

    //Check if the user is already logged in. If they are then start the MainActivity.Class
    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            switchToMainActivity();

        }

    }

    //Switch the user to the MainActivity class if they are logged in
    private void switchToMainActivity() {
        Intent switchToMainActivityIntent = new Intent(ForgotPasswordActivity.this,
                MainActivity.class);
        startActivity(switchToMainActivityIntent);

    }


}