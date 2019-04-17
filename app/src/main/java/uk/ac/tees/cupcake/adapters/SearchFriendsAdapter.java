package uk.ac.tees.cupcake.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * Created by s6105692 on 17/04/19.
 */

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "SearchFriendsAdapter";
    private ArrayList<Post> profiles;
    private ArrayList<Post> profilesAll;
    private Context context;

    public SearchFriendsAdapter(ArrayList<Post> profiles, Context context) {
        this.profiles = profiles;
        this.profilesAll = new ArrayList<>(profiles);
        this.context = context;
    }

    @Override
    public SearchFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friends_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post profile = profiles.get(position);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        holder.mName.setText(profile.getFirstName() + " " + profile.getLastName());

        if (profile.getProfilePictureUrl() != null) {
            Picasso.with(holder.mImage.getContext())
                    .load(profile.getProfilePictureUrl())
                    .into(holder.mImage);
        }


        holder.mName.setOnClickListener(v -> {
//            Log.d(TAG, "onBindViewHolder: " + profile.getUserUid());#
            /*Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("index", 2);
            context.startActivity(intent);*/
            Map<String, Serializable> extras = new HashMap<>();
            extras.put("index", 2);
            IntentUtils.invokeViewWithExtras(context, MainActivity.class, extras);
//          IntentUtils.invokeVideoView(context, MainActivity.class, "User ID" , profile.getUserUid());
        });


        for(Post profileCheck : profiles){

            if(!profilesAll.contains(profileCheck)) {
                profilesAll.add(profileCheck);
            }
        }
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    @Override
    public Filter getFilter() {
        return profileFilter;
    }

    private Filter profileFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Post> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0 ){
                filteredList.addAll(profilesAll);
            } else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Post item : profilesAll){
                    if (item.getFirstName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            profiles.clear();
            profiles.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private ImageView mImage;


        public ViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.friendlistname);
            mImage = itemView.findViewById(R.id.friendlistimage);
        }
    }
}

