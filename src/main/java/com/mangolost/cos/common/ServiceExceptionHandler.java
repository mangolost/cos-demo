package com.mangolost.cos.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@ControllerAdvice
public class ServiceExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    /**
     * 统一异常处理
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResult exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        int code = ApiStatusCode.SUCCESS;
        String message = "";
        try {
            if (ex instanceof MethodArgumentTypeMismatchException
                    || ex instanceof MissingServletRequestParameterException
                    || ex instanceof NumberFormatException
                    || ex instanceof BindException
            ) {
                code = 430;
                message = "参数错误：可能由于参数未提供、空值、空格符、类型、格式、范围不符要求 ";
            }

            // 405 Method Not Allowed
            else if (ex instanceof HttpRequestMethodNotSupportedException) {
                code = 405;
            }
            // 415 Content type not supported
            else if (ex instanceof HttpMediaTypeNotSupportedException) {
                code = 415;
            }
            // 406 Could not find acceptable representation
            else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                code = 406;
            }
            // 400 Required parameter is not present
            else if (ex instanceof HttpMessageConversionException) {
                code = 400;
                message = "参数错误";
            }
            // 400 Required parameter is not present
            else if (ex instanceof ServletRequestBindingException) {
                code = 400;
                message = "参数错误";
            } else if (ex instanceof ServiceException) {
                ServiceException se = (ServiceException) ex;
                if (se.getCode() != 0) {
                    code = se.getCode();
                }
                if (code == ApiStatusCode.SUCCESS) {
                    code = ApiStatusCode.FAIL_SYSTEM_ERROR;
                }
                if (se.getMessage() != null) {
                    message = se.getMessage();
                }
            } else {
                code = ApiStatusCode.FAIL_SYSTEM_ERROR;
                message = "server error call admin";
                LOGGER.error("服务端异常,api: ", ex);
            }
        } catch (Exception handlerException) {
            handlerException.printStackTrace();
            LOGGER.error("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }

        return buildCommonResult(code, message);
    }

    /**
     * 返回结果处理, JSON字符串化的CommonResult对象
     * @param code
     * @param message
     * @return
     */
    private CommonResult buildCommonResult(Integer code, String message) {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        commonResult.setData(null);
        return commonResult;
    }

}
