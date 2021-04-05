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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CPUCoolerAdapter extends FirestoreRecyclerAdapter<CPUCooler, CPUCoolerAdapter.CPUCoolerHolder> {
    private OnItemClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CPUCoolerAdapter(@NonNull FirestoreRecyclerOptions<CPUCooler> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CPUCoolerAdapter.CPUCoolerHolder holder, int position, @NonNull CPUCooler model) {
        holder.nameOfCPUCooler.setText(model.getCPUCoolerName());
        holder.priceOfCPUCooler.setText(String.format("£%s", String.valueOf(model.getCPUCoolerPrice())));
        holder.fanRpmOfCPUCoolerValue.setText(model.getFanrpm());
        holder.noiseLevelOfCPUCoolerValue.setText(model.getNoiselevel());
        holder.colourOfCPUCoolerValue.setText(model.getColour());
        holder.radiatorSizeOfCPUCoolerValue.setText(model.getRadsize());

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getCPUCoolerName() + ".jpg");
        Log.d("my image of cpu cooler", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(model.getCPUCoolerName(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Create a bitmap object and set the image view item with the relevant image
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            holder.imageOfCPUCooler.setImageBitmap(bitmap);
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

    @NonNull
    @Override
    public CPUCoolerAdapter.CPUCoolerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpucooler_item, parent, false);
        return new CPUCoolerHolder(view);
    }


    //Innerclass for viewHolder that provides a reference to the required views
    class CPUCoolerHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfCPUCooler;
        TextView priceOfCPUCooler;
        TextView fanRpmOfCPUCoolerTitle;
        TextView noiseLevelOfCPUCoolerTitle;
        TextView colourOfCPUCoolerTitle;
        TextView radiatorSizeOfCPUCoolerTitle;
        TextView fanRpmOfCPUCoolerValue;
        TextView noiseLevelOfCPUCoolerValue;
        TextView colourOfCPUCoolerValue;
        TextView radiatorSizeOfCPUCoolerValue;
        ImageView imageOfCPUCooler;
        Button addCPUCoolerToList;


        //Constructor accepts the entire item row and does the view lookups to find each subview
        public CPUCoolerHolder(@NonNull View itemView) {
            super(itemView);
            nameOfCPUCooler = itemView.findViewById(R.id.nameOfCPUCooler);
            priceOfCPUCooler = itemView.findViewById(R.id.priceOfCPUCooler);
            fanRpmOfCPUCoolerTitle = itemView.findViewById(R.id.fanRpmOfCPUCoolerTitle);
            noiseLevelOfCPUCoolerTitle = itemView.findViewById(R.id.noiseLevelOfCPUCoolerTitle);
            colourOfCPUCoolerTitle = itemView.findViewById(R.id.colourOfCPUCoolerTitle);
            radiatorSizeOfCPUCoolerTitle = itemView.findViewById(R.id.radiatorSizeOfCPUCoolerTitle);
            fanRpmOfCPUCoolerValue = itemView.findViewById(R.id.fanRpmOfCPUCoolerValue);
            noiseLevelOfCPUCoolerValue = itemView.findViewById(R.id.noiseLevelOfCPUCoolerValue);
            colourOfCPUCoolerValue = itemView.findViewById(R.id.colourOfCPUCoolerValue);
            radiatorSizeOfCPUCoolerValue = itemView.findViewById(R.id.radiatorSizeOfCPUCoolerValue);
            imageOfCPUCooler = itemView.findViewById(R.id.image_of_cpu_cooler);
            addCPUCoolerToList = itemView.findViewById(R.id.addCPUCoolerToListButton);


            //OnClickListener for the card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Distinguish between different cards in the recycler view by their position
                    int position = getAdapterPosition();
                    Log.d("click card", "card was clicked");
                    //Call the interface method below on the OnItemClickListener listener
                    if (listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            //OnClickListener for Add button
            addCPUCoolerToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Distinguish between different cards in the recycler view by their position
                    int position = getAdapterPosition();
                    Log.d("button on card", "button on card was clicked");
                    //Call the interface method below on the OnItemClickListener listener
                    if (listener != null) {
                        listener.onAddButtonClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }

    }


    //Send the click event to the SelectCPUCooler activity which is then handled there
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

        void onAddButtonClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
