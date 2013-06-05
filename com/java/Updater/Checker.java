package com.java.Updater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 */

/**
 * @author Manikandan.A 25-May-2013 12:52:50 PM Checker.java
 *
 */
public class Checker {
	
	public static String  current_version , avail_version , download_url;
	public static final String __UPDATE_XML_FILE__ = "update-config.xml" , __DOWNLOAD_TEMP_DIR__ = System.getProperty("java.io.tmpdir");
	public static Float current,avail;
	public static boolean isGrater = false;  
	
	
	
	public static boolean Check() throws ParserConfigurationException, SAXException, IOException, NoSuchAlgorithmException, KeyManagementException {
		File xml = new File(__UPDATE_XML_FILE__);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(xml);
		document.getDocumentElement().normalize();
		
		NodeList current_ver = document.getElementsByTagName("Mycompany");
		for(int i=0; i<current_ver.getLength() ; i++) {
			Node node = current_ver.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				current_version = element.getElementsByTagName("version").item(0).getTextContent();
			}
		}
		
		/**
		 * Check Available Updates
		 */
		URL remoteXmlURL = new URL("https://example.com/"+__UPDATE_XML_FILE__);
		SSLContext ctx = SSLContext.getInstance("TLS");
		X509TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { } 
			public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { }
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		ctx.init(null, new TrustManager[]{tm}, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
		
		HostnameVerifier verifier = new HostnameVerifier() {
			
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(verifier);
		URLConnection connection = remoteXmlURL.openConnection();
		InputStream stream = new BufferedInputStream(connection.getInputStream());
		DocumentBuilderFactory checkfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder Checkbuilder = checkfactory.newDocumentBuilder();
		Document checkdocument = Checkbuilder.parse(stream);
		checkdocument.getDocumentElement().normalize();
		
		NodeList list = checkdocument.getElementsByTagName("version");
		for(int j=0;j<list.getLength(); j++) {
			Node checknode = list.item(j);
			if(checknode.getNodeType() == Node.ELEMENT_NODE) {
				Element checkelement = (Element) checknode;
				avail_version = checkelement.getElementsByTagName("avail-version").item(0).getTextContent();
				download_url = checkelement.getElementsByTagName("check-url").item(0).getTextContent();
			}
		}
		current = Float.parseFloat(current_version);
		avail = Float.parseFloat(avail_version);
		if(avail > current) 
			isGrater = true;
		return isGrater;
	}
	
	public static boolean isGrater() {
		return isGrater;
	}
	
	public static String getDownloadUrl() {
		return download_url;
	}
	
	public static String getDownloadPath() {
		return __DOWNLOAD_TEMP_DIR__;
	}
	
	public static String getCurrentverion() {
		return current_version;
	}
	
	public static String	getAvailVerion() {
		return avail_version;
		
	}
}
