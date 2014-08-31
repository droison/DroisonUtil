package com.droison.constants;

public class Constant {
	/**
	 * 网络访问相关的常量,以200作为头，希望HTTP code永远200！
	 */
	public static final int HANDLER_MESSAGE_NORMAL = 0X200001;
	public static final int HANDLER_MESSAGE_NULL = 0X200002;
	public static final int HANDLER_HTTPSTATUS_ERROR = 0X200003;
	public static final int HANDLER_MESSAGE_NONETWORK = 0X200004;

	public static final int HANDLER_APK_DOWNLOAD_FINISH = 0X30001;
	public static final int HANDLER_APK_DOWNLOAD_PROGRESS = 0X30002;
	public static final int HANDLER_APK_STOP = 0X30003;
	public static final int HANDLER_VERSION_UPDATE = 0X30004;
	public static final int databaseversion = 3;
}
