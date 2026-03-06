/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:liulei at Dec 20, 2023 2:13:59 PM
*
*/
package com.nantian.nbp.mybatis.service.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * 基于mybatis-3.4.4 实现, 继承MapperBuilderAssistant 
 * 修改addMappedStatement方法,不写入configuration的MappedStatement中
 * @author w07362
 *
 */
public class FbspMapperBuilderAssistant extends MapperBuilderAssistant {

	private final String resource;
	private Cache currentCache;
	private boolean unresolvedCacheRef; // issue #676

	public FbspMapperBuilderAssistant(Configuration configuration, String resource) {
		super(configuration, resource);
		ErrorContext.instance().resource(resource);
		this.resource = resource;
	}

	@Override
	public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType,
											  SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap,
											  Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType,
											  boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty,
											  String keyColumn, String databaseId, LanguageDriver lang, String resultSets) {

		if (unresolvedCacheRef) {
			throw new IncompleteElementException("Cache-ref not yet resolved");
		}

		id = applyCurrentNamespace(id, false);
		boolean isSelect = sqlCommandType == SqlCommandType.SELECT;

		MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource,
				sqlCommandType).resource(resource).fetchSize(fetchSize).timeout(timeout).statementType(statementType)
						.keyGenerator(keyGenerator).keyProperty(keyProperty).keyColumn(keyColumn).databaseId(databaseId)
						.lang(lang).resultOrdered(resultOrdered).resultSets(resultSets)
						.resultMaps(getStatementResultMaps(resultMap, resultType, id)).resultSetType(resultSetType)
						.flushCacheRequired(valueOrDefault(flushCache, !isSelect))
						.useCache(valueOrDefault(useCache, isSelect)).cache(currentCache);

		ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
		if (statementParameterMap != null) {
			statementBuilder.parameterMap(statementParameterMap);
		}

        return statementBuilder.build();
	}

	private <T> T valueOrDefault(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}

	private ParameterMap getStatementParameterMap(String parameterMapName, Class<?> parameterTypeClass,
			String statementId) {
		parameterMapName = applyCurrentNamespace(parameterMapName, true);
		ParameterMap parameterMap = null;
		if (parameterMapName != null) {
			try {
				parameterMap = configuration.getParameterMap(parameterMapName);
			} catch (IllegalArgumentException e) {
				throw new IncompleteElementException("Could not find parameter map " + parameterMapName, e);
			}
		} else if (parameterTypeClass != null) {
			List<ParameterMapping> parameterMappings = new ArrayList<>();
			parameterMap = new ParameterMap.Builder(configuration, statementId + "-Inline", parameterTypeClass,
					parameterMappings).build();
		}
		return parameterMap;
	}

	private List<ResultMap> getStatementResultMaps(String resultMap, Class<?> resultType, String statementId) {
		resultMap = applyCurrentNamespace(resultMap, true);

		List<ResultMap> resultMaps = new ArrayList<>();
		if (resultMap != null) {
			String[] resultMapNames = resultMap.split(",");
			for (String resultMapName : resultMapNames) {
				try {
					resultMaps.add(configuration.getResultMap(resultMapName.trim()));
				} catch (IllegalArgumentException e) {
					throw new IncompleteElementException(
							"Could not find result map '" + resultMapName + "' referenced from '" + statementId + "'",
							e);
				}
			}
		} else if (resultType != null) {
			ResultMap inlineResultMap = new ResultMap.Builder(configuration, statementId + "-Inline", resultType,
					new ArrayList<>(), null).build();
			resultMaps.add(inlineResultMap);
		}
		return resultMaps;
	}

}
