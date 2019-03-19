package uk.ac.tees.cupcake.videoplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * VideoList Adapter
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private ArrayList<Video> mVideos;
    private final Context mContext;

    VideoListAdapter(ArrayList<Video> mVideos, Context context) {
        this.mVideos = mVideos;
        this.mContext = context;
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
    }

    /**
     * @return size of array
     */
    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    /**
     * inner class that extends the RecyclerView.
     * it defines variables to the id of each element in the card layout.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDescription;
        private ImageView mImageView;
        private TextView mTime;

        ViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.post_title);
            mDescription = itemView.findViewById(R.id.post_desc);
            mImageView = itemView.findViewById(R.id.post_image);
            mTime = itemView.findViewById(R.id.post_time);
        }

    }
}