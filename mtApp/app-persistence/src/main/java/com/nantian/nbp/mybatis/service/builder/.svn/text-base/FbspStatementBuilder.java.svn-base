/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:liulei at Dec 20, 2023 2:13:59 PM
*
*/
package com.nantian.nbp.mybatis.service.builder;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Locale;

/**
 * 基于mybatis-3.4.4 实现,参考 XMLStatementBuilder修改 修改 parseStatementNode 方法返回值
 * @author liulei
 */
public class FbspStatementBuilder extends BaseBuilder {

	private final FbspMapperBuilderAssistant fbspBuilderAssistant;
	private final XNode context;
	private final String requiredDatabaseId;

	public FbspStatementBuilder(Configuration configuration, FbspMapperBuilderAssistant builderAssistant,
			XNode context) {
		this(configuration, builderAssistant, context, null);
	}

	public FbspStatementBuilder(Configuration configuration, FbspMapperBuilderAssistant builderAssistant, XNode context,
			String databaseId) {
		super(configuration);
		this.fbspBuilderAssistant = builderAssistant;
		this.context = context;
		this.requiredDatabaseId = databaseId;
	}

	/**
	 * 调整返回值
	 * @return MappedStatement
	 * @author liulei
	 */

	public MappedStatement parseStatementNode() {
		String id = context.getStringAttribute("id");
		String databaseId = context.getStringAttribute("databaseId");

		if (!databaseIdMatchesCurrent(id, databaseId, this.requiredDatabaseId)) {
			return null;
		}

		Integer fetchSize = context.getIntAttribute("fetchSize");
		Integer timeout = context.getIntAttribute("timeout");
		String parameterMap = context.getStringAttribute("parameterMap");
		String parameterType = context.getStringAttribute("parameterType");
		Class<?> parameterTypeClass = resolveClass(parameterType);
		String resultMap = context.getStringAttribute("resultMap");
		String resultType = context.getStringAttribute("resultType");
		String lang = context.getStringAttribute("lang");
		LanguageDriver langDriver = getLanguageDriver(lang);

		Class<?> resultTypeClass = resolveClass(resultType);
		String resultSetType = context.getStringAttribute("resultSetType");
		StatementType statementType = StatementType
				.valueOf(context.getStringAttribute("statementType", StatementType.PREPARED.toString()));
		ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);

		String nodeName = context.getNode().getNodeName();
		SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
		boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
		boolean flushCache = context.getBooleanAttribute("flushCache", !isSelect);
		boolean useCache = context.getBooleanAttribute("useCache", isSelect);
		boolean resultOrdered = context.getBooleanAttribute("resultOrdered", false);

		// Include Fragments before parsing
		XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, fbspBuilderAssistant);
		includeParser.applyIncludes(context.getNode());

		// Parse selectKey after includes and remove them.
		processSelectKeyNodes(id, parameterTypeClass, langDriver);

		// Parse the SQL (pre: <selectKey> and <include> were parsed and
		// removed)
		SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
		String resultSets = context.getStringAttribute("resultSets");
		String keyProperty = context.getStringAttribute("keyProperty");
		String keyColumn = context.getStringAttribute("keyColumn");
		KeyGenerator keyGenerator;
		String keyStatementId = id + SelectKeyGenerator.SELECT_KEY_SUFFIX;
		keyStatementId = fbspBuilderAssistant.applyCurrentNamespace(keyStatementId, true);
		if (configuration.hasKeyGenerator(keyStatementId)) {
			keyGenerator = configuration.getKeyGenerator(keyStatementId);
		} else {
			keyGenerator = context.getBooleanAttribute("useGeneratedKeys",
					configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType))
							? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
		}

		return fbspBuilderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
				parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache,
				resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);
	}

	private void processSelectKeyNodes(String id, Class<?> parameterTypeClass, LanguageDriver langDriver) {
		List<XNode> selectKeyNodes = context.evalNodes("selectKey");
		if (configuration.getDatabaseId() != null) {
			parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, configuration.getDatabaseId());
		}
		parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, null);
		removeSelectKeyNodes(selectKeyNodes);
	}

	private void parseSelectKeyNodes(String parentId, List<XNode> list, Class<?> parameterTypeClass,
			LanguageDriver langDriver, String skRequiredDatabaseId) {
		for (XNode nodeToHandle : list) {
			String id = parentId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
			String databaseId = nodeToHandle.getStringAttribute("databaseId");
			if (databaseIdMatchesCurrent(id, databaseId, skRequiredDatabaseId)) {
				parseSelectKeyNode(id, nodeToHandle, parameterTypeClass, langDriver, databaseId);
			}
		}
	}

	private void parseSelectKeyNode(String id, XNode nodeToHandle, Class<?> parameterTypeClass,
			LanguageDriver langDriver, String databaseId) {
		String resultType = nodeToHandle.getStringAttribute("resultType");
		Class<?> resultTypeClass = resolveClass(resultType);
		StatementType statementType = StatementType
				.valueOf(nodeToHandle.getStringAttribute("statementType", StatementType.PREPARED.toString()));
		String keyProperty = nodeToHandle.getStringAttribute("keyProperty");
		String keyColumn = nodeToHandle.getStringAttribute("keyColumn");
		boolean executeBefore = "BEFORE".equals(nodeToHandle.getStringAttribute("order", "AFTER"));

		// defaults
		boolean useCache = false;
		boolean resultOrdered = false;
		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		Integer fetchSize = null;
		Integer timeout = null;
		boolean flushCache = false;
		String parameterMap = null;
		String resultMap = null;
		ResultSetType resultSetTypeEnum = null;

		SqlSource sqlSource = langDriver.createSqlSource(configuration, nodeToHandle, parameterTypeClass);
		SqlCommandType sqlCommandType = SqlCommandType.SELECT;

		fbspBuilderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
				parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache,
				resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, langDriver, null);

		id = fbspBuilderAssistant.applyCurrentNamespace(id, false);

		MappedStatement keyStatement = configuration.getMappedStatement(id, false);
		configuration.addKeyGenerator(id, new SelectKeyGenerator(keyStatement, executeBefore));
	}

	private void removeSelectKeyNodes(List<XNode> selectKeyNodes) {
		for (XNode nodeToHandle : selectKeyNodes) {
			nodeToHandle.getParent().getNode().removeChild(nodeToHandle.getNode());
		}
	}

	private boolean databaseIdMatchesCurrent(String id, String databaseId, String requiredDatabaseId) {
		if (requiredDatabaseId != null) {
            return requiredDatabaseId.equals(databaseId);
		} else {
			if (databaseId != null) {
				return false;
			}
			// skip this statement if there is a previous one with a not null
			// databaseId
			id = fbspBuilderAssistant.applyCurrentNamespace(id, false);
			if (this.configuration.hasStatement(id, false)) {
				// issue #2
				MappedStatement previous = this.configuration.getMappedStatement(id, false);
                return previous.getDatabaseId() == null;
			}
		}
		return true;
	}

	private LanguageDriver getLanguageDriver(String lang) {
		Class<?> langClass = null;
		if (lang != null) {
			langClass = resolveClass(lang);
		}
		return fbspBuilderAssistant.getLanguageDriver(langClass);
	}
}
