package org.jts.log4j2.filter;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

@Plugin(name = "CustomFilter", category = "Core", elementType = Filter.ELEMENT_TYPE, printObject = true)
public class CustomFilter extends AbstractFilter {

    private final String keyword;

    @PluginFactory
    public static CustomFilter createFilter(@PluginAttribute("keyword") String keyword) {
        return new CustomFilter(keyword);
    }

    private CustomFilter(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Result filter(LogEvent event) {
        if (event.getMessage().getFormattedMessage().contains(keyword)) {
            return Result.DENY;
        }
        return Result.NEUTRAL;
    }
}
