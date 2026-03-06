/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:liulei at Dec 20, 2023 2:13:59 PM
*
*/
package com.nantian.nbp.mybatis.service.builder;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 基于mybatis-3.4.4 实现,参考XMLMapperBuilder修改
 * @author liulei
 *
 */
@SuppressWarnings("unused")
public class FbspMapperBuilder extends BaseBuilder {

	private final static Logger logger = LoggerFactory.getLogger(FbspMapperBuilder.class);

	private final XPathParser parser;
	private final FbspMapperBuilderAssistant pbBuilderAssistant;
    private final String resource;

	public FbspMapperBuilder(String xmlStr, Configuration configuration, String resource,
			Map<String, XNode> sqlFragments) {
		this(new XPathParser(xmlStr, true, configuration.getVariables(), new XMLMapperEntityResolver()),
				configuration, resource, sqlFragments);
	}

	private FbspMapperBuilder(XPathParser parser, Configuration configuration, String resource,
			Map<String, XNode> sqlFragments) {
		super(configuration);
		this.pbBuilderAssistant = new FbspMapperBuilderAssistant(configuration, resource);
		this.parser = parser;
        this.resource = resource;
	}

	/**
	 * 修改此方法，删除重复mapper判断，sql调整时使用
	 * @author liulei
	 */
	public MappedStatement parse() {
        return configurationElement(parser.evalNode("/mapper"));
	}

	/**
	 * 调整返回值，原来无返回值
	 * @param context XNode
	 * @return MappedStatement
	 * @author w07362
	 */
	private MappedStatement configurationElement(XNode context) {
		try {
			String namespace = context.getStringAttribute("namespace");
			if (namespace == null || namespace.isEmpty()) {
				throw new BuilderException("Mapper's namespace cannot be empty");
			}
			pbBuilderAssistant.setCurrentNamespace(namespace);
			return buildStatementFromContext(context.evalNodes("select|insert|update|delete"), null);
		} catch (Exception e) {
			throw new BuilderException("Error parsing Mapper XML. The XML location is '" + resource + "'. Cause: " + e,
					e);
		}
	}

	/**
	 * 调整返回值，原来无返回值 pb 每个mapper 文件只有一个SQL,增加逻辑判断，大于一个时打印错误信息
	 * @param list
	 * @return MappedStatement
	 * @author w07362
	 */
	private MappedStatement buildStatementFromContext(List<XNode> list, String requiredDatabaseId) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() > 1) {
			logger.error("List<XNode> list >1 ,error");
			return null;
		}
		XNode context = list.get(0);
		final FbspStatementBuilder statementParser = new FbspStatementBuilder(configuration, pbBuilderAssistant,
				context, requiredDatabaseId);
		try {
			return statementParser.parseStatementNode();
		} catch (IncompleteElementException e) {
			logger.error("statementParser parseStatementNode error,errorMsg [{}]", e.getMessage(), e);
		}
		return null;
	}
}
