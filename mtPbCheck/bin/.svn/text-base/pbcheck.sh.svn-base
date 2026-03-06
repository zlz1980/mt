# 函数定义，用于执行导出命令
execute_export() {
    local jar="$1"
    local params="${@:2}"
    echo "执行命令：java -jar $jar $params"
    java -jar "$jar" $params
}
echo "操作命令如下："
echo "全库导出：pbchkout -A 目录"
echo "全库导出应用相关信息：pbchkout -APP -A 目录"
echo "全库导出网关相关信息：pbchkout -GW -A 目录"
echo "按工程ID导出：pbchkout -E 工程ID1|工程ID2|... 目录"
echo "按工程ID导出应用相关信息：pbchkout -APP -E 工程ID1|工程ID2|... 目录"
echo "按工程ID导出网关相关信息：pbchkout -GW -E 工程ID1|工程ID2|... 目录"
echo "单项导出批量任务：pbchkout -S -BATCH TASKNAME1|TASKNAME2|... 目录"
echo "单项导出系统参数：pbchkout -S -SYSCFG DNFNAME1|DNFNAME2|... 目录"
echo "单项导出工具类：pbchkout -S -UTILBEAN BEANNAME1|BEANNAME2|... 目录"
echo "单项导出原子交易：pbchkout -S -ATOM ATOMTRANCODE1|ATOMTRANCODE2|... 目录"
echo "单项导出应用规则组：pbchkout -S -RULE BIZTYPE1|BIZTYPE2|... 目录"
echo "单项导出SQL配置：pbchkout -S -SQL SQLNAME1|SQLNAME2|... 目录"
echo "单项导出静态缓存：pbchkout -S -STATICTBL CACHEID1|CACHEID2|... 目录"
echo "单项导出错误码：pbchkout -S -ERRORCODE ERRORCODE1|ERRORCODE2|... 目录"
echo "单项导出错误码类型：pbchkout -S -ERRORTYPE TYPEID1|TYPEID1|... 目录"
echo "单项导出HTTP通讯：pbchkout -S -HTTP SERNAME1|SERNAME2|... 目录"
echo "单项导出内部交易：pbchkout -S -TRANCODE BUSITYPE1|BUSITYPE2|... TRANCODE1|TRANCODE2... 目录"
echo "单项导出外部交易：pbchkout -S -FTRANCODE CHNLNO1|CHNLNO2|... FTRANCODE1|FTRANCODE2... 目录"
echo "单项导出工程信息：pbchkout -S -P PROID1|PROID2|... 目录"
echo "单项导出处理模块目录：pbchkout -S -MODULE MODULETYPE1|MODULETYPE2|... 目录"
echo "单项导出网关规则组：pbchkout -S -GWRULE BIZTYPE1|BIZTYPE2|... 目录"
echo "单项导出网关静态缓存：pbchkout -S -GWSTATICTBL CACHEID1|CACHEID2|... 目录"
echo "单项导出网关系统参数：pbchkout -S -GWSYSCFG DNFNAME1|DNFNAME2|... 目录"
echo "单项导出网关灰度路由控制表：pbchkout -S -GWTESTROUTE CHNLNO1|CHNLNO2|... FTRANCODE1|FTRANCODE2... 目录"
echo "单项导出网关路由控制表：pbchkout -S -GWTRANROUTE CHNLNO1|CHNLNO2|... FTRANCODE1|FTRANCODE2... 目录"
echo "单项导出网关异常默认报文返回：pbchkout -S -GWEXCEPTION CHNLNO1|CHNLNO2|... CODE1|CODE2... 目录"
echo "导入：pbchkin 目录"
echo "备份表：pbchkin -BAK"
read -p "请输入操作命令：" input
# 解析输入并执行相应命令
if [[ "$input" == pbchkout* ]]; then
    parts=(${input// / })
    jar="PbCheckOut.jar"
    cmd="${parts[0]}"
    params=""
    case "${parts[1]}" in
        "-A")
            params="-A ${parts[2]}"
            ;;
        "-APP"|"-GW")
            option="${parts[1]}"
            case "${parts[2]}" in
                "-A")
                    params="${option}_A ${parts[3]}"
                    echo params
                    ;;
                "-E")
                    params="${option}_E ${parts[3]} ${parts[4]}"
                    ;;
                *)
                    echo "未知操作：$cmd $1"
                    exit 1
                    ;;
            esac
            ;;
        "-E")
            params="-E ${parts[2]} ${parts[3]}"
            ;;
        "-S")
            parts_length=${#parts[@]}
            for(( i=1; i<$parts_length; i++ )); do
                params="${params} ${parts[$i]}"
            done
            ;;
        *)
            echo "未知操作：$cmd"
            exit 1
            ;;
    esac
    execute_export "$jar" $params
elif [[ "$input" == pbchkin* ]]; then
    jar="PbCheckIn.jar"
    dir="${input#pbchkin }"
    if [[ -n "$dir" ]]; then
        execute_export "$jar" "$dir"
    else
        echo "导入命令需要指定目录"
        exit 1
    fi
else
    echo "命令格式有误"
    exit 1
fi