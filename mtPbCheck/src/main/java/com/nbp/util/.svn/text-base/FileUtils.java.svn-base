package com.nbp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nbp.util.Constants.BACKSLASH;
import static com.nbp.util.Constants.BACKSLASH_REPLACE;
import static com.nbp.util.Constants.BASEMSG;
import static com.nbp.util.Constants.CONDMSG;
import static com.nbp.util.Constants.DATAMSG;
import static com.nbp.util.Constants.DATA_NUM;
import static com.nbp.util.Constants.DATA_SPLIT_REGEX;
import static com.nbp.util.Constants.DELETE_SQL_LIST_NAME;
import static com.nbp.util.Constants.FLDSMSG;
import static com.nbp.util.Constants.INSERT_SQL_LIST_NAME;
import static com.nbp.util.Constants.LINE_SEPARATOR;
import static com.nbp.util.Constants.LINE_SEPARATOR2;
import static com.nbp.util.Constants.LINE_SEPARATOR_REPLACE;
import static com.nbp.util.Constants.LINE_SEPARATOR_REPLACE2;
import static com.nbp.util.Constants.SINGLE_QUOTE;
import static com.nbp.util.Constants.SINGLE_QUOTE_REPLACE;
import static com.nbp.util.Constants.SPLIT_REGEX;
import static com.nbp.util.Constants.T_PB_EXT_API;
import static com.nbp.util.Constants.T_PB_EXT_API_CONDMSG;
import static com.nbp.util.Constants.T_PB_FLOW;
import static com.nbp.util.Constants.T_PB_FLOW_CONDMSG;
import static com.nbp.util.Constants.T_PB_FLOW_PARA;
import static com.nbp.util.Constants.T_PB_FLOW_PARA_CONDMSG;
import static com.nbp.util.Constants.T_PB_SYS_CFG;
import static com.nbp.util.Constants.T_PB_SYS_CFG_GW;
import static com.nbp.util.Constants.VERSION;

/**
 * @author w46838
 */
public class FileUtils {

	private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	private final static String NUM_1 = "1";

    /**
     * 全局参数，存储导入、导出文件的全路径
     */
    private final static List<String> FILE_PATH_LIST = new ArrayList<>();

	/** 需要特殊处理删除语句的表的集合，key:表名，value:条件字段集合 */
	private final static Map<String, List<String>> CONDMSG_MAP = new HashMap<String, List<String>>(4) {
		{
			put(T_PB_FLOW, Arrays.asList(T_PB_FLOW_CONDMSG));
			put(T_PB_FLOW_PARA, Arrays.asList(T_PB_FLOW_PARA_CONDMSG));
			put(T_PB_EXT_API, Arrays.asList(T_PB_EXT_API_CONDMSG));
		}
	};

