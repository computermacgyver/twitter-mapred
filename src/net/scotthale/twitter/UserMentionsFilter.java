package net.scotthale.twitter;

import net.scotthale.twitter.mapper.KeyTupleFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class UserMentionsFilter {
	

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "userMentionsFilter");
		job.setJarByClass(UserMentionsFilter.class);
		job.setMapperClass(KeyTupleFilter.class);
		job.setReducerClass(Reducer.class);//Identity class (output matches input)
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);//This is <Text,Text> key--value
		FileInputFormat.addInputPath(job,new Path("output_userMentions/part-r-00000"));
		FileOutputFormat.setOutputPath(job, new Path("output_userMentions_filtered"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}