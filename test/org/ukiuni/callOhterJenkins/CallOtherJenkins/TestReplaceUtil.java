package org.ukiuni.callOhterJenkins.CallOtherJenkins;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.ukiuni.callOtherJenkins.CallOtherJenkins.util.ReplaceUtil;

public class TestReplaceUtil {
	@Test
	public void testReplaceChar() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("test", "success");
		Assert.assertEquals("test is success", ReplaceUtil.replaceParam("test is $test", paramMap));
	}
}
