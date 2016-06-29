import static org.junit.Assert.*;

public class TestCases {

	public void test(String[] path) throws Exception {
		try {
			Weather.readPropertFile(path);
		} catch (Exception e) {
			throw e;
		}
	}

	public void testJSONObject(String inputFile) throws Exception {
		try {
			Weather.generateJSONObjectFromTxtFile(inputFile);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void testOutputFilePath(String outputFile) throws Exception {
		try {
			Weather.getCachedOutputJSONFile(outputFile);
		} catch (Exception e) {
			throw e;
		}
	}
}
