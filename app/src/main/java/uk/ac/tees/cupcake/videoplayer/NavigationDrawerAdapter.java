package uk.ac.tees.cupcake.videoplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * NavigationDrawer Adapter
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class NavigationDrawerAdapter implements NavigationView.OnNavigationItemSelectedListener {

    private final Context context;

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
    }

    /**
     * @param item - that was selected (NavigationView)
     * @return - true if successful and takes to new view.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                IntentUtils.invokeBaseView(context, HomeActivity.class);
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                IntentUtils.invokeBaseView(context, VideoListActivity.class);
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