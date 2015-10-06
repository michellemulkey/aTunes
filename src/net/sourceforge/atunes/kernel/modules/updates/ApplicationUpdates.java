/**
 * aTunes 1.6.0
 * Copyright (C) 2006-2007 Alex Aranda (fleax) alex.aranda@gmail.com
 *
 * http://www.atunes.org
 * http://sourceforge.net/projects/atunes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package net.sourceforge.atunes.kernel.modules.updates;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.net.Proxy.Type;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.images.ImageLoader;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.UrlLabel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.modules.network.NetworkUtils;
import net.sourceforge.atunes.kernel.modules.proxy.Proxy;
import net.sourceforge.atunes.kernel.modules.proxy.ProxyBean;
import net.sourceforge.atunes.kernel.modules.xml.XMLBuilder;
import net.sourceforge.atunes.kernel.modules.xml.XMLUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApplicationUpdates {

	private static final String updatesURL = "http://atunes.sourceforge.net/latest.xml";
	
	private static ApplicationVersion getLastVersion(ProxyBean p) {
		ApplicationVersion result = null;
		try {
			URL url = new URL(updatesURL);
			Proxy proxy;
			if (p != null)
				proxy = new Proxy(p.getType().equals(ProxyBean.HTTP_PROXY) ? Type.HTTP : Type.SOCKS, p.getUrl(), p.getPort(), p.getUser(), p.getPassword());
			else
				proxy = null;
			URLConnection connection = NetworkUtils.getConnection(url, proxy);
			Document xml = XMLBuilder.getXMLDocument(NetworkUtils.readURL(connection));

			result = new ApplicationVersion();
			
			Element el = (Element) xml.getElementsByTagName("latest").item(0);
			result.setDate(XMLUtils.getChildElementContent(el, "date"));
			result.setMajorNumber(Integer.parseInt(XMLUtils.getChildElementContent(el, "majorNumber")));
			result.setMinorNumber(Integer.parseInt(XMLUtils.getChildElementContent(el, "minorNumber")));
			result.setRevisionNumber(Integer.parseInt(XMLUtils.getChildElementContent(el, "revisionNumber")));
			result.setDownloadURL(el.getAttribute("url"));
		} catch (UnknownHostException e1) {
		} catch (IOException e1) {
		}
		return result;
	}
	
	private static boolean updateExists(ApplicationVersion version) {
		int major = version.getMajorNumber();
		int minor = version.getMinorNumber();
		int revision = version.getRevisionNumber();
		
		if (major > Constants.APP_MAJOR_NUMBER)
			return true;
		else if (major < Constants.APP_MAJOR_NUMBER)
			return false;
		else if (minor > Constants.APP_MINOR_NUMBER)
			return true;
		else if (minor < Constants.APP_MINOR_NUMBER)
			return false;
		else if (revision > Constants.APP_REVISION_NUMBER)
			return true;

		return false;
	}
	
	public static void checkUpdates(ProxyBean p) {
		ApplicationVersion version = getLastVersion(p);
		if (updateExists(version)) {
			JFrame frame = getNotificationWindow(version);
			frame.setVisible(true);
		}
		else {
			HandlerProxy.getVisualHandler().showMessage(LanguageTool.getString("NOT_NEW_VERSION"));
		}
 	}
	
	private static JFrame getNotificationWindow(ApplicationVersion version) {
		final JFrame frame = new JFrame(LanguageTool.getString("NEW_VERSION_AVAILABLE"));
		frame.setSize(400, 150);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setIconImage(ImageLoader.APP_ICON.getImage());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel(new GridBagLayout());
		String text = LanguageTool.getString("NEW_VERSION_AVAILABLE_TEXT");
		text = text.replace("(%VERSION%)", Constants.APP_VERSION_NUMBER);
		text = text.replace("(%NEW_VERSION%)", version.getVersionNumber());
		
		JLabel img = new JLabel(ImageLoader.APP_ICON_TITLE);
		
		JTextArea text1 = new JTextArea(text);
		text1.setEditable(false);
		text1.setOpaque(false);
		text1.setLineWrap(true);
		text1.setWrapStyleWord(true);

		UrlLabel url = new UrlLabel(LanguageTool.getString("GO_TO_DOWNLOAD_PAGE"));
		url.setUrl(version.getDownloadURL());
		url.setHorizontalAlignment(SwingConstants.CENTER);
		
		CustomButton ok = new CustomButton(null, LanguageTool.getString("OK"));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.gridheight = 3; c.insets = new Insets(10,10,10,10);
		panel.add(img, c);
		c.gridx = 1; c.gridy = 0; c.gridheight = 1; c.weightx = 1; c.weighty = 0.5; c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(text1, c);
		c.gridy = 1; c.insets = new Insets(0,0,0,0); c.fill = GridBagConstraints.BOTH;
		panel.add(url, c);
		c.gridy = 2; c.weighty = 0; c.fill = GridBagConstraints.NONE; c.insets = new Insets(15,0,10,0);
		panel.add(ok, c);
		
		frame.add(panel);
		return frame;
	}
	
	public static void main(String[] args) {
		checkUpdates(null);
	}
}
