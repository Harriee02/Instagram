package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    List<Post> posts;
    public static final String TAG = "WORKS!";

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postedImage;
        TextView igName;
        TextView description;
        ImageView pfpImage;
        TextView timeStamp;
        ImageView likeBtn;
        ImageView commentBtn;
        ImageView sendDmBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postedImage = itemView.findViewById(R.id.postedImage);
            igName = itemView.findViewById(R.id.igName);
            description = itemView.findViewById(R.id.description);
            timeStamp = itemView.findViewById(R.id.txtcreatedAt);
            pfpImage = itemView.findViewById(R.id.pfpImage);
            likeBtn = itemView.findViewById(R.id.likeBtn);


        }

        public void bind(Post post) {
            description.setText(post.getDescription());
            igName.setText(post.getUser().getUsername());
            if (post.getCreatedAt() != null) {
                timeStamp.setText(post.getCreatedAt().toString());
            }

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).
                        load(image.getUrl())
                        .into(postedImage);
                Glide.with(context).
                        load(image.getUrl())
                        .centerCrop()
                        .transform(new CircleCrop())
                        .into(pfpImage);

            } else {
                postedImage.setVisibility(View.INVISIBLE);
            }
            postedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PostDetails.class);
                    i.putExtra(Post.class.getName(), Parcels.wrap(post));
                    context.startActivity(i);


                }
            });


        }


    }


}
