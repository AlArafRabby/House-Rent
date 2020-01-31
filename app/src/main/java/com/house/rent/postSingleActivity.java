package com.house.rent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class postSingleActivity extends AppCompatActivity {
    private static final int request_call=1;
    private Button call, map;
    private String mPostKey;
    private DatabaseReference mDatabse;
    private ImageView singlePostImg;
    private TextView singlePostAddress, singlePostPhn, singlePostDtl, singlePostArea, singlePostThana, singlePostDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_single);

        call= findViewById(R.id.call);
        map= findViewById(R.id.map);

        singlePostImg= findViewById(R.id.post_view_img);
        singlePostAddress= findViewById(R.id.post_view_address);
        singlePostDtl= findViewById(R.id.post_view_details);
        singlePostPhn= findViewById(R.id.post_view_phn);
        singlePostArea= findViewById(R.id.post_view_area);
        singlePostThana= findViewById(R.id.post_view_thana);
        singlePostDist= findViewById(R.id.post_view_district);

        mPostKey= getIntent().getExtras().getString("post_id");
//        Toast.makeText(postSingleActivity.this, mPostKey, Toast.LENGTH_LONG).show();

        mDatabse= FirebaseDatabase.getInstance().getReference().child("house_details");

        mDatabse.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String post_phn= (String) dataSnapshot.child("phone").getValue();
                String post_address= (String) dataSnapshot.child("address").getValue();
                String post_details= (String) dataSnapshot.child("details").getValue();
                String post_image= (String) dataSnapshot.child("image").getValue();
                String post_area= (String) dataSnapshot.child("area").getValue();
                String post_thana= (String) dataSnapshot.child("thana").getValue();
                String post_dist= (String) dataSnapshot.child("district").getValue();

                singlePostAddress.setText(post_address);
                singlePostDtl.setText(post_details);
                singlePostPhn.setText(post_phn);
                singlePostArea.setText(post_area);
                singlePostThana.setText(post_thana);
                singlePostDist.setText(post_dist);
                Picasso.with(postSingleActivity.this).load(post_image).into(singlePostImg);

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ContextCompat.checkSelfPermission(postSingleActivity.this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(postSingleActivity.this,
                                    new String[] {Manifest.permission.CALL_PHONE}, request_call);
                        }else {
                            String callNum= "tel:"+ post_phn;
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(callNum)));
                        }
                    }
                });

                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openMap= getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
                        if(openMap != null){
                            startActivity(openMap);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
