package com.kay.demo.push.testSender;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FcmSender {

	private static final String serverKey = "AAAASzR7xeM:APA91bHieE7Ad6vVHrSMNpB_idaC_vAdqrfi4u2QBdnwj5S157JGnCY33hQLOCvR90j2CSBDjIPjf7FB3-jHE_Nx0sLXf2g4PCenxJNq97LziMEH-EOH6Bv8diEgUcI14khmrarcq8No";

	// apk安装成功获取到的fcm token.
	 private static final String deviceToken ="fTOAMUw4xw4:APA91bH7lnm3s-9Y1uO8fjMna2b9WuELwSUEhPV8-TLgRhimujTtXErigu77ZJeatUzjTetlXvU-wAQScpM1C0L8tCh4U3MMiPYF2Q7aX2AwCoxXwxil1nnyBISReLD_k7RG9ASXBOMf";

	public static void main(String[] args) {
		sendData();
	}
	

	/**
	 * 取得当前时间戳（精确到秒）
	 * 
	 * @return
	 */
	public static String timeStamp() {
		long time = System.currentTimeMillis();
		String t = String.valueOf(time / 1000);
		return t;
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds
	 *            精确到秒的字符串
	 * @param format
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	private static void sendData() {

		String date = timeStamp2Date(timeStamp(), "yyyy-MM-dd HH:mm:ss");

		try {
			Sender sender = new Sender(serverKey);
			Message.Builder messageBuilder = new Message.Builder().timeToLive(30);

//			 messageBuilder = messageBuilder.addNotification("title", "Message title");
//			 messageBuilder = messageBuilder.addNotification("body", "Message body --->addNotification");

			messageBuilder = messageBuilder.addData("title", "Message title");
			messageBuilder = messageBuilder.addData("body", date+" Msg body addData");

			// messageBuilder = messageBuilder.addNotification("body", "Message body");
			Response response = sender.send(messageBuilder.build(), deviceToken, 1);
			System.out.println("getCanonicalRegistrationId::" + response.getCanonicalRegistrationId());
			System.out.println("getErrorCodeName::" + response.getErrorCodeName());
			System.out.println("getMessageId::" + response.getMessageId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
