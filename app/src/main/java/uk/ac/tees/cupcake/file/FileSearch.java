package uk.ac.tees.cupcake.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FileSearch {

    /**
     * Returns an {@link ArrayList} of directories.
     *
     * @param dir the parent directory.
     * @return list of directories within the specified dir
     */
    public static List<String> getDirectories(String dir) {
        ArrayList<String> arrayPath = new ArrayList<>();
        File[] fileList = new File(dir).listFiles();
        if (fileList != null) {
            for (File listFile : fileList) {
                if (listFile.isDirectory()) {
                    arrayPath.add(listFile.getAbsolutePath());
                }
            }
        }
        return arrayPath;
    }

    /**
     *
     * @param dir
     * @return all the items in a directory
     */
    public static List<String> getFiles(String dir) {
        ArrayList<String> arrayPath = new ArrayList<>();
        File[] fileList = new File(dir).listFiles();
        if (fileList != null) {
            for (File listFile : fileList) {
                if (listFile.isFile()) {
                    arrayPath.add(listFile.getAbsolutePath());
                }
            }
        }
        return arrayPath;
    }
}