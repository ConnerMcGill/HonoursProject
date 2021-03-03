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

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class MotherboardAdapter extends FirestoreRecyclerAdapter<Motherboard, MotherboardAdapter.MotherboardHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MotherboardAdapter(@NonNull FirestoreRecyclerOptions<Motherboard> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MotherboardHolder holder, int position, @NonNull Motherboard model) {
        holder.nameOfMotherboard.setText(model.getMotherboardName());
        holder.priceOfMotherboard.setText(String.format("£%s", String.valueOf(model.getMotherboardPrice())));
        holder.motherboardSocketValue.setText(model.getMotherboardSocket());
        holder.motherboardFormFactorValue.setText(model.getFormFactor());
        holder.motherboardMemoryMaxValue.setText(String.valueOf(model.getMemoryMax() + " GB"));
        holder.motherboardMemorySlotsValue.setText(String.valueOf(model.getMemorySlots()));
        holder.motherboardColourValue.setText(model.getColour());

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getMotherboardName() + ".jpg");
        Log.d("my image of cpu cooler", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(model.getMotherboardName(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Create a bitmap object and set the image view item with the relevant image
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            holder.imageOfMotherboard.setImageBitmap(bitmap);
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
    public MotherboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.motherboard_item, parent, false);
        return new MotherboardHolder(view);
    }

    public class MotherboardHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfMotherboard;
        TextView priceOfMotherboard;
        TextView motherboardSocketTitle;
        TextView motherboardSocketValue;
        TextView motherboardFormFactorTitle;
        TextView motherboardFormFactorValue;
        TextView motherboardMemoryMaxTitle;
        TextView motherboardMemoryMaxValue;
        TextView motherboardMemorySlotsTitle;
        TextView motherboardMemorySlotsValue;
        TextView motherboardColourTitle;
        TextView motherboardColourValue;
        ImageView imageOfMotherboard;
        Button addMotherboardToList;




        //Constructor accepts the entire item row and does the view lookups to find each subview
        public MotherboardHolder(@NonNull View itemView) {
            super(itemView);

            nameOfMotherboard = itemView.findViewById(R.id.nameOfMotherboard);
            priceOfMotherboard = itemView.findViewById(R.id.priceOfMotherboard);
            motherboardSocketTitle = itemView.findViewById(R.id.motherboardSocketTitle);
            motherboardSocketValue = itemView.findViewById(R.id.motherboardSocketValue);
            motherboardFormFactorTitle = itemView.findViewById(R.id.motherboardFormFactorTitle);
            motherboardFormFactorValue = itemView.findViewById(R.id.motherboardFormFactorValue);
            motherboardMemoryMaxTitle = itemView.findViewById(R.id.motherboardMemoryMaxTitle);
            motherboardMemoryMaxValue = itemView.findViewById(R.id.motherboardMemoryMaxValue);
            motherboardMemorySlotsTitle = itemView.findViewById(R.id.motherboardMemorySlotsTitle);
            motherboardMemorySlotsValue = itemView.findViewById(R.id.motherboardMemorySlotsValue);
            motherboardColourTitle = itemView.findViewById(R.id.motherboardColourTitle);
            motherboardColourValue = itemView.findViewById(R.id.motherboardColourValue);
            imageOfMotherboard = itemView.findViewById(R.id.image_of_motherboard);
            addMotherboardToList = itemView.findViewById(R.id.addMotherboardToListButton);


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
            addMotherboardToList.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(MotherboardAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }



}
