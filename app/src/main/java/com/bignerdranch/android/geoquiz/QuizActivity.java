package com.bignerdranch.android.geoquiz;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private final String TAG = "QuizActivity";
    private final String KEY_INDEX = "index";
    private final String KEY_SCORE = "score";
    private final String KEY_ANSWERED = "answered";
    private final String KEY_ANSWERS = "answers";

    private TextView mQuestionTextView;

    private final Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private UserAnswers mUserAnswers = new UserAnswers(mQuestionBank.length);

    private int mCurrentIndex, mScore, mAnswered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mScore = savedInstanceState.getInt(KEY_SCORE, 0);
            mAnswered = savedInstanceState.getInt(KEY_ANSWERED, 0);
            mUserAnswers = savedInstanceState.getParcelable(KEY_ANSWERS);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        final View mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        final View mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        final View mPreviousButton = findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

        final View mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_SCORE, mScore);
        savedInstanceState.putInt(KEY_ANSWERED, mAnswered);
        savedInstanceState.putParcelable(KEY_ANSWERS, mUserAnswers);
    }

    private void previousQuestion() {
        mCurrentIndex--;
        if (mCurrentIndex < 0) {
            mCurrentIndex = mQuestionBank.length - 1;
        }
        updateQuestion();
    }

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void checkAnswer(boolean userPressedTrue) {
        int messageResId = R.string.already_answered;

        // Not answered yet
        if (mUserAnswers.get(mCurrentIndex) == null) {
            mUserAnswers.set(mCurrentIndex, userPressedTrue);

            mAnswered++;

            if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue()) {
                messageResId = R.string.correct_toast;
                mScore++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();

        if (mAnswered == mQuestionBank.length) {
            Toast.makeText(this, getResources().getString(R.string.total_score) + mScore, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}


class UserAnswers implements Parcelable {
    private Boolean[] mAnswers;

    UserAnswers(int answersNumber) {
        mAnswers = new Boolean[answersNumber];
    }


    Boolean get(int index) {
        if (index >= 0 && index < mAnswers.length) {
            return mAnswers[index];
        }

        return null;
    }

    void set(int index, Boolean value) {
        if (index >= 0 && index < mAnswers.length) {
            mAnswers[index] = value;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(this.mAnswers);
    }

    private UserAnswers(Parcel in) {
        this.mAnswers = (Boolean[]) in.readArray(Boolean[].class.getClassLoader());
    }

    public static final Parcelable.Creator<UserAnswers> CREATOR = new Parcelable.Creator<UserAnswers>() {
        @Override
        public UserAnswers createFromParcel(Parcel source) {
            return new UserAnswers(source);
        }

        @Override
        public UserAnswers[] newArray(int size) {
            return new UserAnswers[size];
        }
    };
}