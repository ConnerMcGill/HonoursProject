package com.example.honoursproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class SelectCPUCooler extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectcpuTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to CPU Collection in firestore
    private CollectionReference cpuCoolerReference = db.collection("cpu-coolers");

    //RecyclerView Adapter for the CPU data
    private CPUCoolerAdapter cpuCoolerAdapter;

    //DataStorage instance
    DataStorage cpuCoolerData = new DataStorage();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cpu_cooler);

        //Connect recyclerView to adapter
        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select CPU Cooler");

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
        Query query = cpuCoolerReference.orderBy("price", Query.Direction.ASCENDING);

        //Create FirestoreRecyclerOptions and bind query into the adapter
        FirestoreRecyclerOptions<CPUCooler> options = new FirestoreRecyclerOptions.Builder<CPUCooler>()
                .setQuery(query, CPUCooler.class)
                .build();

        //Assign RecyclerOptions to the adapter
        cpuCoolerAdapter = new CPUCoolerAdapter(options);

        //Reference the recyclerView and assign the adapter to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_cpu_cooler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cpuCoolerAdapter);


        //Implement the OnClickListener interface from the CPUCoolerAdapter CPUCoolerHolder innerclass constructor
        cpuCoolerAdapter.setOnItemClickListener(new CPUCoolerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference cpuCoolerRef = db.collection("cpu-coolers").document(documentName);
                cpuCoolerRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> cpuCoolerDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + cpuCoolerDetails);


                               Intent intent = new Intent(SelectCPUCooler.this, ViewCPUCoolerDetails.class);
                               intent.putExtra("hashMap", cpuCoolerDetails);
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
                DocumentReference cpuCoolerRef = db.collection("cpu-coolers").document(documentName);
                cpuCoolerRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String cpuCoolerName = (String) documentSnapshot.get("name");
                                Log.d(TAG, "CPU Cooler Name: " + cpuCoolerName);
                                //Convert the price double into a string for later
                                Double cpuCoolerPriceLong = (Double) documentSnapshot.get("price");
                                String cpuCoolerPriceString = Double.toString(cpuCoolerPriceLong);
                                Log.d(TAG, "CPU Cooler Price: " + cpuCoolerPriceString);
                                Long cpuCoolerTDP = (Long) documentSnapshot.get("tdp");
                                Log.d(TAG, "CPU Cooler TDP: " + cpuCoolerTDP);


                                //Pass the data back to the CreateComputerListActivity by storing the data
                                // into the DataStorage hashmap and then returning to the CreateComputerListActivity:
                                Intent passCPUCoolerDataToCreateComputerActivity = new Intent
                                        (SelectCPUCooler.this, CreateComputerListActivity.class);

                                cpuCoolerData.getComputerList().put("CPU COOLER NAME", cpuCoolerName);
                                cpuCoolerData.getComputerList().put("CPU COOLER PRICE", cpuCoolerPriceString);
                                cpuCoolerData.getComputerList().put("CPU COOLER TDP", String.valueOf(cpuCoolerTDP));

                                Log.d(TAG, "onSuccess: " + cpuCoolerData.getComputerList().get("CPU COOLER NAME"));
                                Log.d(TAG, "onSuccess: " + cpuCoolerData.getComputerList().get("CPU COOLER PRICE"));
                                Log.d(TAG, "onSuccess: " + cpuCoolerData.getComputerList().get("CPU COOLER TDP"));


                                startActivity(passCPUCoolerDataToCreateComputerActivity);
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
        cpuCoolerAdapter.startListening();
    }

    //When the app goes into the background the recyclerview will not update anything
    @Override
    protected void onStop() {
        super.onStop();
        cpuCoolerAdapter.stopListening();
    }


}
