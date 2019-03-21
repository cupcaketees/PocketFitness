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
public class BodybuildingPartsAdapter extends RecyclerView.Adapter<BodybuildingPartsAdapter.ViewHolder> {

    private final ArrayList<Bodypart> mBodypart;

    public BodybuildingPartsAdapter(ArrayList<Bodypart> mBodypart)
    {
        this.mBodypart = mBodypart;
    }

    @Override
    public BodybuildingPartsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bodybuilding_workout_types, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BodybuildingPartsAdapter.ViewHolder holder, int position)
    {
        holder.mName.setText(mBodypart.get(position).getName());
        holder.mDescription.setText(mBodypart.get(position).getDesc());
        holder.mImageView.setImageResource(mBodypart.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return mBodypart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDescription;
        private ImageView mImageView;

        public ViewHolder(View itemView) {

            super(itemView);

            mName = itemView.findViewById(R.id.post_title);
            mDescription = itemView.findViewById(R.id.post_desc);
            mImageView = itemView.findViewById(R.id.post_image);

        }
    }
}
