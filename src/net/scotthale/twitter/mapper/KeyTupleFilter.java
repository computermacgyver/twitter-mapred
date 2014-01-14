package net.scotthale.twitter.mapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class KeyTupleFilter extends Mapper<Text, Text, Text, Text> {
	
	private static TreeMap<String,String> knownUsers; 
	static {
		//Populate knownUsers


		knownUsers = new TreeMap<String,String>();//init size 7266801
	
		Scanner scanner;
		int i=0;
		try {
			scanner = new Scanner(new FileInputStream("output_tweetLang/part-r-00000"));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] user = line.split("\t");
				if (++i<10) {
					System.err.println(user[0]);
				}
				knownUsers.put(user[0],"");
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.err.println("READY: size is " + knownUsers.size());
	}
	
		
	@Override
	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
		
		String users[] = key.toString().split(",",2);
		if (users.length!=2) {
			System.err.print("Users array of unexpected length: " + key.toString());
			return;
		}
		
		if (knownUsers.containsKey(users[0].trim()) && knownUsers.containsKey(users[1].trim())) {
			context.write(key, value);
		}
	}
}
