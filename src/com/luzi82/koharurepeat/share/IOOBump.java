package com.luzi82.koharurepeat.share;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class IOOBump implements Runnable {
	final InputStream mInputStream;
	final OutputStream mOuputStream0;
	final OutputStream mOuputStream1;
	final Socket mSocket;

	public IOOBump(InputStream in, OutputStream out0, OutputStream out1, Socket s) {
		this.mInputStream = in;
		this.mOuputStream0 = out0;
		this.mOuputStream1 = out1;
		this.mSocket = s;
	}

	public void run() {
		try {
			while (true) {
				int i = mInputStream.read();
				if (i == -1)
					break;
				mOuputStream0.write(i);
				mOuputStream1.write(i);
			}
		} catch (SocketException e) {
		} catch (IOException e) {
			throw new Error();
		}
		try {
			mInputStream.close();
			mOuputStream0.close();
			mOuputStream1.close();
			mSocket.close();
		} catch (IOException e) {
			throw new Error();
		}
	}
}