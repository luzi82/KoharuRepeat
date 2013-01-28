package com.luzi82.koharurepeat.core;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.luzi82.koharurepeat.core.KrCore.ServerSocketListener;

public class SocketCreator implements Runnable {
	/**
	 * 
	 */
	private final ServerSocketManager mServerSocketManager;
	ServerSocketListener mServerSockerListener;

	SocketCreator(ServerSocketManager serverSocketManager, ServerSocketListener aServerSocketListener) {
		mServerSocketManager = serverSocketManager;
		this.mServerSockerListener = aServerSocketListener;
	}

	public void run() {
		Socket c = null, s = null;
		try {
			while (!mServerSocketManager.ss.isClosed()) {
				c = s = null;
				c = mServerSocketManager.ss.accept();
				s = new Socket(mServerSocketManager.sHost, mServerSocketManager.sPort);
				mServerSockerListener.get(new SocketManager(c, s));
			}
		} catch (SocketException e) {
		} catch (UnknownHostException e) {
			mServerSockerListener.error(e);
		} catch (IOException e) {
			throw new Error();
		}
		try {
			if (c != null)
				c.close();
			if (s != null)
				s.close();
		} catch (IOException e) {
			throw new Error();
		}
		try {
			mServerSocketManager.ss.close();
		} catch (IOException e) {
			throw new Error();
		}
		mServerSockerListener.close();
		// System.out.println("hello");
	}
}