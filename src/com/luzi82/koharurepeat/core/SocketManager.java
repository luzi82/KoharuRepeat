package com.luzi82.koharurepeat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import com.luzi82.koharurepeat.core.KrCore.Killable;
import com.luzi82.koharurepeat.core.KrCore.SL;

public class SocketManager implements Killable {
	class SS extends OutputStream {
		@Override
		public void write(int aValue) throws IOException {
			synchronized (SocketManager.this) {
				for (SocketBump sd : mCodeToSocketBump.values()) {
					sd.writeS(aValue);
				}
			}
		}

		@Override
		public void close() throws IOException {
			synchronized (SocketManager.this) {
				for (SocketBump sd : mCodeToSocketBump.values()) {
					sd.closeS();
				}
			}
		}
	}

	class CC extends OutputStream {
		@Override
		public void write(int aValue) throws IOException {
			synchronized (SocketManager.this) {
				for (SocketBump sd : mCodeToSocketBump.values()) {
					sd.writeC(aValue);
				}
			}
		}

		@Override
		public void close() throws IOException {
			synchronized (SocketManager.this) {
				for (SocketBump sd : mCodeToSocketBump.values()) {
					sd.closeC();
				}
			}
		}
	}

	Socket mClientSocket;
	Socket mServerSocket;
	Map<String, SocketBump> mCodeToSocketBump;
	Thread mClientToServerBump;
	Thread mServerToClientBump;

	public SocketManager(Socket c, Socket s) {
		this.mClientSocket = c;
		this.mServerSocket = s;
		mCodeToSocketBump = new TreeMap<String, SocketBump>();
		OutputStream co, so;
		InputStream ci, si;
		try {
			co = c.getOutputStream();
			so = s.getOutputStream();
			ci = c.getInputStream();
			si = s.getInputStream();
		} catch (IOException e) {
			throw new Error();
		}
		mClientToServerBump = new Thread(new IOBump(ci, so, new CC(), s));
		mServerToClientBump = new Thread(new IOBump(si, co, new SS(), c));
	}

	public String getSHost() {
		return mServerSocket.getInetAddress().getHostAddress();
	}

	public String getCHost() {
		return mClientSocket.getInetAddress().getHostAddress();
	}

	public int getSPort() {
		return mServerSocket.getPort();
	}

	public int getCPort() {
		return mClientSocket.getPort();
	}

	public void start(final SL sl) {
		mClientToServerBump.start();
		mServerToClientBump.start();
		new Thread(new Runnable() {
			public void run() {
				SocketManager.this.join();
				sl.close();
			}
		}).start();
	}

	public synchronized SocketBump add(String aCode) {
		if (mCodeToSocketBump.containsKey(aCode))
			return null;
		SocketBump out = new SocketBump(this, aCode);
		mCodeToSocketBump.put(aCode, out);
		return out;
	}

	synchronized void remove(String code) {
		SocketBump tar = (SocketBump) mCodeToSocketBump.remove(code);
		if (tar == null)
			return;
		tar.closeC();
		tar.closeS();
	}

//	synchronized void closeC() {
//		// Iterator t = sds.values().iterator();
//		// while (t.hasNext()) {
//		// SocketBump sd = (SocketBump) t.next();
//		// sd.closeC();
//		// }
//		for (SocketBump sd : mCodeToSocketBump.values()) {
//			sd.closeC();
//		}
//	}

//	synchronized void closeS() {
//		// Iterator t = sds.values().iterator();
//		// while (t.hasNext()) {
//		// SocketBump sd = (SocketBump) t.next();
//		// sd.closeS();
//		// }
//		for (SocketBump sd : mCodeToSocketBump.values()) {
//			sd.closeS();
//		}
//	}

//	synchronized void writeC(int i) {
//		// Iterator t = sds.values().iterator();
//		// while (t.hasNext()) {
//		// SocketBump sd = (SocketBump) t.next();
//		// sd.writeC(i);
//		// }
//		for (SocketBump sd : mCodeToSocketBump.values()) {
//			sd.writeC(i);
//		}
//	}

	// synchronized void writeS(int i) {
	// // Iterator t = sds.values().iterator();
	// // while (t.hasNext()) {
	// // SocketBump sd = (SocketBump) t.next();
	// // sd.writeS(i);
	// // }
	// for (SocketBump sd : mCodeToSocketBump.values()) {
	// sd.writeS(i);
	// }
	// }

	public void kill() {
		try {
			mClientSocket.close();
			mServerSocket.close();
		} catch (IOException e) {
			throw new Error();
		}
	}

	public void join() {
		while (mClientToServerBump.isAlive() || mServerToClientBump.isAlive()) {
			try {
				mClientToServerBump.join();
				mServerToClientBump.join();
			} catch (InterruptedException e) {
				throw new Error();
			}
		}
	}
}