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

public class SearchUserFriendsActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_following);

        mViewPager = findViewById(R.id.userFriendsViewPager);
        setupFragments();
    }

    private void setupFragments() {

        ImageView backArrow = findViewById(R.id.backArrow);
        TextView shareButton = findViewById(R.id.postFinalise);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Friends");

        shareButton.setVisibility(View.GONE);

        backArrow.setOnClickListener(v -> finish());

        SectionsPagerAdapter postPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(new FollowingFragment(), new FollowersFragment(), new FollowerRequestsFragment()));

        mViewPager.setAdapter(postPagerAdapter);

        mViewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("intent")));

        TabLayout tabLayout = findViewById(R.id.tabLayoutFriends);
        tabLayout.setupWithViewPager(mViewPager);

        FirebaseFirestore.getInstance().collection("Users/" + getIntent().getStringExtra("id")+ "/FollowerRequests/").addSnapshotListener((documentSnapshots, e) -> {
            if (documentSnapshots == null || documentSnapshots.isEmpty()) {
                tabLayout.getTabAt(2).setText("Follow Requests: 0");
            } else {
                tabLayout.getTabAt(2).setText("Follow Requests: " + String.valueOf(documentSnapshots.size()));
            }
        });

        tabLayout.getTabAt(0).setText(getIntent().getStringExtra("Following"));
        tabLayout.getTabAt(1).setText(getIntent().getStringExtra("Followers"));


    }
}
