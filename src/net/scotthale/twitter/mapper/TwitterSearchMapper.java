package net.scotthale.twitter.mapper;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class TwitterSearchMapper extends Mapper<Text, Text, Text, Text> {
	
	private static Logger logger = Logger.getLogger(TwitterSearchMapper.class.getName());
	
	private Text val = new Text();
	private Text outKey = new Text();

	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
		try {
			JSONObject json = new JSONObject(key.toString());

			JSONArray tweets;
			if (json.has("results")) {
				tweets=json.getJSONArray("results");//v1API
			} else if (json.has("statuses")){
				tweets=json.getJSONArray("statuses");//v1.1API
			} else {
				logger.warning("No results or statuses array");
				return;
			}
			
			for (int i=0; i<tweets.length(); i++) {
				JSONObject tw = tweets.getJSONObject(i);
				outKey.set(tw.getString("id_str"));
				val.set(tw.toString());
				context.write(outKey, val);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
