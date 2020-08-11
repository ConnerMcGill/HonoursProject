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

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class AccountRegisterActivity extends AppCompatActivity {

    /* Create constant patterns used for password validation:

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


    //Declare instances of user interface elements
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button registerForAccountButton;
    //Text view that will be used to allow user to switch to LoginAccountActivity
    private TextView alreadyHaveAnAccountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_register_enter_email);
        textInputPassword = findViewById(R.id.text_register_enter_password);
        alreadyHaveAnAccountTextView = findViewById(R.id.text_login_redirect);
        registerForAccountButton = findViewById(R.id.button_register);

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


    private void registerNewUser() {

        //Old code for when I was trying to add firebase earlier. I will keep it
        //for when I go back to it

        //Store the email and password that was entered into the EditText fields
        String email, password;

        //email = emailRegisterText.getText().toString();
        //password = passwordRegisterText.getText().toString();

        //You would call the validation functions that check the email and password are fine
        //return if they're not
        //Then run the firebase auth thing and login

        //Then somewhere at the end of the code just have the intent for the textview that
        //switches to the login activity
    }


    public void registerNewUser(View view) {

        if (!validateEmail() | !validatePassword()) {
            return;
        }

    }
}