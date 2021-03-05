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


public class StorageAdapter extends FirestoreRecyclerAdapter<Storage, StorageAdapter.StorageHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StorageAdapter(@NonNull FirestoreRecyclerOptions<Storage> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StorageHolder holder, int position, @NonNull Storage model) {
        holder.nameOfStorage.setText(model.getName());
        holder.priceOfStorage.setText(String.format("£%s", String.valueOf(model.getPrice())));
        holder.capacityOfStorageValue.setText(model.getCapacity());
        holder.pricePerGBOfStorageValue.setText(String.format("£%s", String.valueOf(model.getPricePerGB())));
        holder.typeOfStorageValue.setText(model.getType());
        holder.typeOfCacheStorageValue.setText(model.getCache());
        holder.formFactorOfStorageValue.setText(model.getFormFactor());
        holder.interfaceOfStorageValue.setText(model.getStorageInterface());

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
                            holder.imageOfStorage.setImageBitmap(bitmap);
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
    public StorageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.storage_item, parent, false);
        return new StorageAdapter.StorageHolder(view);
    }

    public class StorageHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfStorage;
        TextView priceOfStorage;
        TextView capacityOfStorageTitle;
        TextView capacityOfStorageValue;
        TextView pricePerGBOfStorageTitle;
        TextView pricePerGBOfStorageValue;
        TextView typeOfStorageTitle;
        TextView typeOfStorageValue;
        TextView typeOfCacheStorageTitle;
        TextView typeOfCacheStorageValue;
        TextView formFactorOfStorageTitle;
        TextView formFactorOfStorageValue;
        TextView interfaceOfStorageTitle;
        TextView interfaceOfStorageValue;
        ImageView imageOfStorage;
        Button addStorageToList;

        //Constructor accepts the entire item row and does the view lookups to find each subview
        public StorageHolder(@NonNull View itemView) {
            super(itemView);

            nameOfStorage = itemView.findViewById(R.id.nameOfStorage);
            priceOfStorage = itemView.findViewById(R.id.priceOfStorage);
            capacityOfStorageTitle = itemView.findViewById(R.id.storageCapacityTitle);
            capacityOfStorageValue = itemView.findViewById(R.id.storageCapacityValue);
            pricePerGBOfStorageTitle = itemView.findViewById(R.id.storagePricePerGBTitle);
            pricePerGBOfStorageValue = itemView.findViewById(R.id.storagePricePerGBValue);
            typeOfStorageTitle = itemView.findViewById(R.id.storageTypeTitle);
            typeOfStorageValue = itemView.findViewById(R.id.storageTypeValue);
            typeOfCacheStorageTitle = itemView.findViewById(R.id.storageCacheTitle);
            typeOfCacheStorageValue = itemView.findViewById(R.id.storageCacheValue);
            formFactorOfStorageTitle = itemView.findViewById(R.id.storageFormFactorTitle);
            formFactorOfStorageValue = itemView.findViewById(R.id.storageFormFactorValue);
            interfaceOfStorageTitle = itemView.findViewById(R.id.storageInterfaceTitle);
            interfaceOfStorageValue = itemView.findViewById(R.id.storageInterfaceValue);
            imageOfStorage = itemView.findViewById(R.id.image_of_storage);
            addStorageToList = itemView.findViewById(R.id.addStorageToListButton);


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
            addStorageToList.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(StorageAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
