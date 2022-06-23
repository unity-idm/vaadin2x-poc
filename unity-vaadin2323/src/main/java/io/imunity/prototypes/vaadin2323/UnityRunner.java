package io.imunity.prototypes.vaadin2323;

import pl.edu.icm.unity.MessageSource;
import pl.edu.icm.unity.engine.server.UnityApplication;

public class UnityRunner {
	public static void main(String[] args) {
		UnityApplication theServer = new UnityApplication(MessageSource.PROFILE_FAIL_ON_MISSING);
		theServer.run(new String[] {"src/main/resources/unityServer.conf"});
	}
}