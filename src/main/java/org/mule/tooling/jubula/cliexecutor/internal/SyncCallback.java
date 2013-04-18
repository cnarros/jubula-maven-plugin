package org.mule.tooling.jubula.cliexecutor.internal;

import java.util.concurrent.Semaphore;

import org.mule.tooling.jubula.cliexecutor.Callback;

final class SyncCallback implements Callback {
	private int result;
	private Semaphore semaphore = new Semaphore(0);

	@Override
	public void success(int returnCode) {
		this.result = returnCode;
		semaphore.release();
	}

	@Override
	public int getResult() throws InterruptedException {
		semaphore.acquire();
		return result;
	}

	@Override
	public void failure(int returnCode) {
		this.result = returnCode;
		semaphore.release();
	}
}