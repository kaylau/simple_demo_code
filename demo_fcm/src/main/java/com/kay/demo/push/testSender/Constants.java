package com.kay.demo.push.testSender;

/**
 * Constants used on FCM service.
 */
public final class Constants {

  /**
   * Endpoint for sending messages.
   */
  public static final String FCM_SEND_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
  
  //Send to JSON param key
  public static final String PARAM_SEND_TO = "to";
  
  //Collapse key JSON param key
  public static final String PARAM_COLLAPSE_KEY = "collapse_key";
  
  //Time To Live JSON param key
  public static final String PARAM_TIME_TO_LIVE = "time_to_live";
  
  //Restricted Package Name JSON param key
  public static final String PARAM_RESTRICTED_PACKAGE_NAME = "restrictedPackageName";
  
  //Priority payload param key
  public static final String PARAM_PRIORITY = "priority";
  
  //Data payload param key
  public static final String PARAM_DATA_PAYLOAD = "data";
  
  //Notification payload param key
  public static final String PARAM_NOTIFICATION_PAYLOAD = "notification";
  
  public static final String JSON_RESULTS = "results";
  
  //canonical registration id
  public static final String JSON_RESULTS_REGISTRATION_ID = "registration_id";
  
  //Response message id
  public static final String JSON_RESULTS_MESSAGE_ID = "message_id";
  
  //Response error
  public static final String JSON_RESULTS_ERROR = "error";
  
  private Constants() {
    throw new UnsupportedOperationException();
  }

}
