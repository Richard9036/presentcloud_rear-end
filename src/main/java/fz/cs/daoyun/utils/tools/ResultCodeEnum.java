package fz.cs.daoyun.utils.tools;

/**
 * @author cjbi
 */
public enum ResultCodeEnum {

    OK("200", "处理成功"),
    CREATED("201", "创建成功"),
    DELETED("204", "删除成功"),
    TimeOut("205", "超时"),
    DistanceOut("206", "超出范围"),
    AlreadySign("207", "已经签到"),
    BAD_REQUEST("400", "请求参数有误"),
    UNAUTHORIZED("401", "未授权"),
    FORBIDDEN("403", "被禁止访问"),
    PARAMS_MISS("483", "缺少接口中必填参数"),
    PARAM_ERROR("484", "参数非法"),
    FAILED_DEL_OWN("485", "不能删除自己"),
    FAILED_USER_ALREADY_EXIST("486", "该用户已存在"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),
    NOT_IMPLEMENTED("501", "业务异常");



    private String code;
    private String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
