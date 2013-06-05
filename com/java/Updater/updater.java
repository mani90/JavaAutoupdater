package com.java.Updater;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */

/**
 * @author Manikandan.A 25-May-2013 5:18:35 PM updater.java
 * 
 */
public class updater {

	static Shell updateshell;
	static int downloaded;
	static int size;
	static int status;
	static ProgressBar progressBar;
	static Label label;

	public updater() {
		open();
	}

	private static void open() {
		final Display display = Display.getDefault();
		createcontents();
		updateshell.open();
		while (!updateshell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private static void createcontents() {
		updateshell = new Shell(SWT.CLOSE | SWT.MIN);
		updateshell.setSize(600, 200);
		updateshell.setText("My Application Update");

		progressBar = new ProgressBar(updateshell, SWT.NONE);
		progressBar.setBounds(73, 62, 461, 14);

		label = new Label(updateshell, SWT.NONE);
		label.setBounds(73, 82, 228, 17);
		new Downloader(Display.getDefault(), progressBar, label,
				Checker.getDownloadUrl()).start();

		Label lblNewLabel = new Label(updateshell, SWT.NONE);
		lblNewLabel.setBounds(73, 25, 460, 17);
		lblNewLabel.setText("Downloading updates... Please Wait...");
	}
}
