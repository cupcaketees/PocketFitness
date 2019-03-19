package uk.ac.tees.cupcake.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.tees.cupcake.R;

/**
 * Created by s6065431 on 12/02/2019.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private final List<Post> posts;

    public FeedAdapter(List<Post> posts) {
        this.posts = posts;
        setHasStableIds(true);
    }

    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.post_layout, parent, false);

        return new FeedViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Post post = posts.get(position);
        
        holder.descriptionTextView.setText(post.getDescription());
        holder.usernameTextView.setText(post.getUsername());
        holder.dateTextView.setText(post.getDate());
        
        Picasso.
                with(holder.itemView.getContext())
                .load(post.getImage())
                .placeholder(R.drawable.cupcake)
                .into(holder.mImageView);
        
        Log.d("Positiontag", "position: "+position);
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder{

        private TextView descriptionTextView;

        private ImageView mImageView;

        private ImageView mProfilePic;

        private TextView usernameTextView;

        private TextView dateTextView;

        private Button optionsButton;

        private  TextView ratingCount;

        FeedViewHolder(View postView) {

            super(postView);
            descriptionTextView = postView.findViewById(R.id.post_description);
            mImageView = postView.findViewById(R.id.post_image);
            usernameTextView = postView.findViewById(R.id.post_username);
            dateTextView = postView.findViewById(R.id.post_date);
            mProfilePic = postView.findViewById(R.id.post_profile_picture);
            mProfilePic.setImageResource(R.drawable.profilepic);
            ratingCount = postView.findViewById(R.id.post_ratingcount);
            ratingCount.setText("999");
            optionsButton = (Button) postView.findViewById(R.id.dropdown_menu);
            optionsButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(postView.getContext(), optionsButton);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(item -> {
                        Toast.makeText(postView.getContext(), "You clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    });
                    popupMenu.show(); //showing popup menu
                }
            });
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}