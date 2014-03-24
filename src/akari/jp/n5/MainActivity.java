package akari.jp.n5;

import akari.jp.base.DatabaseHandler;
import akari.jp.base.FileManager;
import akari.jp.base.QuestionHandler;
import akari.jp.utils.Debug;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	DatabaseHandler database;
	QuestionHandler questionHandle;
	DatabaseHandler databaseHandle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		FileManager.create(getApplication());
		database = new DatabaseHandler(this);		
		if(database.checkDatabaseExist() == false){
			Debug.out("Insert data from xls file to DB successfully");
		}
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
		 * Copy DB from asset to SD card
		 */	
		databaseHandle = new DatabaseHandler(MainActivity.this);		
		if (databaseHandle.checkDatabaseExist() == false) {			
			databaseHandle.checkAndCopyDatabase();
		}		
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
				((N5Support) getApplication()).setForm(0);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});

		btn_listening.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						ListeningActivity.class);
				((N5Support) getApplication()).setForm(4);
				((N5Support) getApplication()).setKind(0);
				startActivity(i);
			}
		});

		btn_practice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
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

}
