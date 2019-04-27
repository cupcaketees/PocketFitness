package uk.ac.tees.cupcake.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.dietplan.Diet;
import uk.ac.tees.cupcake.dietplan.DietDetailsActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class DietListAdapter extends RecyclerView.Adapter<DietListAdapter.ViewHolder> {

    private ArrayList<Diet> mDiets;
    private final Context mContext;

    public DietListAdapter(ArrayList<Diet> mDiets, Context context) {
        this.mDiets = mDiets;
        this.mContext = context;
    }

    /**
     * @param parent - the current view. it will inflate the card layout on top.
     * @return view with card layout
     */
    @Override
    public DietListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_food_days, parent, false);
        return new DietListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   - the view with all the variables to be able to change them for each item.
     * @param position - the current position similar to looping.
     */
    @Override
    public void onBindViewHolder(DietListAdapter.ViewHolder holder, int position) {
        holder.mDay.setText(mDiets.get(position).getDay());

        holder.itemView.setOnClickListener(v -> IntentUtils.invokeDietPlan(mContext, DietDetailsActivity.class, "DIET", mDiets.get(position)));
    }

    /**
     * @return size of array
     */
    @Override
    public int getItemCount() {
        return mDiets.size();
    }

    /**
     * inner class that extends the RecyclerView.
     * it defines variables to the id of each element in the card layout.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mDay;

        ViewHolder(View itemView) {
            super(itemView);
            mDay = itemView.findViewById(R.id.foodDayPage);
        }
    }
}