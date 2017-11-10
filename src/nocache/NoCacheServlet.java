package nocache;

import javax.naming.ldap.Control;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通过响应发送三个http头,禁用页面的缓存
 * @author sqm
 * @Date 2017-11-10
 */
@WebServlet(name = "NoCacheServlet", urlPatterns = "/nocacheservlet")
public class NoCacheServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //在响应中设置3个http头共同实现禁用缓存的功能
        //Cache-Control:no-cache
        //Pragma:no-cache
        //Expires:-1-----代表当前时间,时间格式为Date,所以方法用setDateHeader 
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires",-1);


        //在页面中显示当前时间
        //时间格式为SimpleDateFormat给定的格式
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = format.format(date);
        response.getWriter().write(time);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doGet方法和doPost方法相同时使用
        this.doGet(request, response);


    }


}
