package uk.ac.tees.cupcake.posts;

import android.content.Intent;
import android.graphics.Bitmap;
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

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.IntentUtils;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

public class CameraFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 5;

    View view;
    Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        button = view.findViewById(R.id.button_camera);
        initialiseButton();
        return view;
    }

    public void initialiseButton() {

        button.setOnClickListener(v -> {
            Log.d(TAG, "initialiseButton: Launching Camera");
            if (((PostActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                if (PermissionCheck.checkPermissions(Permissions.CAMERA_PERMISSSION[0], getContext())) {
                    Log.d(TAG, "initialiseButton: starting camera");
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                } else {
                    IntentUtils.invokeFromFragmentView(getActivity(), PostActivity.class);
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: picture taken");

            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");
            IntentUtils.invokePhotoView(getActivity(), FinalisePost.class, bitmap);
        }
    }

}
