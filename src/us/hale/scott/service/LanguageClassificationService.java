package us.hale.scott.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;*/


import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

public class LanguageClassificationService {

	private static final String PROFILES_FOLDER = "profiles/";
		
	public LanguageClassificationService() {
		try {
			DetectorFactory.clear();
			URL jar=DetectorFactory.class.getProtectionDomain().getCodeSource().getLocation();
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry ze = null;
			List<String> profiles = new ArrayList<String>();
			while( ( ze = zip.getNextEntry() ) != null ) {
				if ( ze.getName().startsWith(PROFILES_FOLDER)) {

					InputStream is=DetectorFactory.class.getResourceAsStream("/" + ze.getName());
					if (is!=null) {
				    	BufferedReader br= new BufferedReader(new InputStreamReader(is));
			 
				    	StringBuilder sb = new StringBuilder();
			 
				    	String line;
				    	while ((line = br.readLine()) != null) {
				    		sb.append(line);
				    	} 
			 
				    	line=sb.toString();
				    	if (line!=null && !"".equals(line)) {
				    		profiles.add(sb.toString());
				    	}
				    	br.close();
					}
				}
			}
			DetectorFactory.loadProfile(profiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LanguageClassification classifyText(String text) {
		if (text!=null && !"".equals(text)) {
			try {
				Detector detector = DetectorFactory.create();
				detector.append(text);
				Language lang = detector.getProbabilities().get(0);
				return new LanguageClassification(lang.lang,lang.prob);
			} catch (LangDetectException e) {
				//e.printStackTrace();
				return null;
			}
		} else {
			return new LanguageClassification("blank",0);
		}
	}
	
	/*public LanguageClassification classifyHtml(String html) {
		Document htmlDoc = Jsoup.parse(html);
		System.out.println("Text is: " + htmlDoc.text());
		return classifyText(htmlDoc.text());
	}*/
}
