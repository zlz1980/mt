package com.n.fbsp.atom.platform.seed.other.utils;


/**
 * @author User
 */
public interface Constants {
    /*
     分布式体系报文规范-报文体----START------
    */
    /**
     * 原始请求系统编号
     */
    String CEB_BIZ_ORGSYS = "ceb-biz-orgsys";
    /**
     * pin加密方式
     */
    String EDSP_PIN_KEY = "EDSP-PIN-Key";
    /**
     * 原始请求系统编号
     */
    String CEB_BIZ_CURSYS = "ceb-biz-cursys";
    /**
     * 原始请求系统编号
     */
    String EDSP_MAC_FLAG = "edsp-mac-flag";
    /**
     * head-报文体-head部分
     */
    String HEAD = "head";
    /**
     * error-报文体-error部分
     */
    String ERROR = "error";
    /**
     * data-报文体-data部分
     */
    String DATA = "data";
    /**
     * domain-报文体-domain部分
     */
    String DOMAIN = "domain";
    /*
     分布式体系报文规范-报文体----END------
    */
    /*
     分布式体系报文规范-报文体head部分----START------
    */

    /**
     * returnCode-错误或成功响应编码，采用14位标识，后7位兼容SOP报文现有响应码
     */
    String HEAD_RETURNCODE = "returnCode";
    /**
     * org-组织机构
     */
    String HEAD_ORG = "org";
    /**
     * id-员工号或虚拟柜员号
     */
    String HEAD_ID = "id";
    /**
     * requestNo-前台流水
     */
    String HEAD_REQUESTNO = "requestNo";
    /**
     * responseNo-后台流水
     */
    String HEAD_RESPONSENO = "responseNo";
    /**
     * channelNo-渠道号
     */
    String HEAD_CHANNELNO = "channelNo";
    /**
     * messageType-报文类型：正常报文（normal），错误报文（error)、复杂错误报文（errnor）【含错误和标准两个返回对象】等。
     */
    String HEAD_MESSAGETYPE = "messageType";
    /**
     * date-日期，采用yyyyMMdd格式
     */
    String HEAD_DATE = "date";
    /**
     * 时间-采用24小时HH:mm:ss格式
     */
    String HEAD_TIME = "time";
    /**
     * domain-领域标识，同HTTP报文头，多领域逗号分隔。
     */
    String HEAD_DOMAIN = "domain";
    /**
     * version-根据指引版本发布，如DEV-IT-ITF-011-2022.xx，企业分布式服务平台金融服务目录登记版本。
     * 非标准版本以系统在金融服务目录登记的非标准版本号为准。
     */
    String HEAD_VERSION = "version";
    /**
     * 报文类型，messageType-normal：正常报文（normal）。
     */
    String MESSAGETYPE_NORMAL = "normal";
    /**
     * 报文类型，messageType-error：错误报文（error)。
     */
    String MESSAGETYPE_ERROR = "error";
    /**
     * 报文类型，messageType-errnor：复杂错误报文（errnor）【含错误和标准两个返回对象】等。
     */
    String MESSAGETYPE_ERRNOR = "errnor";
    /**
     * 分布式体系规范报文版本
     */
    String VERSION_011_2024 = "DEV-IT-ITF-011-2024";
    /*
     分布式体系报文规范-报文体head部分----END------
    */
    /*
     分布式体系报文规范-报文体error部分----START------
    */
    /**
     * code-原则上同HTTP报文头中ceb-common-returncode及，JSON报文体head中returnCode一致
     */
    String ERROR_CODE = "code";
    /**
     * type-同返回码一级分类，用于区别业务（B）或技术（T）错误
     */
    String ERROR_TYPE = "type";
    /**
     * message-错误信息简要描述，多用于客户端显示，以屏蔽后台敏感信息且客户友好型描述为主。
     */
    String ERROR_MESSAGE = "message";
    /**
     * description-错误信息详细描述，多用于服务端错误逻辑判断或详细日志落地，协助应用处理或故障排查。
     */
    String ERROR_DESCRIPTION = "description";
    /**
     * 分布式体系报文规范，错误类型-技术（T）错误
     */
    String ERROR_TYPE_T = "T";
    /**
     * 分布式体系报文规范，错误类型-业务（B）错误
     */
    String ERROR_TYPE_B = "B";
    /*
     分布式体系报文规范-报文体error部分----END------
    */

    /*
     CFPS分布式体系报文规范-报文体head部分
    */
    String TRAN_DATE = "tranDate";
    String DEF_JOUR_BEAN = "defJournal";
    String DEF_CARD_BEAN = "defCardInfo";

    String CARD_BIN = "cardBin";
    String BIN_LEN = "binLen";
    String CARD_NAME = "cardName";
    String CARD_FLAG = "cardFlag";
    String CARD_ASSO = "cardAssociation";
    String ROUTE_FLAG = "routeFlag";
    String CARD_TYPE = "cardType";
    String ACCT_NO = "acctNo";
    String T_EB_CARDINF = "t_eb_cardinf";

    /**
     * 卡bin长度信息缓存key
     */
    String CARD_BIN_LENGTH_KEY = "card_bin_length";

    /*
     tran作用域流水操作流转状态
     */
    /**
     * operateType-流水操作类型
     */
    String OPERATE_TYPE = "operateType";

