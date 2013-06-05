package com.java.Updater;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * @author Manikandan.A 25-May-2013 11:52:26 AM JavaAutoUpdater.java
 * 
 */
public class JavaAutoUpdater {

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			JavaAutoUpdater window = new JavaAutoUpdater();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(500, 200);
		shell.setText("My Application Updater");
		final ProgressBar progressBar = new ProgressBar(shell,
				SWT.INDETERMINATE);
		progressBar.setBounds(30, 70, 430, 14);
		progressBar.setVisible(false);
		final Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(30, 30, 430, 17);
		lblNewLabel.setText("Checking available Updates... Please Wait...");
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Thread.currentThread().resume();
				MessageBox cancel = new MessageBox(shell, SWT.YES | SWT.NO);
				cancel.setText("Cancel");
				cancel.setMessage("Are you sure want to cancel.");
				int can = cancel.open();
				if (can == SWT.YES) {
					Thread.currentThread().run();
					Thread.currentThread().stop();
					shell.close();
				} else {
					Thread.currentThread().run();
				}
			}
		});
		btnNewButton.setBounds(320, 120, 124, 29);
		btnNewButton.setText("Cancel Update");

		new CheckClass(Display.getDefault(), progressBar, shell).start();

	}
}
