package com.house.rent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class postActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageButton imgSelect;
    private EditText address, phn, details, area, thana, district;
    private Button post;
    public String test;
    Uri imgUri= null;
    public static final int Gallery_req= 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("house_details");

        mAuth= FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()== null){
                    Intent loginIntent= new Intent(postActivity.this, LoginPage.class);
                    Toast.makeText(postActivity.this, "Please Login to post", Toast.LENGTH_LONG).show();
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        imgSelect= (ImageButton) findViewById(R.id.select_image);
        address= (EditText) findViewById(R.id.address);
        phn= (EditText) findViewById(R.id.phone);
        details= (EditText) findViewById(R.id.details);
        area= findViewById(R.id.area);
        thana= findViewById(R.id.thana);
        district= findViewById(R.id.district);
        post= (Button) findViewById(R.id.post);
        mProgress= new ProgressDialog(this);

        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_req);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        mProgress.setMessage("Posting");

        final String addr_val= address.getText().toString().trim();
        final String phn_val= phn.getText().toString().trim();
        final String dtl_val= details.getText().toString().trim();
        final String area_val= area.getText().toString().trim();
        final String thana_val= thana.getText().toString().trim();
        final String dis_val= district.getText().toString().trim();

        if(!TextUtils.isEmpty(addr_val) && !TextUtils.isEmpty(phn_val) && !TextUtils.isEmpty(dtl_val) && !TextUtils.isEmpty(area_val) && !TextUtils.isEmpty(thana_val) && !TextUtils.isEmpty(dis_val) && imgUri != null){
            mAuth.addAuthStateListener(mAuthListener);
            mProgress.show();
            final StorageReference filePath= mStorage.child("House_Pics").child(imgUri.getLastPathSegment());

            filePath.putFile(imgUri). addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // get the image Url of the file uploaded
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // getting image uri and converting into string
                            Uri downloadUrl = uri;
                            String fileUrl = downloadUrl.toString();
                            DatabaseReference newPost= mDatabase.push();
                            newPost.child("address").setValue(addr_val);
                            newPost.child("phone").setValue(phn_val);
                            newPost.child("details").setValue(dtl_val);
                            newPost.child("area").setValue(area_val);
                            newPost.child("thana").setValue(thana_val);
                            newPost.child("district").setValue(dis_val);
                            newPost.child("image").setValue(fileUrl);
                            mProgress.dismiss();

                            startActivity(new Intent(postActivity.this, userActivity.class));


                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Gallery_req){
            imgUri= data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

            imgSelect.setImageURI(imgUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgSelect.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
