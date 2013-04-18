package org.mule.tooling.jubula.cliexecutor;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mule.tooling.jubula.cliexecutor.internal.UnixJubulaCliExecutor;
import org.mule.tooling.jubula.cliexecutor.internal.WindowsJubulaCliExecutor;

public class CliExecutorTest {

	@Test
	public void testFactoryReturnsTheCorrespondingExecutor() {
		JubulaCliExecutor jubulaCliExecutor = new JubulaCliExecutorFactory()
				.getNewInstance("this-points-to-jubula");
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			assertThat(jubulaCliExecutor, is(WindowsJubulaCliExecutor.class));
		else
			assertThat(jubulaCliExecutor, is(UnixJubulaCliExecutor.class));
	}

}
