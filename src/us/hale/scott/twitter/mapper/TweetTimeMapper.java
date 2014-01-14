package us.hale.scott.twitter.mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

public class TweetTimeMapper extends Mapper<Text, Text, Text, LongWritable> {

	private Text outKey = new Text("*");

	private LongWritable val = new LongWritable();
	
	//Wed Aug 27 13:08:45 +0000 2008
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
	

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

			String dateStr = json.getString("created_at");
			Date date = dateFormat.parse(dateStr.replace("+0000", ""));
			
			
			
			outKey.set(username);
			if (date != null) {
				val.set(date.getTime());
				context.write(outKey, val);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
