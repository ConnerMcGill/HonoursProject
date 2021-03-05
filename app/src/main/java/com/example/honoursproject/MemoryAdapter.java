package com.example.honoursproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

public class MemoryAdapter extends FirestoreRecyclerAdapter<Memory, MemoryAdapter.MemoryHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MemoryAdapter(@NonNull FirestoreRecyclerOptions<Memory> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MemoryHolder holder, int position, @NonNull Memory model) {
        holder.nameOfMemory.setText(model.getName());
        holder.priceOfMemory.setText(String.format("£%s", String.valueOf(model.getPrice())));
        holder.speedOfMemoryValue.setText(model.getSpeed());
        holder.modulesOfMemoryValue.setText(model.getModules());
        holder.PricePerGBOfMemoryValue.setText(String.format("£%s", String.valueOf(model.getPricePerGB())));
        holder.colourOfMemoryValue.setText(model.getColour());
        holder.firstWordLatencyOfMemoryValue.setText(model.getFirstWordLatency());
        holder.casLatencyOfMemoryValue.setText(String.valueOf(model.getCasLatency()));

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getName() + ".jpg");
        Log.d("my image of memory", String.valueOf(storageReference));

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
                            holder.imageOfMemory.setImageBitmap(bitmap);
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
    public MemoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_item, parent, false);
        return new MemoryAdapter.MemoryHolder(view);
    }

    public class MemoryHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfMemory;
        TextView priceOfMemory;
        TextView speedOfMemoryTitle;
        TextView speedOfMemoryValue;
        TextView modulesOfMemoryTitle;
        TextView modulesOfMemoryValue;
        TextView pricePerGBOfMemoryTitle;
        TextView PricePerGBOfMemoryValue;
        TextView colourOfMemoryTitle;
        TextView colourOfMemoryValue;
        TextView firstWordLatencyOfMemoryTitle;
        TextView firstWordLatencyOfMemoryValue;
        TextView casLatencyOfMemoryTitle;
        TextView casLatencyOfMemoryValue;
        ImageView imageOfMemory;
        Button addMemoryToList;

        //Constructor accepts the entire item row and does the view lookups to find each subview
        public MemoryHolder(@NonNull View itemView) {
            super(itemView);

            nameOfMemory = itemView.findViewById(R.id.nameOfMemory);
            priceOfMemory = itemView.findViewById(R.id.priceOfMemory);
            speedOfMemoryTitle = itemView.findViewById(R.id.memorySpeedTitle);
            speedOfMemoryValue = itemView.findViewById(R.id.memorySpeedValue);
            modulesOfMemoryTitle = itemView.findViewById(R.id.memoryModulesTitle);
            modulesOfMemoryValue = itemView.findViewById(R.id.memoryModulesValue);
            pricePerGBOfMemoryTitle = itemView.findViewById(R.id.memoryPricePerGBTitle);
            PricePerGBOfMemoryValue = itemView.findViewById(R.id.memoryPricePerGBValue);
            colourOfMemoryTitle = itemView.findViewById(R.id.memoryColourTitle);
            colourOfMemoryValue = itemView.findViewById(R.id.memoryColourValue);
            firstWordLatencyOfMemoryTitle = itemView.findViewById(R.id.memoryFirstWordLatencyTitle);
            firstWordLatencyOfMemoryValue = itemView.findViewById(R.id.memoryFirstWordLatencyValue);
            casLatencyOfMemoryTitle = itemView.findViewById(R.id.memoryCASLatencyTitle);
            casLatencyOfMemoryValue = itemView.findViewById(R.id.memoryCASLatencyValue);
            imageOfMemory = itemView.findViewById(R.id.image_of_memory);
            addMemoryToList = itemView.findViewById(R.id.addMemoryToListButton);

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
            addMemoryToList.setOnClickListener(new View.OnClickListener() {
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

    //Send the click event to the SelectMotherboard activity which is then handled there
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

        void onAddButtonClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(MemoryAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
