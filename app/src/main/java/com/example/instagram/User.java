package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class User extends AppCompatActivity {
    ImageView userProfile;
    TextView and;
    Post post;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        ParseUser user = new ParseUser();
        String username = user.getUsername();
//        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
//
//        // Set Home selected
//        bottomNavigationView.setSelectedItemId(R.id.home);
//
//        // Perform item selected listener
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch(item.getItemId())
//                {
//                    case R.id.post:
//                        startActivity(new Intent(getApplicationContext(),PostImage.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.person:
//                        return true;
//                    case R.id.home:
//                        startActivity(new Intent(getApplicationContext(),Feed.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });
        userProfile = findViewById(R.id.userProfile);


//        Glide.with(context).load(user.getParseFile(Post.KEY_PROFILE).getUrl()).into(userProfile);
        and = findViewById(R.id.and);
        and.setText(username);
        if (post != null) {
            ParseFile image = post.getProfilePicture();
            if (image != null) {
                Glide.with(context).
                        load(image.getUrl())
                        .into(userProfile);
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutBtn) {
            logout();
        }
        if (item.getItemId() == R.id.postImageBtn) {
            Intent i = new Intent(this, PostImage.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(User.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }

        });
    }
}


