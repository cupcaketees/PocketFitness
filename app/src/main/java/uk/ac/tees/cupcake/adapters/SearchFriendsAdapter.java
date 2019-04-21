package uk.ac.tees.cupcake.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.UserProfile;

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> implements Filterable {

    private final ArrayList<UserProfile> profiles;
    private final ArrayList<UserProfile> profilesAll;
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

    public SearchFriendsAdapter(ArrayList<UserProfile> profiles) {
        this.profiles = profiles;
        this.profilesAll = new ArrayList<>(profiles);
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

        if (profile.getProfilePictureUrl() != null) {
            Picasso.with(holder.mImage.getContext())
                    .load(profile.getProfilePictureUrl())
                    .into(holder.mImage);
        }

        holder.mName.setOnClickListener(v -> {

//          IntentUtils.invokeVideoView(context, FriendsProfileActivity.class, "User ID" , profile.getUserUid());
        });

        for (UserProfile profileCheck : profiles) {
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

