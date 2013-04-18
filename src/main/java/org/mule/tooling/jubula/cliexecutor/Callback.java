package org.mule.tooling.jubula.cliexecutor;


public interface Callback {
	public int getResult() throws InterruptedException;

	public void failure(int returnCode);

	public void success(int returnCode);
}