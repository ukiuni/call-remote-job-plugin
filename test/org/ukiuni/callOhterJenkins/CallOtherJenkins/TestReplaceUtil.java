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
	@Test
	public void testReplaceTwoChar() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("test1", "success");
		paramMap.put("test2", "fail");
		Assert.assertEquals("test is success and fail", ReplaceUtil.replaceParam("test is $test1 and $test2", paramMap));
	}
	@Test
	public void testReplaceDuplicatePreChar() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("test", "fail");
		paramMap.put("testLong", "success");
		Assert.assertEquals("test is success and fail", ReplaceUtil.replaceParam("test is $testLong and $test", paramMap));
	}
}
