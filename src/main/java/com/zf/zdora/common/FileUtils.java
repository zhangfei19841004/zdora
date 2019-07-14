package com.zf.zdora.common;

import java.io.File;
import java.util.List;

/**
 * Created by zhangfei on 2019/7/14.
 */
public class FileUtils {

    public static void getAllFilePaths(List<String> paths, String path) {
        File file = new File(path);
        if(file.isFile()){
            paths.add(file.getAbsolutePath());
        }else{
            File[] files = file.listFiles(t -> !t.isHidden() && (t.isFile() || (t.isDirectory() && !t.getName().startsWith("."))));
            for (File f : files) {
                if (f.isFile()) {
                    paths.add(f.getAbsolutePath());
                } else {
                    getAllFilePaths(paths, f.getAbsolutePath());
                }
            }
        }
    }

}
