package com.kay.demo.push.testSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.kay.demo.push.testSender.Constants.FCM_SEND_ENDPOINT;
import static com.kay.demo.push.testSender.Constants.JSON_RESULTS;
import static com.kay.demo.push.testSender.Constants.JSON_RESULTS_ERROR;
import static com.kay.demo.push.testSender.Constants.JSON_RESULTS_MESSAGE_ID;
import static com.kay.demo.push.testSender.Constants.JSON_RESULTS_REGISTRATION_ID;
import static com.kay.demo.push.testSender.Constants.PARAM_COLLAPSE_KEY;
import static com.kay.demo.push.testSender.Constants.PARAM_DATA_PAYLOAD;
import static com.kay.demo.push.testSender.Constants.PARAM_NOTIFICATION_PAYLOAD;
import static com.kay.demo.push.testSender.Constants.PARAM_SEND_TO;
import static com.kay.demo.push.testSender.Constants.PARAM_TIME_TO_LIVE;


/**
 * FCM notification sender by using JSON
 */
public class Sender {

  private static final String UTF8 = "UTF-8";

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final String key;
  private static final int sleepTime = 2000;
  
  /**
   * Default constructor.
   *
   * @param key API key FCM server key.
   */
  public Sender(String key) {
    this.key = nonNull(key);
  }

  public Response send(Message message, String registrationId, int retries) throws Exception {
    int attempt = 0;
    Response result = null;
    boolean tryAgain;
    do {
      attempt++;
      result = sendNoRetry(message, registrationId);
      tryAgain = result == null && attempt <= retries;
      if (tryAgain) {
        sleep(sleepTime);
      }
    } while (tryAgain);
    if (result == null) {
      throw new IOException("Could not send message after " + attempt +  " attempts");
    }
    return result;
  }

  void sleep(long millis) {
	    try {
	      Thread.sleep(millis);
	    } catch (InterruptedException e) {
	      Thread.currentThread().interrupt();
	    }
  }
  
  public Response sendNoRetry(Message message, String registrationId) throws Exception {
	JSONObject obj = new JSONObject();
	JSONObject data = new JSONObject();
	JSONObject notification = new JSONObject();
    obj.put(PARAM_SEND_TO, registrationId);
    String collapseKey = message.getCollapseKey();
    if (collapseKey != null) {
      obj.put(PARAM_COLLAPSE_KEY, collapseKey);
    }
    Integer timeToLive = message.getTimeToLive();
    if (timeToLive != null) {
      obj.put(PARAM_TIME_TO_LIVE, (timeToLive));
    }
    for (Entry<String, String> entry : message.getData().entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      data.put(key, value);
    }
    //Notification
    for (Entry<String, String> entry : message.getNotification().entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        notification.put(key, value);
      }
    if(data!=null){
    	obj.put(PARAM_DATA_PAYLOAD, data);
    }
    if(notification!=null){
    	obj.put(PARAM_NOTIFICATION_PAYLOAD, notification);
    }
    String requestBody = JSONValue.toJSONString(obj);
    System.out.println(">>> Request body: "+requestBody);
    HttpURLConnection conn = post(FCM_SEND_ENDPOINT, requestBody);
    int status = conn.getResponseCode();
    if (status == 503) {
      logger.fine("FCM service is unavailable");
      return null;
    }
    if (status != 200) {
      throw new FCMException(status);
    }
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(conn.getInputStream()));
      try {
        String line = reader.readLine();

        if (line == null || line.equals("")) {
          throw new IOException("Received empty response from FCM service.");
        }
        String value =  getValue(line, JSON_RESULTS_MESSAGE_ID);
        String error = getValue(line, JSON_RESULTS_ERROR);
        if (value!=null) {
          Response.Builder builder = new Response.Builder().messageId(value);
          // check for canonical registration id
          line = reader.readLine();
          if (line != null) {
            value = getValue(line, JSON_RESULTS_REGISTRATION_ID);
            if (value!=null) {
              builder.canonicalRegistrationId(value);
            }
          }

          Response result = builder.build();
          if (logger.isLoggable(Level.FINE)) {
            logger.fine("Message created succesfully (" + result + ")");
          }
          return result;
        } else if (error!=null) {
          return new Response.Builder().errorCode(error).build();
        } else {
          throw new IOException("Received invalid response from FCM: " + line);
        }
      } finally {
        reader.close();
      }
    } finally {
      conn.disconnect();
    }
  }

  /**
   * Make an HTTP post to a given URL.
   *
   * @return HTTP response.
   */
  protected HttpURLConnection post(String url, String body)
      throws IOException {
    return post(url, "application/json", body);
  }

  protected HttpURLConnection post(String url, String contentType, String body)
      throws IOException {
    if (url == null || body == null) {
      throw new IllegalArgumentException("arguments cannot be null");
    }
    if (!url.startsWith("https://")) {
      logger.warning("URL does not use https: " + url);
    }
    logger.fine("Sending POST to " + url);
    logger.finest("POST body: " + body);
    byte[] bytes = body.getBytes();
    HttpURLConnection conn = getConnection(url);
    conn.setDoOutput(true);
    conn.setUseCaches(false);
    conn.setFixedLengthStreamingMode(bytes.length);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", contentType);
    conn.setRequestProperty("Authorization", "key=" + key);
    OutputStream out = conn.getOutputStream();
    out.write(bytes);
    out.close();
    return conn;
  }


  /**
   * Gets an {@link HttpURLConnection} given an URL.
   */
  protected HttpURLConnection getConnection(String url) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
    return conn;
  }

  static <T> T nonNull(T argument) {
    if (argument == null) {
      throw new IllegalArgumentException("argument cannot be null");
    }
    return argument;
  }

/**
 * Get values from result by key
 * 
 * @param body
 * @return value
 */
public static String getValue(String body, String key){
	  String value=null;
	  JSONParser parser = new JSONParser();
	  try {
		JSONObject  jsonResponse = (JSONObject) parser.parse(body);
		JSONArray   obj = (JSONArray)jsonResponse.get(JSON_RESULTS);
		for(int i =0; i<obj.size(); i++){
			JSONObject object = (JSONObject)obj.get(i);
			if(object.get(key)!=null){
				value = object.get(key).toString();
				break;
			}
		}
	} catch (ParseException e) {
		e.printStackTrace();
	}
	  return value;
  }
}
