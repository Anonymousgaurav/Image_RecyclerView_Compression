package com.example.image_recyclerview_compression;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.image_recyclerview_compression.RealPathUtil.filePath;

public class MainActivity extends AppCompatActivity {
    Button button;
    RecyclerView recyclerView;
    private static final int GalleryPick = 2;
    GalleryAdapter galleryAdapter;
    ArrayList<String> tfiles = new ArrayList<String>();
    ArrayList<Uri> ufiles = new ArrayList<Uri>();
    long length;
    File file;
    Bitmap myBitmap;
    ArrayList<String> totalGalleryImagesList = new ArrayList<>();
    int count = 0;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    ArrayList<String> pathLists = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.upload_pic);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
    }

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();

        if (requestCode == 2 && resultCode == RESULT_OK) {

            assert data != null;
            if (data.getData() != null) {
                Uri mImageUri = data.getData();
                Log.d("img_uri", String.valueOf(mImageUri));
                Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT < 11) {
                    filePath = RealPathUtil.getRealPathFromURI_BelowAPI11(MainActivity.this, mImageUri);
                    Log.d("CCCC:",filePath);
                }
                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                {
                    filePath = RealPathUtil.getRealPathFromURI_API11to18(MainActivity.this, mImageUri);
                    Log.d("AAAA:",filePath);
                }
                    // SDK > 19 (Android 4.4)
                else
                {
                    filePath = RealPathUtil.getRealPathFromURI_API19(MainActivity.this, mImageUri);
                    Log.d("BBBB:", filePath);
                }
                    file = new File(filePath);
                    length = file.length() / 1024;
                    Log.d("filess", String.valueOf(file));
                    Log.d("length_file", String.valueOf(length));
            }

                Bitmap bm = BitmapFactory.decodeFile(filePath);
                myBitmap = scaleBitmap(bm);

                if (length < 1024) {
                    Toast.makeText(this, "You Choose Right Image", Toast.LENGTH_SHORT).show();
                    Uri tempUri = getImageUri(getApplicationContext(), myBitmap);
                    File finalFile1 = new File(getRealPathFromURI(tempUri));
                    String ff1 = finalFile1.getPath();
                    totalGalleryImagesList.add(ff1);

                } else {
                    Toast.makeText(this, "Compressed", Toast.LENGTH_SHORT).show();
                    Uri temp1Uri = getImageUri(getApplicationContext(), myBitmap);
                    File finalFile = new File(getRealPathFromURI(temp1Uri));
                    String ff = finalFile.getPath();
                    totalGalleryImagesList.add(ff);
                }


                galleryAdapter = new GalleryAdapter(getApplicationContext(), totalGalleryImagesList);
                recyclerView.setAdapter(galleryAdapter);

                int image_size = totalGalleryImagesList.size();

                Log.d("total_size", String.valueOf(image_size));

                if (image_size == 4) {
                    button.setVisibility(View.INVISIBLE);
                }
            }

        }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    Bitmap scaleBitmap(Bitmap bitmap) {

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        int min = 250;


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / height;
            height = min;
            width = (int) (height * ratio);
        } else {
            float ratio = (float) height / width;
            width = min;
            height = (int) (width * ratio);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return bitmap;
    }

}
