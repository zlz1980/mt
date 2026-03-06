package com.nbp.util;

import com.nbp.db.DbConnection;

import java.sql.Connection;
import java.util.List;

public class VersionUtils {

    private final static String SELECT_SQL;
    private final static String GW_SELECT_SQL;

    static {
        SELECT_SQL = "select DEFVALUE from T_PB_SYS_CFG WHERE DEFNAME = 'cache-version'";
        GW_SELECT_SQL = "select DEFVALUE from T_PB_SYS_CFG_GW WHERE DEFNAME = 'cache-version'";
    }

    /**
     * 获取应用版本号
     * @return 版本号
     */
    public static String getVersion(Connection conn) {
        List<String> list = DbConnection.excuteQueryList(conn, SELECT_SQL);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return "";
        }
    }

    /**
     * 获取网关版本号
     * @return 网关版本号
     */
    public static String getGWVersion(Connection conn) {
        List<String> list = DbConnection.excuteQueryList(conn, GW_SELECT_SQL);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return "";
        }
    }

}
