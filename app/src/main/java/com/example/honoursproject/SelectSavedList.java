package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class SelectSavedList extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "SelectcpuTAG";

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to user-lists Collection in firestore
    private CollectionReference userListsRef = db.collection("user-lists");

    //RecyclerView Adapter for the user-lists data
    private UserListAdapter userListAdapter;

    //Declare an instance of firebaseAuthentication
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_saved_list);

        //Initialise the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        setupRecyclerView();

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View Your Saved Lists!");

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

    }

    private void setupRecyclerView() {

        String currentUser = mAuth.getUid();

        Query query = userListsRef.whereEqualTo("userID", currentUser);

        FirestoreRecyclerOptions<UserList> options = new FirestoreRecyclerOptions.Builder<UserList>()
                .setQuery(query, UserList.class)
                .build();

        userListAdapter = new UserListAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_lists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userListAdapter);

        //When user swipes item in list off the screen to the left delete the document from firestore
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                userListAdapter.deleteItem(viewHolder.getAdapterPosition());
                Toast.makeText(getApplicationContext(), "List Deleted!",
                        Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //Implement the OnClickListener interface from the UserListAdapter UserListHolder innerclass constructor
        userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                //Get the document ID from the clicked card
                String documentName = documentSnapshot.getId();
                Log.d(TAG, "Document ID: " + documentName);

                //Get all the data I need from the document now that I have the specific path and open the new activity:
                DocumentReference userListRef = db.collection("user-lists").document(documentName);
                userListRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                HashMap<String, Object> userListDetails = (HashMap<String, Object>) documentSnapshot.getData();
                                Log.d(TAG, "On Success: " + userListDetails);


                                Intent intent = new Intent(SelectSavedList.this, ViewSavedList.class);
                                intent.putExtra("hashMap", userListDetails);
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


        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        userListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userListAdapter.stopListening();
    }

    private void returnToMainActivity() {
        Intent openMainActivity = new Intent
                (this, MainActivity.class);
        startActivity(openMainActivity);
    }

    //Over ride back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SelectSavedList.this, MainActivity.class));
        finish();
    }
}