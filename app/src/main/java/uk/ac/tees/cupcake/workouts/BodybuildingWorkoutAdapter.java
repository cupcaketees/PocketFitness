package uk.ac.tees.cupcake.workouts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workouts Adapter
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingWorkoutAdapter extends RecyclerView.Adapter<BodybuildingWorkoutAdapter.ViewHolder> {

     final ArrayList<Workout> mWorkout;

    public BodybuildingWorkoutAdapter(ArrayList<Workout> mWorkout) {
        this.mWorkout = mWorkout;
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
        holder.mImageView.setImageResource(mWorkout.get(position).getImage());
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
