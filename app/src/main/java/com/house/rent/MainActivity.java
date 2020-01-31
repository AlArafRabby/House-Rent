package com.house.rent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private RecyclerView postList;
    private Button search;
    private FirebaseAuth mAuth;
    private EditText searchField;
    private DatabaseReference mDatabse, mDatabaseUsers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter, adapter_search;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search= (Button) findViewById(R.id.search_btn);
        searchField= (EditText) findViewById(R.id.search_field);

        database= FirebaseDatabase.getInstance();
        mDatabse= database.getReference("house_details");
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("users");
//        getphn= FirebaseDatabase.getInstance().getReference().child("house_details");
        mDatabaseUsers.keepSynced(true);

        postList= (RecyclerView) findViewById(R.id.post_list);
        postList.setHasFixedSize(true);
        postList.setLayoutManager(new LinearLayoutManager(this));

        showList();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

//        mAuth= FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.action_login){
            startActivity(new Intent(MainActivity.this, LoginPage.class));
        }
        if(item.getItemId()== R.id.home_post){
            startActivity(new Intent(MainActivity.this, postActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showList(){
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(mDatabse, Blog.class)
                        .build();

        adapter= new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final BlogViewHolder blogViewHolder, int i, @NonNull Blog blog) {
                final String post_key= getRef(i).getKey();

                mDatabse.child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //final String phn= (String) dataSnapshot.child("phone").getValue();
                        blogViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_LONG).show();
                                Intent singlePostIntent= new Intent(MainActivity.this, postSingleActivity.class);
                                singlePostIntent.putExtra("post_id", post_key);
                                startActivity(singlePostIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                blogViewHolder.setAddress(blog.getAddress());
                blogViewHolder.setPhone(blog.getPhone());
                blogViewHolder.setDetails(blog.getDetails());
                blogViewHolder.setArea(blog.getArea());
                blogViewHolder.setThana(blog.getThana());
                blogViewHolder.setDistrict(blog.getDistrict());
                blogViewHolder.setImage(getApplicationContext(), blog.getImage());
            }

            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_row, parent, false);
                return new BlogViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        postList.setAdapter(adapter);
    }

    private void firebaseUserSearch(String searchText){
        Query firebaseSearchQuery = mDatabse.orderByChild("area")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(firebaseSearchQuery, Blog.class)
                        .build();

        adapter_search= new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final BlogViewHolder blogViewHolder, int i, @NonNull Blog blog) {

                final String post_key= getRef(i).getKey();

                mDatabse.child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //final String phn= (String) dataSnapshot.child("phone").getValue();
                        blogViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_LONG).show();
                                Intent singlePostIntent= new Intent(MainActivity.this, postSingleActivity.class);
                                singlePostIntent.putExtra("post_id", post_key);
                                startActivity(singlePostIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                blogViewHolder.setAddress(blog.getAddress());
                blogViewHolder.setPhone(blog.getPhone());
                blogViewHolder.setDetails(blog.getDetails());
                blogViewHolder.setArea(blog.getArea());
                blogViewHolder.setThana(blog.getThana());
                blogViewHolder.setDistrict(blog.getDistrict());
                blogViewHolder.setImage(getApplicationContext(), blog.getImage());
            }

            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_row, parent, false);
                return new BlogViewHolder(view);
            }
        };

        adapter_search.startListening();
        adapter_search.notifyDataSetChanged();
        postList.setAdapter(adapter_search);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView= itemView;
        }

        public void setAddress(String address){
            TextView post_address= (TextView) mView.findViewById(R.id.post_address);
            post_address.setText(address);
        }

        public void setPhone(String phone){
            TextView post_phone= (TextView) mView.findViewById(R.id.post_phn);
            post_phone.setText(phone);
        }

        public void setDetails(String details){
            TextView post_details= (TextView) mView.findViewById(R.id.post_details);
            post_details.setText(details);
        }
        public void setArea(String area){
            TextView post_area= (TextView) mView.findViewById(R.id.post_area);
            post_area.setText(area);
        }
        public void setThana(String thana){
            TextView post_thana= (TextView) mView.findViewById(R.id.post_thana);
            post_thana.setText(thana);
        }
        public void setDistrict(String district){
            TextView post_dist= (TextView) mView.findViewById(R.id.post_district);
            post_dist.setText(district);
        }
        public void setImage(Context ctx, String image){
            ImageView post_image= (ImageView) mView.findViewById(R.id.post_img);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}
