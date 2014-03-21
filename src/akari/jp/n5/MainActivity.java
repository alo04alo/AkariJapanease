package akari.jp.n5;

import akari.jp.base.DatabaseHandler;
import akari.jp.base.FileManager;
import akari.jp.base.Question;
import akari.jp.base.QuestionHandler;
import akari.jp.base.Xml;
import akari.jp.utils.Debug;
import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	DatabaseHandler database;
	QuestionHandler questionHandle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		FileManager.create(getApplication());
		database = new DatabaseHandler(this);	
		database.getWritableDatabase();
//		if(database.checkDatabaseExist() == false){
			this.insertDb();
			Debug.out("Insert data from xls file to DB successfully");
//		}
		Debug.out("Hello");
		
//		database = new DatabaseHandler(getApplication());
//		database.checkAndCopyDatabase();
//		database.openDataBase();
		/**
		 * Creating all buttons instances
		 * */
		Button btn_grammar = (Button) findViewById(R.id.btn_grammar);

		Button btn_kanji = (Button) findViewById(R.id.btn_kanji);

		Button btn_katakana = (Button) findViewById(R.id.btn_katakana);

		Button btn_listening = (Button) findViewById(R.id.btn_listening);

		Button btn_practice = (Button) findViewById(R.id.btn_practice);

		Button btn_word = (Button) findViewById(R.id.btn_word);

		/**
		 * Handling all button click events
		 * */

		btn_grammar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.this, GrammarActivity.class);
				((N5Support) getApplication()).setForm(0);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});

		btn_kanji.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						KanjiActivity.class);
				((N5Support) getApplication()).setForm(0);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});

		btn_katakana.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						KatakanaActivity.class);
				((N5Support) getApplication()).setForm(1);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});

		btn_listening.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						ListeningActivity.class);
				((N5Support) getApplication()).setForm(0);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});

		btn_practice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(),
						PracticeActivity.class);
				((N5Support) getApplication()).setForm(0);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});

		btn_word.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						WordActivity.class);
				((N5Support) getApplication()).setForm(0);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});
	}

	 public void insertDb() {
		  Xml xml = FileManager.loadXmlFromHome("data17.xml");
		  if (xml == null)
		   return;
		  Xml questionXml = xml.getChild("Question");
		  Xml question = this.nextQuestion(questionXml);
		  while (question != null) {
			   question = this.nextQuestion(question);
		  }
		 }

	private Xml nextQuestion(Xml questionXml) {
		Debug.out("Hello next");
		String content = "";
		String answer1 = "";
		String answer2 = "";
		String answer3 = "";
		String answer4 = "";		
		int form = 0;
		int result = 0;
		if (questionXml != null) {
			content = questionXml.getAttrib("content");
			form = Integer.parseInt(questionXml.getAttrib("form"));
			result = Integer.parseInt(questionXml.getAttrib("correctAnswer"));
			Xml answer1Xml = questionXml.getChild("Answer1");
			answer1 = answer1Xml.getAttrib("content");
			Xml answer2Xml = questionXml.getChild("Answer2");
			answer2 = answer2Xml.getAttrib("content");
			Xml answer3Xml = questionXml.getChild("Answer3");
			answer3 = answer3Xml.getAttrib("content");
			Xml answer4Xml = questionXml.getChild("Answer4");
			answer4 = answer4Xml.getAttrib("content");
		}
		
		Question qu = new Question(form, 0, content, result, answer1,
				answer2, answer3, "", 0);
		Debug.out(answer4);
		if (database.checkExistQuestion(content) == false) {
			database.addQuestion(qu);
		}		
		if (questionXml == null)
			   return null;
		return questionXml.getNext();
	}
}
