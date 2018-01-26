package com.example.shobhit.dictionaryawesome;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.StrictMath.log;

public class AddWords extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH = 1234;
    private static final int REQ_CODE_CAMERA = 2234;
    private static final int REQUEST_CODE_ASK_PERMISSIONS=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_words);
        Intent intent = getIntent();
    }

    public void addword(View view) throws FileNotFoundException {
        EditText word = (EditText) findViewById(R.id.word);
        String new_word = word.getText().toString();
        EditText defn = (EditText) findViewById(R.id.defn);
        String new_defn = defn.getText().toString();
        PrintStream output = new PrintStream(openFileOutput("added_words.txt", MODE_PRIVATE | MODE_APPEND));

        output.println(new_word+"\t"+new_defn);
        output.close();
        Intent goback = new Intent();
        goback.putExtra("Word", new_word);
        goback.putExtra("Defn", new_defn);
        setResult(RESULT_OK, goback);
        finish();

    }

    public void speechtotext(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak the word");
        startActivityForResult(intent, REQ_CODE_SPEECH);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void takepic(View view) {
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            camera_launch();
        }
        else{
            String[] permsissionRequest = {Manifest.permission.CAMERA};
            requestPermissions(permsissionRequest, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                camera_launch();
            }
            else{
                Toast.makeText(this, "Unable to open Camera",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void camera_launch(){
        Intent picintent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        startActivityForResult(picintent, REQ_CODE_CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SPEECH){
            ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = list.get(0);
            Toast.makeText(this, "The line is "+text, Toast.LENGTH_LONG).show();
            Log.d("The word is ", text);
        }
        if(requestCode == REQ_CODE_CAMERA){


            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            TextRecognizer text = new TextRecognizer.Builder(getApplicationContext()).build();
            Frame outputFrame = new Frame.Builder().setBitmap(bmp).build();
            final SparseArray<TextBlock> items = text.detect(outputFrame);
            final StringBuilder strings = new StringBuilder();
            if(items.size() != 0){
                for(int i =0 ; i<items.size(); i++) {
                    TextBlock item = items.valueAt(i);
                    strings.append(item.getValue());
                    Log.e("Value", item.getValue());
                    strings.append("\n");
                }
            }
            TextView tv = (TextView) findViewById(R.id.detectedtext);
            tv.setText(strings.toString());

        }
    }

    public void pythoncall(View view) {
        
    }
}
