package net.scotthale.twitter;

import net.scotthale.twitter.mapper.UserMentionMapper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import us.hale.scott.hadoop.reducer.SumReducer;

public class UserMentions {
	

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "userMentions");
		job.setJarByClass(UserMentions.class);
		job.setMapperClass(UserMentionMapper.class);
		job.setReducerClass(SumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		FileInputFormat
				.addInputPath(
						job,
						new Path("/path/to/twitter_sample.*.queue"));
		FileOutputFormat.setOutputPath(job, new Path("output_userMentions"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}