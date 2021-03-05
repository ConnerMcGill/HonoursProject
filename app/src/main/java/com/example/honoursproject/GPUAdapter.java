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

public class GPUAdapter extends FirestoreRecyclerAdapter<GPU, GPUAdapter.GPUHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GPUAdapter(@NonNull FirestoreRecyclerOptions<GPU> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final GPUHolder holder, int position, @NonNull GPU model) {
        holder.nameOfGPU.setText(model.getName());
        holder.priceOfGPU.setText(String.format("£%s", String.valueOf(model.getPrice())));
        holder.chipsetOfGPUValue.setText(model.getChipset());
        holder.memoryOfGPUValue.setText(String.valueOf(model.getMemory() + " GB"));
        holder.coreClockOfGPUValue.setText(String.valueOf(model.getCoreClock() + " MHz"));
        holder.boostClockOfGPUValue.setText(String.valueOf(model.getBoostClock() + " MHZ"));
        holder.colourOfGPUValue.setText(model.getColour());
        holder.lengthOfGPUValue.setText(model.getLength() );

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getName() + ".jpg");
        Log.d("my image of storage", String.valueOf(storageReference));

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
                            holder.imageOfGPU.setImageBitmap(bitmap);
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
    public GPUHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gpu_item, parent, false);
        return new GPUAdapter.GPUHolder(view);
    }

    public class GPUHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfGPU;
        TextView priceOfGPU;
        TextView chipsetOfGPUTitle;
        TextView chipsetOfGPUValue;
        TextView memoryOfGPUTitle;
        TextView memoryOfGPUValue;
        TextView coreClockOfGPUTitle;
        TextView coreClockOfGPUValue;
        TextView boostClockOfGPUTitle;
        TextView boostClockOfGPUValue;
        TextView colourOfGPUTitle;
        TextView colourOfGPUValue;
        TextView lengthOfGPUTitle;
        TextView lengthOfGPUValue;
        ImageView imageOfGPU;
        Button addGPUToList;


        //Constructor accepts the entire item row and does the view lookups to find each subview
        public GPUHolder(@NonNull View itemView) {
            super(itemView);

            nameOfGPU = itemView.findViewById(R.id.nameOfGPU);
            priceOfGPU = itemView.findViewById(R.id.priceOfGPU);
            chipsetOfGPUTitle = itemView.findViewById(R.id.gpuChipsetTitle);
            chipsetOfGPUValue = itemView.findViewById(R.id.gpuChipsetValue);
            memoryOfGPUTitle = itemView.findViewById(R.id.gpuMemoryTitle);
            memoryOfGPUValue = itemView.findViewById(R.id.gpuMemoryValue);
            coreClockOfGPUTitle = itemView.findViewById(R.id.gpuCoreClockTitle);
            coreClockOfGPUValue = itemView.findViewById(R.id.gpuCoreClockValue);
            boostClockOfGPUTitle = itemView.findViewById(R.id.gpuBoostClockTitle);
            boostClockOfGPUValue = itemView.findViewById(R.id.gpuBoostClockValue);
            colourOfGPUTitle = itemView.findViewById(R.id.gpuColourTitle);
            colourOfGPUValue = itemView.findViewById(R.id.gpuColourValue);
            lengthOfGPUTitle = itemView.findViewById(R.id.gpuLengthTitle);
            lengthOfGPUValue = itemView.findViewById(R.id.gpuLengthValue);
            imageOfGPU = itemView.findViewById(R.id.image_of_gpu);
            addGPUToList = itemView.findViewById(R.id.addGPUToListButton);

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
            addGPUToList.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(GPUAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
