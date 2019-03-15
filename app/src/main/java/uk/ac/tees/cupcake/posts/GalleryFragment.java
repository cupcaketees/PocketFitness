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

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.GridImageAdapter;
import uk.ac.tees.cupcake.file.FilePathImages;
import uk.ac.tees.cupcake.file.FileSearch;
import uk.ac.tees.cupcake.home.HomeActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";

    //Widgets
    private GridView gridView;
    private ImageView imageGallery;
    private String mSelectedImage;
    private ProgressBar mProgressBar;
    private Spinner dirSpinner;
    private View view;

    private ArrayList<String> directories;
    private static final int NUM_GRID_COLUMNS = 3;
    private String mAppend = "file:/";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        imageGallery = view.findViewById(R.id.galleryImageView);
        gridView = view.findViewById(R.id.gridView);
        dirSpinner = view.findViewById(R.id.spinnerDirectories);
        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        initialiseToolbar();
        init();

        return view;
    }

    private void initialiseToolbar() {
        ImageView shareClose = view.findViewById(R.id.exitPost);
        shareClose.setOnClickListener(v -> {
            Log.d(TAG, "initialiseView: closing gallery fragment");
            IntentUtils.invokeBaseView(getActivity(), HomeActivity.class);
//              getActivity().finish();
        });

        TextView nextFragment = view.findViewById(R.id.postNext);
        nextFragment.setOnClickListener(v -> {
            Log.d(TAG, "initialiseView: navigate to finalise post");
            IntentUtils.invokeVideoView(getActivity(), FinalisePost.class, "Selected_image",mSelectedImage);
        });




    }

    private void init() {
        FilePathImages filePathImages = new FilePathImages();

        if (FileSearch.getDirectoryPath(filePathImages.PICTURES) != null) {
            directories = FileSearch.getDirectoryPath(filePathImages.CAMERA);
        }

        directories.add(filePathImages.CAMERA);
        directories.add(filePathImages.SCREENSHOTS);
        directories.add(filePathImages.MESSENGER);
        directories.add(filePathImages.INSTAGRAM);

        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            int index = directories.get(i).lastIndexOf("/");
            String substring = directories.get(i).substring(index).replace("/", "");
            directoryNames.add(substring);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dirSpinner.setAdapter(adapter);

        dirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        final ArrayList<String> imageURLS = FileSearch.getFilePaths(selectedDirectory);

        Collections.reverse(imageURLS);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;

        gridView.setColumnWidth(imageWidth);
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.create_post_layout_gridview, mAppend, imageURLS);
        gridView.setAdapter(adapter);

        setImage(imageURLS.get(0), imageGallery, mAppend);
        mSelectedImage = imageURLS.get(0);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: selected image" + imageURLS.get(position));
            setImage(imageURLS.get(position), imageGallery, mAppend);
            mSelectedImage = imageURLS.get(position);
        });
    }


    private void setImage(String imgURL, ImageView image, String append) {
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