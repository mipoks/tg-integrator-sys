package design.kfu.tgintegrator.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

import static design.kfu.tgintegrator.config.TelegramBotConfig.TELEGRAM_AUTH_HEADER;
import static design.kfu.tgintegrator.controller.BotMessageRecieveController.CALLBACK_ENDPOINT;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
public class HeaderSecurityFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String rURI = ((HttpServletRequest) servletRequest).getRequestURI();
            if (CALLBACK_ENDPOINT.equals(rURI)) {
                String header = ((HttpServletRequest) servletRequest).getHeader("X-Telegram-Bot-Api-Secret-Token");
                if (TELEGRAM_AUTH_HEADER.equals(header)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}

