package org.ukiuni.callOtherJenkins.CallOtherJenkins.util;

import java.util.Map;

public class ReplaceUtil {
	private ReplaceUtil() {

	}

	public static String replaceParam(String src, Map<String, String> parameterMap) {
		for (String key : parameterMap.keySet()) {
			src = src.replace("$" + key, parameterMap.get(key));
		}
		return src;
	}
}
