package com.example.attendence;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class activity_status extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
   EditText etstatus;
   TextView txtcurrent_time;
   ImageView isend,icamera,setimage;
   FirebaseAuth auth;
   FirebaseFirestore db;
    private StorageReference storageReference;
    ProgressDialog progressDialog;
    private Uri capImageURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait a sec...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        etstatus = findViewById(R.id.edit_status);
        isend = findViewById(R.id.isend);
        txtcurrent_time = findViewById(R.id.current_time);
        setimage = findViewById(R.id.setprofile);
        icamera = findViewById(R.id.icamera);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Database").child("Users");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        txtcurrent_time.setText(strDate);
        isend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                if (capImageURI == null) {
                    String email=auth.getCurrentUser().getEmail();
                    String data = etstatus.getText().toString();
                    String time = txtcurrent_time.getText().toString();
                    final String uid = auth.getCurrentUser().getUid();
                    final Teacherstatus status = new Teacherstatus();
                    status.setData(data);
                    status.setUid(FirebaseAuth.getInstance().getUid());
                    status.setTimesep(time);
                    status.setTemail(email);
                    db.collection("Data").document().set(status)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.show();
                                    Toast.makeText(activity_status.this, "status sent", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), activity_navigation.class));
                                   finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_status.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), capImageURI);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    // bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                    final byte[] fileInBytes = outputStream.toByteArray();
                    final StorageReference filePath = storageReference.child(auth.getCurrentUser().getUid()).child("ProfilePic").child(capImageURI.getLastPathSegment());
                    filePath.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String data = etstatus.getText().toString();
                                    String email=auth.getCurrentUser().getEmail();
                                    String time = txtcurrent_time.getText().toString();
                                    final String uid = auth.getCurrentUser().getUid();
                                    final Teacherstatus status = new Teacherstatus();
                                    status.setData(data);
                                    status.setTimesep(time);
                                    status.setTemail(email);
                                    status.setImagename(capImageURI.getLastPathSegment());
                                    status.setImagestatus(uri.toString());

                                    final DocumentReference ref = db.collection("Data").document();
                                    ref.set(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.show();


                                                    Toast.makeText(activity_status.this, "status sent", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), activity_navigation.class));
                                                   finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(activity_status.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(activity_status.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



       icamera.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final CharSequence[] items = {"Take a new photo", "Choose from gallery", "Cancel"};
               AlertDialog.Builder builder = new AlertDialog.Builder(activity_status.this);
               builder.setTitle("Add Photo");
               builder.setItems(items, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int i) {
                       if (items[i].equals("Take a new photo")) {
                           //request permission start camera intent
                           requestCameraPermission();
                       } else if (items[i].equals("Choose from gallery")) {
                           //request gallery permission and start gallery intent
                           requestGalleryPermission();
                       } else if (items[i].equals("Cancel")) {
                           //dismiss the alert dialog
                           dialog.dismiss();
                       }
                   }
               });
               builder.show();
           }
       });
    }
    //Gallery
    private void requestGalleryPermission() {
        int result = ContextCompat
                .checkSelfPermission(activity_status.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat
                    .requestPermissions(activity_status.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            GALLERY_REQUEST);
        }
    }
    //Camera
    private void requestCameraPermission() {
        int result = ContextCompat.checkSelfPermission(activity_status.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(activity_status.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Permission denied to open Camera", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case GALLERY_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Permission denied to open Gallery", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_REQUEST);
    }
    private void startCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, CAMERA_REQUEST);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_REQUEST: {
                if (resultCode == RESULT_OK && data.getData() != null) {
                    capImageURI = data.getData();
                    setProfileImage(data.getData());
                } else {
                    Toast.makeText(this, "No Image Selected! Try AgainNo Image Selected! Try Again", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case CAMERA_REQUEST: {
                if (resultCode == RESULT_OK && data.getData() != null) {
                    capImageURI = data.getData();
                    setProfileImage(data.getData());
                } else {
                    Toast.makeText(this, "No Image Captured! Try Again", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void setProfileImage(Uri data) {
        setimage.setImageURI(data);
    }


}
