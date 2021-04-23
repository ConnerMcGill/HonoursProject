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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
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
    private Button deleteAccountButton;

    //Variables for getting the text in the email and Password Fields
    String emailInput;
    String passwordInput;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String userID;


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
                returnToMainActivity();
                finish();
            }
        });

        //Initialise user interface elements
        textInputEmail = findViewById(R.id.text_update_email);
        textInputPassword = findViewById(R.id.text_update_password);
        updateEmailButton = findViewById(R.id.updateEmailBtn);
        updatePasswordButton = findViewById(R.id.updatePasswordBtn);
        deleteAccountButton = findViewById(R.id.deleteAccountBtn);


        //Initialise the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Get the users ID
        userID = mAuth.getUid();

        //Add OnClick Methods here when I setup the other methods for validation
        updateEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsersEmail();
            }
        });

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsersPassword();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUsersAccountDetails();
            }
        });


    }

    private void returnToMainActivity() {
        Intent openMainActivity = new Intent
                (this, MainActivity.class);
        startActivity(openMainActivity);
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


    //https://firebase.google.com/docs/auth/android/manage-users

    private void updateUsersEmail() {

        if (!validateEmail()) {
            return;
        }

        //Get the text in the email field if the details are valid
        emailInput = textInputEmail.getEditText().getText().toString().trim();


        user.updateEmail(emailInput)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            Toast.makeText(getApplicationContext(), "Email Has Been Updated Successfully!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Email Failed To Update!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    //https://firebase.google.com/docs/auth/android/manage-users

    private void updateUsersPassword() {
        if (!validatePassword()) {
            return;
        }

        passwordInput = textInputPassword.getEditText().getText().toString();

        user.updatePassword(passwordInput)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                            Toast.makeText(getApplicationContext(), "Password Has Been Updated Successfully!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Password Failed To Update!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    //Delete the users account button:
    private void deleteUsersAccountDetails() {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            //Call Batch Write Function that deletes the users lists from the Database
                            deleteUsersLists();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed To Delete Account Details!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Delete the users saved lists

    private void deleteUsersLists() {
        //Setup reference to user-lists collection in firestore
        FirebaseFirestore.getInstance().collection("user-lists")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Store the users lists in a list which is then deleted as a batch
                        //This provides better performance than deleting each document individually
                        //https://firebase.google.com/docs/firestore/manage-data/transactions#batched-writes
                        WriteBatch batch = FirebaseFirestore.getInstance().batch();

                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList) {
                            batch.delete(snapshot.getReference());
                        }

                        //Call Batch.Commit() in order to push the changes to the database
                        batch.commit()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Your Account and Details Have Been Deleted!");
                                        Toast.makeText(getApplicationContext(), "Account Deleted Successfully!",
                                                Toast.LENGTH_SHORT).show();
                                        //Use Intent that takes user to the login screen
                                        Intent intent = new Intent(EditAccountDetails.this,
                                                LoginAccountActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Failed to commit batch delete");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed To Delete Users Lists");
                    }
                });
    }

    //Over ride back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditAccountDetails.this, MainActivity.class));
        finish();
    }

}