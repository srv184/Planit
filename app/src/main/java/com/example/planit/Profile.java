package com.example.planit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    ImageView imageView;
    TextView streak;
    EditText fullname,email,address;
    ImageButton imageButton1,editButton,imageButton3,imageButton4,imageButton5,imageButton6;
    Bitmap bmp;
    Uri uri;

    FirebaseFirestore dbroot;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    DocumentReference documentReference;
    FirebaseUser user;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView=findViewById(R.id.imageView);
        streak=findViewById(R.id.textView);
        fullname=findViewById(R.id.editText1);
        fullname.setEnabled(false);
        email=findViewById(R.id.editText2);
        email.setEnabled(false);
        address=findViewById(R.id.editText3);
        address.setEnabled(false);
        imageButton1=findViewById(R.id.imageButton1); imageButton1.setEnabled(false);
        editButton=findViewById(R.id.imageButton2);
        imageButton3=findViewById(R.id.imageButton3);
        imageButton4=findViewById(R.id.imageButton4);
        imageButton5=findViewById(R.id.imageButton5);imageButton5.setEnabled(false);
        imageButton6=findViewById(R.id.imageButton6);imageButton6.setEnabled(false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
        user = fAuth.getCurrentUser();
        userId = user.getUid();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton1.setVisibility(View.VISIBLE);
                imageButton1.setEnabled(true);
                imageButton5.setVisibility(View.VISIBLE);
                imageButton5.setEnabled(true);
                imageButton6.setVisibility(View.VISIBLE);
                imageButton6.setEnabled(true);
                fullname.setEnabled(true);
                email.setEnabled(true);
                address.setEnabled(true);
            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Profile.this, "Choose an Image", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,100);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, friendList.class));
            }
        });
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUser();
                imageButton1.setVisibility(View.INVISIBLE);
                imageButton1.setEnabled(false);
                imageButton5.setVisibility(View.INVISIBLE);
                imageButton5.setEnabled(false);
                imageButton6.setVisibility(View.INVISIBLE);
                imageButton6.setEnabled(false);
                fullname.setEnabled(false);
                email.setEnabled(false);
                address.setEnabled(false);
            }
        });
        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Profile.class));
            }
        });


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
    }

    private void UpdateUser()
    {
        String updatedname=fullname.getText().toString();
        String updatedemail= email.getText().toString();
        String updatedloc=address.getText().toString();
        //update user info
        user.updateEmail(updatedemail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Profile.this, "Email Updated", Toast.LENGTH_SHORT).show();

                documentReference.update("email",updatedemail);
                documentReference.update("name",updatedname);
                documentReference.update("location",updatedloc);
                Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Profile.this, "Email Update Failed due to" +e.toString() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            assert data != null;
            imageView.setImageURI(data.getData());
            uri=data.getData();
            try {
                bmp= MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bmp= Bitmap.createScaledBitmap(bmp,100,100,true);
                uploadImageToFirebase(uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // image uploaded to firebase storage toast
                        Picasso.get().load(uri).into(imageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Failed." + e, Toast.LENGTH_SHORT).show();

            }
        });
    }
}