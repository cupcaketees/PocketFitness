package uk.ac.tees.cupcake.videoplayer;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import uk.ac.tees.cupcake.R;

/**
 * VideoPlayer Activity
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class VideoPlayerActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayerActivity";

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private MediaSource mVideoSource;

    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;
    private boolean mExoPlayerFullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onStart");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exo_activity_video_player);
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //initialiseView();
        Log.d(TAG, "onCreate: onEnd");
    }

    /**
     * @param outState - ensures all states are saved so when returned they can continue where they left off
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: onStart");
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        Log.d(TAG, "onSaveInstanceState: Resume position: " + mResumePosition + "\n Resume Window: " + mResumeWindow + "\n isFullscreen: " + mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }

    /**
     * When fullscreen disables all bars
     * If clicked button go to {@link #closeFullscreenDialog()} to leave fullscreen.
     */
    private void initFullscreenDialog() {
        Log.d(TAG, "initFullscreenDialog: onStart");
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
        Log.d(TAG, "initFullscreenDialog: onEnd");
    }

    /**
     * @param newConfig - fixes when orientation is changed it doesn't reset the video.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged orientation: " + newConfig);
        super.onConfigurationChanged(newConfig);
    }

    /**
     * When fullscreen removes all other layouts. changes the drawable.
     */
    private void openFullscreenDialog() {
        Log.d(TAG, "openFullscreenDialog: onStart");
        ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        mFullScreenDialog.addContentView(mSimpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) mSimpleExoPlayerView.getParent()).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.ic_fullscreen_shrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
        Log.d(TAG, "openFullscreenDialog: onEnd");
    }

    /**
     * When minimised restores original layout. changes the drawable back to original.
     */
    private void closeFullscreenDialog() {
        Log.d(TAG, "closeFullscreenDialog: onStart");
        ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_frame)).addView(mSimpleExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.ic_fullscreen_expand));
        Log.d(TAG, "closeFullscreenDialog: onEnd");
    }

    /**
     * configures the fullscreen button
     * checks if fullscreen if it isn't navigates to {@link #openFullscreenDialog()} otherwise it goes to {@link #closeFullscreenDialog()}
     */
    private void initFullscreenButton() {
        Log.d(TAG, "initFullscreenButton: onStart");
        PlaybackControlView controlView = mSimpleExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        FrameLayout mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        Log.d(TAG, "initFullscreenButton: isFullscreen: " + mExoPlayerFullscreen);
        mFullScreenButton.setOnClickListener(v -> {
            if (!mExoPlayerFullscreen)
                openFullscreenDialog();
            else
                closeFullscreenDialog();
        });
        Log.d(TAG, "initFullscreenButton: onEnd");
    }

    /**
     * Initialises the exo player
     */
    private void initExoPlayer() {
        Log.d(TAG, "initExoPlayer: onStart");
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mSimpleExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mSimpleExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        mSimpleExoPlayerView.getPlayer().prepare(mVideoSource);
        mSimpleExoPlayerView.getPlayer().setPlayWhenReady(true);
        Log.d(TAG, "initExoPlayer: onEnd");
    }

    /**
     * when this page gets reopened it will continue the video where it left off.
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: onStart");
        super.onResume();

        if (mSimpleExoPlayerView == null) {

            mSimpleExoPlayerView = findViewById(R.id.exoplayer);
            initFullscreenDialog();
            initFullscreenButton();
            String streamUrl = getIntent().getStringExtra("VIDEO_NAME");
            //String streamUrl = "http://techslides.com/demos/sample-videos/small.mp4";
            String userAgent = Util.getUserAgent(VideoPlayerActivity.this, getApplicationContext().getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(VideoPlayerActivity.this, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(streamUrl);
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            //MP4 PLAYER
            mVideoSource = new ExtractorMediaSource(daUri, dataSourceFactory, extractorsFactory, null, null);
            //OTHER TYPE OF PLAYER
//            mVideoSource = new HlsMediaSource(daUri, dataSourceFactory, 1, null, null);
        }

        initExoPlayer();

        //if it gets left off fullscreen
        if (mExoPlayerFullscreen) {
            ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
            mFullScreenDialog.addContentView(mSimpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.ic_fullscreen_shrink));
            mFullScreenDialog.show();
        }

        Log.d(TAG, "onResume: onEnd");
    }

    /**
     * What happens when the video is paused gets the current time of the video
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: onStart");
        super.onPause();

        if (mSimpleExoPlayerView != null && mSimpleExoPlayerView.getPlayer() != null) {
            mResumeWindow = mSimpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mSimpleExoPlayerView.getPlayer().getContentPosition());

            mSimpleExoPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();

        Log.d(TAG, "onPause: onEnd");
    }
}