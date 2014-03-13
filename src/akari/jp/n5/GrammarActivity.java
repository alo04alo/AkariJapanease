package akari.jp.n5;

import akari.jp.base.DatabaseHandler;
import akari.jp.base.Question;
import akari.jp.base.QuestionHandler;
import akari.jp.utils.DefineVariable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GrammarActivity extends Activity {
	TextView txtQuestion;
	Button btnAnswer1;
	Button btnAnswer2;
	Button btnAnswer3;
	DatabaseHandler database;
	QuestionHandler questionHandle;
	Question[] questions;
	Question question;
	DefineVariable defineVariable;
	// the oder of current question
	int count;
	int kind;
	int form;
	Context ctx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_layout);

		form = ((N5Support) getApplication()).getForm();
		kind = ((N5Support) getApplication()).getKind();
		txtQuestion = (TextView) findViewById(R.id.txt_question);
		btnAnswer1 = (Button) findViewById(R.id.btn_result1);
		btnAnswer2 = (Button) findViewById(R.id.btn_result2);
		btnAnswer3 = (Button) findViewById(R.id.btn_result3);
		count = 0;
		ctx = this;
		defineVariable = new DefineVariable();
		database = new DatabaseHandler(getApplication());
		database.openDataBase();
		questions = database.getQuestion(form, kind, defineVariable.MAX_QUESTION);
		nextQuestion();
		questionHandle = new QuestionHandler(questions);

		btnAnswer1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (question != null)
					question.setChoose(1);
				nextQuestion();
			}
		});
		btnAnswer2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (question != null)
					question.setChoose(2);
				nextQuestion();
			}
		});
		btnAnswer3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (question != null)
					question.setChoose(3);
				nextQuestion();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void setText(Question question) {
		txtQuestion.setText(question.getContent());
		btnAnswer1.setText(question.getAnswer1());
		btnAnswer2.setText(question.getAnswer2());
		btnAnswer3.setText(question.getAnswer3());
	}

	private void nextQuestion() {
		if (count < defineVariable.MAX_QUESTION) {
			question = questions[count];
			database.updateOneQuestion(question);
			this.setText(question);
			count++;
		} else {
			finishStudy();
		}
	}

	private void finishStudy() {
		((N5Support) getApplication()).setScore(questionHandle.getScore());
		((N5Support) getApplication()).setQuestions(questions);
		((N5Support) getApplication()).setQuestionHandler(questionHandle);
		startActivity(new Intent(GrammarActivity.this, ShowResultActivity.class));
	}
}
