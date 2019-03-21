package uk.ac.tees.cupcake.workouts;

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
 * Bodybuilding Parts Adapter
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingPartsAdapter extends RecyclerView.Adapter<BodybuildingPartsAdapter.ViewHolder> {

    private final ArrayList<Bodypart> mBodypart;
    private final Context mContext;

    public BodybuildingPartsAdapter(ArrayList<Bodypart> mBodypart, Context mContext)
    {
        this.mBodypart = mBodypart;
        this.mContext = mContext;
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
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(view -> {
            switch (position) {
                case 0:
                    IntentUtils.invokeBaseView(mContext, BodybuildingArmsActivity.class);
                    break;
                case 1:
                    IntentUtils.invokeBaseView(mContext, BodybuildingBackActivity.class);
                    break;
                case 2:
                    IntentUtils.invokeBaseView(mContext, BodybuildingChestActivity.class);
                    break;
                case 3:
                    IntentUtils.invokeBaseView(mContext, BodybuildingLegsActivity.class);
                    break;
                case 4:
                    IntentUtils.invokeBaseView(mContext, BodybuildingShouldersActivity.class);
                    break;
                default:
                    break;
            }
        });

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
