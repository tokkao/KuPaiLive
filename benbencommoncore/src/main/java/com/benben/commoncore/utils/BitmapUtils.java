package com.benben.commoncore.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能:
 * 作者：zjn on 2017/9/9 14:32
 */

public class BitmapUtils {
    /**
     * 取得指定区域的图形
     *
     * @param source
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmap(Bitmap source, int x, int y, int width,
                                   int height) {
        Bitmap bitmap = Bitmap.createBitmap(source, x, y, width, height);
        return bitmap;
    }

    /**
     * 从大图中截取小图
     *
     * @param context
     * @param source
     * @param row
     * @param col
     * @param rowTotal
     * @param colTotal
     * @return
     */
    public static Bitmap getImage(Context context, Bitmap source, int row,
                                  int col, int rowTotal, int colTotal, float multiple,
                                  boolean isRecycle) {
        Bitmap temp = getBitmap(source, (col - 1) * source.getWidth()
                        / colTotal, (row - 1) * source.getHeight() / rowTotal,
                source.getWidth() / colTotal, source.getHeight() / rowTotal);

        if (isRecycle) {
            recycleBitmap(source);
        }
        if (multiple != 1.0) {
            Matrix matrix = new Matrix();
            matrix.postScale(multiple, multiple);
            temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
                    temp.getHeight(), matrix, true);
        }
        return temp;
    }


    public static Bitmap imageZoom(Bitmap bitMap, double maxSize) {
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
        }
        return bitMap;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
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
     * 从大图中截取小图
     *
     * @param context
     * @param source
     * @param row
     * @param col
     * @param rowTotal
     * @param colTotal
     * @return
     */
    public static Drawable getDrawableImage(Context context, Bitmap source,
                                            int row, int col, int rowTotal, int colTotal, float multiple) {

        Bitmap temp = getBitmap(source, (col - 1) * source.getWidth()
                        / colTotal, (row - 1) * source.getHeight() / rowTotal,
                source.getWidth() / colTotal, source.getHeight() / rowTotal);
        if (multiple != 1.0) {
            Matrix matrix = new Matrix();
            matrix.postScale(multiple, multiple);
            temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
                    temp.getHeight(), matrix, true);
        }
        Drawable d = new BitmapDrawable(context.getResources(), temp);
        return d;
    }

    public static Drawable[] getDrawables(Context context, int resourseId,
                                          int row, int col, float multiple) {
        Drawable drawables[] = new Drawable[row * col];
        Bitmap source = decodeResource(context, resourseId);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                drawables[temp] = getDrawableImage(context, source, i, j, row,
                        col, multiple);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
            source = null;
        }
        return drawables;
    }

    public static Drawable[] getDrawables(Context context, String resName,
                                          int row, int col, float multiple) {
        Drawable drawables[] = new Drawable[row * col];
        Bitmap source = decodeBitmapFromAssets(context, resName);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                drawables[temp] = getDrawableImage(context, source, i, j, row,
                        col, multiple);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
            source = null;
        }
        return drawables;
    }

    /**
     * 根据一张大图，返回切割后的图元数组
     *
     * @param resourseId :资源id
     * @param row        ：总行数
     * @param col        ：总列数 multiple:图片缩放的倍数1:表示不变，2表示放大为原来的2倍
     * @return
     */
    public static Bitmap[] getBitmaps(Context context, int resourseId, int row,
                                      int col, float multiple) {
        Bitmap bitmaps[] = new Bitmap[row * col];
        Bitmap source = decodeResource(context, resourseId);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bitmaps[temp] = getImage(context, source, i, j, row, col,
                        multiple, false);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
            source = null;
        }
        return bitmaps;
    }

    public static Bitmap[] getBitmaps(Context context, String resName, int row,
                                      int col, float multiple) {
        Bitmap bitmaps[] = new Bitmap[row * col];
        Bitmap source = decodeBitmapFromAssets(context, resName);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bitmaps[temp] = getImage(context, source, i, j, row, col,
                        multiple, false);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
            source = null;
        }
        return bitmaps;
    }

    public static Bitmap[] getBitmapsByBitmap(Context context, Bitmap source,
                                              int row, int col, float multiple) {
        Bitmap bitmaps[] = new Bitmap[row * col];
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bitmaps[temp] = getImage(context, source, i, j, row, col,
                        multiple, false);
                temp++;
            }
        }
        return bitmaps;
    }

    public static Bitmap decodeResource(Context context, int resourseId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true; // 需把 inPurgeable设置为true，否则被忽略
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resourseId);
        return BitmapFactory.decodeStream(is, null, opt); // decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，无需再使用java层的createBitmap，从而节省了java层的空间
    }

    /**
     * 从assets文件下解析图片
     *
     * @param resName
     * @return
     */
    public static Bitmap decodeBitmapFromAssets(Context context, String resName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream in = null;
        try {
            in = context.getAssets().open(resName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in, null, options);
    }

    /**
     * 回收不用的bitmap
     *
     * @param b
     */
    public static void recycleBitmap(Bitmap b) {
        if (b != null && !b.isRecycled()) {
            b.recycle();
            b = null;
        }
    }

    /**
     * 获取某些连在一起的图片的某一个画面（图片为横着排的情况）
     *
     * @param source
     * @param frameIndex 从1开始
     * @param totalCount
     * @return
     */
    public static Bitmap getOneFrameImg(Bitmap source, int frameIndex,
                                        int totalCount) {
        int singleW = source.getWidth() / totalCount;
        return Bitmap.createBitmap(source, (frameIndex - 1) * singleW, 0,
                singleW, source.getHeight());
    }

    public static void recycleBitmaps(Bitmap bitmaps[]) {
        if (bitmaps != null) {
            for (Bitmap b : bitmaps) {
                recycleBitmap(b);
            }
            bitmaps = null;
        }
    }

    /**
     * drawable转换成bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());

            drawable.draw(canvas);
            return bitmap;
        } else {
            throw new IllegalArgumentException(
                    "can not support this drawable to bitmap now!!!");
        }
    }

    /**
     * 将byte数组转化为bitmap
     *
     * @param data
     * @return
     */

    public static Bitmap byte2bitmap(byte[] data) {

        if (null == data) {

            return null;

        }

        return BitmapFactory.decodeByteArray(data, 0, data.length);

    }

    /**
     * 将bitmap转化为drawable
     *
     * @param bitmap
     * @return
     */

    public static Drawable bitmap2Drawable(Bitmap bitmap) {

        if (bitmap == null) {

            return null;

        }

        return new BitmapDrawable(bitmap);

    }

    /**
     * 按指定宽度和高度缩放图片,不保证宽高比例
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {

        if (bitmap == null) {

            return null;

        }

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidht = ((float) w / width);

        float scaleHeight = ((float) h / height);

        matrix.postScale(scaleWidht, scaleHeight);

        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,

                matrix, true);

        return newbmp;

    }

    /**
     * 保存文件到指定路径 ，更新系统相册
     *
     * @param context
     * @param bitmap
     * @param path
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bitmap, String path) {
        try {
            //系统相册目录
            File file = new File(path);

            File parent = file.getParentFile();

            if (!parent.exists()) {

                parent.mkdirs();

            }

            FileOutputStream
                    fos = new FileOutputStream(file);

            boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();

            fos.close();

            //把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (b) {
                return true;
            } else {
                return false;
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return false;
    }

    /**
     * 将bitmap位图保存到path路径下，图片格式为Bitmap.CompressFormat.PNG，质量为100
     *
     * @param bitmap
     * @param path
     */

    public static boolean saveBitmap(Bitmap bitmap, String path) {

        try {

            File file = new File(path);

            File parent = file.getParentFile();

            if (!parent.exists()) {

                parent.mkdirs();

            }

            FileOutputStream
                    fos = new FileOutputStream(file);

            boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();

            fos.close();

            return b;

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return false;

    }

    /**
     * 将bitmap位图保存到path路径下
     *
     * @param bitmap
     * @param path    保存路径-Bitmap.CompressFormat.PNG或Bitmap.CompressFormat.JPEG.PNG
     * @param format  格式
     * @param quality Hint to the compressor, 0-100. 0 meaning compress for small
     *                <p>
     *                size, 100 meaning compress for max quality. Some formats, like
     *                <p>
     *                PNG which is lossless, will ignore the quality setting
     * @return
     */

    public static boolean saveBitmap(Bitmap bitmap, String path,

                                     Bitmap.CompressFormat format, int quality) {

        try {

            File file = new File(path);

            File parent = file.getParentFile();

            if (!parent.exists()) {

                parent.mkdirs();

            }

            FileOutputStream fos = new FileOutputStream(file);

            boolean b = bitmap.compress(format, quality, fos);

            fos.flush();

            fos.close();

            return b;

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return false;

    }

    /**
     * 获得圆角图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        if (bitmap == null) {

            return null;

        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),

                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    /**
     * 获得带倒影的图片
     */

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {

        if (bitmap == null) {

            return null;

        }
        final int reflectionGap = 4;

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();

        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,

                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,

                (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);

        canvas.drawBitmap(bitmap, 0, 0, null);

        Paint deafalutPaint = new Paint();

        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();

        LinearGradient
                shader = new LinearGradient(0, bitmap.getHeight(), 0,

                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,

                0x00ffffff, Shader.TileMode.CLAMP);

        paint.setShader(shader);

        // Set the Transfer mode to be porter duff and destination in

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        // Draw a rectangle using the paint with our linear gradient

        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()

                + reflectionGap, paint);

        return bitmapWithReflection;

    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fis);
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

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
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

    /**
     * 获取bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap getBitmapByFile(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
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

    /**
     * 创建缩略图
     *
     * @param context
     * @param largeImagePath 原始大图路径
     * @param thumbfilePath  输出缩略图路径
     * @param square_size    输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     */
    public static void createImageThumbnail(Context context,
                                            String largeImagePath, String thumbfilePath, int square_size,
                                            int quality) throws IOException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        // 原始图片bitmap
        Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);

        if (cur_bitmap == null)
            return;

        // 原始图片的高宽
        int[] cur_img_size = new int[]{cur_bitmap.getWidth(),
                cur_bitmap.getHeight()};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);
        // 生成缩放后的bitmap
        Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0],
                new_img_size[1]);
        // 生成缩放后的图片文件
        saveImageToSD(null, thumbfilePath, thb_bitmap, quality);
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio)};
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath,
                                     Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static boolean saveImage(Bitmap photo, String spath) {
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

    public static boolean saveImage(String path, String spath, int yaSuoBi) {
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
    public static Bitmap getBitmap(String filePath) {

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

    // 旋转图片
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }

    // 读取图像的旋转度
    public static int readBitmapDegree(String path) {
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

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getEncryptBitmap(String filePath) {
        String sdPath = filePath;
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
            f.renameTo(new File(path));
//            //复制图片
//            FileUtils.copyFile(path,path+ Constants.BITMAP_SUFFIX);
//            FileUtils.deleteFile(path);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 照片转byte二进制
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }
}
