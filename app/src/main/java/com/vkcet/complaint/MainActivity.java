package com.vkcet.complaint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;


public class MainActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 1;
    String selectedImagePath;
    Bitmap bitmap;
    String imageName = "something.png";

    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.mImageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraImageButton_onClick(getCurrentFocus());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void cameraImageButton_onClick(View view) {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String path = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_PICTURES;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        dir = new File(path, imageName);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(dir));
        startActivityForResult(cameraIntent, RESULT_TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_TAKE_PICTURE && resultCode == RESULT_OK) {

            selectedImagePath = Environment.getExternalStorageDirectory()
                    + File.separator + Environment.DIRECTORY_PICTURES
                    + File.separator + imageName;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            options.inSampleSize = calculateInSampleSize(options,
                    254, 254);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
            mImageView.setImageBitmap(bitmap);

            saveImage(bitmap, imageName);

        }

    }



    private void saveImage(Bitmap bitmap, String name) {
        String path = getApplicationContext().getFilesDir().toString()
                + File.separator;
        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
        }

        path = path + name;
        dir = new File(path);

        try {
            dir.createNewFile();
            FileOutputStream fos = new FileOutputStream(dir);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
