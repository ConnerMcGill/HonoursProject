/*
Honours Project - PC part Builder
File: CPUAdapter Class
Author: Conner McGill - B00320975
Date: 2021/02/19

Summary of file:

In order to populate the data into recyclerView an adapter is required. The CPUAdapter class helps
to convert the CPU class object at a position into a list row item for insertion. As I am using
the FirebaseUI open source library the adapter is a subclass of the normal RecyclerView adapter
and deals with the specific RecyclerView adapter needs as well as allowing for a query to be bind
to the recyclerView.

An adapter needs a ViewHolder object as well which describes and provides the access to the views
within each item row. The viewHolder is defined as an innerclass

The following guides were helpful in learning more about Recyclerview and firebaseUI:
https://codinginflow.com/tutorials/android/firebaseui-firestorerecycleradapter/part-3-firestorerecycleradapter
https://codinginflow.com/tutorials/android/firebaseui-firestorerecycleradapter/part-6-onitemclicklistener
https://firebaseopensource.com/projects/firebase/firebaseui-android/database/readme/#using_firebaseui%20to%20populate%20a%20recyclerview
https://guides.codepath.com/android/using-the-recyclerview
 */


package com.example.honoursproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import io.grpc.Context;

public class CPUAdapter extends FirestoreRecyclerAdapter<CPU, CPUAdapter.CPUHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CPUAdapter(@NonNull FirestoreRecyclerOptions<CPU> options) {
        super(options);
    }

    //Populate the item with data through the viewHolder
    @Override
    protected void onBindViewHolder(@NonNull final CPUHolder holder, int position, @NonNull CPU model) {
        //Set the item views with the appropriate data and formatting
        holder.nameOfCPU.setText(model.getName());
        holder.priceOfCPU.setText(String.format("£%s", String.valueOf(model.getPrice())));
        holder.coreCountOfCPUValue.setText(String.valueOf(model.getCoreCount()));
        holder.clockSpeedOfCPUValue.setText(model.getCoreClock());
        holder.boostClockSpeedOfCPUValue.setText(model.getBoostClock());
        holder.tdpOfCPUValue.setText(String.valueOf(model.getTdp() + "W"));
        holder.integratedGraphicsOfCPUValue.setText(model.getIntegratedGraphics());
        holder.smtOfCPUValue.setText(model.getSmt());

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getName() + ".jpg");
        Log.d("my image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(model.getName(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Create a bitmap object and set the image view item with the relevant image
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            holder.imageOfCPU.setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Inflate the cpu_item.xml file and return the holder
    @NonNull
    @Override
    public CPUHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpu_item,
                parent, false);
        return new CPUHolder(view);
    }


    //Innerclass for viewHolder that provides a reference to the required views
    class CPUHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfCPU;
        TextView priceOfCPU;
        TextView coreCountOfCPUTitle;
        TextView clockSpeedOfCPUTitle;
        TextView boostClockSpeedOfCPUTitle;
        TextView tdpOfCPUTitle;
        TextView integratedGraphicsOfCPUTitle;
        TextView smtOfCPUTitle;
        TextView coreCountOfCPUValue;
        TextView clockSpeedOfCPUValue;
        TextView boostClockSpeedOfCPUValue;
        TextView tdpOfCPUValue;
        TextView integratedGraphicsOfCPUValue;
        TextView smtOfCPUValue;
        ImageView imageOfCPU;
        Button addCPUToList;

        //Constructor accepts the entire item row and does the view lookups to find each subview
        public CPUHolder(@NonNull View itemView) {
            super(itemView);
            nameOfCPU = itemView.findViewById(R.id.nameOfCPU);
            priceOfCPU = itemView.findViewById(R.id.priceOfCPU);
            coreCountOfCPUTitle = itemView.findViewById(R.id.coreCountOfCPUTitle);
            clockSpeedOfCPUTitle = itemView.findViewById(R.id.clockSpeedOfCPUTitle);
            boostClockSpeedOfCPUTitle = itemView.findViewById(R.id.boostClockSpeedOfCPUTitle);
            tdpOfCPUTitle = itemView.findViewById(R.id.tdpOfCPUTitle);
            integratedGraphicsOfCPUTitle = itemView.findViewById(R.id.integratedGraphicsOfCPUTitle);
            smtOfCPUTitle = itemView.findViewById(R.id.smtOfCPUTitle);
            coreCountOfCPUValue = itemView.findViewById(R.id.coreCountOfCPUValue);
            clockSpeedOfCPUValue = itemView.findViewById(R.id.clockSpeedOfCPUValue);
            boostClockSpeedOfCPUValue = itemView.findViewById(R.id.boostClockSpeedOfCPUValue);
            tdpOfCPUValue = itemView.findViewById(R.id.tdpOfCPUValue);
            integratedGraphicsOfCPUValue = itemView.findViewById(R.id.integratedGraphicsOfCPUValue);
            smtOfCPUValue = itemView.findViewById(R.id.smtOfCPUValue);
            imageOfCPU = itemView.findViewById(R.id.image_of_cpu);
            addCPUToList = itemView.findViewById(R.id.addCPUtoListButton);

            //OnClickListener for the card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Distinguish between different cards in the recycler view by their position
                    int position = getAdapterPosition();
                    Log.d("click card", "card was clicked");
                    //Call the interface method below on the OnItemClickListener listener
                    if (listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            //OnClickListener for Add button
            addCPUToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Distinguish between different cards in the recycler view by their position
                    int position = getAdapterPosition();
                    Log.d("button on card", "button on card was clicked");
                    //Call the interface method below on the OnItemClickListener listener
                    if (listener != null){
                        listener.onAddButtonClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    //Send the click event to the SelectCPU activity which will then deal with the firestore stuff
    //I will reword this comment later with better details of what I end up doing
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onAddButtonClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
