package us.hale.scott.twitter.mapper;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeotagMapper extends Mapper<Text, Text, Text, Text> {
	private Text val = new Text("");
	private Text outKey = new Text();

	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
		try {
			JSONObject json = new JSONObject(key.toString());

			if (!json.has("user")) {
				// System.err.println("No user object");
				return;
			}

			JSONObject user = json.getJSONObject("user");
			String username = user.getString("screen_name");
			
			JSONObject coord = json.optJSONObject("coordinates");
			double lat=0;
			double lng=0;
			if (coord!=null) {
				JSONArray points = coord.getJSONArray("coordinates");
				lng=points.getDouble(0);
				lat=points.getDouble(1);
			}
			
			
			String location = user.optString("location");
			String timezone = user.optString("time_zone");
			int offset = user.optInt("utc_offset");
			

			outKey.set(username);
			val.set(lat+"\t"+lng+"\t"+offset+"\t"+timezone+"\t"+location);
			context.write(outKey, val);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
