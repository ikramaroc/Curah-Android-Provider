package com.curahservice.netset.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;


import com.curahservice.netset.R;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;

import static com.curahservice.netset.util.AppConstants.CAMERA_REQ;
import static com.curahservice.netset.util.AppConstants.GALLERY_REQ;


/**
 * Created by Neeraj Narwal on 17/10/16.
 */
public class ImageUtils {

    private static Uri outputUri;
    private static Context activity = null;
    private static ImageUtils imageUtils;
    private static ImageSelectCallback imageSelectCallback;
    private static int reqCode;
    private boolean onlyCamera;
    private boolean onlyGallery;
    private boolean doCrop;
    private static int width;
    private static int height;
    private static Uri inputUri;
    private static boolean videoView = false;

    private ImageUtils(ImageSelect.Builder builder) {
        activity = builder.activity;
        reqCode = builder.reqCode;
        imageSelectCallback = builder.imageSelectCallback;
        this.onlyCamera = builder.onlyCamera;
        this.onlyGallery = builder.onlyGallery;
        this.doCrop = builder.doCrop;
        width = builder.width;
        height = builder.height;
    }

    public static class ImageSelect {

        public static class Builder {
            private ImageSelectCallback imageSelectCallback;
            private Context activity;
            private int reqCode;
            private boolean onlyCamera;
            private boolean onlyGallery;
            private boolean doCrop;
            private int width, height;
            boolean video = false;

            public Builder(Context activity, ImageSelectCallback imageSelectCallback, int reqCode) {
                this.activity = activity;
                this.reqCode = reqCode;
                this.imageSelectCallback = imageSelectCallback;
            }

            public Builder(Context activity, ImageSelectCallback imageSelectCallback, int reqCode, boolean video) {
                this.activity = activity;
                this.reqCode = reqCode;
                this.imageSelectCallback = imageSelectCallback;
                this.video = video;
            }

            public Builder onlyCamera(boolean onlyCamera) {
                this.onlyCamera = onlyCamera;
                return this;
            }

            public Builder onlyGallery(boolean onlyGallery) {
                this.onlyGallery = onlyGallery;
                return this;
            }

            public Builder crop() {
                this.doCrop = true;
                return this;
            }

            public Builder aspectRatio(int width, int height) {
                this.width = width;
                this.height = height;
                this.doCrop = true;
                return this;
            }

