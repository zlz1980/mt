package org.jts.predicate;

import org.jts.cache.CacheClientApi;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.http.server.PathContainer;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.springframework.http.server.PathContainer.parsePath;

/**
 *
 *  predicates:
 *    - name: BpsSvcDispatcher
 *      args:
 *        dbFlag: true #没命中是否开启DB查询
 */
public class SvcDispatcherRoutePredicateFactory extends AbstractRoutePredicateFactory<SvcDispatcherRoutePredicateFactory.Config>{

    private static final Logger LOGGER = LoggerFactory.getLogger(SvcDispatcherRoutePredicateFactory.class);

    private final CacheClientApi cacheClientApi;

    private final SqlSessionTemplate sqlSessionTemplate;

    public SvcDispatcherRoutePredicateFactory(CacheClientApi cacheClientApi, SqlSessionTemplate sqlSessionTemplate) {
        super(Config.class);
        this.cacheClientApi = cacheClientApi;
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return (GatewayPredicate) exchange -> {
            PathContainer path = parsePath(exchange.getRequest().getURI().getRawPath());
            List<String> list = cacheClientApi.getAllNewCodeList();
            PathContainer typePathContainer = path.subPath(3,4);
            String urlType = typePathContainer.value();
            PathContainer codePathContainer;
            // /webapi/execWorkFlow/f/zd9908/nt0016/1008-02
            if(Objects.equals("execWorkFlow",urlType)){
                codePathContainer = path.subPath(7,8);
            // /webapi/execTran/0720/1005-62
            }else if(Objects.equals("execTran",urlType)){
                codePathContainer = path.subPath(5,6);
            }else {
                throw new RuntimeException(String.format("Unsupported URL type[%s]",urlType));
            }
            // 缓存校验
            String code = codePathContainer.value();
            boolean isNewCode = list.contains(code);
            if(!isNewCode){
                // DB校验
                if(config.isDbFlag()){
                    Integer count = sqlSessionTemplate.selectOne("bpsCache.findNewCode",code);
                    if(count > 0){
                        return true;
                    }
                }
                LOGGER.info("[{}] is old code",code);
            }
            return isNewCode;
        };
    }

    public static class Config {
        private boolean dbFlag;

        public Config(boolean dbFlag) {
            this.dbFlag = dbFlag;
        }

        public boolean isDbFlag() {
            return dbFlag;
        }

        public void setDbFlag(boolean dbFlag) {
            this.dbFlag = dbFlag;
        }
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("dbFlag");
    }
}
