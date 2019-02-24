package uk.ac.tees.cupcake.VideoPlayer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class NavigationDrawerAdapter implements NavigationView.OnNavigationItemSelectedListener {

    public Activity activity;

    public NavigationDrawerAdapter(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param item - that was selected (NavigationView)
     * @return - true if successful and takes to new view.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                IntentUtils.invokeBaseView(activity, HomeActivity.class);
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                IntentUtils.invokeVideoView(activity, VideoPlayerActivity.class, "VIDEO_NAME", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                break;
            default:
                return false;
        }
        return true;
    }

}
