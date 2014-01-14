package net.scotthale.twitter.mapper;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

import us.hale.scott.service.UrlUtils;

public class TweetLinkMapper extends Mapper<Text, Text, Text, Text> {

	private Text outKey = new Text("*");

	private Text val = new Text("*");	
	
	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
		try {
			JSONObject json = new JSONObject(key.toString());

			if (!json.has("user")) {
				// System.err.println("No user object");
				return;
			}
			
			String timezone = json.getJSONObject("user").optString("time_zone");
			outKey.set(timezone);
			
			JSONArray urls = json.getJSONObject("entities").getJSONArray("urls");
			for (int j=0; j<urls.length(); j++) {
				JSONObject url = urls.getJSONObject(j);
				String link = url.getString("url");
				if (url.has("expanded_url") && !url.isNull("expanded_url")) {
					link=url.getString("expanded_url");
				}
				if (link != null) {
					link=UrlUtils.getDomain(link);
					val.set(link);
					context.write(outKey, val);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
