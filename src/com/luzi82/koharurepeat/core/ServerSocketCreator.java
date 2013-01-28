package com.luzi82.koharurepeat.core;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketCreator {

	public ServerSocketManager create(int cPort, String sHost, int sPort) throws IOException {
		return new ServerSocketManager(new ServerSocket(cPort), sHost, sPort);
	}
}