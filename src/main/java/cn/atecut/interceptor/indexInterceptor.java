package cn.atecut.interceptor;

import cn.atecut.controller.LibraryController;
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

    static AtomicLong tiems = new AtomicLong(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("handle " + IPUtil.getIpAdrress(request) + "request   " + tiems.incrementAndGet());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
