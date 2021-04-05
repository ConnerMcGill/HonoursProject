/*
Honours Project - PC part Builder
File: SelectStorage Class
Author: Conner McGill - B00320975
Date: 2021/03/05

Summary of file:

    This class setups the recyclerview and populates the recyclerview with the relevant data
    from the Storage firestore collection documents. The user can then either view the part in more
    detail which will open a new activity with all the relevant data for the Storage or add the Storage to
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

public class SelectStorage extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectStorageTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to CPU Collection in firestore
    private CollectionReference storageReference = db.collection("storage");

    //RecyclerView Adapter for the motherboard data
    private StorageAdapter storageAdapter;

    //DataStorage instance
    DataStorage storageData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_storage);

        //Connect recyclerView to adapter
        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Storage");

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
        //Create query against the storage collection which sorts the data by the price from lowest price
        //to highest price i.e. ascending order
        Query query = storageReference.orderBy("price", Query.Direction.ASCENDING);

        //Create FirestoreRecyclerOptions and bind query into the adapter
        FirestoreRecyclerOptions<Storage> options = new FirestoreRecyclerOptions.Builder<Storage>()
                .setQuery(query, Storage.class)
                .build();

        //Assign RecyclerOptions to the adapter
        storageAdapter = new StorageAdapter(options);

        //Reference the recyclerView and assign the adapter to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_storage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(storageAdapter);

        //Implement the OnClickListener interface from the StorageAdapter StorageHolder innerclass constructor
        storageAdapter.setOnItemClickListener(new StorageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference storageRef = db.collection("storage").document(documentName);
                storageRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> storageDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + storageDetails);


                                Intent intent = new Intent(SelectStorage.this, ViewStorageDetails.class);
                                intent.putExtra("hashMap", storageDetails);
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
                DocumentReference storageRef = db.collection("storage").document(documentName);
                storageRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String storageName = (String) documentSnapshot.get("name");
                                Log.d(TAG, "Storage Name: " + storageName);
                                //Convert the price double into a string for later
                                Double storagePriceDouble = (Double) documentSnapshot.get("price");
                                String storagePriceString = Double.toString(storagePriceDouble);
                                Log.d(TAG, "Storage Price: " + storagePriceString);
                                Long storageTDP = (Long) documentSnapshot.get("tdp");


                                //Pass the data back to the CreateComputerListActivity by storing the data
                                // into the DataStorage hashmap and then returning to the CreateComputerListActivity:
                                Intent passStorageDataToCreateComputerActivity = new Intent
                                        (SelectStorage.this, CreateComputerListActivity.class);

                                storageData.getComputerList().put("STORAGE NAME", storageName);
                                storageData.getComputerList().put("STORAGE PRICE", storagePriceString);
                                storageData.getComputerList().put("STORAGE TDP", String.valueOf(storageTDP));
                                Log.d(TAG, "onSuccess: " + storageData.getComputerList().get("STORAGE NAME"));
                                Log.d(TAG, "onSuccess: " + storageData.getComputerList().get("STORAGE PRICE"));
                                Log.d(TAG, "onSuccess: " + storageData.getComputerList().get("STORAGE TDP"));


                                startActivity(passStorageDataToCreateComputerActivity);
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
        storageAdapter.startListening();
    }

    //When the app goes into the background the recyclerview will not update anything
    @Override
    protected void onStop() {
        super.onStop();
        storageAdapter.stopListening();
    }
}