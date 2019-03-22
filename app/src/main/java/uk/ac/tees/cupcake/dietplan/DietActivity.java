package uk.ac.tees.cupcake.dietplan;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.DietListAdapter;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

/**
 * Diet Activity
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class DietActivity extends NavigationBarActivity {
    private static final String TAG = "DietActivity";

    @Override
    public void setup() {

        stub.setLayoutResource(R.layout.activity_video_list);
        stub.inflate();
        setupDays();
    }

    /**
     * Initialise Recycler View add diets to then be added.
     */
    public void setupDays() {
        RecyclerView mRecyclerView = findViewById(R.id.myRecycleView);
        ArrayList<Diet> mDiets = new ArrayList<>();

        mDiets.add(new Diet("Day: 1","Mushroom barley soup", "kcal: 174", "70 Minutes","Mexican penne with avocado","kcal: 495", "30 Minutes","Lean turkey burger with sweet potato wedges", "kcal: 428", "40 Minutes"));
        mDiets.add(new Diet("Day: 2","Tomato soup with pasta", "kcal: 187", "40 Minutes","Moroccan chicken stew","kcal: 348", "55 Minutes","Silvana's Mediterranean & basil pasta", "kcal: 452", "35 Minutes"));
        mDiets.add(new Diet("Day: 3","Store-cupboard pasta salad", "kcal: 189", "5 Minutes","Herb & garlic baked cod","kcal: 409", "30 Minutes","Ratatouille pasta salad with rocket", "kcal: 301", "13 Minutes"));
        mDiets.add(new Diet("Day: 4","Chunky minestrone soup", "kcal: 420", "45 Minutes","Double bean & roasted pepper chilli","kcal: 327", "105 Minutes","Pizza pasta salad", "kcal: 547", "25 Minutes"));
        mDiets.add(new Diet("Day: 5","Green bean minestrone", "kcal: 260", "50 Minutes","Vegan shepherd’s pie","kcal: 348", "110 Minutes","Roasted ratatouille pasta", "kcal: 450", "45 Minutes"));
        mDiets.add(new Diet("Day: 6","Tortellini & pesto minestrone", "kcal: 493", "10 Minutes","Meatballs with fennel & balsamic beans & courgette noodles","kcal: 380", "75 Minutes","Prawn, dill & cucumber pasta", "kcal: 370", "22 Minutes"));
        mDiets.add(new Diet("Day: 7","Hot pasta salad", "kcal: 476", "20 Minutes","Pomegranate chicken with almond couscous","kcal: 590", "25 Minutes","Vitality veggie pasta", "kcal: 377", "25 Minutes"));
        mDiets.add(new Diet("Day: 8","Pasta shells with broccoli & anchovies", "kcal: 426", "20 Minutes","Duck ragu with pappardelle & swede","kcal: 471", "110 Minutes","Tomato & aubergine pasta", "kcal: 459", "30 Minutes"));
        mDiets.add(new Diet("Day: 9","Pesto & Parmesan spaghetti", "kcal: 336", "5 Minutes","Slow cooker lasagne","kcal: 448", "255 Minutes","Courgette, broccoli & gremolata pasta", "kcal: 390", "30 Minutes"));
        mDiets.add(new Diet("Day: 10","10-minute tortellini", "kcal: 482", "10 Minutes","Garlicky mushroom penne","kcal: 436", "35 Minutes","Spicy tuna pasta", "kcal: 552", "25 Minutes"));
        mDiets.add(new Diet("Day: 11","Noodle bowl salad", "kcal: 476", "20 Minutes","Fennel spaghetti","kcal: 321", "45 Minutes","Roasted pepper sauce for pasta or chicken", "kcal: 115", "35 Minutes"));
        mDiets.add(new Diet("Day: 12","Hot pasta salad", "kcal: 476", "20 Minutes","Sardine pasta with crunchy parsley crumbs","kcal: 536", "35 Minutes","Mild chilli & bean pasta bake", "kcal: 675", "55 Minutes"));
        mDiets.add(new Diet("Day: 13","Store-cupboard pasta salad", "kcal: 189", "5 Minutes","Roast pepper pesto with pasta","kcal: 636", "25 Minutes","Salmon pasta salad with lemon & capers", "kcal: 475", "30 Minutes"));
        mDiets.add(new Diet("Day: 14","Green bean minestrone", "kcal: 260", "50 Minutes","Lentil kofta with orzo & feta","kcal: 598", "55 Minutes","Pea pesto with pasta shapes", "kcal: 103", "15 Minutes"));
        mDiets.add(new Diet("Day: 15","Mushroom barley soup", "kcal: 174", "70 Minutes","Super smoky bacon & tomato spaghetti","kcal: 500", "25 Minutes","Broccoli pasta salad with salmon & sunflower seeds", "kcal: 590", "20 Minutes"));
        mDiets.add(new Diet("Day: 16","Hot pasta salad", "kcal: 476", "20 Minutes","Tuna pasta with rocket & parsley pesto","kcal: 494", "25 Minutes","Pasta salad with tuna, capers & balsamic dressing", "kcal: 527", "20 Minutes"));
        mDiets.add(new Diet("Day: 17","Mushroom barley soup", "kcal: 174", "70 Minutes","Roasted red pepper & parsley pesto with penne","kcal: 495", "15 Minutes","Broad bean, pea & orzo salad", "kcal: 334", "20 Minutes"));
        mDiets.add(new Diet("Day: 18","Pasta shells with broccoli & anchovies", "kcal: 426", "20 Minutes","Cherry tomato, kale, ricotta & pesto pasta","kcal: 641", "25 Minutes","Punchy spaghetti", "kcal: 436", "25 Minutes"));
        mDiets.add(new Diet("Day: 19","Green bean minestrone", "kcal: 260", "50 Minutes","Spaghetti Bolognese","kcal: 608", "30 Minutes","Punchy spaghetti", "kcal: 4228", "25 Minutes"));
        mDiets.add(new Diet("Day: 20","Pesto & Parmesan spaghetti", "kcal: 336", "5 Minutes","Lemon spaghetti with tuna & broccoli","kcal: 440", "15 Minutes","Garlicky mushroom penne", "kcal: 436", "35 Minutes"));
        mDiets.add(new Diet("Day: 21","Tortellini & pesto minestrone", "kcal: 493", "10 Minutes","Tofu & spinach cannelloni","kcal: 284", "85 Minutes","Spaghetti with lemon, Parmesan & peas", "kcal: 420", "30 Minutes"));
        mDiets.add(new Diet("Day: 22","Chunky minestrone soup", "kcal: 420", "45 Minutes","Crab linguine with chilli & parsley","kcal: 546", "30 Minutes","Crab & asparagus pappardelle", "kcal: 576", "25 Minutes"));
        mDiets.add(new Diet("Day: 23","Noodle bowl salad", "kcal: 476", "20 Minutes","Homemade pappardelle with crab & broad beans","kcal: 416", "75 Minutes","Squash & spinach fusilli with pecans", "kcal: 353", "50 Minutes"));
        mDiets.add(new Diet("Day: 24","Tomato soup with pasta", "kcal: 187", "40 Minutes","Rigatoni with spiced prawns, tomatoes & chorizo","kcal: 462", "45 Minutes","Linguine with asparagus & egg", "kcal: 500", "35 Minutes"));
        mDiets.add(new Diet("Day: 25","Pesto & Parmesan spaghetti", "kcal: 336", "5 Minutes","Broccoli pesto pasta","kcal: 604", "20 Minutes","Creamy linguine with ham, lemon & basil", "kcal: 349", "25 Minutes"));
        mDiets.add(new Diet("Day: 26","Pasta shells with broccoli & anchovies", "kcal: 426", "20 Minutes","Vegetable pasta","kcal: 323", "45 Minutes","Linguine with avocado, tomato & lime", "kcal: 450", "30 Minutes"));
        mDiets.add(new Diet("Day: 27","Store-cupboard pasta salad", "kcal: 189", "5 Minutes","Lazy summer pasta","kcal: 460", "55 Minutes","Frying pan pizza", "kcal: 331", "45 Minutes"));
        mDiets.add(new Diet("Day: 28","Hot pasta salad", "kcal: 476", "20 Minutes","Tagliatelle with grilled chicken & tomatoes","kcal: 423", "25 Minutes","Lean turkey burger with sweet potato wedges", "kcal: 428", "40 Minutes"));
        mDiets.add(new Diet("Day: 29","Mushroom barley soup", "kcal: 174", "70 Minutes","Creamy pasta with asparagus & peas","kcal: 658", "17 Minutes","Roasted pepper sauce for pasta or chicken", "kcal: 530", "28 Minutes"));
        mDiets.add(new Diet("Day: 30","10-minute tortellini", "kcal: 482", "10 Minutes","Smoked trout & pea pasta","kcal: 445", "20 Minutes","Pasta salad with tuna, capers & balsamic dressing", "kcal: 527", "20 Minutes"));

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new DietListAdapter(mDiets, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}
