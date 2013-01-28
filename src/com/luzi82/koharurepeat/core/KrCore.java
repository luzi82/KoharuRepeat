package com.luzi82.koharurepeat.core;

import com.luzi82.koharurepeat.KoharuRepeat;

public class KrCore {
	/**
	 * 
	 */
	final KoharuRepeat mKoharuRepeat;

	/**
	 * @param aKoharuRepeat
	 */
	public KrCore(KoharuRepeat aKoharuRepeat) {
		mKoharuRepeat = aKoharuRepeat;
	}

	public interface Killable {
		void kill();

		void join();
	}

//	public interface Listener {
//		void write(int i);
//
//		void close();
//	}

	public interface SL {
		void close();
	}

	public interface ServerSocketListener {
		void get(SocketManager s);

		void close();

		void error(Exception e);
	}

	public ServerSocketCreator getSSC() {
		return new ServerSocketCreator();
	}
}