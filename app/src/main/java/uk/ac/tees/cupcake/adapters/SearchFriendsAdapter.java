package uk.ac.tees.cupcake.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.account.ViewProfileActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "SearchFriendsAdapter";
    private final ArrayList<UserProfile> profiles;
    private final ArrayList<UserProfile> profilesAll;
    private String searchLocation;
    private Filter profileFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<UserProfile> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(profilesAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (UserProfile item : profilesAll) {
                    if (item.getFirstName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            profiles.clear();
            profiles.addAll((ArrayList<UserProfile>) results.values);
            notifyDataSetChanged();
        }
    };

    public SearchFriendsAdapter(ArrayList<UserProfile> profiles, String searchLocation) {
        this.profiles = profiles;
        this.profilesAll = new ArrayList<>(profiles);
        this.searchLocation = searchLocation;
    }

    @Override
    public SearchFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_friends_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserProfile profile = profiles.get(position);

        String name = profile.getFirstName() + " " + profile.getLastName();
        holder.mName.setText(name);
        if (searchLocation.equals("FollowerRequests")) {
            holder.mConfirm.setVisibility(View.VISIBLE);
            holder.mDecline.setVisibility(View.VISIBLE);

            holder.mConfirm.setOnClickListener(v -> {
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()  + "/Followers/" + profile.getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {

                            Log.d(TAG, "onBindViewHolder: " + profile.getUid());
                            Map<String, Object> followTimeStamp = new HashMap<>();
                            followTimeStamp.put("timestamp", FieldValue.serverTimestamp());

                            documentSnapshot.getReference()
                                    .set(followTimeStamp)
                                    .addOnSuccessListener(aVoid -> {

                                        FirebaseFirestore.getInstance().collection("Users").document( profile.getUid() + "/Following/" + FirebaseAuth.getInstance().getUid())
                                                .set(followTimeStamp);
                                    });

                            deleteRequest(profile.getUid(), position);


                        });
                    });
        }

        holder.mDecline.setOnClickListener(v -> {
            deleteRequest(profile.getUid(), position);
        });

        if (profile.getProfilePictureUrl() != null) {
            Picasso.with(holder.mImage.getContext())
                    .load(profile.getProfilePictureUrl())
                    .into(holder.mImage);
        }

        holder.mName.setOnClickListener(v -> {
            IntentUtils.invokeVideoView(v.getContext(), ViewProfileActivity.class, "profileId", profile.getUid());

        });

        for (UserProfile profileCheck : profiles) {
            if (!profilesAll.contains(profileCheck)) {
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private ImageView mImage;
        private ImageButton mConfirm;
        private ImageButton mDecline;

        public ViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.friendlistname);
            mImage = itemView.findViewById(R.id.friendlistimage);
            mConfirm = itemView.findViewById(R.id.confirm_friend);
            mDecline = itemView.findViewById(R.id.decline_friend);
        }
    }

    private void deleteRequest(String uId, int pos) {
        Log.d(TAG, "deleteRequest: " + uId);
        FirebaseFirestore.getInstance().collection("Users/").document(FirebaseAuth.getInstance().getUid() + "/FollowerRequests/" + uId )
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        // Deletes current user from viewed profile followers collection.
                        documentSnapshot.getReference()
                                .delete()
                                .addOnSuccessListener(aVoid -> {

                                    // Deletes viewed profile uid from current user followers collection.
                                    FirebaseFirestore.getInstance().collection("Users/").document( uId+ "/FollowingRequests/" + FirebaseAuth.getInstance().getUid())
                                            .delete();

                                    profiles.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, profiles.size());
                                    notifyDataSetChanged();

                                });

                    }
                });
    }
}

