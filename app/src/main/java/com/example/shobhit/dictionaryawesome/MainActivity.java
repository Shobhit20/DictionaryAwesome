package com.example.shobhit.dictionaryawesome;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Map<String, String> dictionary;
    private List<String> words;
    private List<String> defns;
    private int points;
    private int highscore;
    private int check_ques = 0;
    MediaPlayer mp;

    private void ReadFileData() {
        Scanner Scan = new Scanner(getResources().openRawResource(R.raw.testfile));
        ReadFileHelp(Scan);
        Scanner Scan2 = null;

        try {
            Scan2 = new Scanner(openFileInput("added_words.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("snfkjsdf","bhjbs");
        }
        ReadFileHelp(Scan2);

    }
    private void ReadFileHelp(Scanner Scan){
        if (Scan == null){
            return;
        }
        while(Scan.hasNextLine()){
            String Line = Scan.nextLine();
            String[] parts = Line.split("\t");
            Log.d(parts[0], parts[1]);
            dictionary.put(parts[0], parts[1]);
            words.add(parts[0]);
        }
        Log.e("Shobhit", "Maheshwari");
    }
    private void ChooseWords(){
        Random randy = new Random();
        int random = randy.nextInt(words.size());
        String theword = words.get(random);
        String corr_defn = dictionary.get(theword);
        defns = new ArrayList<>(dictionary.values());
        defns.remove(corr_defn);
        Collections.shuffle(defns);
        defns = defns.subList(0,4);
        defns.add(corr_defn);
        Collections.shuffle(defns);
        TextView text = (TextView) findViewById(R.id.word);
        text.setText(theword);
        YoYo.with(Techniques.BounceIn).playOn(text);
        ListView list = (ListView) findViewById(R.id.defn);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, defns);
        list.setAdapter(adapter);
        YoYo.with(Techniques.BounceIn).playOn(list);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dictionary = new HashMap<>();
        words = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
        highscore = prefs.getInt("HighScore", 0);
        TextView hs = (TextView) findViewById(R.id.highsc);
        hs.setText("HighScore: "+ highscore);
        ReadFileData();
        ChooseWords();
        mp = MediaPlayer.create(this, R.raw.script);
        mp.start();
        points=0;


        ListView list = (ListView) findViewById(R.id.defn);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String definition = adapterView.getItemAtPosition(i).toString();
                TextView the_word = (TextView) findViewById(R.id.word);
                String word_disp = the_word.getText().toString();
                String correct_defn = dictionary.get(word_disp);
                ImageView img = (ImageView) findViewById(R.id.images);
                TextView point_count = (TextView) findViewById(R.id.point);
                point_count.setText("Points: "+ points);
                if (definition.contentEquals(correct_defn)) {
                    Log.d("Correct", "Good work");

                    points++;
                    if (highscore < points){
                        highscore = points;
                        SharedPreferences prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
                        SharedPreferences.Editor prefseditor = prefs.edit();
                        prefseditor.putInt("HighScore", points);
                        prefseditor.apply();
                        TextView hs = (TextView) findViewById(R.id.highsc);
                        hs.setText("HighScore: "+highscore);

                    }
                    Picasso.with(MainActivity.this).load("https://static1.squarespace.com/static/51239e9ae4b0dce195cba126/t/59e0c23bf7e0ab92564ac3b9/1507902031981/thumbs-up.png").into(img);
                    ChooseWords();
                    point_count.setText("Points: " + points);

                } else {
                    Log.d("Incorrect", "OOPS!!");
                    points--;
                    Toast.makeText(MainActivity.this, "The correct defn is - "+correct_defn, Toast.LENGTH_LONG).show();
                    Picasso.with(MainActivity.this).load("http://s3.amazonaws.com/pix.iemoji.com/images/emoji/apple/ios-11/256/thumbs-down.png").into(img);
                    ChooseWords();
                    point_count.setText("Points: " + points);

                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("points", points);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("jshshfsjf", Integer.toString(savedInstanceState.getInt("points", 0)));
        points = savedInstanceState.getInt("points", 0);
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