    /**
     * Updated-已更新
     */
    String OPERATE_TYPE_U = "U";
    /**
     * Pending Update-待更新
     */
    String OPERATE_TYPE_P = "P";
    /**
     * Inserted-已插入
     */
    String OPERATE_TYPE_I = "I";
    /**
     * select-查询
     */
    String OPERATE_TYPE_S = "S";


    // CUPS使用
    String RESP_CD = "respCd";
    String RESP_DESC = "respDesc";
    String SUB_RESP_CD = "subRespCd";
    String SUB_RESP_DESC = "subRespDesc";
    /**
     * fbsp-AMS系统唯一编号
     */
    String AMS_FBSP_CODE = "000651";
    /**
     * fbsp渠道号
     */
    String FBSP_CHNLNO = "830";
    String RESERVED = "000";
    String DEFAULT = "default";
    String SYS_NAME_FBSP = "FBSP";

    String MQ_THREAD_TYPE_LOG = "logThreadPool";
    String MQ_THREAD_TYPE_SMS = "smsThreadPool";
    String MQ_THREAD_TYPE_JOU = "jouThreadPool";

    /**
     * MQ线程池线名称key
     */
    String MQ_THREAD_POOL_NAME_KEY = "threadPoolName";

    /**
     * MQ线程池线核心数
     */
    String MQ_THREAD_CORE_POOL_SIZE = "corePoolSize";

    /**
     * MQ线程池允许的最大线程数
     */
    String MQ_THREAD_MAX_POOL_SIZE = "maxPoolSize";

    /**
     * MQ线程池任务队列容量
     */
    String MQ_THREAD_QUEUE_CAPACITY = "queueCapacity";

    /**
     * MQ线程池非核心线程空闲时间
     */
    String MQ_THREAD_KEEP_ALIVE_SECONDS = "keepAliveSeconds";
    /**
     * MQ topic 发送短信
     */
    String MQ_TOPIC_SMS = "FBSP_SMS";
    /**
     * MQ topic 登记日志
     */
    String MQ_TOPIC_LOG = "FBSP_LOG";
    /**
     * MQ topic 原交易登记簿
     */
    String MQ_TOPIC_ORIMSG = "FBSP_ORIMSG";
    /**
     * MQ topic 流水扩展表
     */
    String MQ_TOPIC_JOUEXT = "FBSP_JOUEXT";
    /**
     * MQ 消息平台开关
     */
    String MQ_OPENFLAG = "mqOpenFlag";
    /**
     * HTTPCONF fbsp自身服务名
     */
    String HTTP_CONF_FBSP = "core_FBSP";

    /**
     * FBSP金额前缀
     */
    String FBSP_AMOUNT_PRE = "y_";
    /**
     * 银联金额前缀
     */
    String UPS_AMOUNT_PRE = "ups_";
    /**
     * yuntong传统金额前缀
     */
    String YT_AMOUNT_PRE = "yt_";
    /**
     * YTwuka金额前缀
     */
    String YTWK_AMOUNT_PRE = "ytwk_";
    /**
     * DJKHX金额前缀
     */
    String DJK_AMOUNT_PRE = "c_";
    /**
     * JJKHX金额前缀
     */
    String JJK_AMOUNT_PRE = "d_";
    /**
     * PIN密文
     */
    String PIN_CIPHER = "pinValue";
    /**
     * PIN密钥密文
     */
    String PIN_KEY_CIPHER = "pinKey";
    /**
     * fbsp验签公钥名
     */
    String VERIFY_SIGN_PK_NAME = "VERIFY_SIGN_PK_NAME";
    /**
     * fbsp解密私钥名
     */
    String PIN_VK_NAME = "PIN_VK_NAME";
    /**
     * 银联PIN密钥名
     */
    String PIN_ZPK_NAME = "PIN_ZPK_NAME";
    /**
     * 请求方上送的验签公钥名
     */
    String SC_ESP_MAC_KEY = "sc-mac-key";
    /**
     * 加密平台SDK配置文件加载路径
     */
    String EC_CFG_PATH = "EC_CFG_PATH";
    /**
     * 金额字段
     */
    String AMOUNT_KEY = "amount";
    /**
     * essc_conf表配置的加密证书相关存储到statictbl的key名称
     */
    String EC_CONF_STATBL_KEY = "es_conf_confname";
    /**
     * essc_conf表配置的加密证书相关存储到statictbl的值key名称
     */
    String EC_CONF_CONF_VALUE_KEY = "confvalue";
    /**
     * 万事网联渠道渠道8583报文MAC使用的秘钥
     */
    String MCS_MAC_KEY = "618_mcs_mac_key_name";
    /**
     * 万事网联渠道渠道8583报文PIN使用的秘钥
     */
    String MCS_PIN_KEY = "618_mcs_pin_key_name";

    /**
     * fbspMAC公钥名
     */
    String FBSP_MAC_PK = "fbsp_mac_pk_key_name";
    /**
     * fbspMAC私钥名
     */
    String FBSP_MAC_VK = "fbsp_mac_vk_key_name";
    /**
     * fbspPIN公钥名
     */
    String FBSP_PIN_PK = "fbsp_pin_pk_key_name";
    /**
     * fbspPIN私钥名
     */
    String FBSP_PIN_VK = "fbsp_pin_vk_key_name";
}
