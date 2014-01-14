package us.hale.scott.hadoop.reducer;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;

public class MapReducer extends Reducer<Text, Text, Text, Text> {

	// private MapWritable map = new MapWritable();
	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	private Text outVal = new Text();

	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		map.clear();
		for (Text lang : values) {
			// IntWritable count = (IntWritable) map.get(lang);
			Integer count = map.get(lang.toString());
			if (count == null) {
				// count = new IntWritable(1);
				count = Integer.valueOf(1);
			} else {
				// count.set(count.get()+1);
				count = Integer.valueOf(count.intValue() + 1);
			}
			map.put(lang.toString(), count);

		}
		// context.write(key, map);
		// map.write(context);
		JSONObject json = new JSONObject(map);
		outVal.set(json.toString());
		context.write(key, outVal);
	}
}