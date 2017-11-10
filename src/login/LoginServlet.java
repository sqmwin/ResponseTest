package login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆失败重定向
 * 若login成功,则访问此sevlet,若login失败,则重定向回登陆页面
 * @author sqm
 * @Date 2017-11-10
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/loginservlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //通过request获得提交表单中的用户名和密码
        //获取表单的内容
        //getParameter(String)--通过表单中的name属性获取表单中的参数
        String name = request.getParameter("username");
        String password = request.getParameter("password");

        //如果name和password都相同则登陆成功，如果name和password不同则重定向页面
        if ("admin".equals(name) && "admin".equals(password)) {
            //登陆成功，响应
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("登陆成功");

        }else {
            //用户名或密码错误，则重定向，返回登陆页面
            response.setStatus(302);
            response.setHeader("Location", "/servlet/html/login.html");
            response.sendRedirect("/servlet/html/login.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doGet方法和doPost方法相同时使用
        this.doGet(request, response);
    }


}
