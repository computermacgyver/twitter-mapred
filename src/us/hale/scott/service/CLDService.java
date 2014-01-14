package us.hale.scott.service;

import com.mzsanford.cld.CompactLanguageDetector;
import com.mzsanford.cld.LanguageDetectionCandidate;
import com.mzsanford.cld.LanguageDetectionResult;

public class CLDService {
	
	static CompactLanguageDetector myCLD =  new CompactLanguageDetector();
	
	public static LanguageClassification detectLanguage(String text) {
		LanguageDetectionResult result = myCLD.detect(text);
		 
		if (result!=null && result.getCandidates()!=null && result.getCandidates().size()!=0) {
			LanguageDetectionCandidate candidate = result.getCandidates().get(0);
			if (candidate.getLocale()!=null) {
				return new LanguageClassification(candidate.getLocale().getLanguage(),candidate.getNormalizedScore());
			}			
		}
		return null;
	}

	public static void main(String[] args) {
		CompactLanguageDetector compactLanguageDetector = new CompactLanguageDetector();
		LanguageDetectionResult result = compactLanguageDetector.detect("This is my sample text");
		if (result.isReliable()) {
		  // getProbableLocale returns a java.util.Locale
		  System.out.println("Pretty sure that's " + result.getProbableLocale().getDisplayName());
		} else {
		  for (LanguageDetectionCandidate candidate : result.getCandidates()) {
		    System.out.println("Maybe it's " + candidate.getLocale().getDisplayName());
		  }
		}
	}
}
