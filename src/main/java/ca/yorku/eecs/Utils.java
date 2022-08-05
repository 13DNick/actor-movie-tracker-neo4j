package ca.yorku.eecs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import com.sun.net.httpserver.HttpExchange;

public class Utils {
	
	public static String uriDb = "bolt://localhost:7687";
    public static String uriUser ="http://localhost:8080";
    public static Config config = Config.builder().withoutEncryption().build();
    public static Driver driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","123456"), config);
	
	public static String convert(InputStream inputStream) throws IOException {
		 
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
	
	public static void sendString(HttpExchange request, String data, int restCode) 
			throws IOException {
		request.sendResponseHeaders(restCode, data.length());
        OutputStream os = request.getResponseBody();
        os.write(data.getBytes());
        os.close();
	}
	
	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
	
	public static boolean withinRange(int range, double rating) {
		if(range == 0) {
			if(rating >= 0 && rating <= 0.99) {
				return true;
			}
			return false;
		} else if(range == 1) {
			if(rating >= 1.0 && rating <= 1.99) {
				return true;
			}
			return false;
		} else if(range == 2) {
			if(rating >= 2.0 && rating <= 2.99) {
				return true;
			}
			return false;
		} else if(range == 3) {
			if(rating >= 3.0 && rating <= 3.99) {
				return true;
			}
			return false;
		} else if(range == 4) {
			if(rating >= 4.0 && rating <= 4.99) {
				return true;
			}
			return false;
		} else {
			if(rating == 5.0) {
				return true;
			}
			return false;
		}
	}
	
	public static double trimToTwoDecimals(double rating) {
		String s = String.format("%.2f", rating);
		return Double.parseDouble(s);
	}
}
