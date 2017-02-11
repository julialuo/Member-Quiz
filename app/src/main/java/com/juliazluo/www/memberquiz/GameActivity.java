package com.juliazluo.www.memberquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    final Random random = new Random();
    ImageView imageView;
    TextView scoreTxt, countTxt;
    Button [] choiceBtns;
    int score = 0, timeSec = 0;
    long timeMS = 0;
    int memberNum;
    boolean returnFromContacts = false;
    String[] members = {"Jessica Cherny", "Kevin Jiang", "Jared Gutierrez", "Kristin Ho",
            "Christine Munar", "Mudit Mittal", "Richard Hu", "Shaan Appel", "Edward Liu",
            "Wilbur Shi", "Young Lin", "Abhinav Koppu", "Abhishek Mangla  Akkshay Khoslaa",
            "Andy Wang", "Aneesh Jindal", "Anisha Salunkhe", "Ashwin Vaidyanathan", "Cody Hsieh",
            "Jeffrey Zhang", "Justin Kim", "Krishnan Rajiyah", "Lisa Lee", "Peter Schafhalter",
            "Sahil Lamba", "Sameer Suresh", "Sirjan Kafle", "Tarun Khasnavis", "Billy Lu",
            "Aayush Tyagi", "Ben Goldberg", "Candice Ye", "Eliot Han", "Emaan Hariri",
            "Jessica Chen", "Katharine Jiang", "Kedar Thakkar", "Leon Kwak", "Mohit Katyal",
            "Rochelle Shen", "Sayan Paul", "Sharie Wang", "Shreya Reddy", "Shubham Goenka",
            "Victor Sun", "Vidya Ravikumar"};
    String[] imageNames;
    CountDownTimer count;

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
        countTxt = (TextView) findViewById(R.id.count_txt);

        choiceBtns = new Button[4];
        choiceBtns[0] = (Button) findViewById(R.id.btn1);
        choiceBtns[1] = (Button) findViewById(R.id.btn2);
        choiceBtns[2] = (Button) findViewById(R.id.btn3);
        choiceBtns[3] = (Button) findViewById(R.id.btn4);

        for (Button button: choiceBtns) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = ((Button) v).getText().toString();
                    checkAnswer(answer, false);
                    count.cancel();
                    refreshScreen();
                }
            });
        }

        //quit game functionality
        ((Button) findViewById(R.id.quit_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pause timer
                count.cancel();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);

                // set title
                alertDialogBuilder.setTitle("Quit Game");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to quit the game?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(getApplicationContext(),
                                        StartActivity.class);
                                startActivity(intent);
                                count.cancel();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                createTimer(timeMS);
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //clicking image creates new contact
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creates a new Intent to insert a contact
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                // Sets the MIME type to match the Contacts Provider
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, members[memberNum]);
                startActivity(intent);
                returnFromContacts = true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (returnFromContacts) {
            createTimer(timeMS);
            returnFromContacts = false;
        } else {
            refreshScoreText();
            refreshScreen();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        count.cancel();
    }

    protected void refreshScreen() {
        //Change image, button text, and restart timer
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

        createTimer(5000);
    }

    protected void checkAnswer(String answer, boolean outOfTime) {
        if (answer.equals(members[memberNum])) {
            score += 1;
            refreshScoreText();
        } else {
            String wrongText;
            if (!outOfTime) {
                wrongText = "You suck! The correct answer was " + members[memberNum];
            } else {
                wrongText = "You ran out of time! The correct answer was " + members[memberNum];
            }
            Toast toast = Toast.makeText(getApplicationContext(), wrongText, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void refreshScoreText() {
        scoreTxt.setText("Score: " + score);
    }

    protected void createTimer(long startMS) {
        count = new CountDownTimer(startMS, 250) {

            public void onTick(long ms) {
                timeMS = ms;
                if (Math.round((float)ms / 1000.0f) != timeSec) {
                    timeSec = Math.round((float)ms / 1000.0f);
                    countTxt.setText(timeSec + "");
                }
                Log.i("Time", "" + ms);
            }

            public void onFinish() {
                checkAnswer("Out of time uh oh", true);
                refreshScreen();
            }
        };
        count.start();
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
