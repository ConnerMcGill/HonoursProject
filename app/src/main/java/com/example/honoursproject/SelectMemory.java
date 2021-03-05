/*
Honours Project - PC part Builder
File: SelectMotherboard Class
Author: Conner McGill - B00320975
Date: 2021/03/04

Summary of file:

    This class setups the recyclerview and populates the recyclerview with the relevant data
    from the Memory firestore collection documents. The user can then either view the part in more
    detail which will open a new activity with all the relevant data for the Memory or add the Memory to
    their list which will return the selected Memory to the CreateComputerListActivity

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

public class SelectMemory extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectMemoryTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to CPU Collection in firestore
    private CollectionReference memoryReference = db.collection("memory");

    //RecyclerView Adapter for the motherboard data
    private MemoryAdapter memoryAdapter;

    //DataStorage instance
    DataStorage memoryData = new DataStorage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_memory);


        //Connect recyclerView to adapter
        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Memory");

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
        Query query = memoryReference.orderBy("price", Query.Direction.ASCENDING);

        //Create FirestoreRecyclerOptions and bind query into the adapter
        FirestoreRecyclerOptions<Memory> options = new FirestoreRecyclerOptions.Builder<Memory>()
                .setQuery(query, Memory.class)
                .build();

        //Assign RecyclerOptions to the adapter
        memoryAdapter = new MemoryAdapter(options);

        //Reference the recyclerView and assign the adapter to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_memory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(memoryAdapter);

        //Implement the OnClickListener interface from the MotherboardAdapter MotherboardHolder innerclass constructor
        memoryAdapter.setOnItemClickListener(new MemoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference memoryRef = db.collection("memory").document(documentName);
                memoryRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> memoryDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + memoryDetails);


                                Intent intent = new Intent(SelectMemory.this, ViewMemoryDetails.class);
                                intent.putExtra("hashMap", memoryDetails);
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
                DocumentReference memoryRef = db.collection("memory").document(documentName);
                memoryRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String memoryName = (String) documentSnapshot.get("name");
                                Log.d(TAG, "Memory Name: " + memoryName);
                                //Convert the price double into a string for later
                                Double memoryPriceDouble = (Double) documentSnapshot.get("price");
                                String memoryPriceString = Double.toString(memoryPriceDouble);
                                Log.d(TAG, "Memory Price: " + memoryPriceString);


                                //Pass the data back to the CreateComputerListActivity by storing the data
                                // into the DataStorage hashmap and then returning to the CreateComputerListActivity:
                                Intent passMemoryDataToCreateComputerActivity = new Intent
                                        (SelectMemory.this, CreateComputerListActivity.class);

                                memoryData.getComputerList().put("MEMORY NAME", memoryName);
                                memoryData.getComputerList().put("MEMORY PRICE", memoryPriceString);
                                Log.d(TAG, "onSuccess: " + memoryData.getComputerList().get("MEMORY NAME"));
                                Log.d(TAG, "onSuccess: " + memoryData.getComputerList().get("MEMORY PRICE"));


                                startActivity(passMemoryDataToCreateComputerActivity);
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
        memoryAdapter.startListening();
    }

    //When the app goes into the background the recyclerview will not update anything
    @Override
    protected void onStop() {
        super.onStop();
        memoryAdapter.stopListening();
    }
}