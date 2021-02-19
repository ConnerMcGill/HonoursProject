/*
Honours Project - PC part Builder
File: MainActivity Class
Author: Conner McGill - B00320975
Date: 2020/08/18

Summary of file:
This class is the landing page of the app once the user has logged into their account. From here
the user will be able to access a range of different options within the app such as creating a list
of components or viewing their lists, etc. This class is a Superclass - Parent class of the
GuidesLandingPageActivity due to how similar they are. This allows the subclass to reuse methods
such as logging the user out

 */


package com.example.honoursproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declare an instance of firebaseAuthentication
    protected FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise the firebaseAuthentication instance
        mAuth = FirebaseAuth.getInstance();

        //Initialise the Toolbar interface element
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialise user interface elements
        CardView guidesCard = findViewById(R.id.view_guides_card);

        //When the user 'clicks' the view guides card call the openGuidesLandingPageActivity method
        guidesCard.setOnClickListener(this);

        CardView createListCard = findViewById(R.id.create_list_card);
        createListCard.setOnClickListener(this);

    }

    //Take the user to their respective activity from the button the click
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_guides_card:
                openGuideLandingPageActivity();
                break;
            case R.id.create_list_card:
                openCreateListActivity();
                break;
            default:
                Log.d("my switch statement failed", "Something went wrong with the switch statement");
                Toast.makeText(MainActivity.this, "There has been an error with selecting a button...", Toast.LENGTH_SHORT).show();
        }
    }

    //Create the inflater menu (three dots) in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        menu.removeItem(R.id.item2);
        return true;
    }

    //Add an option in the inflater menu (three dots) to let the user logout of their account by
    //calling the logoutUserFromAccount method
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                logoutUserFromAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //When the method is called by the user sign out the user with the firebase authentication
    //instance and then return the user to the login activity
    protected void logoutUserFromAccount() {

        mAuth.signOut();
        returnToLoginActivity();

    }

    //When the user logs out of their account display a toast message alerting the user and then
    //switch from the current activity to the LoginAccountActivity
    protected void returnToLoginActivity() {

        Toast.makeText(getApplicationContext(), "Logging out!",
                Toast.LENGTH_SHORT).show();

        Intent returnToLoginIntent = new Intent(MainActivity.this,
                LoginAccountActivity.class);
        startActivity(returnToLoginIntent);
    }

    //If the user 'clicks' the open guides button then switch the user from the MainActivity to the
    //openGuideLandingPageActivity
    private void openGuideLandingPageActivity() {
        Intent openGuideLandingPage = new Intent(MainActivity.this,
                GuidesLandingPageActivity.class);
        startActivity(openGuideLandingPage);
    }


    //If the user 'clicks' the create list button then switch the user from the MainActivity to the
    //CreateComputerListActivity
    private void openCreateListActivity() {
        Intent openCreateComputerListActivity = new Intent(MainActivity.this,
                CreateComputerListActivity.class);
        startActivity(openCreateComputerListActivity);
    }
}