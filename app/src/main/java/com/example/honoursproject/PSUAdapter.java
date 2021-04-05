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

public class PSUAdapter extends FirestoreRecyclerAdapter<PSU, PSUAdapter.PSUHolder> {
    private OnItemClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public PSUAdapter(@NonNull FirestoreRecyclerOptions<PSU> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final PSUHolder holder, int position, @NonNull PSU model) {
        holder.nameOfPSU.setText(model.getName());
        holder.priceOfPSU.setText(String.format("£%s", String.valueOf(model.getPrice())));
        holder.formFactorOfPSUValue.setText(model.getFormFactor());
        holder.efficiencyRatingOfPSUValue.setText(model.getEfficiencyRating());
        holder.wattageOfPSUValue.setText(String.valueOf(model.getWattage() + " W"));
        holder.modularOfPSUValue.setText(model.getModular());

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getName() + ".jpg");
        Log.d("my image of psu", String.valueOf(storageReference));

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
                            holder.imageOfPSU.setImageBitmap(bitmap);
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
    public PSUHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.psu_item, parent, false);
        return new PSUAdapter.PSUHolder(view);
    }


    public class PSUHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfPSU;
        TextView priceOfPSU;
        TextView formFactorOfPSUTitle;
        TextView formFactorOfPSUValue;
        TextView efficiencyRatingOfPSUTitle;
        TextView efficiencyRatingOfPSUValue;
        TextView wattageOfPSUTitle;
        TextView wattageOfPSUValue;
        TextView modularOfPSUTitle;
        TextView modularOfPSUValue;
        ImageView imageOfPSU;
        Button addPSUToList;

        public PSUHolder(@NonNull View itemView) {
            super(itemView);

            nameOfPSU = itemView.findViewById(R.id.nameOfPSU);
            priceOfPSU = itemView.findViewById(R.id.priceOfPSU);
            formFactorOfPSUTitle = itemView.findViewById(R.id.psuFormFactorTitle);
            formFactorOfPSUValue = itemView.findViewById(R.id.psuFormFactorValue);
            efficiencyRatingOfPSUTitle = itemView.findViewById(R.id.psuEfficiencyRatingTitle);
            efficiencyRatingOfPSUValue = itemView.findViewById(R.id.psuEfficiencyRatingValue);
            wattageOfPSUTitle = itemView.findViewById(R.id.psuWattageTitle);
            wattageOfPSUValue = itemView.findViewById(R.id.psuWattageValue);
            modularOfPSUTitle = itemView.findViewById(R.id.psuModularTitle);
            modularOfPSUValue = itemView.findViewById(R.id.psuModularValue);
            imageOfPSU = itemView.findViewById(R.id.image_of_psu);
            addPSUToList = itemView.findViewById(R.id.addPSUToListButton);

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
            addPSUToList.setOnClickListener(new View.OnClickListener() {
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

    //Send the click event to the SelectPSU activity which is then handled there
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

        void onAddButtonClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(PSUAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


}
