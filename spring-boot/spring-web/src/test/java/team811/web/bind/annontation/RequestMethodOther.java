package team811.web.bind.annontation;

/**
 * @create: 2017-07-03
 * @description: 尝试其它枚举写法
 */
public enum RequestMethodOther {
    GET("GET","GET请求"),
    HEAD("HEAD","HEAD请求"),
    POST("POST","POST请求"),
    PUT("PUT","PUT请求"),
    PATCH("PATCH","PATCH请求"),
    DELETE("DELETE","DELETE请求"),
    OPTIONS("OPTIONS","OPTIONS请求"),
    TRACE("TRACE","TRACE请求");

    private final String key;
    private final String value;

    RequestMethodOther(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}

