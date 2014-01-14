package net.scotthale.twitter;

import net.scotthale.twitter.mapper.GeotagMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class GeotagExtract {
	

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "userMentions");
		job.setJarByClass(GeotagExtract.class);
		job.setMapperClass(GeotagMapper.class);
		//job.setReducerClass(...);//Identity reducer
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		FileInputFormat
				.addInputPath(
						job,
						new Path("/path/to/twitter_sample.*.queue"));
		FileOutputFormat.setOutputPath(job, new Path("output_geotag"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}