package refresh;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 页面定时刷新refresh--"将在5/4/3/2/1秒后跳转"
 * @author sqm
 * @Date 2017-11-10
 */
public class RefreshServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //在刷新(refresh)跳转的页面(login.html)中设置头信息的标签
        //<meta http-equiv="refresh" content="5;url=/servlet/html/login.html" />
        response.setHeader("refresh","5;url=/servlet/html/login.html");
        response.getWriter().println("页面将在5秒后跳转到login.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doGet方法和doPost方法相同时使用
        this.doGet(request, response);
        //设置跳转的页面头信息中的<meta http-equiv="Content-Type" content="text/html ; charset=UTF-8">

    }


}
