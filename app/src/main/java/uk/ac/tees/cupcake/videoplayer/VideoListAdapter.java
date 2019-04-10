package uk.ac.tees.cupcake.videoplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * VideoList Adapter
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "VideoListAdapter";
    private ArrayList<Video> mVideos;
    private final Context mContext;
    private ArrayList<Video> mVideosAll;

    VideoListAdapter(ArrayList<Video> mVideos, Context context) {
        this.mVideos = mVideos;
        this.mContext = context;
        this.mVideosAll = new ArrayList<>(mVideos);
    }

    /**
     * @param parent - the current view. it will inflate the card layout on top.
     * @return view with card layout
     */
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_video_items, parent, false);
        return new ViewHolder(view);
    }

    /**
     * @param holder   - the view with all the variables to be able to change them for each item.
     * @param position - the current position similar to looping.
     */
    @Override
    public void onBindViewHolder(VideoListAdapter.ViewHolder holder, int position) {
        holder.mName.setText(mVideos.get(position).getName());
        holder.mDescription.setText(mVideos.get(position).getDesc());
        holder.mImageView.setImageResource(mVideos.get(position).getImage());
        holder.mTime.setText(mVideos.get(position).getTime());

        holder.itemView.setOnClickListener(v -> IntentUtils.invokeVideoView(mContext, VideoPlayerActivity.class, "VIDEO_NAME", mVideos.get(position).getExtra()));

        if(holder.mName.getText() == "NO RESULTS FOUND...") {
            holder.itemView.setClickable(false);
            holder.mTime.setVisibility(View.GONE);
            holder.mImageView.setVisibility(View.GONE);
        } else {
            holder.itemView.setClickable(true);
            holder.mTime.setVisibility(View.VISIBLE);
            holder.mImageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @return size of array
     */
    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    @Override
    public Filter getFilter() {
        return videoFilter;
    }

    private Filter videoFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Video> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0 ){
                filteredList.addAll(mVideosAll);
            } else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Video item : mVideosAll){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    filteredList.add(new Video("NO RESULTS FOUND...", "",R.drawable.temp_man_running,"","",""));

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mVideos.clear();
            mVideos.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    /**
     * inner class that extends the RecyclerView.
     * it defines variables to the id of each element in the card layout.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDescription;
        private ImageView mImageView;
        private TextView mTime;
        public TextView mNoResults;

        ViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.post_title);
            mDescription = itemView.findViewById(R.id.post_desc);
            mImageView = itemView.findViewById(R.id.post_image);
            mTime = itemView.findViewById(R.id.post_time);
            mNoResults = itemView.findViewById(R.id.no_results);
        }

    }
}