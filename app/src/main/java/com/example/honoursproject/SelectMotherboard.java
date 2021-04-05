/*
Honours Project - PC part Builder
File: SelectMotherboard Class
Author: Conner McGill - B00320975
Date: 2021/03/03

Summary of file:

    This class setups the recyclerview and populates the recyclerview with the relevant data
    from the Motherboard firestore collection documents. The user can then either view the part in more
    detail which will open a new activity with all the relevant data for the Motherboard or add the Motherboard to
    their list which will return the selected Motherboard to the CreateComputerListActivity

 */

package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class SelectMotherboard extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectMotherboardTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to CPU Collection in firestore
    private CollectionReference motherboardReference = db.collection("motherboard");

    //RecyclerView Adapter for the motherboard data
    private MotherboardAdapter motherboardAdapter;

    //DataStorage instance
    DataStorage motherboardData = new DataStorage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_motherboard);

        //Connect recyclerView to adapter
        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Motherboard");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });


    }

    private void setupRecyclerView() {
        //Create query against the cpu collection which sorts the data by the price from lowest price
        //to highest price i.e. ascending order

        String cpuSocket = motherboardData.getComputerList().get("CPU SOCKET");

        Query query;

        if (cpuSocket != null) {
            query = motherboardReference
                    .orderBy("price", Query.Direction.ASCENDING)
                    .whereEqualTo("socket", cpuSocket);
        } else {
            query = motherboardReference.orderBy("price", Query.Direction.ASCENDING);
        }


        //Create FirestoreRecyclerOptions and bind query into the adapter
        FirestoreRecyclerOptions<Motherboard> options = new FirestoreRecyclerOptions.Builder<Motherboard>()
                .setQuery(query, Motherboard.class)
                .build();

        //Assign RecyclerOptions to the adapter
        motherboardAdapter = new MotherboardAdapter(options);

        //Reference the recyclerView and assign the adapter to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_motherboard);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(motherboardAdapter);

        //Implement the OnClickListener interface from the MotherboardAdapter MotherboardHolder innerclass constructor
        motherboardAdapter.setOnItemClickListener(new MotherboardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference motherboardRef = db.collection("motherboard").document(documentName);
                motherboardRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> motherboardDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + motherboardDetails);


                                Intent intent = new Intent(SelectMotherboard.this, ViewMotherboardDetails.class);
                                intent.putExtra("hashMap", motherboardDetails);
                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        });
            }

            @Override
            public void onAddButtonClick(DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked button on the card as a test
                //Just to see if I can make sure the button click is separated from the card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Btn Add Document ID: " + documentName);

                //Get the required data I am wanting from the document
                DocumentReference motherboardRef = db.collection("motherboard").document(documentName);
                motherboardRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String motherboardName = (String) documentSnapshot.get("name");
                                Log.d(TAG, "Motherboard Name: " + motherboardName);
                                //Convert the price double into a string for later
                                Double motherboardPriceDouble = (Double) documentSnapshot.get("price");
                                String motherboardPriceString = Double.toString(motherboardPriceDouble);
                                Log.d(TAG, "Motherboard Price: " + motherboardPriceString);
                                Long motherboardTDP = (Long) documentSnapshot.get("tdp");
                                Log.d(TAG, "Motherboard tdp: " + motherboardTDP);
                                String motherboardSocket = (String) documentSnapshot.get("socket");
                                String motherboardSize = (String) documentSnapshot.get("form-factor");


                                //Pass the data back to the CreateComputerListActivity by storing the data
                                // into the DataStorage hashmap and then returning to the CreateComputerListActivity:
                                Intent passMotherboardDataToCreateComputerActivity = new Intent
                                        (SelectMotherboard.this, CreateComputerListActivity.class);

                                motherboardData.getComputerList().put("MOTHERBOARD NAME", motherboardName);
                                motherboardData.getComputerList().put("MOTHERBOARD PRICE", motherboardPriceString);
                                motherboardData.getComputerList().put("MOTHERBOARD TDP", String.valueOf(motherboardTDP));
                                motherboardData.getComputerList().put("MOTHERBOARD SOCKET", motherboardSocket);

                                Log.d(TAG, "onSuccess: " + motherboardData.getComputerList().get("MOTHERBOARD NAME"));
                                Log.d(TAG, "onSuccess: " + motherboardData.getComputerList().get("MOTHERBOARD PRICE"));
                                Log.d(TAG, "onSuccess: " + motherboardData.getComputerList().get("MOTHERBOARD TDP"));
                                Log.d(TAG, "onSuccess: " + motherboardData.getComputerList().get("MOTHERBOARD SOCKET"));


                                startActivity(passMotherboardDataToCreateComputerActivity);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        });
            }
        });


    }


    //When the app goes into the foreground the recyclerview listen for database changes
    @Override
    protected void onStart() {
        super.onStart();
        motherboardAdapter.startListening();
    }

    //When the app goes into the background the recyclerview will not update anything
    @Override
    protected void onStop() {
        super.onStop();
        motherboardAdapter.stopListening();
    }


}