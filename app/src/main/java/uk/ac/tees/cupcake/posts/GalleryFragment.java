package uk.ac.tees.cupcake.posts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.GridImageAdapter;
import uk.ac.tees.cupcake.file.FilePathImages;
import uk.ac.tees.cupcake.file.FileSearch;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

/*
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";

    private GridView imageGrid;
    private ImageView imageContainer;
    private String mSelectedImage;
    private ProgressBar mProgressBar;
    private Spinner imageDirectory;
    private View view;

    private static final int NUM_GRID_COLUMNS = 4;
    private String mAppend = "file:/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        imageContainer = view.findViewById(R.id.galleryImageView);
        imageGrid = view.findViewById(R.id.gridView);
        imageDirectory = view.findViewById(R.id.spinnerDirectories);
        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        initialiseToolbar();
        init();

        return view;
    }

    private void initialiseToolbar() {
        ImageView shareClose = view.findViewById(R.id.exitPost);
        shareClose.setOnClickListener(v -> IntentUtils.invokeBaseView(getActivity(), MainActivity.class));

        TextView nextFragment = view.findViewById(R.id.postNext);
        nextFragment.setOnClickListener(v -> {
            Log.d(TAG, "initialiseView: navigate to finalise post");
           // IntentUtils.invokeVideoView(getActivity(), FinalisePost.class, "Selected_image",mSelectedImage);
        });
    }

    private void init() {
        final List<String> directories = new ArrayList<>();

        directories.add(FilePathImages.PICTURES);
        directories.add(FilePathImages.CAMERA);
        directories.add(FilePathImages.SCREENSHOTS);
        directories.add(FilePathImages.MESSENGER);
        directories.add(FilePathImages.INSTAGRAM);

        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            if (FileSearch.getFiles(directories.get(i)).isEmpty()) {
                continue;
            }

            int index = directories.get(i).lastIndexOf("/");
            String substring = directories.get(i).substring(index).replace("/", "");
            directoryNames.add(substring);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageDirectory.setAdapter(adapter);

        imageDirectory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupGridView(String selectedDirectory) {
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final List<String> imageURLS = FileSearch.getFiles(selectedDirectory);

        if (imageURLS.isEmpty()) {
            return;
        }

        Collections.reverse(imageURLS);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;

        imageGrid.setColumnWidth(imageWidth);
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.create_post_layout_gridview, mAppend, imageURLS);
        imageGrid.setAdapter(adapter);

        setImage(imageURLS.get(0), imageContainer);
        mSelectedImage = imageURLS.get(0);

        imageGrid.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: selected image" + imageURLS.get(position));
            setImage(imageURLS.get(position), imageContainer);
            mSelectedImage = imageURLS.get(position);
        });
    }

    private void setImage(String imgURL, ImageView image) {
        Log.d(TAG, "setImage: setting image");

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mAppend + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}