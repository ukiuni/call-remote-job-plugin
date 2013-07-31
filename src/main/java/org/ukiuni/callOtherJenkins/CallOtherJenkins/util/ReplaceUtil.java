package org.ukiuni.callOtherJenkins.CallOtherJenkins.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ReplaceUtil {
	private ReplaceUtil() {

	}

	public static String replaceParam(String src, Map<String, String> parameterMap) {
		TreeMap<String, String> map = new TreeMap<String, String>(new LongKeyValueComparator());
		map.putAll(parameterMap);
		for (String key : map.keySet()) {
			src = src.replace("$" + key, map.get(key));
		}
		return src;
	}
	
	private static class LongKeyValueComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			int compairing = o2.length() - o1.length();
			return 0 != compairing ? compairing : o1.compareTo(o2);
		}
	}
}
