package com.java.Updater;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;


/**
 * @author Manikandan.A 27-May-2013 6:42:35 PM Downloader.java
 *
 */
public class Downloader extends Thread {
	Display display1;
	Label label;
	ProgressBar progress;
	String error_msg;
	int byteread, bytewrite = 0, downloaded;
	String url1;
	int contentlength;
	InputStream inputStream;
	OutputStream stream;
	URL down_url;

	public Downloader(Display display, ProgressBar progressBar, Label label,
			String string) {
		this.progress = progressBar;
		this.display1 = display;
		this.label = label;
		this.url1 = string;
		progress.setMinimum(0);
	}

	public void run() {
		display1.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (progress.getShell().isDisposed()) 
						return;
				
					try {
						URL down_url = new URL(url1);
						HttpsURLConnection connection;
						SSLContext ctx = SSLContext.getInstance("TLS");
						X509TrustManager tm = new X509TrustManager() {
							public void checkClientTrusted(X509Certificate[] xcs,
									String string) throws CertificateException {
							}

							public void checkServerTrusted(X509Certificate[] xcs,
									String string) throws CertificateException {
							}

							public X509Certificate[] getAcceptedIssuers() {
								return null;
							}
						};
						ctx.init(null, new TrustManager[] { tm }, null);
						HttpsURLConnection.setDefaultSSLSocketFactory(ctx
								.getSocketFactory());

						HostnameVerifier verifier = new HostnameVerifier() {

							@Override
							public boolean verify(String arg0, SSLSession arg1) {
								return true;
							}
						};
						HttpsURLConnection.setDefaultHostnameVerifier(verifier);
						connection = (HttpsURLConnection) down_url.openConnection();
						if (connection.getResponseCode() != 200) {
							error_msg = "Bad response please Check Your Internet Connection";
							error(error_msg);
						}
						contentlength = connection.getContentLength();
						if (contentlength < 1) {
							error_msg = "An error occured Please try later.";
							error(error_msg);
						}
						
						stream = new BufferedOutputStream(
								new FileOutputStream(Checker.getDownloadPath()
										+ File.separator + getFilename(down_url)));
						inputStream = connection.getInputStream();
					progress.setMaximum(contentlength);
					byte buffer[] = new byte[1024];
					while ((byteread = inputStream.read(buffer)) != -1) {
						downloaded += byteread;
						stream.write(buffer, 0, byteread);
						progress.setSelection(downloaded);
						label.setText(SizeConverter(downloaded , contentlength));
					}
					stream.close();
					inputStream.close();
					new UpdateUnpack(Checker.getDownloadPath()+File.separator+getFilename(down_url));
					progress.getShell().dispose();
				}
				catch (Exception e1) {}
			}

			private String SizeConverter(int downloaded, int contentlength) {
				String down = null, total = null;
				final String[] units = {"B" , "KB" , "MB" , "GB" };
				for(int i = units.length ; i > 0; i--) {
					double step = Math.pow(1024, i);
					if (downloaded >= step) {
						down = String.format("%3.1f %s", downloaded / step, units[i]);
			            break;
			       }
				}
				for (int j = units.length; j > 0; j--) {
					double step = Math.pow(1024, j);
					if (contentlength >= step) {
						total = String.format("%3.1f %s", contentlength / step, units[j]);
			            break;
			       }
				}
				return "Downloaded " +down+" of " + total;
			}
			
		});
	}

	/**
	 * @param down_url
	 * @return Filename
	 */
	private String getFilename(URL down_url) {
		String name = down_url.getFile();
		return name.substring(name.lastIndexOf("/") + 1);
	}

	/**
	 * @param error_msg
	 */
	private void error(String error_msg) {
		Shell shell = progress.getShell();
		MessageBox box = new MessageBox(shell);
		box.setText("Error");
		box.setMessage(error_msg);
		box.open();
	}

}
