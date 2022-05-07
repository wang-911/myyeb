package com.xxx.server.utils;

public class Constants {
    /**
     * 文件后缀
     */
    public static final String FILE_SUFFIX = ".sql";

    /**
     * 备份数据库的名称
     */
    public static final String DATA_BASE_NAME="yeb-dev";
    /**
     * 判断操作系统类型、Linux|Windows
     */
    public static boolean isSystem(String osName) {
        Boolean flag = null;
        if (osName.startsWith("windows")) {
            flag = true;
        } else if (osName.startsWith("linux")) {
            flag = false;
        }
        return flag;
    }
}
