package com.example.shobhit.dictionaryawesome;

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
    private int check_ques = 0;

    private void ReadFileData() {
        Scanner Scan = new Scanner(getResources().openRawResource(R.raw.words));
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
        ListView list = (ListView) findViewById(R.id.defn);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, defns);
        list.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dictionary = new HashMap<>();
        words = new ArrayList<>();
        ReadFileData();
        ChooseWords();
        MediaPlayer mp = MediaPlayer.create(this, R.raw.script);
        mp.start();

        ListView list = (ListView) findViewById(R.id.defn);
        points = 0;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String definition = adapterView.getItemAtPosition(i).toString();
                TextView the_word = (TextView) findViewById(R.id.word);
                String word_disp = the_word.getText().toString();
                String correct_defn = dictionary.get(word_disp);
                TextView point_count = (TextView) findViewById(R.id.point);
                if(definition.contentEquals(correct_defn)){
                    Log.d("Correct", "Good work");
                    points++;
                    ChooseWords();
                    point_count.setText("Points: " +points);

                }
                else{
                    Log.d("Incorrect", "Fuck off");
                    points--;
                    ChooseWords();
                    point_count.setText("Points: "+points);

                }

            }
        });
    }
}
