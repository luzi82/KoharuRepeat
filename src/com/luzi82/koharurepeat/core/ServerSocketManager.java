package com.luzi82.koharurepeat.core;

import java.io.IOException;
import java.net.ServerSocket;

import com.luzi82.koharurepeat.core.KrCore.Killable;
import com.luzi82.koharurepeat.core.KrCore.ServerSocketListener;

public class ServerSocketManager implements Killable {
	ServerSocket ss;
	String sHost;
	int sPort;
	Thread sc;

	ServerSocketManager(ServerSocket ss, String sHost, int sPort) {
		this.ss = ss;
		this.sHost = sHost;
		this.sPort = sPort;
	}

	public void setL(ServerSocketListener l) {
		sc = new Thread(new SocketCreator(this, l));
		sc.start();
	}

	public void kill() {
		try {
			ss.close();
		} catch (IOException e) {
			throw new Error();
		}
	}

	public void join() {
		while (sc.isAlive()) {
			try {
				sc.join();
			} catch (InterruptedException e) {
				throw new Error();
			}
		}
	}

	public int getCPort() {
		return ss.getLocalPort();
	}

	public String getSHost() {
		return sHost;
	}

	public int getSPort() {
		return sPort;
	}
}