package org.ukiuni.callOhterJenkins.CallOtherJenkins;

import java.io.IOException;

import org.junit.Test;
import org.ukiuni.callOtherJenkins.CallOtherJenkins.JenkinsRemoteIF;
import org.ukiuni.callOtherJenkins.CallOtherJenkins.JenkinsRemoteIF.LastCompleteBuild;
import org.ukiuni.callOtherJenkins.CallOtherJenkins.JenkinsRemoteIF.TimeoutException;

public class TestCallOtherJenkinsBuilder {
	@Test
	public void testPerform() throws InterruptedException, IOException, TimeoutException {
		JenkinsRemoteIF jenkinsRemoteIF = new JenkinsRemoteIF("localhost:8000", "MyFirstJob", false);
		jenkinsRemoteIF.setAuthentication("admin", "password");
		long id = jenkinsRemoteIF.loadLastBuildNumber(System.out);
		long next = jenkinsRemoteIF.loadNextBuildNumber(System.out);
		System.out.println("id:next" + id + "," + next);
		jenkinsRemoteIF.exec(System.out);
		long span = 1000;
		long retry = 20;
		LastCompleteBuild lastCompleteBuild = jenkinsRemoteIF.seekEnd(System.out, next, span, retry);
		System.out.println("lastCompleteBuild " + lastCompleteBuild.number + "," + lastCompleteBuild.success);
	}
}
