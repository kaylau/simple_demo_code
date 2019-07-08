package com.kay.demo.push.testSender;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Message implements Serializable {
  private String collapseKey;
  private Integer timeToLive;
  private String restrictedPackageName;
//Valid values are "normal" and "high"
  private String priority="high";
  private final Map<String, String> data;
  private final Map<String, String> notification;
  
  public static final class Builder {
    private final Map<String, String> data;
    private final Map<String, String> notification;
    private String collapseKey;
    private Integer timeToLive;
    private String restrictedPackageName;
    //Valid values are "normal" and "high"
    private String priority="high";
    
    public Builder() {
      this.data = new LinkedHashMap<String, String>();
      this.notification = new LinkedHashMap<String, String>();
    }

    /**
     * Sets the collapseKey property.
     */
    public Builder collapseKey(String value) {
      collapseKey = value;
      return this;
    }

    /**
     * Sets the time to live, in seconds.
     */
    public Builder timeToLive(int value) {
      timeToLive = value;
      return this;
    }

    /**
     * Sets the restrictedPackageName.
     */
    public Builder restrictedPackageName(String value){
    	restrictedPackageName = value;
    	return this;
    }
    
    /**
     * Sets the priority.
     */
    public Builder priority(String value){
    	priority = value;
    	return this;
    }
    
    /**
     * Adds a key/value pair to the payload data.
     */
    public Builder addData(String key, String value) {
      data.put(key, value);
      return this;
    }
    /**
     * Adds a key/value pair to the payload notification.
     */
    
    public Builder addNotification(String key, String value) {
        notification.put(key, value);
        return this;
      }

    public Builder setData(Map<String,String> data) {
      this.data.clear();
      this.data.putAll(data);
      return this;
    }
    public Builder setNotification(Map<String,String> notification) {
        this.notification.clear();
        this.notification.putAll(notification);
        return this;
      }

    public Message build() {
      return new Message(this);
    }
  }

  private Message(Builder builder) {
    collapseKey = builder.collapseKey;
    data = Collections.unmodifiableMap(builder.data);
    notification = Collections.unmodifiableMap(builder.notification);
    timeToLive = builder.timeToLive;
    restrictedPackageName = builder.restrictedPackageName;
    priority = builder.priority;
  }

  /**
   * Gets the collapse key.
   */
  public String getCollapseKey() {
    return collapseKey;
  }


  /**
   * Gets the delayWhileIdle flag.
   */
  public Integer getTimeToLive() {
    return timeToLive;
  }
  
  /**
   * Gets the package name.
   */
  public String getRestrictedPackageName() {
	    return restrictedPackageName;
  }
  
  /**
   * Gets the priority.
   */
  public String getPriority() {
	    return priority;
  }
  
  /**
   * Gets the data.
   */
  public Map<String, String> getData() {
    return data;
  }
  /**
   * Gets the payload notification.
   */
  public Map<String, String> getNotification() {
	    return notification;
 }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Message(");
    if (collapseKey != null) {
      builder.append("collapseKey=").append(collapseKey).append(", ");
    }
    if (timeToLive != null) {
      builder.append("timeToLive=").append(timeToLive).append(", ");
    }
    if (restrictedPackageName != null) {
        builder.append("restrictedPackageName=").append(restrictedPackageName).append(", ");
      }
    if (priority != null) {
        builder.append("priority=").append(priority).append(", ");
      }
    if (!data.isEmpty()) {
      builder.append("data: {");
      for (Map.Entry<String, String> entry : data.entrySet()) {
        builder.append(entry.getKey()).append("=").append(entry.getValue())
            .append(",");
      }
      builder.delete(builder.length() - 1, builder.length());
      builder.append("}").append(",");
    }
    if (!notification.isEmpty()) {
        builder.append("notification: {");
        for (Map.Entry<String, String> entry : notification.entrySet()) {
          builder.append(entry.getKey()).append("=").append(entry.getValue())
              .append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        builder.append("}");
      }
    if (builder.charAt(builder.length() - 1) == ' ') {
      builder.delete(builder.length() - 2, builder.length());
    }
    builder.append(")");
    return builder.toString();
  }

}
