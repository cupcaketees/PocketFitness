package uk.ac.tees.cupcake.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.like.LikeButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.feed.FeedAdapter;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.friends.Comments;

/**
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comments> comments;
    private static final String TAG = "CommentAdapter";

    public CommentAdapter(List<Comments> comments) {
        this.comments = comments;
        setHasStableIds(true);

    }

    /**
     * @param parent - the current view. it will inflate the card layout on top.
     * @return view with card layout
     */
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.comment_row, parent, false);
        return new CommentAdapter.CommentViewHolder(postView);
    }

    /**
     * @param holder   - the view with all the variables to be able to change them for each item.
     * @param position - the current position similar to looping.
     */
    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + comments.get(position).getId());
        Comments comment = comments.get(position);

        FirebaseFirestore.getInstance().collection("Users/")
                .document(comment.getId())
                .get()
                .addOnSuccessListener(v -> {
                    if(v.exists()) {
                        UserProfile profile = v.toObject(UserProfile.class) ;

                        holder.comment_user_name.setText(String.format("%s%s", profile.getFirstName(), profile.getLastName()));
                        holder.comment_desc.setText(comment.getMessage());
                        if(profile.getProfilePictureUrl() != null){
                            Picasso.with(holder.itemView.getContext())
                                    .load(profile.getProfilePictureUrl())
                                    .into(holder.profile_image);
                        }
                    }

                });

    }

    /**
     *
     * @return - size of list
     */
    @Override
    public int getItemCount() {
        return comments.size();
    }

    /**
     * Initialises all the variables in the View.
     */
    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView comment_user_name;
        private TextView comment_desc;
        private ImageView profile_image;


        public CommentViewHolder(View postView) {
            super(postView);

            comment_user_name = postView.findViewById(R.id.comment_name);
            comment_desc = postView.findViewById(R.id.comment_desc);
            profile_image = postView.findViewById(R.id.comment_image);

        }
    }
}


