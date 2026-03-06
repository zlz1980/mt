package org.jts.filter;

import org.jts.pkg.PkgErr;
import org.jts.pkg.PkgErrRes;
import org.jts.pkg.PkgResHead;
import org.jts.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.jts.pkg.PkgErr.TECH_ERROR_TYPE;
import static org.jts.pkg.PkgHttpHeadKeys.GW_AUTH_ERR_CODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_RETURNCODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_JSON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_GID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_LID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_PID;

public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthGatewayFilterFactory.class);

    private static final String AUTH_HEADER_KEY = "Authorization";

    public AuthGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 获取请求头中的鉴权信息
            String authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER_KEY);
            // 检查鉴权信息是否为空或无效
            if (!isValidAuth(authHeader,config.getToken())) {
                ServerHttpRequest req = exchange.getRequest();
                return handleResponse(req.getHeaders(),exchange.getResponse());
            }
            // 鉴权通过，继续处理请求
            return chain.filter(exchange);
        };
    }

    // 可引入相关算法
    private boolean isValidAuth(String authHeader,String token) {
        if(!StringUtils.hasText(authHeader)){
            LOGGER.warn("请求头中未携带鉴权信息");
            return false;
        }
        return Objects.equals(authHeader,token);
    }

    private Mono<Void> handleResponse(HttpHeaders reqHttpHeaders,ServerHttpResponse res) {
        HttpHeaders httpHeaders = res.getHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<String> gidList = reqHttpHeaders.get(PKG_TRACE_GID);
        if(!CollectionUtils.isEmpty(gidList)){
            httpHeaders.set(PKG_TRACE_GID,gidList.get(0));
        }
        List<String> reqLidList = reqHttpHeaders.get(PKG_TRACE_LID);
        if(!CollectionUtils.isEmpty(reqLidList)){
            httpHeaders.set(PKG_TRACE_PID,reqLidList.get(0));
        }
        String retCode = GW_AUTH_ERR_CODE;

        httpHeaders.set(PKG_TRACE_LID,"");
        httpHeaders.set(PKG_COMMON_RETURNCODE,retCode);
        httpHeaders.set(PKG_COMMON_VERSION, PKG_JSON_VERSION);

        PkgErr pkgErr = new PkgErr();
        pkgErr.setCode(retCode);
        pkgErr.setType(TECH_ERROR_TYPE);
        pkgErr.setMessage("请求处理需联系应用管理员，请稍后再试！");
        pkgErr.setDescription("当前请求鉴权失败，被网关技术性拦截");

        PkgResHead head = new PkgResHead();
        head.setReturnCode(retCode);

        PkgErrRes errRes =new PkgErrRes();
        errRes.setHead(head);
        errRes.setError(pkgErr);
        String responseBody = JsonUtils.objToString(errRes);
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = res.bufferFactory().wrap(bytes);
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        return res.writeWith(Mono.just(buffer));
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("token");
    }

    public static class Config {
        String token;

        public Config(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
