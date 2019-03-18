package uk.ac.tees.cupcake.file;

import android.os.Environment;

public final class FilePathImages {

    private FilePathImages() {
        // prevents instantiation.
    }
    
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public static final String PICTURES = ROOT_DIR + "/Pictures";

    public static final String MESSENGER = ROOT_DIR + "/Pictures/Messenger";

    public static final String INSTAGRAM = ROOT_DIR + "/Pictures/Instagram";

    public static final String SCREENSHOTS = ROOT_DIR +"/DCIM/Screenshots";
    
    public static final String CAMERA = ROOT_DIR + "/DCIM/Camera";

}
