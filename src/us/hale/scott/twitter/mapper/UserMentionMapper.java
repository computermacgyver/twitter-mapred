package us.hale.scott.twitter.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserMentionMapper extends Mapper<Text, Text, Text, IntWritable> {
	private IntWritable val = new IntWritable(1);
	private Text outKey = new Text();

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
			String reply = json.optString("in_reply_to_screen_name");
			if (reply != null && !"".equals(reply)) {
				outKey.set(username + "," + reply);
				context.write(outKey, val);
				// System.err.println("[OUTPUT] " + outKey);
			}

			JSONObject entities = json.getJSONObject("entities");
			JSONArray mentions;
			if (entities != null && entities.has("user_mentions")
					&& !entities.isNull("user_mentions")) {
				mentions = entities.getJSONArray("user_mentions");
				for (int i = 0; i < mentions.length(); i++) {
					String name = mentions.getJSONObject(i).getString(
							"screen_name");
					if (!reply.equalsIgnoreCase(name)) {
						outKey.set(username + "," + name);
						context.write(outKey, val);
						// System.err.println("[OUTPUT] " + outKey);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
