package com.benben.commoncore.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jzg on 2016/3/2.
 */
public class BitmapUtils2 {

    private static Bitmap bitmap;

    public boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 读取图像的旋转度
    public int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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

    // 旋转图片
    public Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }


    public boolean saveImage(String path, String spath) {
        BufferedOutputStream bos = null;

        try {

            bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            Bitmap photo = getBitmap(path);
            photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean saveImage(String path, String spath, int yaSuoBi) {
        BufferedOutputStream bos = null;

        try {

            bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            Bitmap photo = getBitmap(path);
            photo.compress(Bitmap.CompressFormat.JPEG, yaSuoBi, bos);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     * 将string类型转换成Bitmap
     *
     * @param filePath
     * @return
     */
    public Bitmap getBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize1(options, 480, 800);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = rotateBitmap(readBitmapDegree(filePath), BitmapFactory.decodeFile(filePath, options));
        return bitmap;

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize1(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap getBitmapByPath(String filePath,
                                         BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }
    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize1(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }
    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getEncryptBitmap(String filePath) {
        String sdPath = filePath + ".zrjm";
        File file = new File(sdPath);
        if(file.exists()){
            boolean isSuccess = file.renameTo(new File(filePath));
            FileInputStream fis = null;
            Bitmap bitmap = null;
            try {
                if(isSuccess){
                    File newfile = new File(filePath);
                    fis = new FileInputStream(newfile);
                    bitmap = BitmapFactory.decodeStream(fis, null, null);
                    newfile.renameTo(file);
                }else{
                    bitmap = null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
            return bitmap;
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize1(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 官网：获取压缩后的图片
     * *      * @param res
     * * @param resId
     * * @param reqWidth
     * *            所需图片压缩尺寸最小宽度
     * * @param reqHeight
     * *            所需图片压缩尺寸最小高度
     * * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filepath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        options.inSampleSize = calculateInSampleSize1(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    /**
     * 　　* 将bitmap转换成base64字符串
     * <p>
     * 　　*
     * <p>
     * 　　* @param bitmap
     * <p>
     * 　　* @return base64 字符串
     * <p>
     *
     */

        public static String bitmaptoString(Bitmap bitmap)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] appicon = baos.toByteArray();// 转为byte数组
            return Base64.encodeToString(appicon, Base64.DEFAULT);

        }

    /**
     * 按指定宽度和高度缩放图片,不保证宽高比例
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */

    public static Bitmap zoomBitmap(Bitmap bitmap, float w, float h) {

        if (bitmap == null) {
            return null;

        }

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidht = (w / width);

        float scaleHeight = (h / height);

        matrix.postScale(scaleWidht, scaleHeight);

        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,

                matrix, true);

        return newbmp;

    }

    /**
     * 功能:将图片保存到sd卡
     *
     * @param bitmap    图片
     * @param path      图片文件路径，含文件名
     * @param scaleSize 压缩的百分比 0~100
     */
    public static void saveBitmapToSD(Bitmap bitmap, String path, int scaleSize) {
        File f = new File(path);

        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            Bitmap.CompressFormat t = getCompressFormat(path);
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(t, scaleSize, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
 /**
     * 功能:将图片修改文件名保存到sd卡
     *
     * @param bitmap    图片
     * @param path      图片文件路径，含文件名
     * @param scaleSize 压缩的百分比 0~100
     */
    public static void saveEncryptBitmapToSD(Bitmap bitmap, String path, int scaleSize) {
        File f = new File(path);

        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            Bitmap.CompressFormat t = getCompressFormat(path);
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(t, scaleSize, out);
            out.flush();
            out.close();
            f.renameTo(new File(path+ ".zrjm"));
//            //复制图片
//            FileUtils.copyFile(path,path+ Constants.BITMAP_SUFFIX);
//            FileUtils.deleteFile(path);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 功能:获取图片压缩格式
     *
     * @param str 文件名
     * @return
     */
    private static Bitmap.CompressFormat getCompressFormat(String str) {
        String strType = str.substring(str.lastIndexOf(".") + 1);
        Bitmap.CompressFormat t = null;
        if (strType.equals("png") || strType.equals("PNG")) {
            t = Bitmap.CompressFormat.PNG;
        } else if (strType.equals("jpg") || strType.equals("JPG")) {
            t = Bitmap.CompressFormat.JPEG;
        }
        return t;
    }

    /**
     * 图片-chicu-压缩
     * @param path
     * @return
     */
    public static File scal(String path){
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize =2 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }else{
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;

    }
    public static Uri createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }

    public static void copyFileUsingFileChannels(File source, File dest){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public static  String initTempPic(String path, String fileName){
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath() + path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirFile.getPath() + File.separator + fileName + ".jpg");

        if (file.exists()) {//如果存在，就先删除再创建
            file.delete();
        }
        return file.getAbsolutePath();
    }

    /**
     * 将bitmap转化为byte数组
     * @param bitmap
     * @return
     */
    public static byte[] bitmaptobyte(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return appicon;

    }

    /**
     * 压缩图片
     * @param image
     * @param outPath
     * @param maxSize
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while ( os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }
}
