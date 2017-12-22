package com.study.yang.lifehelper.utils;

import java.io.File;

/**
 * Created by Administrator on 2017/4/18/018.
 */

public class FileUtils {
    public static void delete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            System.out.println(fileList.length);
            if (fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    delete(fileList[i]);
                }
            } else {
                file.delete();
            }
        } else {
            file.delete();
        }
        file.delete();
    }
}
