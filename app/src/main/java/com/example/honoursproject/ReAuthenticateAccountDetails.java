/*
Honours Project - PC part Builder
File: ReAuthenticateAccountDetails Class
Author: Conner McGill - B00320975
Date: 2021/04/05

Summary of file:

   The user is taken to this activity first whenever they would like to update account details.
   Firestore and Firebase require the user to re-authenticate their details if they haven't logged in
   recently before updating their account details otherwise the following exception is called:
   https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuthRecentLoginRequiredException

   More about the re authentication method can be found here:
   https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user

 */


package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ReAuthenticateAccountDetails extends AppCompatActivity {

    private static final String TAG = "ReAuthenticateAccountDetails";

    //Regex pattern for password form
    private static final Pattern MIN_CHARS_PATTERN = Pattern.compile(".{6,}");

    //Declare instances for user interface elements
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button submitAccountDetails;

    String emailInput;
    String passwordInput;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_autehnticate_account_details);

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_update_email);
        textInputPassword = findViewById(R.id.text_update_password);
        submitAccountDetails = findViewById(R.id.submitAccountDetails);

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Re-Authenticate Account Details:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });



        //Setup the re-authentication submit button onclick here
        submitAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reAuthenticateUserDetails();
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

    //Check users email and password are valid and then try re-authenticate the users details
    private void reAuthenticateUserDetails() {

        if (!validateEmail() | !validatePassword()) {
            return;
        }

        //Get the text in the email and password fields if the details are valid
        emailInput = textInputEmail.getEditText().getText().toString().trim();
        passwordInput = textInputPassword.getEditText().getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(emailInput, passwordInput);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                        if (task.isSuccessful()) {
                            //Do some intent here that takes user to other activity
                            Intent intent = new Intent(ReAuthenticateAccountDetails.this,
                                    EditAccountDetails.class);
                            startActivity(intent);
                        } else {
                            // Password is incorrect
                            Toast.makeText(getApplicationContext(), "Login failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}