/*
Honours Project - PC part Builder
File: SelectCase Class
Author: Conner McGill - B00320975
Date: 2021/03/08

Summary of file:

    This class setups the recyclerview and populates the recyclerview with the relevant data
    from the case firestore collection documents. The user can then either view the part in more
    detail which will open a new activity with all the relevant data for the case or add the case to
    their list which will return the selected case to the CreateComputerListActivity

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

public class SelectCase extends AppCompatActivity {


    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectCaseTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to CPU Collection in firestore
    private CollectionReference caseReference = db.collection("cases");

    //RecyclerView Adapter for the motherboard data
    private CaseAdapter caseAdapter;

    //DataStorage instance
    DataStorage caseData = new DataStorage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_case);

        //Connect recyclerView to adapter
        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Case");

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
        //Create query against the cases collection which sorts the data by the price from lowest price
        //to highest price i.e. ascending order
        Query query = caseReference.orderBy("price", Query.Direction.ASCENDING);

        //Create FirestoreRecyclerOptions and bind query into the adapter
        FirestoreRecyclerOptions<Case> options = new FirestoreRecyclerOptions.Builder<Case>()
                .setQuery(query, Case.class)
                .build();

        //Assign RecyclerOptions to the adapter
        caseAdapter = new CaseAdapter(options);

        //Reference the recyclerView and assign the adapter to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_case);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(caseAdapter);

        //Implement the OnClickListener interface from the CaseAdapter CaseHolder innerclass constructor
        caseAdapter.setOnItemClickListener(new CaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference casesRef = db.collection("cases").document(documentName);
                casesRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> caseDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + caseDetails);


                                Intent intent = new Intent(SelectCase.this, ViewCaseDetails.class);
                                intent.putExtra("hashMap", caseDetails);
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
                DocumentReference casesRef = db.collection("cases").document(documentName);
                casesRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String caseName = (String) documentSnapshot.get("name");
                                Log.d(TAG, "Case Name: " + caseName);
                                //Convert the price double into a string for later
                                Double casePriceDouble = (Double) documentSnapshot.get("price");
                                String casePriceString = Double.toString(casePriceDouble);
                                Log.d(TAG, "Case Price: " + casePriceString);


                                //Pass the data back to the CreateComputerListActivity by storing the data
                                // into the DataStorage hashmap and then returning to the CreateComputerListActivity:
                                Intent passCaseDataToCreateComputerActivity = new Intent
                                        (SelectCase.this, CreateComputerListActivity.class);

                                caseData.getComputerList().put("CASE NAME", caseName);
                                caseData.getComputerList().put("CASE PRICE", casePriceString);
                                Log.d(TAG, "onSuccess: " + caseData.getComputerList().get("CASE NAME"));
                                Log.d(TAG, "onSuccess: " + caseData.getComputerList().get("CASE PRICE"));


                                startActivity(passCaseDataToCreateComputerActivity);
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
        caseAdapter.startListening();
    }

    //When the app goes into the background the recyclerview will not update anything
    @Override
    protected void onStop() {
        super.onStop();
        caseAdapter.stopListening();
    }



}