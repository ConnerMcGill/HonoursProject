package com.example.honoursproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserListAdapter extends FirestoreRecyclerAdapter<UserList, UserListAdapter.UserListHolder> {
    private UserListAdapter.OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserListAdapter(@NonNull FirestoreRecyclerOptions<UserList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserListHolder holder, int position, @NonNull UserList model) {
        holder.userListTitle.setText(model.getTitle());
        holder.userListDescription.setText(model.getDescription());
    }

    @NonNull
    @Override
    public UserListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent, false);
        return new UserListHolder(view);
    }

    //Delete document from Firestore when users swipes item in recycler view
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    public class UserListHolder extends RecyclerView.ViewHolder {
        TextView userListTitle;
        TextView userListDescription;


        public UserListHolder(@NonNull View itemView) {
            super(itemView);
            userListTitle = itemView.findViewById(R.id.nameOfUsersList);
            userListDescription = itemView.findViewById(R.id.userListDescription);

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



        }
    }

    //Send the click event to the SelectSavedList activity which is then handled there
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        //Add a delete button click event here eventually
    }

    public void setOnItemClickListener(UserListAdapter.OnItemClickListener listener){
        this.listener = listener;
    }



}
