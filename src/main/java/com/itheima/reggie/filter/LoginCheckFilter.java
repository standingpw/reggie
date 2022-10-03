package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();//路径匹配器
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
            /*
        *   A. 获取本次请求的URI
            B. 判断本次请求, 是否需要登录, 才可以访问
            C. 如果不需要，则直接放行
            D. 判断登录状态，如果已登录，则直接放行
            E. 如果未登录, 则返回未登录结果
        * */
        String RequestURI = request.getRequestURI();
        log.info("拦截到请求：{}",RequestURI);
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg"

        };//不需要处理，直接放行
        boolean check = check(urls, RequestURI);
        if(check){

            log.info("本次请求不需要处理，{}",RequestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //需要处理，
        HttpSession session = request.getSession();
        Object employee = session.getAttribute("employee");
        //4-1判断用户是否登录
        if(employee!=null){
            //已经登录了
            log.info("用户已经登录，用户ID:{}",employee);

            Long empId = (long)session.getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //4-2判断移动端用户是否登录
        if(request.getSession().getAttribute("user")!=null){
            //已经登录了
            Long userId = (long)session.getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //未登录，通过输出流的方式向客户端页面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }
    //进行路径匹配，检查需要放行
    public boolean check(String[] urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
