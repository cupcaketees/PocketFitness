package uk.ac.tees.cupcake.file;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {

    /**
     *
     * @param dir -
     * @return - list of all directories inside
     */
    public static ArrayList<String> getDirectoryPath (String dir) {
        ArrayList<String> arrayPath = new ArrayList<>();
        File file = new File(dir);
        File[] fileList = file.listFiles();
        for (File listFile : fileList) {
            if (listFile.isDirectory()) {
                arrayPath.add(listFile.getAbsolutePath());
            }
        }
        return arrayPath;

    }

    /**
     *
     * @param dir
     * @return all the items in a directory
     */
    public static ArrayList<String> getFilePaths(String dir) {
        ArrayList<String> arrayPath = new ArrayList<>();
        File file = new File(dir);
        File[] fileList = file.listFiles();
        for (File listFile : fileList) {
            if (listFile.isFile()) {
                arrayPath.add(listFile.getAbsolutePath());
            }
        }
        return arrayPath;

    }





}
