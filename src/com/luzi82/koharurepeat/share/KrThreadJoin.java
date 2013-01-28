package com.luzi82.koharurepeat.share;

import com.luzi82.koharurepeat.core.KrCore.Killable;

public class KrThreadJoin implements Killable {
	private boolean mDeath = false;

	public synchronized void kill() {
		mDeath = true;
		notify();
	}

	public synchronized boolean isDeath() {
		return mDeath;
	}

	public synchronized void join() {
		while (!mDeath) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
			notify();
		}
	}
}