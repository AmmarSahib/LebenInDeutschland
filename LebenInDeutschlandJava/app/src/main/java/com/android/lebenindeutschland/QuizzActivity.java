package com.android.lebenindeutschland;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class QuizzActivity extends AppCompatActivity {
    private static Firebase fb;
    private static ImageView iv_picture;
    private static RadioGroup rg_choices;
    private static RadioButton rb_selected;

    private static Switch sw_arabic;

    private static Spinner sp_nr;
    private static Spinner sp_type;

    private static RadioButton rb_choiceA;
    private static RadioButton rb_choiceB;
    private static RadioButton rb_choiceC;
    private static RadioButton rb_choiceD;

    private TextView tv_question;
    private TextView tv_number;

    private static Button b_submit;
    private static Button b_advance;
    private static Button b_previous;

    private int currentQuestionIndex;
    private ArrayList<Question> questions;
    private ArrayList<Question> allQuestions;
    private ArrayList<Question> rightAnsweredQuestions;
    private ArrayList<Question> wrongAnsweredQuestions;

    private boolean arabic_enabled;
    private boolean solution_enabled;

    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        this.initialize();
    }

    private void initialize() {
        Firebase.setAndroidContext(this);
        arabic_enabled = true;
        rightAnsweredQuestions = new ArrayList<Question>();
        wrongAnsweredQuestions = new ArrayList<Question>();
        fb = new Firebase("https://lebenindeutschland-2be26.firebaseio.com/questions_de_ar");

        fb.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                questions = new ArrayList<Question>();

                for (DataSnapshot q: snapshot.getChildren()) {
                    questions.add(q.getValue(Question.class));
                }
                addItemsOnNrSpinner();
                addItemsOnTypeSpinner();
                currentQuestionIndex = 0;
                displayQuestion(currentQuestionIndex);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        iv_picture = (ImageView) findViewById(R.id.picture_iv);
        rg_choices = (RadioGroup) findViewById(R.id.choices_rq);
        rb_choiceA = (RadioButton) findViewById(R.id.a_rb);
        rb_choiceB = (RadioButton) findViewById(R.id.b_rb);
        rb_choiceC = (RadioButton) findViewById(R.id.c_rb);
        rb_choiceD = (RadioButton) findViewById(R.id.d_rb);

        sp_type = (Spinner) findViewById(R.id.type_sp);

        tv_question = (TextView) findViewById(R.id.question_tv);
        tv_question.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv_question.setTypeface(null, Typeface.BOLD);
        tv_number = (TextView) findViewById(R.id.numver_tv);
        tv_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv_number.setTypeface(null, Typeface.BOLD);

        tv_question.setOnTouchListener(new OnSwipeTouchListener(this.getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                previous();
            }

            @Override
            public void onSwipeRight() {
                advance();
            }
        });

        sp_nr = (Spinner) findViewById(R.id.nr_sp);
        sp_nr.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                        currentQuestionIndex=Integer.valueOf(item)-1;
                        displayQuestion(currentQuestionIndex);
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
        });
        b_submit = (Button) findViewById(R.id.submit_b);
        b_submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (this.answerIsRight()) {
                            rightAnsweredQuestions.add(questions.get(currentQuestionIndex));
                            Toast.makeText(getApplicationContext(), "richtige Antwort " + "\n " + "richtige Antwort Anzahl: "+rightAnsweredQuestions.size(), Toast.LENGTH_LONG).show();
                        } else {
                            wrongAnsweredQuestions.add(questions.get(currentQuestionIndex));
                            Toast.makeText(getApplicationContext(), "falsche Antwort " + "\n " + "falsche Antwort Anzahl: "+wrongAnsweredQuestions.size(), Toast.LENGTH_LONG).show();
                        }
                        // logging
                        Bundle params = new Bundle();
                        params.putString("question_id", String.valueOf(questions.get(currentQuestionIndex).getQuestionID()));
                        mFirebaseAnalytics.logEvent("submit", params);
                    }
                    private boolean answerIsRight() {
                        String answer = "x";
                        int id = rg_choices.getCheckedRadioButtonId();
                        rb_selected = (RadioButton) findViewById(id);
                        if (rb_selected == rb_choiceA) answer = "a";
                        if (rb_selected == rb_choiceB) answer = "b";
                        if (rb_selected == rb_choiceC) answer = "c";
                        if (rb_selected == rb_choiceD) answer = "d";
                        return questions.get(currentQuestionIndex).isCorrectAnswer(answer);
                    }
                }
        );


        sw_arabic = (Switch) findViewById(R.id.arabic_sw);
        sw_arabic.setChecked(true);
        sw_arabic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arabic_enabled = true;
                    sw_arabic.setText("عربي");
                    // logging
                    Bundle params = new Bundle();
                    params.putString("question_id", String.valueOf(questions.get(currentQuestionIndex).getQuestionID()));
                    mFirebaseAnalytics.logEvent("arabic_enabled", params);
                } else {
                    arabic_enabled = false;
                    sw_arabic.setText("الماني");
                    // logging
                    Bundle params = new Bundle();
                    params.putString("question_id", String.valueOf(questions.get(currentQuestionIndex).getQuestionID()));
                    mFirebaseAnalytics.logEvent("arabic_disabled", params);
                }
                displayQuestion(currentQuestionIndex);
            }
        });
    }

    private void displayQuestion(int index) {
        if (arabic_enabled) {
            tv_question.setText(questions.get(currentQuestionIndex).getQuestionText_de() +
                    "\n" + questions.get(currentQuestionIndex).getQuestionText_ar());
            rb_choiceA.setText(questions.get(currentQuestionIndex).getChoiceA_de() +
                    "\n" + questions.get(currentQuestionIndex).getChoiceA_ar());
            rb_choiceB.setText(questions.get(currentQuestionIndex).getChoiceB_de() +
                    "\n" + questions.get(currentQuestionIndex).getChoiceB_ar());
            rb_choiceC.setText(questions.get(currentQuestionIndex).getChoiceC_de() +
                    "\n" + questions.get(currentQuestionIndex).getChoiceC_ar());
            rb_choiceD.setText(questions.get(currentQuestionIndex).getChoiceD_de() +
                    "\n" + questions.get(currentQuestionIndex).getChoiceD_ar());
        } else {
            tv_question.setText(questions.get(currentQuestionIndex).getQuestionText_de());
            rb_choiceA.setText(questions.get(currentQuestionIndex).getChoiceA_de());
            rb_choiceB.setText(questions.get(currentQuestionIndex).getChoiceB_de());
            rb_choiceC.setText(questions.get(currentQuestionIndex).getChoiceC_de());
            rb_choiceD.setText(questions.get(currentQuestionIndex).getChoiceD_de());
        }

        tv_number.setText("Aufgabe "+questions.get(currentQuestionIndex).getQuestionID()+" von "+(questions.size()+1));
        rg_choices.clearCheck();
    }

    private void addItemsOnTypeSpinner() {
        sp_type = (Spinner) findViewById(R.id.type_sp);
        List<String> list = new ArrayList<String>();
        list.add("Alle fragen");
        list.add("Richtige Fragen");
        list.add("Falsche Fragen");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(dataAdapter);
    }

    private void addItemsOnNrSpinner() {
        sp_nr = (Spinner) findViewById(R.id.nr_sp);
        List<String> list = new ArrayList<String>();

        for (Question question : questions) {
            list.add(String.valueOf(question.getQuestionID()));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_nr.setAdapter(dataAdapter);
    }

    private void advance() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size();
        displayQuestion(currentQuestionIndex);
    }

    private void previous() {
        currentQuestionIndex = (currentQuestionIndex - 1) % questions.size();
        if (currentQuestionIndex <0) currentQuestionIndex = questions.size()-1;
        displayQuestion(currentQuestionIndex);
    }
}
