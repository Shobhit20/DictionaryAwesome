package com.example.shobhit.dictionaryawesome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int REQUEST_CAMERA_PERMISSION = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "Unable to open Camera",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraView = (SurfaceView) findViewById(R.id.surfaceview);
        textView = (TextView) findViewById(R.id.textview);
        TextRecognizer text = new TextRecognizer.Builder(getApplicationContext()).build();
        
        Log.e("IN","ON CREATE");
        final StringBuilder strings = new StringBuilder();
        if (!text.isOperational()){
            Log.e("Is","On operational");

        }
        else{
            Log.e("IN","ON else condition camera source");
            cameraSource = new CameraSource.Builder(getApplicationContext(), text)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try{
                        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            text.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0){

                        textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    for(int i =0 ; i<items.size(); i++) {
                                        TextBlock item = items.valueAt(i);
                                        strings.append(item.getValue());
                                        strings.append("\n");
                                    }
                                    textView.setText(strings.toString());
                                }

                                });
                    }


                }
            });
        }
        Intent goback = new Intent();
        goback.putExtra("Detection", strings.toString());
        setResult(RESULT_OK, goback);
        finish();
    }

}
