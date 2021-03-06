/************************************************************
  *  * EaseMob CONFIDENTIAL 
  * __________________ 
  * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved. 
  *  
  * NOTICE: All information contained herein is, and remains 
  * the property of EaseMob Technologies.
  * Dissemination of this information or reproduction of this material 
  * is strictly forbidden unless prior written permission is obtained
  * from EaseMob Technologies.
  */
package com.sens.baseapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class ImageUtils {


	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		return getRoundedCornerBitmap(bitmap, 6);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND,则类型要使用MICRO_KIND作为kind的值，这样会节省内存
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度
     * @param kind 参照MediaStore。Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND.
     * 			   其中，MINI_KIND:512*384,MICRO_KIND:96*96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath,int width,int height,int kind)
    {
    	Bitmap bitmap=null;
    	//获取视频的缩略图
    	bitmap=ThumbnailUtils.createVideoThumbnail(videoPath, kind);
    	Log.d("getVideoThumbnail", "video thumb width:"+bitmap.getWidth());
    	Log.d("getVideoThumbnail", "video thumb height:"+bitmap.getHeight());
    	bitmap=ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    	return bitmap;
    }
    
    
    /**
     * 保存video的缩略图
     * @param videoFile 视频文件
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度
     * @param kind 参照MediaStore。Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND.
     * 			   其中，MINI_KIND:512*384,MICRO_KIND:96*96
     * @return 缩略图绝对路径
     */
	public static String saveVideoThumb(File videoFile, int width, int height, int kind) {
		Bitmap bitmap = getVideoThumbnail(videoFile.getAbsolutePath(), width, height, kind);
		File file = new File(PathUtil.getInstance().getVideoPath(), "th" + videoFile.getName());
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
    }
    
    
	
	
	public static Bitmap decodeScaleImage(String imagePath, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		Options options = getBitmapOptions(imagePath);

		// Calculate inSampleSize
		int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Log.d("img", "original wid" + options.outWidth + " original height:" + options.outHeight + " sample:"
				+ sampleSize);
		options.inSampleSize = sampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(imagePath, options);
		//图片旋转角度
		int degree = readPictureDegree(imagePath);
		Bitmap rotateBm = null;
		if(bm != null && degree != 0){
			rotateBm = rotateImageView(degree, bm);
			bm.recycle();
			bm = null;
			return rotateBm;
		}else{
			return bm;
		}
		// return BitmapFactory.decodeFile(imagePath, options);
	}

	public static Bitmap decodeScaleImage(Context context, int drawableId, int reqWidth, int reqHeight) {
		Bitmap bitmap;
		Options options = new Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(context.getResources(), drawableId, options);
		@SuppressWarnings("UnnecessaryLocalVariable")
		int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;

		bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId, options);
		return bitmap;
	}

	public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static String getThumbnailImage(String imagePath, int thumbnailSize) {

	    /*
	    BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageWidth <= thumbnailSize && imageHeight <= thumbnailSize) {
	        //it is already small image within thumbnail required size. directly using it
	        return imagePath;
	    }*/

        Bitmap image = decodeScaleImage(imagePath, thumbnailSize, thumbnailSize);

        try {
            File tempFile = File.createTempFile("image", ".jpg");
            FileOutputStream stream = new FileOutputStream(tempFile);

            image.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            stream.close();
            Log.d("img", "generate thumbnail image at:" + tempFile.getAbsolutePath() + " size:" + tempFile.length());
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            //if any error, return original file
            return imagePath;
        }

	}

	/**
	 * deu to the network bandwidth limitation, we will scale image to smaller
	 * size before send out
	 *
	 * @param, appContext, the application context to get file dirs for creating temp image file
	 * @param imagePath
	 * @return
	 */
	public static String getScaledImage(Context appContext, String imagePath) {
		// if file size is less than 100k, no need to scale, directly return
		// that file path. imagePath
		File originalFile = new File(imagePath);
		if (!originalFile.exists()) {
			// wrong input
			return imagePath;
		}
		long fileSize = originalFile.length();
		Log.d("img", "original img size:" + fileSize);
		if (fileSize <= 100 * 1024) {
			Log.d("img", "use original small image");
			return imagePath;
		}

		// scale image to required size
		Bitmap image = decodeScaleImage(imagePath, SCALE_IMAGE_WIDTH, SCALE_IMAGE_HEIGHT);
		// save image to a temp file
		try {
			/*
			 * String extension =
			 * imagePath.substring(imagePath.lastIndexOf(".")+1); String imgExt
			 * = null; if (extension.equalsIgnoreCase("jpg")) { imgExt = "jpg";
			 * } else { imgExt = "png"; }
			 */
			File tempFile = File.createTempFile("image", ".jpg", appContext.getFilesDir());
			FileOutputStream stream = new FileOutputStream(tempFile);

			image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
			/*
			 * if (extension.equalsIgnoreCase("jpg") ||
			 * extension.equalsIgnoreCase("jpeg")) {
			 * image.compress(Bitmap.CompressFormat.JPEG, 60, stream); } else {
			 * image.compress(Bitmap.CompressFormat.PNG, 60, stream); }
			 */
			stream.close();
			Log.d("img", "compared to small fle" + tempFile.getAbsolutePath() + " size:" + tempFile.length());
			return tempFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return imagePath;
	}

	/**
	 * 得到"eaemobTemp"+i.jpg为文件名的临时图片
	 * @param imagePath
	 * @param i
	 * @return
	 */

	public static String getScaledImage(Context appContext, String imagePath,int i) {
//		List<String> temPaths = new ArrayList<String>();
//		for (int i = 0; i < imageLocalPaths.size(); i++) {
			File originalFile = new File(imagePath);
			if (originalFile.exists()) {
				long fileSize = originalFile.length();
				Log.d("img", "original img size:" + fileSize);
				if (fileSize > 100 * 1024) {
					// scale image to required size
					Bitmap image = decodeScaleImage(imagePath, SCALE_IMAGE_WIDTH, SCALE_IMAGE_HEIGHT);
					// save image to a temp file
					try {
						File tempFile = new File(appContext.getExternalCacheDir(), "eaemobTemp" + i + ".jpg");
						FileOutputStream stream = new FileOutputStream(tempFile);
						image.compress(Bitmap.CompressFormat.JPEG, 60, stream);
						stream.close();
						Log.d("img",
								"compared to small fle" + tempFile.getAbsolutePath() + " size:" + tempFile.length());
						return tempFile.getAbsolutePath();
					} catch (Exception e) {
						e.printStackTrace();

					}
				}

//			}

//			temPaths.add(imagePath);

		}
		return imagePath;

	}


	/**
	 * merge multiple images into one the result will be 2*2 images or 3*3
	 * images
	 *
	 * @param targetWidth
	 * @param targetHeight
	 * @param images
	 * @return
	 */
	public static Bitmap mergeImages(int targetWidth, int targetHeight, List<Bitmap> images) {
		Bitmap mergeBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(mergeBitmap);
		canvas.drawColor(Color.LTGRAY);
		Log.d("img", "merge images to size:" + targetWidth + "*" + targetHeight + " with images:" + images.size());
		int size;
		if (images.size() <= 4) {
			size = 2; // 2*2 images
		} else {
			size = 3; // 3*3 images
		}
		// draw 2*2 images
		// expect targeWidth == targetHeight
		int imgIdx = 0;
		int smallImageSize = (targetWidth - 4) / size;
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				// load image into small size
				Bitmap originalImage = images.get(imgIdx);
				Bitmap smallImage = Bitmap.createScaledBitmap(originalImage, smallImageSize, smallImageSize, true);

				Bitmap smallRoundedImage = getRoundedCornerBitmap(smallImage, 2);
				smallImage.recycle();
				// draw on merged canvas
				canvas.drawBitmap(smallRoundedImage, column * smallImageSize + (column + 2), row * smallImageSize
						+ (row + 2), null);
				smallRoundedImage.recycle();

				imgIdx++;
				if (imgIdx == images.size()) {
					return mergeBitmap;
				}
			}
		}

		return mergeBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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

	/*
	 * 旋转图片
	 *
	 * @param angle
	 *
	 * @param bitmap
	 *
	 * @return Bitmap
	 */
	public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		@SuppressWarnings("UnnecessaryLocalVariable")
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * get bitmap options
	 * @param imagePath
	 * @return
	 */
	public static Options getBitmapOptions(String imagePath){
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		return options;
	}

	public static final int SCALE_IMAGE_WIDTH = 640;
	public static final int SCALE_IMAGE_HEIGHT = 960;

	//保存文件到指定路径
	public static boolean saveImageToGallery(Context context, Bitmap bmp, String filename) {
		// 首先保存图片
		String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ApplicationUtil.getApplicationName();
		File appDir = new File(storePath);
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = (!filename.isEmpty() ? filename + "-" : "") + System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			//通过io流的方式来压缩保存图片
			boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
			fos.flush();
			fos.close();

			//把文件插入到系统图库
			//MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

			//保存图片后发送广播通知更新数据库
			Uri uri = Uri.fromFile(file);
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}