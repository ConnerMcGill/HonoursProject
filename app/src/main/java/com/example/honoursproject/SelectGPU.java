/*
Honours Project - PC part Builder
File: SelectGPU Class
Author: Conner McGill - B00320975
Date: 2021/03/05

Summary of file:

    This class setups the recyclerview and populates the recyclerview with the relevant data
    from the gpu firestore collection documents. The user can then either view the part in more
    detail which will open a new activity with all the relevant data for the gpu or add the gpu to
    their list which will return the selected Storage to the CreateComputerListActivity

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

public class SelectGPU extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectGPUTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to CPU Collection in firestore
    private CollectionReference gpuReference = db.collection("gpu");

    //RecyclerView Adapter for the motherboard data
    private GPUAdapter gpuAdapter;

    //DataStorage instance
    DataStorage gpuData = new DataStorage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gpu);

        //Connect recyclerView to adapter
        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select GPU");

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
        //Create query against the gpu collection which sorts the data by the price from lowest price
        //to highest price i.e. ascending order
        Query query = gpuReference.orderBy("price", Query.Direction.ASCENDING);

        //Create FirestoreRecyclerOptions and bind query into the adapter
        FirestoreRecyclerOptions<GPU> options = new FirestoreRecyclerOptions.Builder<GPU>()
                .setQuery(query, GPU.class)
                .build();

        //Assign RecyclerOptions to the adapter
        gpuAdapter = new GPUAdapter(options);

        //Reference the recyclerView and assign the adapter to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_gpu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(gpuAdapter);

        //Implement the OnClickListener interface from the GPUAdapter GPUHolder innerclass constructor
        gpuAdapter.setOnItemClickListener(new GPUAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference gpuRef = db.collection("gpu").document(documentName);
                gpuRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> gpuDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + gpuDetails);


                                Intent intent = new Intent(SelectGPU.this, ViewGPUDetails.class);
                                intent.putExtra("hashMap", gpuDetails);
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
                DocumentReference gpuRef = db.collection("gpu").document(documentName);
                gpuRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String gpuName = (String) documentSnapshot.get("name");
                                Log.d(TAG, "GPU Name: " + gpuName);
                                //Convert the price double into a string for later
                                Double gpuPriceDouble = (Double) documentSnapshot.get("price");
                                String gpuPriceString = Double.toString(gpuPriceDouble);
                                Log.d(TAG, "GPU Price: " + gpuPriceString);
                                Long gpuTDP = (Long) documentSnapshot.get("tdp");


                                //Pass the data back to the CreateComputerListActivity by storing the data
                                // into the DataStorage hashmap and then returning to the CreateComputerListActivity:
                                Intent passGPUDataToCreateComputerActivity = new Intent
                                        (SelectGPU.this, CreateComputerListActivity.class);

                                gpuData.getComputerList().put("GPU NAME", gpuName);
                                gpuData.getComputerList().put("GPU PRICE", gpuPriceString);
                                gpuData.getComputerList().put("GPU TDP", String.valueOf(gpuTDP));
                                Log.d(TAG, "onSuccess: " + gpuData.getComputerList().get("GPU NAME"));
                                Log.d(TAG, "onSuccess: " + gpuData.getComputerList().get("GPU PRICE"));
                                Log.d(TAG, "onSuccess: " + gpuData.getComputerList().get("GPU TDP"));


                                startActivity(passGPUDataToCreateComputerActivity);
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
        gpuAdapter.startListening();
    }

    //When the app goes into the background the recyclerview will not update anything
    @Override
    protected void onStop() {
        super.onStop();
        gpuAdapter.stopListening();
    }


}