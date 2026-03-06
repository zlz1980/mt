package org.jts.pkg;

public class PkgErr {

    public static final String BIZ_ERROR_TYPE="B";
    public static final String TECH_ERROR_TYPE="T";

    // 错误码,原则上同HTTP报文头中ceb-common-returncode及，JSON报文体head中returnCode一致
    private String code;
    // 错误类型,同返回码一级分类，用于区别业务（B）或技术（T）错误
    private String type;
    // 错误简述,错误信息简要描述，多用于客户端显示，以屏蔽后台敏感信息且客户友好型描述为主
    private String message;
    // 必要的错误详述,错误信息详细描述，多用于服务端错误逻辑判断或详细日志落地，协助应用处理或故障排查
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
