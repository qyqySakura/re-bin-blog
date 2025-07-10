package llf.llf.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {
    private int code;       // 状态码
    private String message; // 描述信息
    private T data;         // 返回的数据

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 错误返回（仅状态码和消息）
     */
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 错误返回（包含数据）
     */
    public static <T> Result<T> error(int code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 快捷方法：返回参数错误（400）
     */
    public static <T> Result<T> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 快捷方法：返回未授权（401）
     */
    public static <T> Result<T> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED.value(), message);
    }

    /**
     * 快捷方法：返回系统错误（500）
     */
    public static <T> Result<T> internalError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    // ————————————————————————
    // 示例：如何在实际业务中使用
    // ————————————————————————
    /*
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "用户名或密码错误");

        Result<Map<String, Object>> result = Result.error(400, "用户名或密码错误", map);
        System.out.println(result);
    }
    */
}
