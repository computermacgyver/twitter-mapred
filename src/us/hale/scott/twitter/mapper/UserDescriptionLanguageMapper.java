package us.hale.scott.twitter.mapper;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import us.hale.scott.service.CLDService;
import us.hale.scott.service.LanguageClassification;

public class UserDescriptionLanguageMapper extends Mapper<Text, Text, Text, Text> {

	private Text outKey = new Text("*");

	private Text val = new Text();

	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
		try {
			JSONObject json = new JSONObject(key.toString());

			if (!json.has("user")) {
				// System.err.println("No user object");
				return;
			}

			String username = json.getJSONObject("user").getString(
					"screen_name");
			// String text = json.getString("text");
			String text = json.getJSONObject("user").optString("description");
			if (text == null) {
				return;
			}

			outKey.set(username);
			LanguageClassification result = CLDService.detectLanguage(text);
			if (result != null) {
				val.set(result.getLanguage());
				context.write(outKey, val);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
