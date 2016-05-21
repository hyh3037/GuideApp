package com.jyyl.guideapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理 工具类
 */
public class ImageUtils {
    private ImageUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 根据路径获取bitmap 默认为ARGB_8888: 每个像素4字节. 共32位。 Alpha_8: 只保存透明度，共8位，1字节。 ARGB_4444: 共16位，2字节。
     * RGB_565:共16位，2字节。
     * @param imgPath
     *
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = 1;
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }


    /**
     * 根据Uri获取Bitmap
     * @param uri
     *         Uri
     * @param mContext
     *         上下文
     *
     * @return bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            return MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存bitmap到指定文件
     * @param strFileDir
     *         文件夹路径
     * @param strFileName
     *         文件名
     * @param mBitmap
     *
     * @throws IOException
     */
    public static void saveBitmap(String strFileDir, String strFileName, Bitmap mBitmap) throws
            IOException {
        File file = FileUtils.getFile(strFileDir, strFileName);
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recycle(mBitmap);
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 回收 bitmap资源
     * @param bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
        }
    }

    /**=========================================压缩START==========================================*/
    /**
     * bitmap转Base64
     * @param bitmap
     *
     * @return
     */
    public static String bitmap2Base64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //把压缩的数据写入了outputstream流中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] byteBitmap = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteBitmap, Base64.DEFAULT);

    }

    /**
     * bitmap转byte[]
     * @param bitmap
     *
     * @return
     */
    public static byte[] bitmap2Byte(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //把压缩的数据写入了outputstream流中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();

    }


    /**
     * 质量压缩 压缩图片返回 bitmap
     * @param bitmap
     *
     * @return bitmap
     */
    public static Bitmap compressImage(Bitmap bitmap) {

        int quality = 50;   //压缩质量,一般取在30-100之间，越小表示压缩的越厉害,100表示不压缩
        int max = 200; //压缩后图片最大大小 kb

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //把压缩的数据写入了outputstream流中
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

        //循环判断如果压缩后图片是否大于 max kb,大于继续压缩
        while (byteArrayOutputStream.toByteArray().length / 1024 > max) {
            byteArrayOutputStream.reset();
            quality = quality - 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        }

        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream
                .toByteArray());

        //把ByteArrayInputStream数据生成图片返回
        return BitmapFactory.decodeStream(inputStream, null, null);
    }


    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     * @param filePath
     *
     * @return
     */
    public static Bitmap getCompressBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        options.inJustDecodeBounds = false;

        //设置缩放比例,大于1表示缩小了原来的多少,如果小于1则和1相同
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(filePath, options);

        return bitmap;

    }


    /**
     * 根据bitmap获得图片并压缩返回bitmap
     * @param imageBitmap
     *
     * @return
     */
    public static Bitmap getCompressBitmap(Bitmap imageBitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //判断如果图片大于1M,进行压缩避免在生成图片
        //（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();//重置baos即清空baos
            //这里压缩50%，把压缩后的数据存放到baos中
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();

        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds = true;
        //此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

        options.inJustDecodeBounds = false;

        //设置缩放比例,大于1表示缩小了原来的多少,如果小于1则和1相同
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        inputStream = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(inputStream, null, options);

        return bitmap;

    }

    /**
     * 计算图片的缩放值
     * @param options
     * @param reqWidth
     * @param reqHeight
     *
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1; //1表示不缩放
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
    /**=========================================压缩END============================================*/


    /**
     * @param filename
     *
     * @return
     */
    private static int readImageExif(String filename) {
        int orientation = -2;
        int res = 0;
        try {
            ExifInterface exif = new ExifInterface(filename);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -2);
            Log.e("Exif", "Orientation: " + orientation);
            //根据图片头部信息判断需要旋转的角度，
            //在三星的手机里，会自动在Exif头信息里写入旋转角度，拍照时设置ratation是无效的
            switch (orientation) {
                case 3:
                    res = 180;
                    break;
                case 6:
                    res = 90;
                    break;
                case 8:
                    res = 270;
                    break;
            }
        } catch (Exception e) {
            Log.e("Exif", "No Exif info");
        }
        return res;
    }

}
