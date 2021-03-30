/*
Honours Project - PC part Builder
File: EditAccountDetails Class
Author: Conner McGill - B00320975
Date: 2021/03/30

Summary of file:
This class acts as the activity that allows the user to update their account email or password.
The user if they choose to is also able to delete their account

 */


package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class EditAccountDetails extends AppCompatActivity {

    private static final String TAG = "EditAccountDetails";

    //Declare an instance of firebaseAuthentication
    protected FirebaseAuth mAuth;

    //Regex validation to match Firebase reset password rules that can't be changed
    private static final Pattern MIN_CHARS_PATTERN = Pattern.compile(".{6,}");

    //Declare instances for user interface elements
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button updateEmailButton;
    private Button updatePasswordButton;

    //Variables for getting the text in the email and Password Fields
    String emailInput;
    String passwordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_details);


        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Account Details:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_update_email);
        textInputPassword = findViewById(R.id.text_update_password);
        updateEmailButton = findViewById(R.id.updateEmailBtn);
        updatePasswordButton = findViewById(R.id.updatePasswordBtn);

        //Initialise the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Add OnClick Methods here when I setup the other methods for validation
        updateEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsersEmail();
            }
        });

    }

    //Validate the users email address
    private Boolean validateEmail() {

        //Gets the text from the email text field and trim any whitespace
        emailInput = textInputEmail.getEditText().getText().toString().trim();

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
        passwordInput = textInputPassword.getEditText().getText().toString().trim();

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


    //In order to update email, delete accounts or reset passwords the user account must be re-authenticated which is great tae learn now
    //So I am going to leave this here as a note to self:

    //https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user

    //I will probably need to remake similar activates or one if I can which deals with the user re-authenticating their
    //details first before I can then do things like update email:

    //https://firebase.google.com/docs/auth/android/manage-users

    private void updateUsersEmail(){

        if (!validateEmail()) {
            return;
        }

        //Get the text in the email field if the details are valid
        emailInput = textInputEmail.getEditText().getText().toString().trim();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(emailInput)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });

    }










}