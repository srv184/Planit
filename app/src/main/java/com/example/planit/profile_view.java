package com.example.planit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class profile_view extends AppCompatActivity {

    ImageButton imageButton1,imageButton2;
    ImageView imageView;
    TextView fullname,email,address,streak;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    DocumentReference documentReference;
    FirebaseUser user;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        imageButton1=findViewById(R.id.imageButton1);
        imageButton2=findViewById(R.id.imageButton2);
        imageView=findViewById(R.id.imageView);
        fullname=findViewById(R.id.editText1);
        email=findViewById(R.id.editText2);
        address=findViewById(R.id.editText3);
        streak=findViewById(R.id.textView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        String userId = getIntent().getStringExtra("userId");

        StorageReference profileRef = storageReference.child("users/"+userId+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });


//        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    fullname.setText(documentSnapshot.getString("name"));
                    email.setText(documentSnapshot.getString("email"));
                    address.setText(documentSnapshot.getString("location"));
                    Object str=documentSnapshot.get("streak");
                    streak.setText(str.toString()+ " \ud83d\udd25");
//                    streak.setText( documentSnapshot.get("streak").toString()+" \ud83d\udd25");

                    //convert streak to string from integer

//                    streak.setText("0");
                }
                else {
                    Log.d("tag", "onEvent: Document do not exists"+userId);
//                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(profile_view.this, "Request Sent \ud83d\udc8c", Toast.LENGTH_SHORT).show();
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile_view.this, friendList.class));
            }
        });
    }
}