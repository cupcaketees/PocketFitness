package uk.ac.tees.cupcake.file;

import android.os.Environment;

public class FilePathImages {

    public final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public final String PICTURES = ROOT_DIR + "/Pictures";

    public final String MESSENGER = ROOT_DIR + "/Pictures/Messenger";

    public final String INSTAGRAM = ROOT_DIR + "/Pictures/Instagram";

    public final String SCREENSHOTS = ROOT_DIR +"/DCIM/Screenshots";


    public final String CAMERA = ROOT_DIR + "/DCIM/Camera";

}
