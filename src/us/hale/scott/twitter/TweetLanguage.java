package us.hale.scott.twitter;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import us.hale.scott.hadoop.reducer.MapReducer;
import us.hale.scott.twitter.mapper.TweetLanguageMapper;

public class TweetLanguage {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "tweetLanguage");
		job.setJarByClass(TweetLanguage.class);
		job.setMapperClass(TweetLanguageMapper.class);
		job.setReducerClass(MapReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		FileInputFormat
				.addInputPath(
						job,
						new Path("/path/to/twitter_sample.*.queue"));
		FileOutputFormat.setOutputPath(job, new Path("output_tweetLang_clean"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}