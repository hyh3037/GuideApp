package com.jyyl.jinyou.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtils {


    /**
     * 随机生成32位字符串
     * @return
     */
    public static String getUuidName(){
        return UUID.randomUUID().toString().trim().replace("-", "");
    }

    /**
     * 指定目录创建jpg文件，并获取uri
     * @param strFileDir
     * @return
     * @throws IOException
     */
    public static Uri getUriByFileDir(String strFileDir) throws IOException {
        String strFileName = getUuidName()+".jpg";
        Uri uri = null;
        uri = getUriByFileDirAndFileName(strFileDir, strFileName);
        return uri;
    }

    /****
     * 通过目录和文件名来创建/获取Uri
     * @param strFileDir   目录
     * @param strFileName   文件名
     * @return  Uri
     * @throws IOException  IO异常
     */
    public static Uri getUriByFileDirAndFileName(String strFileDir,String strFileName) throws IOException {
        Uri uri = null;
        File file = getFile(strFileDir, strFileName);
        uri = Uri.fromFile(file);  //获取Uri
        return uri;
    }

    /**
     * 检查是否存在SD卡
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过Uri返回File文件
     * 注意：通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     * @param uri   Uri
     * @param context  上下文对象  Activity
     * @return   File
     */
    public static File getFileByUri(Activity context,Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    Log.e("FileUtils","temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        }
        return null;
    }

    /**
     * 获取文件
     * @param strFileDir 目录名
     * @param strFileName 文件名
     * @return
     * @throws IOException
     */
    @NonNull
    public static File getFile(String strFileDir, String strFileName) throws IOException {
        File fileDir = new File(Environment.getExternalStorageDirectory(), strFileDir);  //定义目录
        if (!fileDir.exists()) {   //判断目录是否存在
            fileDir.mkdirs();      //如果不存在则先创建目录
        }
        File file = new File(fileDir, strFileName);   //定义文件
        if (!file.exists()) {  //判断文件是否存在
            file.createNewFile();    //如果不存在则先创建文件
        }
        return file;
    }

    /**
     * 把一个文件转化为字节
     * @param file
     * @return   byte[]
     * @throws Exception
     */
    public static byte[] getByte(File file) throws Exception
    {
        byte[] bytes = null;
        if(file!=null)
        {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if(length>Integer.MAX_VALUE)   //当文件的长度超过了int的最大值
            {
                Log.e("FileUtils","this file is max ");
                return null;
            }
            bytes = new byte[length];
            int offset = 0;
            int numRead = 0;
            while(offset<bytes.length&&(numRead=is.read(bytes,offset,bytes.length-offset))>=0)
            {
                offset+=numRead;
            }
            //如果得到的字节长度和file实际的长度不一致就可能出错了
            if(offset<bytes.length)
            {
                Log.e("FileUtils","file length is error ");
                return null;
            }
            is.close();
        }
        return bytes;
    }
}