            public void start() {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "Write External storage Permission not specified", Toast.LENGTH_LONG).show();
                    return;
                }
                imageUtils = new ImageUtils(this);
                if (imageUtils.onlyCamera) {
                    captureCameraImage();
                } else if (imageUtils.onlyGallery) {
                    selectGalleryImage();
                } else {
                    videoView = video;
                    selectImageDialog(video);
                }
            }
        }
    }


    public interface ImageSelectCallback {
        void onImageSelected(String imagePath, int resultCode);
    }

    private static AlertDialog.Builder builder;
    private static AlertDialog alert;

    private static void selectImageDialog(final boolean video) {
        try {

            if (alert != null) {
                alert = null;
            }
            if (!((Activity) activity).isFinishing() && alert == null) {
                builder = new AlertDialog.Builder(activity);
                builder.setTitle(Html.fromHtml("<font color='#000000'>Choose Image Resource</font>"));
                builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                if (video) {
                                    selectGalleryVideoImage();
                                } else {
                                    selectGalleryImage();
                                }
                                dialog.dismiss();

                                break;
                            case 1:


                                if (video) {
                                    captureCameraVideoImage();
                                } else {
                                    captureCameraImage();
                                }
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                alert = builder.create();
                alert.show();
            } else {
                if (!((Activity) activity).isFinishing() && !alert.isShowing()) {
                    alert.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alertDismiss() {
        if (alert.isShowing()) {
            alert.dismiss();
        }
    }

    private static void captureCameraVideoImage() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("Image--", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

            }
        }

        inputUri = null;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 300);

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        Intent[] intentArray = new Intent[]{cameraIntent, takeVideoIntent};
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 300);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose an action");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        ((Activity) activity).startActivityForResult(chooserIntent, AppConstants.CAMERA_REQ2);
    }

    public static String mCurrentPhotoPath;

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static File imageFileCreate;

    private static void captureCameraImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        imageFileCreate = new File(storageDir, String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
                inputUri = FileProvider.getUriForFile(activity, activity.getString(R.string.file_provider_authority_), imageFileCreate.getAbsoluteFile());

            } else {
                inputUri = Uri.fromFile(imageFileCreate.getAbsoluteFile());
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, inputUri);
            ((Activity) activity).startActivityForResult(takePictureIntent, AppConstants.CAMERA_REQ);
        }
    }

    private static void selectGalleryImage() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent1.setType("image/*");
        ((Activity) activity).startActivityForResult(intent1, AppConstants.GALLERY_REQ);
    }


    private static void selectGalleryVideoImage() {


        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/* video/*");
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 300);
            intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
            ((Activity) activity).startActivityForResult(Intent.createChooser(intent, "Choose an action"), AppConstants.GALLERY_REQ2);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 300);
            intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
            ((Activity) activity).startActivityForResult(intent, AppConstants.GALLERY_REQ2);
        }


    }


    public static void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.GALLERY_REQ && resultCode == Activity.RESULT_OK) {
            inputUri = data.getData();
            if (imageUtils.doCrop && !videoView) {
                File file = new File("" + activity.getCacheDir() + System.currentTimeMillis() + ".jpg");
                outputUri = Uri.fromFile(file);
                Crop();
            } else {
                sendBackImagePath(inputUri, reqCode);
            }

        } else if (requestCode == AppConstants.CAMERA_REQ && resultCode == Activity.RESULT_OK) {
            Log.e("data", data + "");
            if (imageUtils.doCrop && !videoView) {
                File file = new File("" + activity.getCacheDir() + System.currentTimeMillis() + ".jpg");
                outputUri = Uri.fromFile(file);
                Crop();
            } else {
                sendBackImagePath(inputUri, reqCode);
            }
            //sendBackImagePath(inputUri, reqCode);

        } else if (requestCode == AppConstants.CAMERA_REQ2 && resultCode == Activity.RESULT_OK) {
            Log.e("data", data + "");
            if (imageUtils.doCrop && !videoView) {
                File file = new File("" + activity.getCacheDir() + System.currentTimeMillis() + ".jpg");
                outputUri = Uri.fromFile(file);
                Crop();
            } else {
                if (inputUri == null && data != null && data.getData() != null) {
                    inputUri = data.getData();
                    sendBackImagePath(inputUri, reqCode);
                } else {
                    Log.e("----mCurrentPhotoPath--", mCurrentPhotoPath);
                    if (isImageFile(mCurrentPhotoPath)) {
                        inputUri = Uri.parse(mCurrentPhotoPath);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), inputUri);
                            File file = bitmapToFile(bitmap, (Activity) activity);
                            outputUri = Uri.fromFile(file);
                            Crop();

                        } catch (Exception e) {
                            Log.e("Exception-----------", e.getLocalizedMessage());
                        }


                    } else {
                        sendBackImagePath(Uri.parse(mCurrentPhotoPath), reqCode);
                    }

                }


            }

        }
        if (requestCode == AppConstants.GALLERY_REQ2 && resultCode == Activity.RESULT_OK) {
            inputUri = data.getData();
            if (imageUtils.doCrop && !videoView) {
                File file = new File("" + activity.getCacheDir() + System.currentTimeMillis() + ".jpg");
                outputUri = Uri.fromFile(file);
                Crop();
            } else {
                try {
                    MediaPlayer mp = MediaPlayer.create(activity, inputUri);

                    if (mp != null) {
                        int duration = mp.getDuration();
                        mp.release();
                        if ((duration / 1000) > 180) {
                            // Show Your Messages
                            Toast.makeText(activity, "Please select another video, This video is larger video", Toast.LENGTH_LONG).show();
                        } else {
                            sendBackImagePath(inputUri, reqCode);
                        }
                    } else {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), inputUri);
                        File file = bitmapToFile(bitmap, (Activity) activity);
                        outputUri = Uri.fromFile(file);
                        Crop();
                    }

                } catch (Exception e) {
                    //sendBackImagePath(inputUri, reqCode);
                    e.printStackTrace();
                }


            }

        } else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            sendBackImagePath(outputUri, reqCode);
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(activity, "Write External storage Permission not specified", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap = null;
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }


    public static Uri handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);
        img = rotateImageIfRequired(context, img, selectedImage);


        return getImageUri(context, img);
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static void sendBackImagePath(Uri inputUri, int reqCode) {
        try {
            String path = getRealPath(activity, inputUri);
            imageSelectCallback.onImageSelected(path, reqCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private static void Crop() {
        Log.e("width------------", width + "");
        if (width != 0) {
            Crop.of(inputUri, outputUri).withAspect(width, height).start(((Activity) activity));
        } else

        {

            /*int rotate = 0;
            try {
                activity.getContentResolver().notifyChange(inputUri, null);
                File imageFile = new File(imageFileCreate.getAbsolutePath());

                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }

                Log.e("RotateImage", "Exif orientation: " + orientation);
                Log.e("RotateImage", "Rotate value: " + rotate);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            try {
                inputUri = handleSamplingAndRotationBitmap(activity, inputUri);
                Crop.of(inputUri, outputUri).asSquare().start(((Activity) activity));

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("exception----------", e.getLocalizedMessage() + "");
            }


        }
    }

    public static File bitmapToFile(Bitmap bitmap, Activity activity) {
        File f = new File(activity.getCacheDir(), System.currentTimeMillis() + ".jpg");
        try {
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
        return f;
    }

    public static String getRealPath(Context context, Uri uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {

                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static Bitmap imageCompress(String picturePath) {
        return imageCompress(picturePath, 816.0f, 612.0f);
    }

    public static Bitmap imageCompress(String picturePath, float maxHeight, float maxWidth) {

        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bmp = BitmapFactory.decodeFile(picturePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        } else {
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeFile(picturePath);
            bitmap = Bitmap.createScaledBitmap(bitmap, actualWidth, actualHeight, true);
            return bitmap;
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(picturePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        bmp.recycle();
        ExifInterface exif;
        try {
            exif = new ExifInterface(picturePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        float totalPixels = width * height;
        float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static Bitmap blur(Activity context, Bitmap image) {

        float BITMAP_SCALE = 0.4f;
        float BLUR_RADIUS = 5f;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = null;
            theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
            return outputBitmap;
        }
        return null;
    }

}