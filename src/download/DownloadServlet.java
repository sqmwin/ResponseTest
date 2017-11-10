package download;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 下载文件的Servlet程序
 * @author sqm
 * @Date 2017-11-10
 */
//绝对地址为:/servlet/downloadservlet
@WebServlet(name = "DownloadServlet", urlPatterns = "/downloadservlet")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //要想获得文件,先获得文件的路径,之后通过ServletContext对象
        //用getRealPath(String path)获得文件的绝对路径后再获得输入流
        String path = "/img/阿拉蕾.jpg";
        //InputStream inputStream =getServletContext().getResourceAsStream(path);
        //获得绝对路径后,获得文件,通过绝对路径获得文件的字节输入流
        String realPath = getServletContext().getRealPath(path);
        InputStream inputStream = new FileInputStream(realPath);

        //通过response设置输出流输出到浏览器端(客户端)
        OutputStream outputStream = response.getOutputStream();
        
        //通过缓冲流让输入输出更快
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        //输入输出
        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = bufferedInputStream.read(bys)) != -1) {
            bufferedOutputStream.write(bys, 0, len);
        }

        //设置头信息,让客户端弹出下载窗口
        //首先获取文件名,再用utf-8编码
        //通过substring方法截取文件名,长度是最后一个\到末尾,但\的索引包含\自己,所以要索引+1,\在双引号中要有\\表示
        String name = realPath.substring(realPath.lastIndexOf("\\") + 1);
        //把文件名用utf-8编码
        String encodeName = URLEncoder.encode(name, "utf-8");

        //设置头信息,让客户端弹出下载窗口
        //Content-Disposition = "attachment;filename=文件名"
        response.setHeader("Content-Disposition","attachment;filename="+encodeName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doGet方法和doPost方法相同时使用
        this.doGet(request, response);


    }


}
