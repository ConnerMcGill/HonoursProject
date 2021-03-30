/*
Honours Project - PC part Builder
File: LoginAccountActivity Class
Author: Conner McGill - B00320975
Date: 2020/08/07

Summary of file:

    This file contains code which allows a user to login into their account. The user must enter
    their email and password. The users details will then be validated on the clients-side
    to make sure they are valid. Once valid then the details will be authenticated on Firebase to
    check that they are valid. If valid the user will be taken to the MainActivity.

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

public class LoginAccountActivity extends AppCompatActivity {

     /* Create constant patterns used for password validation:

       The following regex rules are:
       (?=.*[0-9]) - At least one digit
       (?=.*[a-z]) - At least one lower case letter
       (?=.*[A-Z]) - At least one upper case letter
       .{4,} - At least 6 characters

     */

    //private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
    //private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
    //private static final Pattern DIGITAL_CASE_PATTERN = Pattern.compile("[0-9]");

    //Commented out old regex validation to match Firebase reset password rules that can't be changed
    private static final Pattern MIN_CHARS_PATTERN = Pattern.compile(".{6,}");

    //Declare instances for user interface elements
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button loginToAccountButton;
    //Text view that will be used to allow user to switch to AccountRegisterActivity
    private TextView registerForAccountTextView;
    //Text view that will be used to allow the user to switch to ForgotPasswordActivity
    private TextView forgotPasswordForAccountTextView;


    //Declare an instance of firebaseAuthentication
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_login_enter_email);
        textInputPassword = findViewById(R.id.text_login_enter_password);
        registerForAccountTextView = findViewById(R.id.text_register_redirect);
        forgotPasswordForAccountTextView = findViewById(R.id.text_password_reset_redirect);
        loginToAccountButton = findViewById(R.id.button_login);


        //Initialise the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();


        //User will attempt to sign into their account if they press the sign in button
        loginToAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserToAccount();
            }
        });

        /*Create on click listener for the register for account text-view which will allow the user
        to 'click' the text view and go to the register form activity. The activity will be opened
        by calling a function which calls an intent to start the AccountRegisterActivity. */
        registerForAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountRegisterActivity();
            }
        });

        /*Create on click listener for the reset password for account text-view which will allow
        the user to 'click' the text view and go to the reset password form activity. The activity
        will be opened by calling a function which calls an intent to start the
        ForgotPasswordActivity. */
        forgotPasswordForAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotPasswordActivity();
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
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Password field can't be empty!");
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
        Login to users account by checking that the users details match the account details
        stored on Firebase. If valid then switch the user to the MainActivity after they have
        logged in.
     */
    private void loginUserToAccount() {

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
        mAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If login is successful go to game main menu activity
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful!",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginAccountActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*This function is called when a user 'clicks' the registerForAccountTextView. This function
      sets up an intent which starts the AccountRegisterActivity class. */
    private void openAccountRegisterActivity() {
        Intent openAccountRegisterActivityIntent = new Intent
                (this, AccountRegisterActivity.class);
        startActivity(openAccountRegisterActivityIntent);

    }

    /*This function is called when a user 'clicks' the forgotPasswordForAccountTextView. This
      function sets up an intent which starts the ForgotPasswordActivity class. */
    private void openForgotPasswordActivity() {
        Intent openForgotPasswordActivityIntent = new Intent
                (this, ForgotPasswordActivity.class);
        startActivity(openForgotPasswordActivityIntent);
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

    //Switch the user to the MainActivity class if user is already logged in
    private void switchToMainActivity() {
        Intent switchToMainActivityIntent = new Intent(LoginAccountActivity.this,
                MainActivity.class);
        startActivity(switchToMainActivityIntent);

    }


}