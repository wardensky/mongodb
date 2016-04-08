package com.wisdombud.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GridFsToolTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public GridFsToolTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(GridFsToolTest.class);
	}

	public void testDeleteAll() {
		try {
			GridFsTool gft = new GridFsTool();
			gft.deleteAll();
			assertTrue(gft.getAllNames().isEmpty());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void testUpload() {
		try {
			GridFsTool gft = new GridFsTool();
			gft.uploadFile("d:\\tmp\\1月.JPG");
			gft.downloadFileByName("1月.JPG", "d:\\tmp\\1月_1.JPG");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
