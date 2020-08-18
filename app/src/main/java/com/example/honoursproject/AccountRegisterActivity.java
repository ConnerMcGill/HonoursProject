/*
Honours Project - PC part Builder
File: AccountRegisterActivity Class
Author: Conner McGill - B00320975
Date: 2020/08/11

Summary of file:

    This file contains code which allows a user to register for an account. The user must enter a
    valid email address and password. Once the users details are valid their details will be
    authenticated and stored on Firebase. The user will then be taken to the MainActivity of the
    app.

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class AccountRegisterActivity extends AppCompatActivity {

    /* Create constant patterns used for email and password validation:

       The following regex rules are:
       (?=.*[0-9]) - At least one digit
       (?=.*[a-z]) - At least one lower case letter
       (?=.*[A-Z]) - At least one upper case letter
       .{4,} - At least 6 characters

     */

    private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGITAL_CASE_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern MIN_CHARS_PATTERN = Pattern.compile(".{6,}");
    private static final String TAG = "MyActivity";


    //Declare instances of user interface elements
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button registerForAccountButton;
    //Text view that will be used to allow user to switch to LoginAccountActivity
    private TextView alreadyHaveAnAccountTextView;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_register_enter_email);
        textInputPassword = findViewById(R.id.text_register_enter_password);
        registerForAccountButton = findViewById(R.id.button_register);
        //Text view that will allow the user to switch to the LoginAccountActivity
        alreadyHaveAnAccountTextView = findViewById(R.id.text_login_redirect);

        //Initialise the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //User will attempt to sign up for an account if they press the sign up button
        registerForAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
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

    //Validate the users password
    private Boolean validatePassword() {

        //Gets the text from the email text field and trim any whitespace
        String passwordInput = textInputPassword.getEditText().getText().toString();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Password field can't be empty!");
            return false;
        } else if (!UPPER_CASE_PATTERN.matcher(passwordInput).find()) {
            textInputPassword.setError("Password must have one uppercase letter!");
            return false;
        } else if (!LOWER_CASE_PATTERN.matcher(passwordInput).find()) {
            textInputPassword.setError("Password must have one lowercase letter!");
            return false;
        } else if (!DIGITAL_CASE_PATTERN.matcher(passwordInput).find()) {
            textInputPassword.setError("Password must contain at least one number!");
            return false;
        } else if (!MIN_CHARS_PATTERN.matcher(passwordInput).find()) {
            textInputPassword.setError("Password must have a minimum of 6 characters");
            return false;
        } else {
            //Remove the error message
            textInputPassword.setError(null);
            textInputPassword.setErrorEnabled(false);
            return true;
        }

    }

    /*
       Register the users account and store the details on firebase if they are valid when they
       press the sign up button on the registration form. Switch to the MainActivity if the
       registration is successful.
     */
    private void registerNewUser() {

        //If the email or password field have invalid details then do not register the user
        if (!validateEmail() | !validatePassword()) {
            return;
        }

        //Get the text in the email and password fields if the details are valid
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        String passwordInput = textInputPassword.getEditText().getText().toString();

        /*
          This method is provided by the Firebase documentation:
          https://firebase.google.com/docs/auth/android/start
         */
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If registration is successful then switch to the login activity
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration Successful!",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AccountRegisterActivity.this,
                                    MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Failed!",
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

    //Switch the user to the MainActivity class
    private void switchToMainActivity() {
        Intent switchToMainActivityIntent = new Intent(AccountRegisterActivity.this,
                MainActivity.class);
        startActivity(switchToMainActivityIntent);

    }

}