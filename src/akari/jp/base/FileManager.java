package akari.jp.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class FileManager {

	public static void create(Context context) {
		FileManager.context = context;
	}

	public static Xml loadXmlFromResource(int id) {
		String line;
		StringBuilder content = new StringBuilder();
		try {
			InputStream inputStream = context.getResources()
					.openRawResource(id);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));
			line = reader.readLine();
			while (line != null) {
				content.append(line);
				line = reader.readLine();
			}
			Log.i("xml", content.toString());
			Xml xml = new Xml(content.toString());
			if (xml.isValid())
				return xml;
			else
				return null;
		} catch (IOException e) {
			return null;
		}
	}

	public static Xml loadXmlFromHome(String source) {
		String line;
		StringBuilder content = new StringBuilder();
		AssetManager assetManager = context.getAssets();
		try {
			InputStream inputStream = assetManager.open(source);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));
			line = reader.readLine();
			while (line != null) {
				content.append(line);
				line = reader.readLine();
			}
			Log.i("xml", content.toString());
			Xml xml = new Xml(content.toString());
			if (xml.isValid())
				return xml;
			else
				return null;
		} catch (IOException e) {
			return null;
		}
	}

	private static Context context;

}
