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

public class CaseAdapter extends FirestoreRecyclerAdapter<Case, CaseAdapter.CaseHolder> {
    private OnItemClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CaseAdapter(@NonNull FirestoreRecyclerOptions<Case> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CaseHolder holder, int position, @NonNull Case model) {
        holder.nameOfCase.setText(model.getName());
        holder.priceOfCase.setText(String.format("£%s", String.valueOf(model.getPrice())));
        holder.typeOfCaseValue.setText(model.getType());
        holder.colourOfCaseValue.setText(model.getColour());
        holder.sidePanelOfCaseValue.setText(model.getSidePanel());
        holder.internal3inchBayCaseValue.setText(String.valueOf(model.getInternal3inchBay()));
        holder.internal2inchBayCaseValue.setText(String.valueOf(model.getInternal2inchBay()));

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getName() + ".jpg");
        Log.d("my image of case", String.valueOf(storageReference));

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
                            holder.imageOfCase.setImageBitmap(bitmap);
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
    public CaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_item, parent, false);
        return new CaseAdapter.CaseHolder(view);
    }

    public class CaseHolder extends RecyclerView.ViewHolder {

        //Reference variables for each view within each data item
        TextView nameOfCase;
        TextView priceOfCase;
        TextView typeOfCaseTitle;
        TextView typeOfCaseValue;
        TextView colourOfCaseTitle;
        TextView colourOfCaseValue;
        TextView sidePanelOfCaseTitle;
        TextView sidePanelOfCaseValue;
        TextView internal3inchBayCaseTitle;
        TextView internal3inchBayCaseValue;
        TextView internal2inchBayCaseTitle;
        TextView internal2inchBayCaseValue;
        ImageView imageOfCase;
        Button addCaseToList;

        public CaseHolder(@NonNull View itemView) {
            super(itemView);
            this.nameOfCase = itemView.findViewById(R.id.nameOfCase);
            this.priceOfCase = itemView.findViewById(R.id.priceOfCase);
            this.typeOfCaseTitle = itemView.findViewById(R.id.caseTypeTitle);
            this.typeOfCaseValue = itemView.findViewById(R.id.caseTypeValue);
            this.colourOfCaseTitle = itemView.findViewById(R.id.caseColourTitle);
            this.colourOfCaseValue = itemView.findViewById(R.id.caseColourValue);
            this.sidePanelOfCaseTitle = itemView.findViewById(R.id.caseSidePanelTitle);
            this.sidePanelOfCaseValue = itemView.findViewById(R.id.caseSidePanelValue);
            this.internal3inchBayCaseTitle = itemView.findViewById(R.id.caseInternal3InchBaysTitle);
            this.internal3inchBayCaseValue = itemView.findViewById(R.id.caseInternal3InchBaysValue);
            this.internal2inchBayCaseTitle = itemView.findViewById(R.id.caseInternal2InchBaysTitle);
            this.internal2inchBayCaseValue = itemView.findViewById(R.id.caseInternal2InchBaysValue);
            this.imageOfCase = itemView.findViewById(R.id.image_of_case);
            this.addCaseToList = itemView.findViewById(R.id.addCaseToListButton);

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
            addCaseToList.setOnClickListener(new View.OnClickListener() {
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

    //Send the click event to the SelectCase activity which is then handled there
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

        void onAddButtonClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(CaseAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


}
