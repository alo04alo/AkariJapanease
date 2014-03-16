package akari.jp.n5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowResultActivity extends Activity {

	TextView txtScore;
	Button btShowDetail;
	int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_layout);

		txtScore = (TextView) findViewById(R.id.txtScore);
		btShowDetail = (Button) findViewById(R.id.btnShowDetail);

		// Intent getScore = getIntent();
		// String score = getScore.getStringExtra("score");
		score = ((N5Support) getApplication()).getScore();
		txtScore.setText("Your score:" + score);

		btShowDetail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ShowResultActivity.this,
						ListViewActivity.class));
			}
		});

	}

}
