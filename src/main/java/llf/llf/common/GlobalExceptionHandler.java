package llf.llf.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理所有未捕获的异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // 打印异常堆栈，便于调试
        Result<String> result = new Result<>();
        result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setMessage("发生未知错误: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 处理自定义业务异常（如果有）
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<String>> handleBusinessException(BusinessException ex) {
        ex.printStackTrace(); // 打印异常堆栈，便于调试
        Result<String> result = new Result<>();
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMessage("业务异常: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