	/**
	 * 写文件
	 * @param fileData 文件内容
	 * @param inFilePath 文件路径
	 */
	public static void writeFile(String fileData, String inFilePath) {
		File file = new File(inFilePath);
		File parentFile = file.getParentFile();
		if(!parentFile.exists()) {
			if(!parentFile.mkdirs()){
				logger.error("创建目录失败,文件目录:{}", parentFile.getAbsolutePath());
			}
		}
		BufferedWriter wr = null;
		try {
			wr = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8));
			wr.write(fileData);
			wr.write("1");
			wr.write(MD5(fileData));
			wr.flush();
		} catch (IOException e) {
			logger.error("文件写入失败,文件路径:{}", file.getAbsolutePath());
			logger.error(e.getMessage());
			throw new RuntimeException("文件写入失败！" + e.getMessage(),e);
		} finally {
			if (null != wr) {
				try {
					wr.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
        // 记录导出的文件路径
        FILE_PATH_LIST.add(inFilePath);
	}

	/**
	 * 读文件,并校验MD5
	 * @param file 文件
	 * @return insert、delete SQL集合
	 */
	public static Map<String, Object> readFile(File file) {
		List<String> dataList = new ArrayList<>();
		if (!file.exists()) {
			throw new RuntimeException("读取文件失败：文件不存在！");
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader re = null;
		try {
			re = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
			while(re.ready()) {
				String line = re.readLine();
				sb.append(line);
				sb.append(LINE_SEPARATOR);
				dataList.add(line.replace(LINE_SEPARATOR_REPLACE2,LINE_SEPARATOR2)
						.replace(LINE_SEPARATOR_REPLACE,LINE_SEPARATOR));
			}
			sb.delete(sb.lastIndexOf(LINE_SEPARATOR), sb.length());
			String data = sb.substring(0, sb.lastIndexOf(LINE_SEPARATOR) + LINE_SEPARATOR.length());
			String md5 = sb.substring(sb.lastIndexOf(LINE_SEPARATOR)+LINE_SEPARATOR.length());
			if ("".equals(data)) {
				throw new RuntimeException("读取文件失败：数据文件内容为空！");
			} else if ("".equals(md5)) {
				throw new RuntimeException("读取文件失败：数据文件中MD5内容为空！");
			} else if(md5.startsWith(NUM_1) && !md5.equals(NUM_1 + MD5(data))) {
				throw new RuntimeException("读取文件失败：数据文件中MD5校验失败！filename=" + file.getName());
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("读取文件失败！" + e.getMessage());
		} finally {
			if (null != re) {
				try {
					re.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return toSql(dataList);
	}

	/**
	 * 将文件数据转换为SQL
	 * @param fileData 文件数据
	 * @return SQL
	 */
	public static Map<String, Object> toSql(List<String> fileData){
		Map<String, Object> sqlMap = new HashMap<>(16);
		List<String> deleteSqlList = new ArrayList<>();
		List<String> insertSqlList = new ArrayList<>();
		String tableName = "";
		List<String> primaryKeys = new ArrayList<>();
		List<String> columnNames = new ArrayList<>();
		List<String> params = new ArrayList<>();
		//记录文件中数据行数
		int dataNum = 0;
		//使用set集合判断特殊处理的表是否已添加删除语句
		Set<String> set = new HashSet<>();
		for (String line : fileData) {
			if (line.startsWith(BASEMSG)) {
				tableName = line.split(SPLIT_REGEX)[1];
			}
			if (line.startsWith(CONDMSG)) {
				primaryKeys.clear();
				//针对配置的表获取条件字段的特殊处理逻辑
				if (CONDMSG_MAP.containsKey(tableName.toUpperCase())) {
					primaryKeys.addAll(CONDMSG_MAP.get(tableName.toUpperCase()));
					//默认的获取条件字段逻辑
				} else {
					String[] primaryKeyArr = line.split(SPLIT_REGEX);
					primaryKeys.addAll(Arrays.asList(primaryKeyArr));
				}
			}
			if (line.startsWith(FLDSMSG)) {
				columnNames.clear();
				String[] columnNameArr = line.split(SPLIT_REGEX);
				columnNames.addAll(Arrays.asList(columnNameArr));
			}
			if (line.startsWith(DATAMSG)) {
				//版本号相关数据不做插入
				if (T_PB_SYS_CFG.equalsIgnoreCase(tableName) && line.contains(VERSION)) {
					continue;
				}
				if (T_PB_SYS_CFG_GW.equalsIgnoreCase(tableName) && line.contains(VERSION)) {
					continue;
				}
				params.clear();
				dataNum++;
				String[] paramArr = line.split(DATA_SPLIT_REGEX);
				params.addAll(Arrays.asList(paramArr));
				//补齐参数个数
				for(int i = params.size(); i < columnNames.size(); i++) {
					params.add("");
				}

				StringBuilder deleteSql = new StringBuilder("delete from ");
				StringBuilder insertSql = new StringBuilder("insert into ");
				deleteSql.append(tableName);
				insertSql.append(tableName);
				deleteSql.append(" where 1=1 ");
				insertSql.append(" (");
				for (int i = 1; i < columnNames.size(); i++) {
					if (i != 1) {
						insertSql.append(",");
					}
					insertSql.append(columnNames.get(i));
					for (int j = 1; j < primaryKeys.size(); j++) {
						if (columnNames.get(i).equalsIgnoreCase(primaryKeys.get(j))) {
							deleteSql.append(" and ");
							deleteSql.append(primaryKeys.get(j));
							deleteSql.append(" = '");
							deleteSql.append(params.get(i));
							deleteSql.append("'");
							break;
						}
					}
				}
				insertSql.append(") values (");
				for (int i = 1; i < params.size(); i++) {
					if (i != 1) {
						insertSql.append(",");
					}
					if ("null".equals(params.get(i))) {
						insertSql.append("null");
					} else {
						insertSql.append(SINGLE_QUOTE);
						String paramData = params.get(i).replace(SINGLE_QUOTE, SINGLE_QUOTE_REPLACE)
								.replace(BACKSLASH, BACKSLASH_REPLACE);
						insertSql.append(paramData);
						insertSql.append(SINGLE_QUOTE);
					}
				}
				insertSql.append(")");
				//对生成的delete语句进行判断，存在无条件的delete语句时，抛出异常
				if (deleteSql.toString().trim().endsWith("where 1=1")) {
					logger.error("存在无条件的delete语句，请检查文件数据与表结构！");
					logger.error("deleteSql: " + deleteSql.toString());
					throw new RuntimeException("存在无条件的delete语句，请检查文件数据！");
				}
				//判断待特殊处理的表是否已添加删除语句
				if (CONDMSG_MAP.containsKey(tableName.toUpperCase())) {
					if(!set.contains(tableName)) {
						deleteSqlList.add(deleteSql.toString());
						set.add(tableName);
					}
				} else {
					deleteSqlList.add(deleteSql.toString());
				}
				insertSqlList.add(insertSql.toString());
			}
		}
		sqlMap.put(DATA_NUM, dataNum);
		sqlMap.put(DELETE_SQL_LIST_NAME, deleteSqlList);
		sqlMap.put(INSERT_SQL_LIST_NAME, insertSqlList);
		return sqlMap;
	}

	/**
	 * 生成MD5签名
	 * @param s 要生成md5的字符串
	 * @return MD5签名
	 */
	public static String MD5(String s) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte [] md5Bytes = md5.digest(s.getBytes());
			StringBuilder sb = new StringBuilder();
			for(byte b:md5Bytes) {
				String hex = Integer.toHexString(b&0xFF);
				if (hex.length() == 1) {
					sb.append(0);
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5签名失败！" + e.getMessage());
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if (null != children) {
				for (String child : children) {
					boolean success = deleteDir(new File(dir, child));
					if (!success) {
						// 如果在删除子目录或文件时发生错误，可以选择退出方法或抛出异常
						// 这里为了简单起见，我们只是打印一条消息
						logger.error("Failed to delete {}/{}", dir, child);
						return false;
					}
				}
			}
		}
		// 目录现在是空的，所以可以安全地删除它
		boolean success = dir.delete();
		if (!success) {
			logger.error("Failed to delete directory: {}", dir);
			return false;
		} else {
			logger.info("Directory deleted: {}", dir);
			return true;
		}
	}

    /**
     * 写导出目录文件
     * @param inFilePath 文件路径
     */
    public static void writeMenuFile(String inFilePath) {
        File file = new File(inFilePath);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()) {
            if(!parentFile.mkdirs()){
                logger.error("创建目录失败,文件目录:{}", parentFile.getAbsolutePath());
            }
        }
        BufferedWriter wr = null;
        try {
            wr = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8));
            for (String filePath : FILE_PATH_LIST) {
                wr.write(filePath);
                wr.newLine();
            }
            wr.flush();
        } catch (IOException e) {
            logger.error("文件写入失败,文件路径:{}", file.getAbsolutePath());
            logger.error(e.getMessage());
            throw new RuntimeException("文件写入失败！" + e.getMessage(),e);
        } finally {
            FILE_PATH_LIST.clear();
            if (null != wr) {
                try {
                    wr.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

	/**
	 * 读导入目录文件
	 */
	public static void readMenuFile(String inFilePath) {
		File file = new File(inFilePath);
		if(!file.exists()) {
			logger.error("导入目录不存在,文件目录:{}", inFilePath);
			throw new RuntimeException("导入目录不存在,请检查！");
		}
		FILE_PATH_LIST.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
			reader.lines().forEach(line -> FILE_PATH_LIST.add(line));
		} catch (IOException e) {
			logger.error("文件读取失败,文件路径:{}", inFilePath);
			logger.error(e.getMessage());
			throw new RuntimeException("文件读取失败！" + e.getMessage(),e);
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static boolean checkInputFile(String inFilePath) {
		return FILE_PATH_LIST.contains(inFilePath);
	}

	/**
	 * 递归获取指定目录下所有非目录文件
	 * @param file 指定目录
	 */
	public static List<String> getFileList(File file) {
		List<String> fileList = new ArrayList<>();
		if(file.isDirectory()){
			for(File subFile: Objects.requireNonNull(file.listFiles())){
				fileList.addAll(getFileList(subFile));
			}
		} else {
			fileList.add(file.getAbsolutePath());
		}
		return fileList;
	}

	/**
	 * 校验导入文件目录，并打印日志，返回文件列表
	 * @param fileList 实际导入文件列表
	 * @return fileList 待导入文件列表
	 * @throws RuntimeException 文件列表中缺少导入文件,请检查导入文件是否完整
	 */
	public static List<String> getDifference(List<String> fileList){
		List<String> list1 = fileList.stream()
			.filter(element -> !new HashSet<>(FILE_PATH_LIST).contains(element)).collect(Collectors.toList());
		List<String> list2 = FILE_PATH_LIST.stream()
			.filter(element -> !new HashSet<>(fileList).contains(element)).collect(Collectors.toList());
		if(list1.size() > 0){
			list1.forEach(filePath -> logger.error("文件列表中存在非导入文件:{}", filePath));
		}
		if(list2.size() > 0){
			list2.forEach(filePath -> logger.error("文件列表中缺少导入文件:{}", filePath));
			throw new RuntimeException("文件列表中缺少导入文件,请检查导入文件是否完整");
		}
		return fileList.stream()
			.filter(element -> !new HashSet<>(list1).contains(element)).collect(Collectors.toList());
	}

}
