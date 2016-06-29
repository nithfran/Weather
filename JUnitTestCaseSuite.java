import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JUnitTestCaseSuite {
	public static String INPUT_FILE_WITH_INVALID_JSON = "";
	public static String[] ss = null;

	public static void main(String args[]) {
		ss = args;
		suite();
	}

	public static Test suite() {

		TestSuite suite = new TestSuite("Test for default package");
		
		String[] path = { ss[0] };
		String[] path1 = { ss[1] };
		String[] path2 = { ss[2] };
		String[] path3 = { ss[3] };
		String[] path4 = { ss[4] };
		String[] path5 = { ss[5] };
		String[] path6 = { ss[6] };
		Weather weather = new Weather();

		try {
			weather.readPropertFile(path3);
			INPUT_FILE_WITH_INVALID_JSON = weather.INPUT_FILE;
		} catch (Exception e1) {
			// Ignore
		}
		
		// $JUnit-BEGIN$
		// Test 1
		
		
		System.out.println("---------------------------------------");
		System.out.println("Test 1 :- passing invalid config file name. ");
		System.out.println("---------------------------------------");
		try {
			new TestCases().test(null);
			System.out.println("---->Test Failed");
		} catch (Exception e) {
			System.out.println("" + e.getMessage() + "\n \n---->Test Passed");

		}

		// Test 2
		System.out.println("---------------------------------------");
		System.out.println("Test 2 :- passing valid config file name. ");
		System.out.println("---------------------------------------");
		try {
			new TestCases().test(path);
			System.out.println("---->Test Passed");
		} catch (Exception e) {
			System.out.println("" + e.getMessage() + "\n \n---->Test Failed");

		}

		// Test 3
		System.out.println("---------------------------------------");
		System.out.println("Test 3 :- passing invalid input file name. ");
		System.out.println("---------------------------------------");
		try {
			new TestCases().test(path1);
			System.out.println("---->Test Failed");
		} catch (Exception e) {
			System.out.println("" + e.getMessage() + "\n \n---->Test Passed");

		}

		// Test 4
		System.out.println("---------------------------------------");
		System.out.println("Test 4 :- passing invalid Output file name. ");
		System.out.println("---------------------------------------");
		try {
			new TestCases().test(path2);
			System.out.println("---->Test Failed");
		} catch (Exception e) {
			System.out.println("" + e.getMessage() + "\n \n---->Test Passed");

		}

		// Test 5
		System.out.println("---------------------------------------");
		System.out
				.println("Test 5 :- passing invalid JSON input file which contains City Names file name. ");
		System.out.println("---------------------------------------");
		try {
			new TestCases().testJSONObject(INPUT_FILE_WITH_INVALID_JSON);
			System.out.println("---->Test Failed");
		} catch (Exception e) {
			System.out.println("" + e.getMessage() + "\n \n---->Test Passed");

		}

		// Test 6
		System.out.println("---------------------------------------");
		System.out
				.println("Test 6 :- passing output file with invalid city names. ");
		System.out.println("---------------------------------------");
		try {
			new Weather().runWeather(path4);
			System.out.println("---->Test Passed");
		} catch (Exception e) {
			System.out.println("" + e.getMessage() + "\n \n---->Test Failed");

		}
		
				
		// Test 7
		System.out.println("---------------------------------------");
		System.out
				.println("Test 7 :- Running without an active internet Connection  and no cache file");
		System.out.println("---------------------------------------");
		try {
			Weather.flag = "Inactive Internet Connection";
			Weather.cacheObj = null;
			Weather.flag = "no cache";
			Weather.runWeather(path6);
			System.out.println("---->Test Passed");
		} catch (Exception e) {
			System.out.println("" + e.getMessage() + "\n \n---->Test Failed");

		}
		
		// $JUnit-END$
		return suite;
	}

}
