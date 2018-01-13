package com.example.shobhit.dictionaryawesome;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

public class Startpage extends AppCompatActivity {
    private static final int REQ_CODE= 1234;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        ImageView img = (ImageView) findViewById(R.id.image);

        Picasso.with(this).load("https://lh3.googleusercontent.com/klzb6YkXqOtUhJ0ToAu9H1-FW5p-ORdeiSedoezeiyGPe_FYHUtC-EMs1AhqSyJG1OU=w170").into(img);
        YoYo.with(Techniques.Tada).duration(3000).playOn(img);
        mp = MediaPlayer.create(this, R.raw.script);
        mp.start();
    }

    public void checkvocab(View view) {
        Intent intent_check = new Intent(this, MainActivity.class);
        startActivity(intent_check);

    }

    public void addingword(View view) {
        Log.e("jvjvv", "vjdjsd");
        Intent intent_add = new Intent(this, AddWords.class);
        startActivityForResult(intent_add, REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQ_CODE){
            String word = data.getStringExtra("Word");
            String defn = data.getStringExtra("Defn");
            Log.e(word, defn);
            Toast.makeText(Startpage.this, "New Word Added", Toast.LENGTH_LONG ).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }
}
