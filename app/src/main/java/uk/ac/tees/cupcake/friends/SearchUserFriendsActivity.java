package uk.ac.tees.cupcake.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;

/**
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class SearchUserFriendsActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_following);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Friends");


        mViewPager = findViewById(R.id.userFriendsViewPager);
        setupFragments();
    }

    /**
     *  Initialises the fragments & Viewpager.
     *  Gets the values for the followers/followings and displays it on the viewpager.
     */
    private void setupFragments() {
        SectionsPagerAdapter postPagerAdapter;

        ImageView backArrow = findViewById(R.id.backArrow);

        TextView shareButton = findViewById(R.id.postFinalise);
        shareButton.setVisibility(View.GONE);

        TabLayout tabLayout = findViewById(R.id.tabLayoutFriends);
        tabLayout.setupWithViewPager(mViewPager);

        backArrow.setOnClickListener(v -> finish());

        if (getIntent().getStringExtra("id").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            postPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                    Arrays.asList(new FollowingFragment(), new FollowersFragment(), new FollowerRequestsFragment()));

            FirebaseFirestore.getInstance().collection("Users/" + getIntent().getStringExtra("id") + "/FollowerRequests/").addSnapshotListener((documentSnapshots, e) -> {
                if (documentSnapshots == null || documentSnapshots.isEmpty()) {
                    tabLayout.getTabAt(2).setText("Follow Requests: 0");
                } else {
                    tabLayout.getTabAt(2).setText("Follow Requests: " + String.valueOf(documentSnapshots.size()));
                }
            });
        } else {
            postPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                    Arrays.asList(new FollowingFragment(), new FollowersFragment()));
        }
        mViewPager.setAdapter(postPagerAdapter);

        mViewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("intent")));

        tabLayout.getTabAt(0).setText(getIntent().getStringExtra("Following"));
        tabLayout.getTabAt(1).setText(getIntent().getStringExtra("Followers"));

    }

}
