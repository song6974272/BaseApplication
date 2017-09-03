package com.sens.baseapplication.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by SensYang on 2017/9/2 0002.
 */

public class FileUtils {
    /**
     * 复制asset文件到指定目录
     *
     * @param assetsPath asset下的路径
     * @param newPath    SD卡下保存路径
     */
    public static void copyAssets(Context context, String assetsPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyAssets(context, assetsPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                File file = new File(newPath);
                if (file.exists()) {
                    return;
                }
                if (file.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int byteCount = 0;
                    while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                        // buffer字节
                        fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                    }
                    fos.flush();// 刷新缓冲区
                    is.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
