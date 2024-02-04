package com.example.planit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.firebaseui.firestore.FirestoreRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class RecyclerViewAdapter extends FirestoreRecyclerAdapter< User,RecyclerViewAdapter.ViewHolder>
{
    private OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }
    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.itemView.getContext()).clear(holder.pfp);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User model) {
        holder.textViewName.setText(model.getName());
        holder.textViewStreak.setText(String.valueOf(model.getStreak()));
//           holder.pfp.setImageResource(model.getProfilePictureUrl());
        Glide.with(holder.itemView.getContext())
                .load(model.getProfilePictureUrl())
                .into(holder.pfp);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);

        return new ViewHolder(v);
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewStreak;
        ImageView pfp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStreak=itemView.findViewById(R.id.streak);
            textViewName=itemView.findViewById(R.id.name);
            pfp=itemView.findViewById(R.id.pfp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && listener != null){

                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
                        listener.onItemClick(documentSnapshot, position);

//                        String userId = documentSnapshot.getId();
////                        Toast.makeText(v.getContext(), "You clicked " + userId, Toast.LENGTH_SHORT).show();
//
//                        // create an intent to start the profile view activity
//                        Intent intent = new Intent(itemView.getContext(), profile_view.class);
//                        intent.putExtra("userId", userId);
//                        itemView.getContext().startActivity(intent);
//
//                        // start the intent
//                        itemView.getContext().startActivity(intent);
////                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }
    }
}

