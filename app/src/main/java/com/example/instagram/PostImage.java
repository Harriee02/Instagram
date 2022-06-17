package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.example.instagram.models.Post;
import com.parse.SaveCallback;

public class PostImage extends AppCompatActivity {
    public static final String TAG = "MAIN_ACTIVITY";
    EditText txtDescription;
    ImageView cameraBtn;
    ImageView postImage;
    TextView takePhoto;
    Button postBtn;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_image);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Feed.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.post:
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(), User.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        txtDescription = findViewById(R.id.txtDescription);
        cameraBtn = findViewById(R.id.cameraBtn);
        postImage = findViewById(R.id.postImage);
        takePhoto = findViewById(R.id.takePhoto);
        postBtn = findViewById(R.id.postBtn);
        queryPosts();
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDescription.getText().toString().length() > 0 && file != null) {
                    savePost(txtDescription.getText().toString(), ParseUser.getCurrentUser(), file);
                    Intent i = new Intent(PostImage.this, Feed.class);
                    startActivity(i);
                } else {
                    Log.i(TAG, "Post was unsuccessful");
                    if (txtDescription.getText().length() == 0) {
                        Toast.makeText(PostImage.this, "Post must have a description", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PostImage.this, "Post must have a picture", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference for future access
                Uri uri = getPhotoFileUri(photoFileName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }


            }
        });


    }

    private Uri getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);// this
        return FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", file);

    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, post.getDescription());
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                ImageView ivPreview = (ImageView) findViewById(R.id.postImage);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
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
                    Intent i = new Intent(PostImage.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }

        });
    }

    public void savePost(String description, ParseUser currentUser, File file) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(new ParseFile(file));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, e + "this is the error!");
                } else {
                    Toast.makeText(PostImage.this, "Saved!", Toast.LENGTH_SHORT).show();
                    txtDescription.setText("");
                    postImage.setImageResource(0);
                }
            }
        });

    }
}