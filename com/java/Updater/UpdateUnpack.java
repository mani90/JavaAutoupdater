package com.java.Updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Manikandan.A 28-May-2013 10:23:20 AM UpdateUnpack.java
 * 
 */
public class UpdateUnpack {
	String file;
	String __SOURCE_DIR__ = System.getProperty("user.dir");
	byte[] buffer = new byte[1024];
	String __UPDATE_JAR__ = "update.jar";
	String __CLASS__ = "Your Main Class";
	String __XML_DIR__ = System.getProperty("user.dir") + File.separator
			+ "update-config.xml";

	public UpdateUnpack(String ZipFilePath) {
		this.file = ZipFilePath;
		try {
			ZipInputStream stream = new ZipInputStream(
					new FileInputStream(file));
			ZipEntry entry = stream.getNextEntry();
			while (entry != null) {
				String FileName = entry.getName();
				File newFile = new File(__SOURCE_DIR__ + File.separator+FileName);
				//File newFile = new File("/tmp" + File.separator + FileName);
				if (newFile.exists()) {
					newFile.delete();
				}
				new File(newFile.getParent()).mkdirs();
				FileOutputStream outputStream = new FileOutputStream(newFile);
				int FileLength;
				while ((FileLength = stream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, FileLength);
				}
				outputStream.close();
				entry = stream.getNextEntry();
			}
			stream.closeEntry();
			stream.close();
			boolean delete = new File(file).delete();
			if (delete) {

				boolean WriteStatus = RewriteXml();
				if (WriteStatus) {
					Shell shell = new Shell();
					MessageBox update = new MessageBox(shell,
							SWT.ICON_INFORMATION | SWT.OK);
					update.setText("Update");
					update.setMessage("Successfully updated, Please Restart Your Application.");
					int ok = update.open();
					if (ok == SWT.OK) {
						StringBuilder builder = new StringBuilder();
						builder.append(System.getProperty("java.home")
								+ File.separator + "bin" + File.separator
								+ "java");
						for (String jvmArg : ManagementFactory
								.getRuntimeMXBean().getInputArguments()) {
							builder.append(jvmArg + " ");
						}
						builder.append(" -cp ");
						builder.append(__UPDATE_JAR__).append(" ");
						builder.append(JavaAutoUpdater.class.getName()).append(
								" ");
						Thread.currentThread();
						Thread.sleep(100);
						Runtime.getRuntime().exec(builder.toString());
						System.exit(0);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * @return
	 */
	private boolean RewriteXml() {
		boolean Status;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(__XML_DIR__);
			document.getDocumentElement().normalize();
			NodeList list = document.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					element.getElementsByTagName("version").item(0)
							.setTextContent(Checker.getAvailVerion());
				}
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(__XML_DIR__));
			transformer.transform(source, result);
			Status = true;

		} catch (Exception e) {
			Status = false;
		}
		return Status;
	}
}
