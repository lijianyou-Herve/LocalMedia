package com.example.herve.localmedia.utils.file_utils;

import com.example.herve.localmedia.bean.FileModel;
import com.example.herve.localmedia.bean.FilesGroup;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Herve on 2016/7/18.
 */
public class FileMangerUtils {

    private static String TAG = "排序";
    private FilesGroup filesGroup;

    public FileMangerUtils() {

        filesGroup = new FilesGroup();

    }

    public FilesGroup getData(String path) {

        if (filesGroup == null) {
            filesGroup = new FilesGroup();
        }

        if (filesGroup != null && filesGroup.getData() != null) {

            filesGroup.getData().clear();
        }
        searchFiles(new File(path));

        return filesGroup;
    }

    private void searchFiles(File searchPath) {

        if (searchPath.exists()) {

            if (searchPath.isDirectory()) {
                List<File> files = orderByDate(searchPath);

                if (files != null && files.size() > 0) {
                    for (File file : files) {

                        if (file.isHidden()) {

                        } else if (file.isDirectory()) {
                            FileModel fileModel = new FileModel(-1, file.getName(), file.getAbsolutePath());
                            filesGroup.setSearchPath(searchPath.getAbsolutePath());
                            filesGroup.getData().add(fileModel);
                        } else {

                            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                                FileModel fileModel = null;
                                if (file.getName().endsWith(".jpg")) {

                                    fileModel = new FileModel(0, file.getName(), file.getAbsolutePath());
                                }
                                if (file.getName().endsWith(".png")) {

                                    fileModel = new FileModel(1, file.getName(), file.getAbsolutePath());
                                }

                                filesGroup.setSearchPath(searchPath.getAbsolutePath());

                                filesGroup.getData().add(fileModel);
                            }

                            if (file.getName().endsWith(".mp4")) {

                                FileModel fileModel = new FileModel(2, file.getName(), file.getAbsolutePath());
                                filesGroup.setSearchPath(searchPath.getAbsolutePath());

                                filesGroup.getData().add(fileModel);
                            }

                        }

                    }
                }
            }

        }

    }

    public static List<File> orderByDate(File fliePath) {
        File[] fs = fliePath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {

                if (pathname.canRead() && pathname.canWrite() && !pathname.isHidden()) {

                    return true;
                }

                return false;
            }
        });

        Arrays.sort(fs, new Comparator<File>() {
            public int compare(File f1, File f2) {

                if (f1.isDirectory()) {//文件夹在最上面

                    if (f1.isDirectory() && f2.isFile())
                        return -1;

                    return f1.getName().compareTo(f2.getName());

                } else {

                    if (f2.isDirectory()) {
                        return 1;
                    }

                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return -1;
                    else if (diff == 0)
                        return 0;
                    else
                        return 1;

                }

            }

        });

        List<File> files = Arrays.asList(fs);

        return files;
    }
}
