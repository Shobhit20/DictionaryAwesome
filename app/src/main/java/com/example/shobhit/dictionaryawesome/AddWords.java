package com.example.shobhit.dictionaryawesome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class AddWords extends AppCompatActivity {

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
}
