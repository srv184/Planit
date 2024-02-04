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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class community extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RecyclerViewAdapter.OnItemClickListener{

    Spinner spinner;
    ImageView imageViewreg;
    ImageView pfp2,pfp3,pfp4,pfp5;
    TextView name2,name3,name4,name5;
    TextView streak2,streak3,streak4,streak5;
    ImageButton plus;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    DocumentReference documentReference;
    CollectionReference usersc;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<User> userList;


    String user2="8YjvylJLtMggw3DntAJzlUQ0c2C3";
    String user3="9sMzX5dKAVNR8Frr9i9tg94OAD32";
    String user4="TD3fC84GTbaDk0wdWTXGwyNZpts2";
    String user5="aB0J9KKLRIhOHx1Z3tV3YqjllUz2";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        spinner=findViewById(R.id.spinner);
        imageViewreg=findViewById(R.id.imageViewreg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersc = fStore.collection("users");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        setUpRecyclerView();
//        recyclerViewAdapter = new RecyclerViewAdapter(this, userList);
        recyclerView.setAdapter(recyclerViewAdapter);
//        name2.setClickable(true);


//        pfp2=findViewById(R.id.pfpimage2);
//        pfp3=findViewById(R.id.pfpimage3);
//        pfp4=findViewById(R.id.pfpimage4);
//        pfp5=findViewById(R.id.pfpimage5);
//
//        name2=findViewById(R.id.textname1);
//        name3=findViewById(R.id.textname2);
//        name4=findViewById(R.id.textname3);
//        name5=findViewById(R.id.textname4);
//
//        streak2=findViewById(R.id.streak2);
//        streak3=findViewById(R.id.streak3);
//        streak4=findViewById(R.id.streak4);
//        streak5=findViewById(R.id.streak5);


        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageViewreg);
            }
        });

//        fetch_user(user2,name2,streak2,pfp2);
//        fetch_user(user3,name3,streak3,pfp3);
//        fetch_user(user4,name4,streak4,pfp4);
//        fetch_user(user5,name5,streak5,pfp5);


        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.community,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fStore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
//                                user.streak=Integer.parseInt(document.get("streak").toString());
                                userList.add(user);

//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //toast
//                                Toast.makeText(community.this, "testing1", Toast.LENGTH_SHORT).show();
                            }
//                            for (int i = 0; i < userList.size(); i++) {
////                                Toast.makeText(community.this, userList.get(i).name, Toast.LENGTH_SHORT).show();
////                                Log.d("TAG", "onComplete: " + userList.get(i).name);
//                            }
                            recyclerViewAdapter.notifyDataSetChanged();
                        } else {
//                            Toast.makeText(community.this, "error1", Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void setUpRecyclerView() {
        Query query = usersc.orderBy("streak", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<User> options= new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class).build();
        recyclerViewAdapter = new RecyclerViewAdapter(options);
        recyclerViewAdapter.setOnItemClickListener(this);

    recyclerView.setAdapter(recyclerViewAdapter);
    recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(recyclerViewAdapter);



    }

    protected void onStart() {
        super.onStart();
        recyclerViewAdapter.startListening();
    }
//    protected void onStop() {
//        super.onStop();
//        recyclerViewAdapter.stopListening();
//    }
//    protected void onResume() {
//        super.onResume();
//        recyclerViewAdapter.startListening();
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String func= adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, func, Toast.LENGTH_SHORT).show();
            if(func.equals("Dashboard"))
                startActivity(new Intent(this, MainActivity.class));
            if(func.equals("Profile"))
                startActivity(new Intent(this, Profile.class));
            if(func.equals("Logout"))
                startActivity(new Intent(this, login_reg.class));
            if(func.equals("Report Error")){}
//                startActivity(new Intent(this, profile_x.class));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void fetch_user(String user, TextView name, TextView streak, ImageView pfp)
    {
        StorageReference profileRef2 = storageReference.child("users/"+user+"/profile.jpg");
        profileRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pfp);
            }
        });

        documentReference = fStore.collection("users").document(user);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {

                    name.setText(documentSnapshot.getString("name"));

                    Object str = documentSnapshot.get("streak");
                    streak.setText(str.toString() + " \ud83d\udd25");

                } else {
                    Log.d("tag", "onEvent: Document do not exists" + user);
                }
            }
        });
    }


    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        String userId = documentSnapshot.getId();
//        Toast.makeText(this, "You clicked " + userId, Toast.LENGTH_SHORT).show();
        if(position != RecyclerView.NO_POSITION && documentSnapshot.exists()){
            User user = documentSnapshot.toObject(User.class);
             userId = documentSnapshot.getId();
//            Toast.makeText(this, "You clicked " + userId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, profile_view.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }




//        String userId = documentSnapshot.getId();

        // create an intent to start the profile view activity

    }
}