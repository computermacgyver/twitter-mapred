package us.hale.scott.service;

public class LanguageClassification {
	private final String language;
	private final double confidence;
	public LanguageClassification(String language, double confidence) {
		this.language=language;
		this.confidence=confidence;
	}
	public String getLanguage() {
		return language;
	}
	public double getConfidence() {
		return confidence;
	}
}