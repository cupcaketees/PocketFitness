package uk.ac.tees.cupcake.workouts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workouts Adapter
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingWorkoutAdapter extends RecyclerView.Adapter<BodybuildingWorkoutAdapter.ViewHolder> {

     private final ArrayList<Workout> mWorkout;
     private Context context;

    public BodybuildingWorkoutAdapter(ArrayList<Workout> mWorkout, Context context) {
        this.mWorkout = mWorkout;
        this.context = context;
    }

    @Override
    public BodybuildingWorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bodybuilding_part_workouts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mName.setText(mWorkout.get(position).getName());
        holder.mDescription.setText(mWorkout.get(position).getDesc());
        Glide.with(context)
                .load(mWorkout.get(position).getUrl())
                .into(holder.mImageView);
        holder.mDifficulty.setText(mWorkout.get(position).getDifficulty());
    }

    @Override
    public int getItemCount() {
        return mWorkout.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDescription;
        private ImageView mImageView;
        private TextView mDifficulty;

        public ViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.post_title);
            mDescription = itemView.findViewById(R.id.post_desc);
            mImageView = itemView.findViewById(R.id.post_image);
            mDifficulty = itemView.findViewById(R.id.post_difficulty);

        }
    }
}
