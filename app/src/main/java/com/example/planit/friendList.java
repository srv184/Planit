package com.example.planit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class friendList extends AppCompatActivity implements rcViewClick {

    ImageView pfp;
    TextView name,streak;
    Button following,followers;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    DocumentReference documentReference;
    FirebaseUser user;
    String userId;
    CollectionReference usersc;
    RecyclerView recyclerView;
    Following_rcadapter recyclerViewAdapter;
    ArrayList<User> follow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        pfp=findViewById(R.id.pfp);
        name=findViewById(R.id.name);
        streak=findViewById(R.id.streak);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pfp);
            }
        });
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    name.setText(documentSnapshot.getString("name"));
                    Object str=documentSnapshot.get("streak");
                    streak.setText(str.toString()+ " \ud83d\udd25");
                }
                else {
                    Log.d("tag", "onEvent: Document do not exists"+userId);
                }
            }
        });


        recyclerView = findViewById(R.id.following_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        follow = new ArrayList<>();
        recyclerViewAdapter = new Following_rcadapter(this, follow, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        usersc = fStore.collection("users");
        fStore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                follow.add(user);
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        following=findViewById(R.id.following);
        followers=findViewById(R.id.followers);
        following.setEnabled(false);
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersc = fStore.collection("users");
                fStore.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User user = document.toObject(User.class);
                                        follow.add(user);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }
                                }else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                following.setEnabled(false);
                followers.setEnabled(true);
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersc = fStore.collection("users");
                fStore.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User user = document.toObject(User.class);
                                        follow.add(user);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }
                                }else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                followers.setEnabled(false);
                following.setEnabled(true);
            }
        });

    }

    @Override
    public void itemOnClick(int position) {
        //startActivity(new Intent(this, post_activity.class));
    }
}