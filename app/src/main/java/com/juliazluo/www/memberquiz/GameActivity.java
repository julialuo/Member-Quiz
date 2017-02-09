package com.juliazluo.www.memberquiz;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameActivity extends AppCompatActivity {

    final Random random = new Random();
    ImageView imageView;
    TextView scoreTxt;
    Button [] choiceBtns;
    int score = 0;
    int memberNum;
    String[] members = {"Jessica Cherny", "Kevin Jiang", "Jared Gutierrez", "Kristin Ho",
            "Christine Munar", "Mudit Mittal", "Richard Hu", "Shaan Appel", "Edward Liu",
            "Wilbur Shi", "Young Lin", "Abhinav Koppu", "Abhishek Mangla  Akkshay Khoslaa",
            "Ally Koo", "Andy Wang", "Aneesh Jindal", "Anisha Salunkhe", "Aparna Krishnan",
            "Ashwin Vaidyanathan", "Cody Hsieh", "Jeffrey Zhang", "Justin Kim",
            "Krishnan Rajiyah", "Lisa Lee", "Peter Schafhalter", "Sahil Lamba", "Sameer Suresh",
            "Sirjan Kafle", "Tarun Khasnavis", "Billy Lu", "Aayush Tyagi", "Ben Goldberg",
            "Candice Ye", "Eliot Han", "Emaan Hariri", "Jessica Chen", "Katharine Jiang",
            "Kedar Thakkar", "Leon Kwak", "Mohit Katyal", "Rochelle Shen", "Sayan Paul",
            "Sharie Wang", "Shreya Reddy", "Shubham Goenka", "Victor Sun", "Vidya Ravikumar"};
    String[] imageNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageNames = new String[members.length];
        for (int i = 0; i < members.length; i++) {
            imageNames[i] = members[i].toLowerCase().replaceAll("\\s+","");
        }

        imageView = (ImageView) findViewById(R.id.image);
        scoreTxt = (TextView) findViewById(R.id.score_txt);

        choiceBtns = new Button[4];
        choiceBtns[0] = (Button) findViewById(R.id.btn1);
        choiceBtns[1] = (Button) findViewById(R.id.btn2);
        choiceBtns[2] = (Button) findViewById(R.id.btn3);
        choiceBtns[3] = (Button) findViewById(R.id.btn4);

        refreshScoreText();
        refreshScreen();

        for (Button button: choiceBtns) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = ((Button) v).getText().toString();
                    checkAnswer(answer);
                    refreshScreen();
                }
            });
        }
    }

    protected void refreshScreen() {
        //Change image and button text
        memberNum = random.nextInt(members.length);
        imageView.setImageDrawable
                (   getResources().getDrawable(getResourceID(imageNames[memberNum], "drawable",
                        getApplicationContext()))
                );

        int correctBtn = random.nextInt(4);
        choiceBtns[correctBtn].setText(members[memberNum]);
        int[] randNums = new int[4];
        for (int i = 0; i < 4; i++) {
            if (i != correctBtn) {
                int randNum = random.nextInt(members.length);
                while (Arrays.asList(randNums).contains(randNum) || randNum == memberNum) {
                    randNum = random.nextInt(members.length);
                }
                choiceBtns[i].setText(members[randNum]);
                randNums[i] = randNum;
            }
        }
    }

    protected void checkAnswer(String answer) {
        if (answer == members[memberNum]) {
            score += 1;
            refreshScoreText();
        }
    }

    protected void refreshScoreText() {
        scoreTxt.setText("Score: " + score);
    }

    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }
}
