package akari.jp.utils;

import java.util.ArrayList;
import java.util.HashMap;

import akari.jp.n5.ListViewActivity;
import akari.jp.n5.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public LazyAdapter(Activity activity,
			ArrayList<HashMap<String, String>> data) {
		this.activity = activity;
		this.data = data;
		inflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null)
			view = inflater.inflate(R.layout.list_row, null);

		TextView txtQuestion = (TextView) view.findViewById(R.id.txtQuestion);
		TextView txtOrder = (TextView) view.findViewById(R.id.txtOrder);
		TextView txtResult = (TextView) view.findViewById(R.id.txtResult);
		TextView txtChoose = (TextView) view.findViewById(R.id.txtChoose);
		ImageView imgResult = (ImageView) view.findViewById(R.id.imgAnswer);

		HashMap<String, String> song = new HashMap<String, String>();
		song = data.get(position);

		txtOrder.setText(song.get(ListViewActivity.KEY_ID));
//		txtQuestion.setText(song.get(ListViewActivity.KEY_QUESTION));
		this.setStyleText(txtQuestion, song.get(ListViewActivity.KEY_QUESTION));
		txtResult.setText(song.get(ListViewActivity.KEY_RESULT));
		txtChoose.setText(song.get(ListViewActivity.KEY_CHOOSE));
		if (song.get(ListViewActivity.KEY_ICON_RESULT).equals("TRUE"))
			imgResult.setImageResource(R.drawable.correct);
		else
			imgResult.setImageResource(R.drawable.incorrect);
		return view;
	}
	
	private void setStyleText(TextView tv, String text) {
		int start = text.indexOf(new String("<u>"));
		int end = text.lastIndexOf( new String("<u/>")) - 3;
		text = text.replace("<u>","");
		text = text.replace("<u/>","");
		Spannable wordtoSpan = new SpannableString(text);
//		StyleSpan style = new StyleSpan(android.graphics.Typeface.ITALIC);
//		wordtoSpan.setSpan(new UnderlineSpan(), start, end,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		tv.setText(wordtoSpan);
	}
}
