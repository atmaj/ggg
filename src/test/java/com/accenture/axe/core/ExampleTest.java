/**
 * Copyright (C) 2015 Deque Systems Inc.,
 *
 * Your use of this Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This entire copyright notice must appear in every copy of this file you
 * distribute or in any file that contains substantial portions of this source
 * code.
 */
package com.accenture.axe.core;




import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.accenture.axe.core.AXE;

public class ExampleTest {

	@Rule public TestName testName = new TestName();

	private WebDriver driver;

	String driverPath = System.getProperty("user.dir") + "\\drivers\\chromedriver.exe";


	// Instantiate the WebDriver and navigate to the test site
 private static final URL scriptUrl() throws MalformedURLException {
	 return new URL("file:/C:/Users/s.m.modabbir/Documents/Project/Axe-Java-Demo/js/axe.min.js");
 }

	@Before public void setUp() throws MalformedURLException { // ChromeDriver needed to test for Shadow DOM

		System.setProperty("webdriver.chrome.driver", driverPath);
		driver = new ChromeDriver(); }

	// Ensure we close the WebDriver after finishing



	@After public void tearDown() throws MalformedURLException { driver.quit(); }

	// Basic test



	@Test public void testAccessibility() throws MalformedURLException { driver.get("https://www.bankofireland.com/");
	JSONObject responseJSON = new AXE.Builder(driver, scriptUrl()).analyze();

	JSONArray violations = responseJSON.getJSONArray("violations");

	if (violations.length() == 0) { assertTrue("No violations found", true); }
	else { AXE.writeResults(testName.getMethodName(), responseJSON);
	assertTrue(AXE.report(violations), false); } }

	//**
	//	 * Test with skip frames



	@Test public void testAccessibilityWithSkipFrames() throws MalformedURLException {
		driver.get("https://www.bankofireland.com/"); JSONObject responseJSON = new
				AXE.Builder(driver, scriptUrl()) .skipFrames() .analyze();

		JSONArray violations = responseJSON.getJSONArray("violations");

		if (violations.length() == 0) { assertTrue("No violations found", true); }
		else { AXE.writeResults(testName.getMethodName(), responseJSON);
		assertTrue(AXE.report(violations), false); } }

	// * Test with options



	@Test public void testAccessibilityWithOptions() throws MalformedURLException {
		driver.get("https://www.bankofireland.com/"); JSONObject responseJSON = new
				AXE.Builder(driver, scriptUrl())
				.options("{ rules: { 'accesskeys': { enabled: false } } }") .analyze();

		JSONArray violations = responseJSON.getJSONArray("violations");

		if (violations.length() == 0) { assertTrue("No violations found", true); }
		else { AXE.writeResults(testName.getMethodName(), responseJSON);

		assertTrue(AXE.report(violations), false); } }

	//**
	//	 * Test a specific selector or selectors



	@Test public void testAccessibilityWithSelector() throws MalformedURLException {
		driver.get("https://www.bankofireland.com/"); JSONObject responseJSON = new
				AXE.Builder(driver, scriptUrl()) .include("title") .include("p") .analyze();

		JSONArray violations = responseJSON.getJSONArray("violations");

		if (violations.length() == 0) { assertTrue("No violations found", true); }
		else { AXE.writeResults(testName.getMethodName(), responseJSON);

		assertTrue(AXE.report(violations), false); } }

	//**
	//	 * Test includes and excludes



	@Test public void testAccessibilityWithIncludesAndExcludes() throws MalformedURLException {
		driver.get("https://www.bankofireland.com//include-exclude.html"); JSONObject
		responseJSON = new AXE.Builder(driver, scriptUrl()) .include("body")
		.exclude("h1") .analyze();

		JSONArray violations = responseJSON.getJSONArray("violations");

		if (violations.length() == 0) { assertTrue("No violations found", true); }
		else { AXE.writeResults(testName.getMethodName(), responseJSON);
		assertTrue(AXE.report(violations), false); } }

	//**
	//	 * Test a WebElement



	@Test public void testAccessibilityWithWebElement() throws MalformedURLException {
		driver.get("https://www.bankofireland.com/");

		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl())
				.analyze(driver.findElement(By.tagName("p")));

		JSONArray violations = responseJSON.getJSONArray("violations");

		if (violations.length() == 0) { assertTrue("No violations found", true); }
		else { AXE.writeResults(testName.getMethodName(), responseJSON);
		assertTrue(AXE.report(violations), false); } }

	//**
	//	 * Test a page with Shadow DOM violations



	@Test public void testAccessibilityWithShadowElement() throws MalformedURLException {
		driver.get("https://www.bankofireland.com//shadow-error.html");

		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl()).analyze();

		JSONArray violations = responseJSON.getJSONArray("violations");

		JSONArray nodes = ((JSONObject)violations.get(0)).getJSONArray("nodes");
		JSONArray target = ((JSONObject)nodes.get(0)).getJSONArray("target");

		if (violations.length() == 1) { // assertTrue(AXE.report(violations), true);
			assertEquals(String.valueOf(target), "[[\"#upside-down\",\"ul\"]]"); } else {
				AXE.writeResults(testName.getMethodName(), responseJSON);
				assertTrue("No violations found", false);

			} }

	@Test public void testAxeErrorHandling() throws MalformedURLException {
		driver.get("https://www.bankofireland.com//");

		URL errorScript = ExampleTest.class.getResource("/axe-error.js"); AXE.Builder
		builder = new AXE.Builder(driver, errorScript); 

		boolean didError = false;

		try { builder.analyze(); } catch (AXE.AxeRuntimeException e) {
			assertEquals(e.getMessage(), "boom!"); // See axe-error.js didError = true; }

			assertTrue("Did raise axe-core error", didError); }
	}

		//**
		//	 * Test few include
 
		@Test public void testAccessibilityWithFewInclude() throws MalformedURLException {
			driver.get("https://www.bankofireland.com//include-exclude.html"); JSONObject
			responseJSON = new AXE.Builder(driver, scriptUrl()) .include("div")
			.include("p") .analyze();

			JSONArray violations = responseJSON.getJSONArray("violations");

			if (violations.length() == 0) { assertTrue("No violations found", true); }
			else { AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue(AXE.report(violations), false); } }

		//**
		//	 * Test includes and excludes with violation

		@Test public void testAccessibilityWithIncludesAndExcludesWithViolation() throws MalformedURLException {
			driver.get("https://www.bankofireland.com//include-exclude.html"); JSONObject
			responseJSON = new AXE.Builder(driver, scriptUrl()) .include("body")
			.exclude("div") .analyze();

			JSONArray violations = responseJSON.getJSONArray("violations");

			JSONArray nodes = ((JSONObject)violations.get(0)).getJSONArray("nodes");
			JSONArray target = ((JSONObject)nodes.get(0)).getJSONArray("target");

			if (violations.length() == 1) { assertEquals(String.valueOf(target),
					"[\"span\"]"); } else { AXE.writeResults(testName.getMethodName(),
							responseJSON); assertTrue("No violations found", false); } } }
