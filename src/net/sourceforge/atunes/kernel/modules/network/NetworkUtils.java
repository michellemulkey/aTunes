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

package net.sourceforge.atunes.kernel.modules.network;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.net.Proxy.Type;

import javax.imageio.ImageIO;

import net.sourceforge.atunes.kernel.modules.proxy.Proxy;
import net.sourceforge.atunes.kernel.modules.proxy.ProxyBean;

public class NetworkUtils {

	public static HttpURLConnection getConnection(URL url, Proxy proxy) throws IOException {
		HttpURLConnection connection;
		if (proxy == null)
			connection = (HttpURLConnection) url.openConnection();
		else {
			connection = (HttpURLConnection) proxy.getConnection(url);
		}
		return connection;
	}

	public static String readURL(URLConnection connection) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStream input = connection.getInputStream();
		byte[] array = new byte[1024];
		int read;
		while((read = input.read(array)) > 0) {
			builder.append(new String(array, 0, read, "UTF-8"));
		}
		return builder.toString();
	}

	public static String readURL(URLConnection connection, String charset) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStream input = connection.getInputStream();
		byte[] array = new byte[1024];
		int read;
		while((read = input.read(array)) > 0) {
			builder.append(new String(array, 0, read, charset));
		}
		return builder.toString();
	}

	public static Image getImage(URLConnection connection) throws IOException {
		InputStream input = connection.getInputStream();
		return ImageIO.read(input);
	}
	
	public static String encodeString(String s) {
		return s.replaceAll(" +","+");
	}
	
	public static Proxy getProxy(ProxyBean proxy) throws UnknownHostException, IOException {
		if (proxy == null)
			return null;
		
		return new Proxy(proxy.getType().equals(ProxyBean.HTTP_PROXY) ? Type.HTTP : Type.SOCKS, proxy.getUrl(), proxy.getPort(), proxy.getUser(), proxy.getPassword());
	}

}
