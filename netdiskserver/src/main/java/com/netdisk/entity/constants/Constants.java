package com.netdisk.entity.constants;

public class Constants {

    public static final String SESSION_SHARE_KEY = "session_share_key_";

    public static final String CHECK_CODE_KEY = "check_code_key";

    public static final String CHECK_CODE_KEY_EMAIL = "check_code_key_email";

    public static final int REGISTER_ZERO = 0;


    public static final Integer LENGTH_5 = 5;

    // redis中邮箱消息的键
    public static final String REDIS_KEY_SYS_SETTING = "netdisk:syssetting";

    // 邮箱验证码过期时间
    public static final Integer LENGTH_15 = 15;

    // id长度
    public static final Integer LENGTH_10 = 10;

    // 1MB大小
    public static final Long MB = 1024 * 1024L;

    /**
     * redis key 相关
     */

    /**
     * 过期时间 1分钟
     */
    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60;

    /**
     * 过期时间 1天
     */
    public static final Integer REDIS_KEY_EXPIRES_DAY = REDIS_KEY_EXPIRES_ONE_MIN * 60 * 24;


    /**
     * 过期时间 1 小时
     */
    public static final Integer REDIS_KEY_EXPIRES_ONE_HOUR = REDIS_KEY_EXPIRES_ONE_MIN * 60;

    /**
     * 过期时间5分钟
     */
    public static final Integer REDIS_KEY_EXPIRES_FIVE_MIN = REDIS_KEY_EXPIRES_ONE_MIN * 5;


    public static final String REDIS_KEY_DOWNLOAD = "netdisk:download:";


    public static final String REDIS_KEY_USER_SPACE_USE = "netdisk:user:spaceuse:";

    public static final String REDIS_KEY_USER_FILE_TEMP_SIZE = "netdisk:user:file:temp";

    public static final String SESSION_KEY = "session_key";

    // 头像
    public static final String AVATAR_SUFFIX = ".jpg";

    public static final String FILE_FOLDER_AVATAR_NAME = "/avatar";

    public static final String AVATAR_DEFUALT = "default_avatar.jpg";

    public static final String FILE_FOLDER_FILE = "/file";
    public static final String FILE_FOLDER_TEMP = "/temp";

    public static final String ORDER_RULE = "folder_type desc, last_update_time desc";

    public static final String IMAGE_PNG_SUFFIX = ".png";
    public static final Integer LENGTH_150 = 150;
    public static final String TS_NAME = "index.ts";

    public static final String M3U8_NAME = "index.m3u8";
    public static final String ZERO_STR = "0";
    public static final Integer LENGTH_50 = 50;
    public static final Integer LENGTH_20 = 20;
}
