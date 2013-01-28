package com.luzi82.koharurepeat.share;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * 
 */

public class RWBump implements Runnable {
	final Reader mReader;
	final Writer mWriter;

	public RWBump(Reader aReader, Writer aWriter) {
		this.mReader = aReader;
		this.mWriter = aWriter;
	}

	public void run() {
		try {
			while (true) {
				int i = mReader.read();
				if (i == -1)
					break;
				mWriter.write(i);
				// System.out.println(i);
			}
		} catch (IOException e) {
		}
		try {
			mWriter.close();
		} catch (IOException e) {
		}
	}
}
