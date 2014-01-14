package net.scotthale.twitter.mapper;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import us.hale.scott.service.CLDService;
import us.hale.scott.service.LanguageClassification;

public class TweetLanguageMapper extends Mapper<Text, Text, Text, Text> {

	private Text outKey = new Text("*");

	private Text val = new Text();

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
			String text = json.getString("text");
			
			outKey.set(username);
			String clean=cleanText(text);
			if (clean!=null) {
				LanguageClassification result = CLDService.detectLanguage(clean);
				if (result != null){ // && result.getConfidence()>50) {
					val.set(result.getLanguage());
					context.write(outKey, val);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String cleanText(String s) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(s);
		int tokenCount=0;
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.startsWith("#") || token.startsWith("http://") ||
				token.startsWith("https://") || token.startsWith("@")) {
				continue;
			} else {
				sb.append(token).append(" ");
				tokenCount++;
			}
		}
		if (tokenCount==0) {
			return null;
		} else {
			return sb.toString();
		}
	}
	
	public static void main(String[] args) {
		TweetLanguageMapper tlm = new TweetLanguageMapper();
		System.out.println(tlm.cleanText("@yo how are you? http://bit.ly/1238484"));
		String text = tlm.cleanText("@EIJIMAN0521 @dazzpon @vivagta @Osaru_tencho @hancock_hime @1427xx @akiraDI6 華麗なる豚や #bypokevega");
		System.out.println(text);
		
		
		LanguageClassification result = CLDService.detectLanguage(text);
		if (result != null) {
			System.out.println(result.getLanguage());
			System.out.println(result.getConfidence());
		}
		
	}
}
