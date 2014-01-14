package us.hale.scott.twitter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import org.json.JSONObject;

public class WriteGraph {
	
	//private static TreeMap<String,Integer> knownUsers;
	
	public static void main(String[] args) throws IOException {
		
		FileOutputStream fso = new FileOutputStream("graph_ge4_ge2-20_clean-msin.graphml");

		OutputStreamWriter fileWriter = new OutputStreamWriter(fso,Charset.forName("UTF-8"));
		
		fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		fileWriter.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n");
		//tweetLangs,tweetCount,majLang,majLangCount,majLangPercent
		fileWriter.write("<key attr.name=\"label\" attr.type=\"string\" for=\"node\" id=\"label\"/>\n");
		
		fileWriter.write("<key attr.name=\"tweetLangs\" attr.type=\"string\" for=\"node\" id=\"tweetLangs\"/>\n");
		
		fileWriter.write("<key attr.name=\"tweetCount\" attr.type=\"int\" for=\"node\" id=\"tweetCount\"/>\n");
		fileWriter.write("<key attr.name=\"majLang\" attr.type=\"string\" for=\"node\" id=\"majLang\"/>\n");
		fileWriter.write("<key attr.name=\"majLangCount\" attr.type=\"int\" for=\"node\" id=\"majLangCount\"/>\n");
		fileWriter.write("<key attr.name=\"majLangPercent\" attr.type=\"double\" for=\"node\" id=\"majLangPercent\"/>\n");

		fileWriter.write("<key attr.name=\"tweetCountAdj\" attr.type=\"int\" for=\"node\" id=\"tweetCountAdj\"/>\n");
		fileWriter.write("<key attr.name=\"majLangAdj\" attr.type=\"string\" for=\"node\" id=\"majLangAdj\"/>\n");
		fileWriter.write("<key attr.name=\"majLangCountAdj\" attr.type=\"int\" for=\"node\" id=\"majLangCountAdj\"/>\n");
		fileWriter.write("<key attr.name=\"majLangPercentAdj\" attr.type=\"double\" for=\"node\" id=\"majLangPercentAdj\"/>\n");
		fileWriter.write("<key attr.name=\"langCountAdj\" attr.type=\"int\" for=\"node\" id=\"langCountAdj\"/>\n");
		
		
		fileWriter.write("<key attr.name=\"weight\" attr.type=\"double\" for=\"edge\" id=\"weight\"/>\n");

		
		fileWriter.write("<graph edgedefault=\"directed\">\n");
		
		writeNodes(fileWriter);
		
		writeEdges(fileWriter);
		
		fileWriter.write("</graph>\n");
		fileWriter.write("</graphml>\n");
		
		fileWriter.close();
		fso.close();//TODO: UNTESTED
		
	}
	
	private static void writeEdges(Writer fileWriter) {
		
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream("output/output_userMentions_filtered/part-r-00000"));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] data = line.split("\t");
				String[] users = data[0].split(",");
				
				/*Integer userA = knownUsers.get(users[0]);
				Integer userB = knownUsers.get(users[1]);
				
				if (userA==null || userB==null) {
					continue;
				}*/
				
				fileWriter.write("<edge source=\"" + clean(users[0]) + "\" target=\"" + clean(users[1]) + "\">\n");
				//fileWriter.write("<edge source=\"" + userA.toString() + "\" target=\"" + userB.toString() + "\">\n");
				fileWriter.write("\t<data key=\"weight\">" + cleanInt(data[1]) + "</data>\n");
				fileWriter.write("</edge>\n");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public static void writeNodes(Writer fileWriter) {
		//knownUsers = new TreeMap<String,Integer>();
		Scanner scanner;
		int i=0;
		try {
			scanner = new Scanner(new FileInputStream("output/output_tweetLang_clean/part-r-00000"));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] user = line.split("\t");
				if (i<10) {
					System.err.println(user[0]);
				}
				//knownUsers.put(user[0],i);
				i++;
				
				/*
				 * <node id="kunikak">
					<data key="label">kunikak</data>
					</node>
				 */
				
				String username = clean(user[0]);
				fileWriter.write("<node id=\"" + username + "\">\n");
				fileWriter.write("\t<data key=\"label\">" + username + "</data>\n");
				fileWriter.write("\t<data key=\"tweetLangs\">" + user[1] + "</data>\n");
				
				
				JSONObject jData = new JSONObject(user[1]);
				int count=0;
				String majLang="";
				int majLangCnt=0;
				
				int countAdj=0;
				int majLangCntAdj=0;
				String majLangAdj="";
				int totalLangCntAdj=0;
				
				@SuppressWarnings("rawtypes")
				Iterator keys = jData.keys();
				String lang;
				HashMap<String,Integer> map = new HashMap<String,Integer>();
				while (keys.hasNext()) {
					lang=keys.next().toString();
					int langCnt = jData.getInt(lang);
					
					//Combine ms and id, also zh-tw and zh
					if ("in".equals(lang)) {
						lang="ms";
					} else if ("zh-tw".equals(lang)) {
						lang="zh";
					}
					

					map.put(lang, Integer.valueOf(langCnt));
					
					if (langCnt>majLangCnt) {
						majLang=lang;
						majLangCnt=langCnt;
					}
					count+=langCnt;
				}
				
				double dCount = (double)count;
				for(String mlang : map.keySet()) {
					int langCnt = map.get(mlang).intValue();
					if (langCnt>=2 && langCnt/dCount>0.2) {
					//if (langCnt>=2) {
						countAdj+=langCnt;
						totalLangCntAdj++;
						if (langCnt>majLangCntAdj) {
							majLangAdj=mlang;
							majLangCntAdj=langCnt;
						}
					}
				}
				
				fileWriter.write("\t<data key=\"tweetCount\">" + count + "</data>\n");
				fileWriter.write("\t<data key=\"majLang\">" + majLang + "</data>\n");
				fileWriter.write("\t<data key=\"majLangCount\">" + majLangCnt + "</data>\n");
				fileWriter.write("\t<data key=\"majLangPercent\">" + (majLangCnt/(double)count) + "</data>\n");
				
				fileWriter.write("\t<data key=\"tweetCountAdj\">" + countAdj + "</data>\n");
				fileWriter.write("\t<data key=\"majLangAdj\">" + majLangAdj + "</data>\n");
				fileWriter.write("\t<data key=\"majLangCountAdj\">" + majLangCntAdj + "</data>\n");
				fileWriter.write("\t<data key=\"majLangPercentAdj\">" + (majLangCntAdj/(double)countAdj) + "</data>\n");
				fileWriter.write("\t<data key=\"langCountAdj\">" + totalLangCntAdj + "</data>\n");
				
				
				
				fileWriter.write("</node>\n");
				
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	private static String clean(String string) {
		//return string.replaceAll("[^a-zA-Z0-9_]", "");
		return string;
	}
	
	private static String cleanInt(String string) {
		//return string.replaceAll("[^0-9]", "");
		return string;
	}

}
