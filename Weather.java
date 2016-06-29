import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;

import org.apache.log4j.Logger;
import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.bitpipeline.lib.owm.WeatherData.WeatherCondition;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {

	/**
	 * Author Nithin Francis E
	 * 
	 * @param args
	 */

	static Logger log = Logger.getLogger(Weather.class.getName());

	public static Properties prop = new Properties();
	public static String INPUT_FILE = "";
	public static String OUTPUT_CACHE_FILE = "";
	public static JSONObject inputObj = null;
	public static JSONObject cacheObj = null;
	public static String CLIMATE = null;
	public static String dateUTC = null;
	public static String TEMPARATURE;
	public static float PRESSURE;
	public static float HUMIDITY;

	public static void main(String[] args) throws Exception {

		log.info("Application started at " + new Date());
		log.info("Inside main Method");

		readPropertFile(args);

		generateJSONObjectFromTxtFile();

		getCachedOutputJSONFile();

		Iterator keys = inputObj.keys();

		if (cacheObj == null) {
			log.info("cacheObj is null");
			cacheObj = new JSONObject();
		}

		while (keys.hasNext()) {
			String cityName = ((String) keys.next());
			String id = inputObj.get(cityName).toString();
			CLIMATE = getClimate(cityName, id);
			getParameters(cityName, id);

			System.out.println(cityName.substring(0, Math.min(
					cityName.length(), 3))
					+ "|34.92,138.62,48"
					+ "|"
					+ dateUTC
					+ "|"
					+ CLIMATE
					+ "|"
					+ TEMPARATURE + "|" + PRESSURE + "|" + HUMIDITY);

			cacheObj.put(cityName, cityName.substring(0, Math.min(cityName
					.length(), 3))
					+ "|34.92,138.62,48"
					+ "|"
					+ dateUTC
					+ "|"
					+ CLIMATE
					+ "|"
					+ TEMPARATURE + "|" + PRESSURE + "|" + HUMIDITY);
		}
		PrintWriter writter = new PrintWriter(OUTPUT_CACHE_FILE);
		writter.print(cacheObj.toString());
		writter.close();
	}

	public static void readPropertFile(String[] args) {
		log.info("Entering method readPropertFile(args)");
		File path = null;
		try {
			path = new File(args[0]);
			FileInputStream stream = new FileInputStream(path);
			prop.load(stream);
			stream.close();
			INPUT_FILE = prop.getProperty("INPUT_FILE");
			OUTPUT_CACHE_FILE = prop.getProperty("OUTPUT_CACHE_FILE");
			if (INPUT_FILE == null || INPUT_FILE.equalsIgnoreCase("")) {
				System.out
						.println("input Property File path is invalid. Exiting");
				System.exit(0);
			}
			if (OUTPUT_CACHE_FILE == null || OUTPUT_CACHE_FILE.equalsIgnoreCase("")) {
				System.out
						.println("Output Property File path is invalid. Exiting");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out
					.println("Error reading the input Property File. Exiting");
			System.exit(0);
		}
		log.info("Exiting method readPropertFile(args)");
	}

	public static void generateJSONObjectFromTxtFile() throws Exception {
		log.info("Entering method generateJSONObjectFromTxtFile()");
		FileReader reader = new FileReader(INPUT_FILE);
		BufferedReader bReader = new BufferedReader(reader);
		String tempValueHolder = "";
		String line = "";
		while ((line = bReader.readLine()) != null) {
			tempValueHolder = tempValueHolder + line;
		}
		try {
			inputObj = new JSONObject(tempValueHolder);
		} catch (Exception e) {
			System.out
					.println("No Valid JSON Object present in the Input File. Exiting");
			System.exit(0);
		}

		bReader.close();
		reader.close();
		log.info("Exiting method generateJSONObjectFromTxtFile()");
	}

	public static String getClimate(String cityName, String id)
			throws IOException, JSONException {
		log.info("Entering the method getClimate(cityName)");
		// Get the openWeatherMap Client Object.
		OwmClient owmClient = new OwmClient();
		// Get the Weather Object for the specified City locations.
		WeatherStatusResponse currentWeather = null;
		try {
			currentWeather = owmClient.currentWeatherAtCity(cityName, id);

		} catch (UnknownHostException e) {
			System.out
					.println("There is no active internet connection, hence not able to retrieve values. Trying to get the old cached values");
			if (cacheObj != null && cacheObj.length() > 0) {
				Iterator iter = inputObj.keys();
				while (iter.hasNext()) {
					String tempCityName = null;
					try {
						tempCityName = (String) iter.next();
						System.out.println(cacheObj.get(tempCityName));
					} catch (Exception e2) {
						System.out.println("value for City :- " + tempCityName
								+ " not found in cache.");
					}

				}
			} else {
				System.out.println("cache also null.Exiting");
			}
			System.exit(0);
		}

		// Check if the mentioned City has Weather Information.
		if (currentWeather.hasWeatherStatus()) {
			// Get the Weather Data Object
			WeatherData weather = (WeatherData) currentWeather
					.getWeatherStatus().get(0);
			// Check if current Weather is available.
			WeatherCondition weatherCondition = (WeatherCondition) weather
					.getWeatherConditions().get(0);
			// Get the Current Weather.
			String description = weatherCondition.getDescription();
			return description;
		}
		log.info("Exiting the method getClimate(cityName)");
		return null;
	}

	public static void getParameters(String cityName, String id)
			throws IOException, JSONException {
		log.info("Exiting the method getParametes(cityName, id, months)");
		// Create a new instance of OpenWeatherMap.
		OpenWeatherMap owm = new OpenWeatherMap(
				"44b7893f584ceea58513ae9795cb0525");
		// Get the CurrentWeather object for the mentioned City.
		CurrentWeather cwd = owm.currentWeatherByCityName(cityName, id);

		if (cwd.getCityName() == null) {
			System.out.println("City is not valid");
			System.exit(0);
		}
		Locale locale = Locale.getDefault();
		TimeZone currentTimeZone = TimeZone.getDefault();
		DateFormat formatter = DateFormat.getDateTimeInstance(
				DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date1 = new Date();
		log.info(date1.toString());

		// convert it into UTC Format
		SimpleDateFormat f = new SimpleDateFormat("E yyyy-MM-dd HH:mm:ss");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateStr = f.format(new Date());
		String[] arr = dateStr.split(" ");
		try {
			dateUTC = arr[1] + "T" + arr[2] + "Z";
		} catch (Exception e) {

			log.info("Ignore the Exception");
		}

		double temps = cwd.getMainInstance().getTemperature();
		temps = (temps - 32) * 5 / 9.0;
		temps = Math.round(temps);

		if (temps > 0) {
			TEMPARATURE = "+" + Double.toString(temps);
		} else {
			TEMPARATURE = Double.toString(temps);
		}

		PRESSURE = cwd.getMainInstance().getPressure();
		HUMIDITY = cwd.getMainInstance().getHumidity();
		log.info("Exiting the method getClimate()");
	}

	public static void getCachedOutputJSONFile() throws IOException {
		log.info("Entering method getCachedOutputJSONFile()");
		File file = new File(OUTPUT_CACHE_FILE);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileReader reader = new FileReader(OUTPUT_CACHE_FILE);
		BufferedReader bReader = new BufferedReader(reader);
		String tempValueHolder = "";
		String line = "";
		while ((line = bReader.readLine()) != null) {
			tempValueHolder = tempValueHolder + line;
		}
		try {
			cacheObj = new JSONObject(tempValueHolder);
		} catch (Exception e) {

		} finally {
			bReader.close();
			reader.close();
		}
		log.info("Exiting method getCachedOutputJSONFile()");
	}
}
