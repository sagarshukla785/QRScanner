package com.example.sagarshukla.kkk;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Generate extends AppCompatActivity {
    EditText editText;
    ImageView imageView;
    Image image;
    ImageButton imageButton,imageButton1;
    CircularProgressButton circularProgressButton;
    boolean Save;
    int count=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageButton = (ImageButton) findViewById(R.id.imageButton2);
        imageButton1 = (ImageButton) findViewById(R.id.imageButton3);
        circularProgressButton = (CircularProgressButton) findViewById(R.id.buttonc);
        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty()) {
                    if (circularProgressButton.getProgress() == 0) {
                        circularProgressButton.setProgress(40);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!editText.getText().toString().isEmpty()) {
                                    String data = editText.getText().toString();
                                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                    try {
                                        BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
                                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                        imageView.setImageBitmap(bitmap);
                                        circularProgressButton.setProgress(100);
                                        Save = true;
                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, 3000);
                    } else {
                        if (!editText.getText().toString().isEmpty()) {
                            Toast.makeText(Generate.this, "Clear First.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                    Toast.makeText(Generate.this,"Provide the content.",Toast.LENGTH_SHORT).show();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageView!=null && circularProgressButton.getProgress()!=0){
                    circularProgressButton.setProgress(0);
                    imageView.setImageBitmap(null);
                    Toast.makeText(Generate.this,"Cleared.",Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( editText.getText().toString().isEmpty()){
                    Toast.makeText(Generate.this,"No QR Code is present.",Toast.LENGTH_SHORT).show();
                }
                else if (Save) {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    String Filepath = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File file = new File(Filepath, "QR" + String.valueOf(count) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                        Toast.makeText(Generate.this, "QR Saved with name QR" + String.valueOf(count), Toast.LENGTH_SHORT).show();
                        Save = false;
                        count++;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(Generate.this, "Saved Already!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
