package com.luzi82.koharurepeat.core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.Writer;

import com.luzi82.koharurepeat.KoharuRepeat;
import com.luzi82.koharurepeat.share.KrThreadJoin;

public class SocketBump implements KrCore.Killable {
	/**
	 * 
	 */

	class ReaderToListenerBump implements Runnable {
		Reader mReader;
		Writer mListener;

		ReaderToListenerBump(Reader r, Writer l) {
			this.mReader = r;
			this.mListener = l;
		}

		public void run() {
			try {
				while (true) {
					int i = mReader.read();
					if (i == -1)
						break;
					mListener.write(i);
					// System.out.println(i);
				}
			} catch (IOException e) {
				throw new Error(e);
			}
			try {
				mListener.close();
			} catch (IOException e) {
				throw new Error(e);
			}
		}
	}

	OutputStream mServerOutputStream;
	OutputStream mClientOutputStream;
	Reader mServerReader;
	Reader mClientReader;
	final KrThreadJoin ds = new KrThreadJoin();
	final KrThreadJoin dc = new KrThreadJoin();
	final SocketManager mParent;
	String coding;

	public SocketBump(SocketManager aParent, String aCharset) {
		this.mParent = aParent;
		this.coding = aCharset;
		PipedOutputStream sss = new PipedOutputStream();
		PipedOutputStream ccc = new PipedOutputStream();
		mServerOutputStream = sss;
		mClientOutputStream = ccc;
		try {
			if (aCharset.equals(KoharuRepeat.r02)) {
				mServerReader = KoharuRepeat.r02Reader(new PipedInputStream(sss));
				mClientReader = KoharuRepeat.r02Reader(new PipedInputStream(ccc));
			} else if (aCharset.equals(KoharuRepeat.r08)) {
				mServerReader = KoharuRepeat.r08Reader(new PipedInputStream(sss));
				mClientReader = KoharuRepeat.r08Reader(new PipedInputStream(ccc));
			} else if (aCharset.equals(KoharuRepeat.r10)) {
				mServerReader = KoharuRepeat.r10Reader(new PipedInputStream(sss));
				mClientReader = KoharuRepeat.r10Reader(new PipedInputStream(ccc));
			} else if (aCharset.equals(KoharuRepeat.r16)) {
				mServerReader = KoharuRepeat.r16Reader(new PipedInputStream(sss));
				mClientReader = KoharuRepeat.r16Reader(new PipedInputStream(ccc));
			} else {
				mServerReader = new InputStreamReader(new PipedInputStream(sss), aCharset);
				mClientReader = new InputStreamReader(new PipedInputStream(ccc), aCharset);
			}
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	void writeC(int i) {
		try {
			mClientOutputStream.write(i);
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	void writeS(int i) {
		try {
			mServerOutputStream.write(i);
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	void closeC() {
		try {
			mClientOutputStream.close();
		} catch (IOException ioe) {
			throw new Error();
		}
		dc.kill();
	}

	void closeS() {
		try {
			mServerOutputStream.close();
		} catch (IOException ioe) {
			throw new Error();
		}
		ds.kill();
	}

	public void setClientListener(Writer aWriter) {
		new Thread(new ReaderToListenerBump(mClientReader, aWriter)).start();
	}

	public void setServerListener(Writer aWriter) {
		new Thread(new ReaderToListenerBump(mServerReader, aWriter)).start();
	}

	/*
	 * void setSL(final SL l){ L lc=new L(){ public void write(int i){
	 * l.writeC(i); } public void close(){ l.closeC(); } }; L ls=new L(){ public
	 * void write(int i){ l.writeS(i); } public void close(){ l.closeS(); } };
	 * new Thread(new R2L(c,lc)).start(); new Thread(new R2L(s,ls)).start(); }
	 */
	public void kill() {
		if (ds.isDeath() && dc.isDeath())
			return;
		mParent.remove(coding);
	}

	public void join() {
		ds.join();
		dc.join();
	}
}