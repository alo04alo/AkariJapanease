package akari.jp.n5;

import akari.jp.base.CountUpTimer;
import akari.jp.base.DatabaseHandler;
import akari.jp.base.Question;
import akari.jp.base.QuestionHandler;
import akari.jp.utils.Debug;
import akari.jp.utils.DefineVariable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ListeningActivity extends Activity {
	TextView txtQuestion;
	TextView txtScore;
	TextView txtTimer;
	
	Button btnShowDetailResult;
	Button btnReTest;
	Button btnHome;
	
	Button btnAnswer1;
	Button btnAnswer2;
	Button btnAnswer3;
	Dialog dialogShowResult;

	DatabaseHandler database;
	QuestionHandler questionHandle;
	Question[] questions;
	Question question;
	DefineVariable defineVariable;
	CountUpTimer countUpTimer;
	// the oder of current question
	int count;
	int kind;
	int form;
	int score;
		
	Context ctx;
	MediaPlayer media;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listening_layout);
		
		countUpTimer = new CountUpTimer();
		form = ((N5Support) getApplication()).getForm();
		kind = ((N5Support) getApplication()).getKind();
		
		txtQuestion = (TextView) findViewById(R.id.txt_question);
		btnAnswer1 = (Button) findViewById(R.id.btn_result1);
		btnAnswer2 = (Button) findViewById(R.id.btn_result2);
		btnAnswer3 = (Button) findViewById(R.id.btn_result3);
		
		count = 0;
		ctx = this;
		media = new MediaPlayer();
		defineVariable = new DefineVariable();
		database = new DatabaseHandler(getApplication());
		database.openDataBase();
		questions = database.getQuestion(form, kind, defineVariable.MAX_QUESTION);
		nextQuestion();
		questionHandle = new QuestionHandler(questions);
		countUpTimer.startTimer();
		btnAnswer1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (question != null)
					question.setChoose(0);
				nextQuestion();
			}
		});
		btnAnswer2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (question != null)
					question.setChoose(1);
				nextQuestion();
			}
		});
		btnAnswer3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (question != null)
					question.setChoose(2);
				nextQuestion();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void setText(Question question) {
		btnAnswer1.setText(question.getAnswer1());
		btnAnswer2.setText(question.getAnswer2());
		btnAnswer3.setText(question.getAnswer3());
		Debug.out(question.getResource());
		audioPlayer(question.getResource());
		
	}
	
	private void audioPlayer(String fileName){
		int resID = getResources().getIdentifier(fileName, "raw", getPackageName());
		media = MediaPlayer.create(ctx, resID);
		try {
			media.seekTo(0);
			media.start();
			Runtime.getRuntime().gc();
		} catch (Exception e) {
			Debug.out("File is not exist");
			e.printStackTrace();
		}
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
		media.stop();
		score = ((N5Support) getApplication()).getScore();
		countUpTimer.pauseTimer();
		if (count < defineVariable.MAX_QUESTION) {
			showDialogSubmit();
		} else {
			dialogShowResult = createDialogResult();
			dialogShowResult.show();
		}
	}

	private Dialog createDialogResult() {
		dialogShowResult = new Dialog(ListeningActivity.this);
		dialogShowResult.setContentView(R.layout.result_layout);

		txtScore = (TextView) dialogShowResult.findViewById(R.id.txtScore);
		txtTimer = (TextView)dialogShowResult.findViewById(R.id.txtTime);
		btnShowDetailResult = (Button) dialogShowResult.findViewById(R.id.btnShowDetail);
		btnHome = (Button)dialogShowResult.findViewById(R.id.btnHome);
		btnReTest = (Button)dialogShowResult.findViewById(R.id.btnReTest);
		
		txtScore.setText("Your score: " + score);
		txtTimer.setText("Time: " + countUpTimer.getMinutes() + ":" + String.format("%02d", countUpTimer.getSeconds()));
		
		btnShowDetailResult.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ListeningActivity.this,
						ListViewActivity.class));
			}
		});
		
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
			}
		});
		
		btnReTest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ctx, ListeningActivity.class));
			}
		});
		// dialogShowResult.show();
		return dialogShowResult;

	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
//		startActivity(new Intent(ctx, MainActivity.class));
//		finishStudy();
//		finish();
	}

	private void showDialogSubmit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
		alertDialog.setTitle("Are you sure you want to finish test ?");
		alertDialog.setIcon(R.drawable.ic_action_warning);

		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialogShowResult = createDialogResult();
						dialogShowResult.show();
					}
				});

		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
		alertDialog.show();
	}
}
