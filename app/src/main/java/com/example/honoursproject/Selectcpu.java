/*
Honours Project - PC part Builder
File: Selectcpu Class
Author: Conner McGill - B00320975
Date: 2021/02/19

Summary of file:

    This class setups the recyclerview and populates the recyclerview with the relevant data
    from the CPUs firestore collection documents. The user can then either view the part in more
    detail which will open a new activity with all the relevant data for the CPU or add the cpu to
    their list which will return the selected CPU to the CreateComputerListActivity

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
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class Selectcpu extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectcpuTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to CPU Collection in firestore
    private CollectionReference cpuReference = db.collection("CPUs");

    //RecyclerView Adapter for the CPU data
    private CPUAdapter cpuAdapter;

    //DataStorage instance
    DataStorage cpuData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcpu);

        //Connect recyclerView to adapter
        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select CPU");

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

        //Setup query to filter results to only provide the user with parts compatible with
        //other parts that they have selected in their list

        String motherboardSocket = cpuData.getComputerList().get("MOTHERBOARD SOCKET");

        Query query;

        if (motherboardSocket != null) {
            query = cpuReference
                    .orderBy("price", Query.Direction.ASCENDING)
                    .whereEqualTo("socket", motherboardSocket);
        } else {
            query = cpuReference.orderBy("price", Query.Direction.ASCENDING);
        }


        //Create FirestoreRecyclerOptions and bind query into the adapter
        FirestoreRecyclerOptions<CPU> options = new FirestoreRecyclerOptions.Builder<CPU>()
                .setQuery(query, CPU.class)
                .build();

        //Assign RecyclerOptions to the adapter
        cpuAdapter = new CPUAdapter(options);

        //Reference the recyclerView and assign the adapter to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_cpu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cpuAdapter);


        //Implement the OnClickListener interface from the CPUAdapter CPUHolder innerclass constructor
        cpuAdapter.setOnItemClickListener(new CPUAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference cpuRef = db.collection("CPUs").document(documentName);
                cpuRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> cpuDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + cpuDetails);


                                Intent intent = new Intent(Selectcpu.this, ViewCPUDetails.class);
                                intent.putExtra("hashMap", cpuDetails);
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
                DocumentReference cpuRef = db.collection("CPUs").document(documentName);
                cpuRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String cpuName = (String) documentSnapshot.get("name");
                                Log.d(TAG, "CPU Name: " + cpuName);
                                //Convert the price double into a string for later
                                Double cpuPriceLong = (Double) documentSnapshot.get("price");
                                String cpuPriceString = Double.toString(cpuPriceLong);
                                Log.d(TAG, "CPU Price: " + cpuPriceString);
                                //I will need this later for validation so a user can't have a
                                //amd cpu on a intel motherboard for example
                                String cpuSocket = (String) documentSnapshot.get("socket");
                                Log.d(TAG, "CPU Socket: " + cpuSocket);
                                Long cpuTDP = (Long) documentSnapshot.get("tdp");
                                Log.d(TAG, "CPU TDP: " + cpuTDP);

                                //Pass the data back to the CreateComputerListActivity by storing the
                                // data in the DataStorage Hashmap and then returning to the CreateComputerListActivity:
                                Intent passCPUDataToCreateComputerActivity = new Intent
                                        (Selectcpu.this, CreateComputerListActivity.class);


                                cpuData.getComputerList().put("CPU NAME", cpuName);
                                cpuData.getComputerList().put("CPU PRICE", cpuPriceString);
                                cpuData.getComputerList().put("CPU SOCKET", cpuSocket);
                                cpuData.getComputerList().put("CPU TDP", String.valueOf(cpuTDP));
                                Log.d(TAG, "onSuccess: " + cpuData.getComputerList().get("CPU NAME"));
                                Log.d(TAG, "onSuccess: " + cpuData.getComputerList().get("CPU PRICE"));
                                Log.d(TAG, "onSuccess: " + cpuData.getComputerList().get("CPU SOCKET"));
                                Log.d(TAG, "onSuccess: " + cpuData.getComputerList().get("CPU TDP"));


                                startActivity(passCPUDataToCreateComputerActivity);


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
        cpuAdapter.startListening();
    }

    //When the app goes into the background the recyclerview will not update anything
    @Override
    protected void onStop() {
        super.onStop();
        cpuAdapter.stopListening();
    }
}