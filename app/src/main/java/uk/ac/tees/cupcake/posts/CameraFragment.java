package uk.ac.tees.cupcake.posts;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.IntentUtils;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

/**
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class CameraFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;

    private View view;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        initialiseButton();
        return view;
    }

    /**
     * Starts Camera, when picture is confirmed it'll store the image on the device.
     */
    public void initialiseButton() {
        Button openCamera = view.findViewById(R.id.button_camera);

        openCamera.setOnClickListener(v -> {
            Log.d(TAG, "initialiseButton: Launching Camera");
            if (((PostActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                if (PermissionCheck.checkPermissions(Permissions.CAMERA_PERMISSSION[0], getContext())) {
                    Log.d(TAG, "initialiseButton: starting camera");
                    retrieveUri();
                } else {
                    IntentUtils.invokeFromFragmentView(getActivity(), PostActivity.class);
                }
            }
        });
    }

    /**
     * Stores in a ContentValue (Just a way to store items) but its what the Uri requires.
     * putExtra put's it on your phone allowing for better quality.
     */
    public void retrieveUri() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Cupcake Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image From Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, GALLERY_FRAGMENT_NUM);
    }

    /**
     *
     * @param requestCode = ensures that the request code from who this result came from.
     * @param resultCode = checks they didn't press the back button
     * Returns if they press the back button or if random error occurs.
     * Otherwise passes the URI to {@link FinalisePost} to post the image
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0) {
            IntentUtils.invokeBaseView(getActivity(), PostActivity.class);
        } else if (requestCode == GALLERY_FRAGMENT_NUM) {
            IntentUtils.invokePhotoView(getActivity(),FinalisePost.class, imageUri);
        } else {
            Toast.makeText(getActivity(), "Random Error has occurred", Toast.LENGTH_SHORT).show();
        }
    }

}