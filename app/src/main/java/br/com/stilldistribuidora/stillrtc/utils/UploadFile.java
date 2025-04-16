package br.com.stilldistribuidora.stillrtc.utils;

/**
 * Created by Still Technology and Development Team on 22/04/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ack Lay (Cleidimar Viana) on 3/15/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class UploadFile extends Activity {

    private String namePath = "StillRTC";

    private static final String TAG = UploadFile.class.getSimpleName();

    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CROP_IMAGE = 0x3;

    private Activity activity;
    private PermissManager permissManager;

    public UploadFile(Activity activity) {
        this.activity = activity;
        permissManager = new PermissManager(activity);
    }

    public void onClickUserImage(final File mFile) {

        final CharSequence[] options = {
                "Tirar foto",
                "Foto da galeria"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Tirar foto")) {
                    openCamera(mFile);
                } else {
                    openGallery();
                }
            }
        });
        builder.show();
    }
//11052022
    /**
     * This method opens camera to take pictures.
     */
    public void openCamera(File mFileTemp) {
        try {
            if (!permissManager.requestPermissCamera()) {
                Log.wtf(TAG, "Permission request for Camera");
            } else {
                if (!permissManager.requestPermissExtStorage()) {
                    Log.wtf(TAG, "Permission request for Extorage");
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        Uri mImageCaptureUri = null;
                       // String state = Environment.getExternalStorageState();
                        String state = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            if(Build.VERSION.SDK_INT >=24){
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder.build());
                                mImageCaptureUri = Uri.fromFile(mFileTemp);
                               }else {
                                mImageCaptureUri = Uri.fromFile(mFileTemp);
                            }
                           } else {
                            mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
                        intent.putExtra("return-data", true);

                        activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);


                    } catch (ActivityNotFoundException e) {
                        Log.d("Activity", "cannot take picture", e);
                        AlertDialog alertDialog = new AlertDialog.Builder(this.activity).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Erro do Sistema : (openCamera) "+e.getMessage());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }




                }
            }

        }catch (Exception exception){

                    AlertDialog alertDialog = new AlertDialog.Builder(this.activity).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Erro do Sistema : (openCamera) "+exception.getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
       // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    /**
     * This method opens gallery to select pictures.
     */
    public void openGallery() {
        if (permissManager.requestPermissExtStorage()) {
            Intent photoIntent = new Intent(Intent.ACTION_PICK);
            photoIntent.setType("image/*");
            activity.startActivityForResult(photoIntent, REQUEST_CODE_GALLERY);
        } else {
            Log.wtf(TAG, "Permission request for READ EXTERNAL STORAGE");
        }
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public void cropCapturedImage(Uri picUri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        //       intent.setClassName("com.android.camera", "com.android.camera.CropImage");
        intent.setType("image/*");
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(activity, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(picUri);
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 600);
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);


            intent.putExtra("crop", true);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("scaleUpIfNeeded", true);

            if (size == 1 || size == 2) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                activity.startActivityForResult(i, REQUEST_CROP_IMAGE);
            }
        }
    }

//    public void cropCustom(Uri picUri){
//        Crop.of(picUri, picUri).asSquare().withAspect(4,3).start(activity);
//    }

    public String generateNameFile() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd_hhmmss");
        return ft.format(dNow) + ".jpg";
    }

    public File pathPictures(String nameFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            File diretorio =new File(Environment.getExternalStorageDirectory() + File.separator + namePath);
            File imagem = new File(diretorio.getPath() + "/" + nameFile);
            return imagem;
        }else{
            String state = Environment.getExternalStorageState();
            String destPath = activity.getExternalFilesDir(null).getAbsolutePath();
            File folder = new File(destPath);
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                if (!folder.exists()) {
                    folder.mkdir();
                }
                return new File(folder, nameFile);
            } else {
                return new File(destPath, nameFile);
            }
        }




    }

    public void reduce(String PATH_ORIGINAL_IMAGE){
        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b= BitmapFactory.decodeFile(PATH_ORIGINAL_IMAGE);
        Bitmap out = Bitmap.createScaledBitmap(b, 320, 480, false);

        File file = new File(dir, "resize.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {}
    }

    public String compressImages(String nameFile, String imageUri) {

        String filePath = imageUri; //getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 768.0f;
        float maxWidth = 1024.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

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
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
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

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename(nameFile);
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename(String nameFile) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), namePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + nameFile); //System.currentTimeMillis()
        return uriSting;

    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}