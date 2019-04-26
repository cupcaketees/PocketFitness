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
import uk.ac.tees.cupcake.dietplan.DietActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class DietTypeAdapter extends RecyclerView.Adapter<DietTypeAdapter.ViewHolder> {

    private ArrayList<Diet> mDiets;
    private final Context mContext;

    public DietTypeAdapter( ArrayList<Diet> mDiets, Context context) {
        this.mDiets = mDiets;
        this.mContext = context;
    }

    /**
     * @param parent - the current view. it will inflate the card layout on top.
     * @return view with card layout
     */
    @Override
    public DietTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_food_diet_type, parent, false);
        return new DietTypeAdapter.ViewHolder(view);
    }

    /**
     * @param holder   - the view with all the variables to be able to change them for each item.
     * @param position - the current position similar to looping.
     */
    @Override
    public void onBindViewHolder(DietTypeAdapter.ViewHolder holder, int position) {

        holder.mName.setText(mDiets.get(position).getDietName());
        holder.mDescription.setText(mDiets.get(position).getDietDescription());

        holder.itemView.setOnClickListener(v -> IntentUtils.invokeVideoView(mContext, DietActivity.class, "DIET", Integer.toString(position)));
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

        private TextView mName;
        private TextView mDescription;

        ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.diet_title);
            mDescription = itemView.findViewById(R.id.diet_desc);

        }
    }
}
