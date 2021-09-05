package org.prgrms.kdt.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet(value = "/*",loadOnStartup = 1)//was가 배포될때(요청받기전에) 미리 로딩시키겠다는 옵션 1 , 기본은 -1
public class TestServlet extends HttpServlet {
    public static final Logger logger = LoggerFactory.getLogger(TestServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Init Servlet");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        logger.info("Got Request from {}",requestURI);


        //servlet class 의 do method는 컨테이너가 서블릿을 만들고 알아서 서비스를 호출하고 do method가 호출됨.
        //servlet(웹 컨테이너)에다가 testServlet이 존재하는지 알려줘야함.

        PrintWriter writer = resp.getWriter();
        writer.println("Hello Servlet!");
    }
}
