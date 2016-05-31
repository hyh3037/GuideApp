package com.jyyl.jinyou.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
     * 将Bitmap保存到指定目录下，并且指定好文件名
     *
     * @param bitmap
     * @param folder
     * @param fileName 指定的文件名包含后缀
     * @return 保存成功，返回其对应的File，保存失败则返回null
     */
    public static File saveToFile(Bitmap bitmap, File folder, String fileName) {
        if (bitmap != null) {
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder, fileName + ".jpg");
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 显示图片到相册
     *
     * @param context
     * @param photoFile 要保存的图片文件
     */
    public static void displayToGallery(Context context, File photoFile) {
        if (photoFile == null || !photoFile.exists()) {
            return;
        }
        String photoPath = photoFile.getAbsolutePath();
        String photoName = photoFile.getName();
        // 其次把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
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
     * Google官方代码，计算合适的采样率
     * Calculate an inSampleSize for use in a {@link android.graphics.BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link android.graphics.BitmapFactory}. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and height.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
        // END_INCLUDE (calculate_sample_size)
    }
    /**=========================================压缩END============================================*/


    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }

}
