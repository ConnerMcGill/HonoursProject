/*
Honours Project - PC part Builder
File: LoginAccountActivity Class
Author: Conner McGill - B00320975
Date: 2020/08/07

Summary of file:

    This file contains code which allows a user to login to the main activity of the app.
    Functions in the class allows a user to login to their account with their email and
    password. This is validated on the client. If the values are valid the details are validated
    and authenticated through Firebase. The user can also sign up for an account by clicking on the
    text-line that will take the user to a new activity to register for an account. The user
    can also go to a new activity to request for a new password for their account if they have
    forgotten their password.

 */

package com.example.honoursproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginAccountActivity extends AppCompatActivity {

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

    //Declare instances for User Interface elements
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    //Text view that will be used to allow user to switch to AccountRegisterActivity
    private TextView registerForAccountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_login_enter_email);
        textInputPassword = findViewById(R.id.text_login_enter_password);
        registerForAccountTextView = findViewById(R.id.text_register_redirect);

        /*Create on click listener for text-view which will allow the user to click the text view
        and go to the register form activity. The activity will be opened by calling a function
        which calls an intent to start the AccountRegisterActivity. */
        registerForAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountRegisterActivity();
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


    public void LoginUser(View view) {

        if (!validateEmail() | !validatePassword()) {
            return;
        }

    }


    /*This function is called when a user clicks the registerForAccountTextView. This function
      sets up an intent which starts the AccountRegisterActivity class. */
    public void openAccountRegisterActivity() {
        Intent openAccountRegisterActivityIntent = new Intent
                (this, AccountRegisterActivity.class);
        startActivity(openAccountRegisterActivityIntent);

    }


}