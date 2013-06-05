package com.java.Updater;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Manikandan.A 25-May-2013 3:35:00 PM CheckClass.java
 * 
 */
public class CheckClass extends Thread {
	Display display;
	ProgressBar bar;
	boolean status;
	Shell checkUpdateShell;

	/**
	 * @param display
	 * @param shell
	 * @param Progressbar
	 */
	public CheckClass(Display display, ProgressBar progressBar, Shell shell) {
		this.display = display;
		this.bar = progressBar;
		this.checkUpdateShell = shell;
		bar.setMinimum(0);
		bar.setVisible(true);
	}

	public void run() {
		try {
			status = Checker.Check();
		} catch (Exception e) {
			e.printStackTrace();
		}
		display.asyncExec(new Runnable() {

			@SuppressWarnings("unused")
			@Override
			public void run() {
				if (bar.isDisposed())
					return;
				if (status) {
					bar.setVisible(false);
					Float current = Float.parseFloat(Checker.getCurrentverion());
					Float avail = Float.parseFloat(Checker.getAvailVerion());
					String msg = "New update Available \nPlease Conform update from "
							+ current + " to " + avail;
					MessageBox box = new MessageBox(checkUpdateShell, SWT.YES
							| SWT.NO | SWT.CENTER);
					box.setText("Update");
					box.setMessage(msg);
					int select = box.open();
					if (select == SWT.YES) {
						Shell shell = bar.getShell();
						shell.dispose();
						updater u = new updater();
					} else {
						checkUpdateShell.dispose();
					}

				} else {
					String msg = "No Updates available";
					MessageBox box = new MessageBox(checkUpdateShell,
							SWT.CENTER);
					box.setText("Update");
					box.setMessage(msg);
					int ok = box.open();
					if (ok == SWT.OK) {
						checkUpdateShell.dispose();
					}
				}
			}
		});
	}
}
