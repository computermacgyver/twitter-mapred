package us.hale.scott.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class UrlUtils {
	
	private static final Logger LOG =
			Logger.getLogger(UrlUtils.class.getName());
	
	List<String> domainList;
	
	public UrlUtils() {
		//Initialize list with public suffix list (http://publicsuffix.org/list/)
		domainList=null;
		
		baseURL = null;
		lastBase = null;
	}
	
	public UrlUtils(List<String> domainList) {
		this.domainList=domainList;
	}
	
	public String getDomainPart(String domain, int part) {
		
		String domainClean = getDomain(domain);
		String[] domainParts = domainClean.split("\\.");
		StringBuilder sb = new StringBuilder();
		
		int start = domainParts.length-part;
		if (start<=0) {
			return domainClean;
		} else {
			sb.append(domainParts[start]);
			for (int i=start+1; i<domainParts.length; i++) {
				sb.append(".").append(domainParts[i]);
			}			
		}
		
		
		return sb.toString();
	}
	
	public String getPublicSuffix(String domain) {
		return null;
	}
	
	public static String getDomain(String url) {
		if (url==null) {
			return "";
		}
		int start = url.indexOf("://");
		if (start!=-1) {
			start+=3;
		} else {
			start=0;
		}
		
		int port = url.indexOf(":",start);
		if (port==-1) {
			port=url.length();
		}
		
		int end = url.indexOf("/",start);
		if (end==-1) {
			end=url.length();
		}
		return url.substring(start,Math.min(port, end));
	}
	
	public static void main (String[] args) {
		String test = "http://www.ox.ac.uk:8080/a/b/c";
		UrlUtils utils = new UrlUtils();
		
		System.out.println(utils.getDomainPart(test, 2));
		
		assert utils.getDomainPart(test, 1).equals("uk");
		assert utils.getDomainPart(test, 2).equals("ac.uk");
		assert utils.getDomainPart(test, 3).equals("ox.ac.uk");
		assert utils.getDomainPart(test, 4).equals("www.ox.ac.uk");
		assert utils.getDomainPart(test, 10).equals("www.ox.ac.uk");
		
		System.out.println("Success");
		
		
	}
	
	
	//Copied from Internet Archive
	
	private URL baseURL;
	private String lastBase;
	
	private boolean isAbsolute(String url) {
		return url.startsWith("http://")
			|| url.startsWith("https://")
			|| url.startsWith("ftp://")
			|| url.startsWith("feed://")
			|| url.startsWith("mailto:")
			|| url.startsWith("mail:")
			|| url.startsWith("javascript:")
			|| url.startsWith("rtsp://");
	}

	private String resolve(String base, String rel) {
		URL absURL = null;
		if(lastBase != null) {
			if(lastBase.equals(base)) {
				try {
					absURL = new URL(baseURL,rel);
				} catch (MalformedURLException e) {
					LOG.warning("Malformed rel url:" + rel);
					return null;
				}
			}
		}
		if(absURL == null) {
			try {
				baseURL = new URL(base);
				lastBase = base;
			} catch (MalformedURLException e) {
				LOG.warning("Malformed base url:" + base);
				return null;
			}
			try {
				absURL = new URL(baseURL,rel);
			} catch (MalformedURLException e) {
				LOG.warning("Malformed rel url:" + rel);
				return null;
			}
		}
		return absURL.toString();
	}
	public String doResolve(String page, String base, String url) {
		if((url == null) || (url.length() == 0)) {
			return null;
		}
		if(isAbsolute(url)) {
			return url;
		}
		if((base != null) && (base.length() > 0)) {
			String tmp = resolve(base,url);
			if(tmp != null) {
				return tmp;
			}
		}
		if((page != null) && (page.length() > 0)) {
			String tmp = resolve(page,url);
			if(tmp != null) {
				return tmp;
			}
		}
		return url;
	}
	

}
