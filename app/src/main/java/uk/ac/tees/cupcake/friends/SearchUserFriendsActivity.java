package uk.ac.tees.cupcake.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

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

    private void setupFragments(){

        ImageView backArrow = findViewById(R.id.backArrow);
        TextView shareButton = findViewById(R.id.postFinalise);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Friends");

        shareButton.setVisibility(View.GONE);

        backArrow.setOnClickListener(v -> finish());

        SectionsPagerAdapter postPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(new FollowingFragment(),new FollowersFragment()));

        mViewPager.setAdapter(postPagerAdapter);

        mViewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("intent")));

        TabLayout tabLayout = findViewById(R.id.tabLayoutFriends);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("Following: " + getIntent().getStringExtra("Following"));
        tabLayout.getTabAt(1).setText("Followers: " + getIntent().getStringExtra("Followers"));


    }

}
