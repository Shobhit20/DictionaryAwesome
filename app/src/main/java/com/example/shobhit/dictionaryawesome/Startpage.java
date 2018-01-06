package com.example.shobhit.dictionaryawesome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Startpage extends AppCompatActivity {
    private static final int REQ_CODE= 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
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
}
