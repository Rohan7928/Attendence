package com.example.attendence;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class student_signup extends AppCompatActivity implements View.OnClickListener {
    Button btnsubmit, bthome;
    EditText etfirst, etlast, etemail, etmobile, etfather, etmother, etaddress, etpassword, etconfirmpass;
    EditText etsem, etdept, etclass;
    CircularImageView addstudentphoto;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseFirestore fb;
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private StorageReference storageReference;
    private Uri capImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        btnsubmit = findViewById(R.id.btSubmit);
        bthome = findViewById(R.id.bthomepage1);
        etfirst = findViewById(R.id.etFirstName);
        etlast = findViewById(R.id.etLastName);
        etsem = findViewById(R.id.etsem);
        etdept = findViewById(R.id.etDepartment);
        etclass = findViewById(R.id.etclass);
        etemail = findViewById(R.id.etEmail);
        etpassword = findViewById(R.id.etPassword);
        etconfirmpass = findViewById(R.id.etConfirmpass);
        etmobile = findViewById(R.id.etMobile);
        etfather = findViewById(R.id.etFather);
        etmother = findViewById(R.id.etMother);
        etaddress = findViewById(R.id.etAddress);
        addstudentphoto = findViewById(R.id.add_studentphoto);
        btnsubmit.setOnClickListener(this);
        bthome.setOnClickListener(this);
        addstudentphoto.setOnClickListener(this);
        fb = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a second baby...");
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Database").child("Users");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btSubmit:
                Submit();
                break;
            case R.id.bthomepage1:
                Home();
                break;
            case R.id.add_studentphoto:
                iphoto();
                break;
        }
    }

    private void iphoto() {

        final CharSequence[] items = {"Take a new photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(student_signup.this);
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

    //Gallery
    private void requestGalleryPermission() {
        int result = ContextCompat
                .checkSelfPermission(student_signup.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat
                    .requestPermissions(student_signup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            GALLERY_REQUEST);
        }
    }

    //Camera
    private void requestCameraPermission() {
        int result = ContextCompat.checkSelfPermission(student_signup.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(student_signup.this,
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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepicture.resolveActivity(getPackageManager()) != null) {
            String filename = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, filename);
            capImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takepicture.putExtra(MediaStore.EXTRA_OUTPUT, capImageURI);
            startActivityForResult(takepicture, CAMERA_REQUEST);
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
                if (resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(capImageURI, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturepath = cursor.getString(column_index_data);
                    capImageURI = Uri.parse("file://" + picturepath);
                    setProfileImage(capImageURI);
                } else {
                    Toast.makeText(this, "No Image Captured! Try Again", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void setProfileImage(Uri data) {
        addstudentphoto.setImageURI(data);
    }


    private void Home() {
        Intent intent = new Intent(this, activity_choose.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
        finish();
    }

    private void Submit() {
        final String fname = etfirst.getText().toString().trim();
        final String lname = etlast.getText().toString().trim();
        final String email = etemail.getText().toString().trim();
        final String pass = etpassword.getText().toString().trim();
        String confirmpass = etconfirmpass.getText().toString().trim();
        final String phone = etmobile.getText().toString().trim();
        final String father = etfather.getText().toString().trim();
        final String mother = etmother.getText().toString().trim();
        final String address = etaddress.getText().toString().trim();
        final String sem = etsem.getText().toString().trim();
        final String department = etdept.getText().toString().trim();
        final String clas = etclass.getText().toString().trim();

        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmpass.isEmpty() || phone.isEmpty() || father.isEmpty() || mother.isEmpty() || address.isEmpty() || sem.isEmpty() || department.isEmpty() || clas.isEmpty()) {
            Util.toast(this, "Fill all the Requirments");
            return;
        } else {
            if (isValidPassword(etpassword.getText().toString().trim())) {

                if (pass.equals(confirmpass)) {

                } else {
                    etconfirmpass.setError("Password doesn't match");
                }
            } else {
                etpassword.setError("Your password contain special symbol,One letter in capitals and numeric also");
            }

        }
        if (capImageURI == null) {
            Toast.makeText(this, "Please select a profile image to continue", Toast.LENGTH_SHORT).show();
            return;
        }
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


        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final StorageReference filePath = storageReference.
                            child(auth.getCurrentUser().getUid())
                            .child("ProfilePic").child(capImageURI.getLastPathSegment());
                    filePath.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    savedata(fname, lname, email, pass, phone, father, mother, address, sem, department, clas,uri);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(student_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(student_signup.this, "User not Registered", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(student_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* if (task.isSuccessful()) {
         Toast.makeText(student_signup.this, "User Registered", Toast.LENGTH_SHORT).show();
     } else {
         Toast.makeText(student_signup.this, "User not Registered", Toast.LENGTH_SHORT).show();
     }
 }
}).addOnFailureListener(new OnFailureListener() {
 @Override
 public void onFailure(@NonNull Exception e) {
     Toast.makeText(student_signup.this,e.getMessage(), Toast.LENGTH_SHORT).show();
 }
});*/
    private void savedata(String fname, String lname, String email, String pass, String phone, String father, String mother, String address, String sem, String department, String clas, Uri uri) {
        UserDataR user = new UserDataR(fname, lname, email, pass, phone, father, mother, address, sem, department, clas, "Student",String.valueOf(uri));
        fb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(student_signup.this, "Record", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), studenthome.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(student_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}