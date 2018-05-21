package br.com.android.work.android_quizapp;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.android.work.android_quizapp.Common.Common;

public class Playing extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue = 0;

    CountDownTimer mCountDown;

    int index = 0;
    int score = 0;
    int thisQuestion = 0;
    int totalQuestion;
    int correctAnswer;

    FirebaseDatabase database;
    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestionNum = (TextView) findViewById(R.id.txtTotalQuestion);
        question_text = (TextView) findViewById(R.id.question_text);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        if(index < totalQuestion){
            Button clickedButton = (Button) view;
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())){
                
            }
        }
    }
}
