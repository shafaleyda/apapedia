package com.apapedia.catalogue.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

@Log4j2
@Component
public class ResponseExceptionResolver extends AbstractHandlerExceptionResolver {

    private static final String DEFAULT_ERROR_CODE = "80000";


    public ResponseExceptionResolver() {}

    @SneakyThrows
    @Override
    protected ModelAndView doResolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        return handleException(request, response, ex);
    }

    private ModelAndView handleException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception
    ) {
        if (ObjectUtils.isNotEmpty(response) && HttpStatus.UNAUTHORIZED.value() == response.getStatus()
            && ObjectUtils.isNotEmpty(exception) && exception instanceof BusinessException
        ) {
            BusinessException businessException = (BusinessException) exception;
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(businessException.getErrorCode());
            errorResponse.setTitle(businessException.getErrorDesc());
            errorResponse.setMessage(businessException.getErrorMessage());
            log.info("Error Response Exception : {}", errorResponse);
            return new ModelAndView(new MappingJackson2JsonView(), this.getErrorResponse(errorResponse));
        }
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(DEFAULT_ERROR_CODE);
        errorResponse.setTitle("Kesalahan terjadi");
        errorResponse.setMessage("Kesalahan terjadi");
        return new ModelAndView(new MappingJackson2JsonView(), this.getErrorResponse(errorResponse));
    }

    private Map<String, String> getErrorResponse(ErrorResponse errorResponse) {
        return new ObjectMapper().convertValue(errorResponse, new TypeReference<Map<String, String>>() {
        });
    }
}
