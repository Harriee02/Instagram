package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class PostDetails extends AppCompatActivity {
    int REQUEST_CODE = 20;
    ImageView expandedImage;
    TextView txtCaption;
    TextView txtTimeStamp;
    Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
        expandedImage = findViewById(R.id.expandedImage);
        txtCaption = findViewById(R.id.txtCaption);
        txtTimeStamp = findViewById(R.id.txtTimeStamp);
        Post post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getName()));
        txtCaption.setText(post.getDescription());
        txtTimeStamp.setText(post.getCreatedAt().toString());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(PostDetails.this).load(image.getUrl()).into(expandedImage);
        }
    }


}
