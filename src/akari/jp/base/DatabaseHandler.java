package akari.jp.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import akari.jp.utils.Debug;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "QuestionN5DB.sqlite";
	private static final String TABLE_NAME = "questions";
	private static final String DATABASE_PATH = "/data/data/akari.jp.n5/databases/";
	private SQLiteDatabase myDB;
	private final Context ctx;
	public static final String TAG = "Database";
	private String sql;
	
	private static final String ID = "id";
	private static final String FORM = "form";
	private static final String KIND = "kind";
	private static final String CONTENT = "content";
	private static final String RESULT = "result";
	private static final String ANSWER1 = "answer1";
	private static final String ANSWER2 = "answer2";
	private static final String ANSWER3 = "answer3";
	private static final String NOTE = "note";
	private static final String COUNT = "count";

	private static final String[] COLUMNS = { ID, FORM, KIND, CONTENT, RESULT,
			ANSWER1, ANSWER2, ANSWER3, NOTE, COUNT };
	private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE "
			+ TABLE_NAME + "("+ ID + " INTEGER PRIMARY KEY," + FORM + " TEXT,"
			+ KIND + " TEXT," + CONTENT + " TEXT," + RESULT + " INTEGER,"
			+ ANSWER1 + " TEXT," + ANSWER2 + " TEXT," + ANSWER3 + " TEXT,"
			+ NOTE + " TEXT," + COUNT + " INTEGER" + ")";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_QUESTIONS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		this.onCreate(db);
	}

	public synchronized void close() {
		if (myDB != null)
			myDB.close();
		super.close();
	}

	public boolean checkAndCopyDatabase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			Log.d(TAG, "database already exist!");
			return true;
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.d(TAG, "Error copying database");
			}
			return false;
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
		}
		if (checkDB != null)
			checkDB.close();
		return checkDB != null ? true : false;
	}
	
	public boolean checkDatabaseExist(){
		String myPath = DATABASE_PATH + DATABASE_NAME;
		File file = new File(myPath);
		if(file.exists()){
			return true;
		}else{
			return false;
		}
		
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[2 * 1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public SQLiteDatabase openDataBase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		try {
			myDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			return myDB;
		} catch (Exception e) {
			Debug.out("Can't open database");
			return null;
		}

	}

	public Question getOneQuestion(int id) {
		Cursor cursor = null;
		try {
			cursor = myDB.query(TABLE_NAME, COLUMNS, ID + " = " + id, null,
					null, null, null);
		} catch (Exception e) {
			Debug.out(e.toString());
		}
		Question question = new Question();
		if (cursor.moveToFirst()) {
			do {
				question.setId(cursor.getInt(0));
				question.setForm(cursor.getInt(1));
				question.setKind(cursor.getInt(2));
				question.setContent(cursor.getString(3));
				question.setResult(cursor.getInt(4));
				question.setAnswer1(cursor.getString(5));
				question.setAnswer2(cursor.getString(6));
				question.setAnswer3(cursor.getString(7));
				question.setNote(cursor.getString(8));
				question.setCount(cursor.getInt(9));

			} while (cursor.moveToNext());
		}
		return question;
	}

	/**
	 * 
	 * @return many questions
	 */
	public Question[] getQuestion(int form, int kind, int limit) {
		Question[] questions = new Question[limit];
		sql = "select * from " + TABLE_NAME + " where form = " + form
				+ " and kind = " + kind + " order by count asc limit 0,"
				+ limit;
		Cursor cusor = myDB.rawQuery(sql, null);
		int i = 0;
		if (cusor != null && cusor.getCount() > 0) {
			if (cusor.moveToFirst()) {
				do {
					questions[i] = new Question();
					questions[i].setId(cusor.getInt(cusor.getColumnIndex(ID)));
					questions[i].setForm(Integer.parseInt(cusor.getString(cusor
							.getColumnIndex(FORM))));
					questions[i].setKind(cusor.getInt(cusor
							.getColumnIndex(KIND)));
					questions[i].setContent(cusor.getString(cusor
							.getColumnIndex(CONTENT)));
					questions[i].setResult(cusor.getInt(cusor
							.getColumnIndex(RESULT)));
					questions[i].setAnswer1(cusor.getString(cusor
							.getColumnIndex(ANSWER1)));
					questions[i].setAnswer2(cusor.getString(cusor
							.getColumnIndex(ANSWER2)));
					questions[i].setAnswer3(cusor.getString(cusor
							.getColumnIndex(ANSWER3)));
					questions[i].setNote(cusor.getString(cusor
							.getColumnIndex(NOTE)));
					questions[i].setCount(cusor.getInt(cusor
							.getColumnIndex(COUNT)));
					i++;
				} while (cusor.moveToNext());
			}
		}
		return questions;
	}

	public int updateOneQuestion(Question question) {
		ContentValues values = new ContentValues();
		values.put(COUNT, question.getCount() + 1);
		return myDB.update(TABLE_NAME, values, ID + " = ?",
				new String[] { String.valueOf(question.getId()) });
	}
}