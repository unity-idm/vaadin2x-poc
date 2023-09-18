package io.imunity.prototypes.runner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class JarGetter {
	static List<String> vaadinJarsContainsInPath = List.of(
		"vaadin/"
	);

	static String getJarsRegex(Set<String> classPathElements){
		return vaadinJarsContainsInPath.stream().collect(Collectors.joining(".*|.*", "(.*", ".*|")) +
			classPathElements.stream().collect(Collectors.joining("|", "", ")"));
	}
}
