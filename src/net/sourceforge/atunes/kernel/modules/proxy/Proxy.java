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

package net.sourceforge.atunes.kernel.modules.proxy;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class Proxy extends java.net.Proxy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7495084217081194366L;
	private String url;
	private int port;
	private String user;
	private String password;
	
	public Proxy(Type type, String url, int port, String user, String password) throws UnknownHostException, IOException {
		super(type, new Socket(url, port).getRemoteSocketAddress());
		this.url = url;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	public URLConnection getConnection(URL url) throws IOException {
        URLConnection con = url.openConnection(this);
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String encodedUserPwd = encoder.encode((user + ':' + password).getBytes());
        con.setRequestProperty("Proxy-Authorization", "Basic " + encodedUserPwd);
        return con;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}
	
}
