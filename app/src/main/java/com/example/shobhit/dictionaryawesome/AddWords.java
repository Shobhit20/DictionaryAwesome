package com.example.shobhit.dictionaryawesome;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
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

    public void takepic(View view) {
        Intent picintent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        startActivityForResult(picintent, REQ_CODE_CAMERA);
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
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

            if(askpermission()){
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ImageView img = (ImageView) findViewById(R.id.disp_img);
                img.setImageBitmap(bmp);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean askpermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return true;
        }
        else{
            return false;
        }
    }


}
