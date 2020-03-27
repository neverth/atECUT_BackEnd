package cn.atecut.interceptor;

import cn.atecut.unti.IPUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author liyang
 */

public class indexInterceptor implements HandlerInterceptor {

    private static Logger logger = LogManager.getLogger(indexInterceptor.class);

    static AtomicLong times = new AtomicLong(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("添加跨域");
        response.setHeader("Access-Control-Allow-Methods", "HEAD,GET,POST,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length,Authorization,If-Match,If-None-Match,X-Experience-API-Version,X-Experience-API-Consistent-Through");
        response.setHeader("Access-Control-Expose-Headers", "ETag,Last-Modified,Cache-Control,Content-Type,Content-Length,WWW-Authenticate,X-Experience-API-Version,X-Experience-API-Consistent-Through");
        response.setHeader("Access-Control-Allow-Origin", "*");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("handle " + IPUtil.getIpAdrress(request) + "request   " + times.incrementAndGet());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
