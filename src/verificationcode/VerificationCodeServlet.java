package verificationcode;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * 生成验证码
 * </p>
 *
 * @author sqm
 * @version 1.0
 * 2017-11-12
 */
@WebServlet(name = "VerificationCodeServlet", urlPatterns = "/verificationcodeservlet")
public class VerificationCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //在内存中生成验证码
        //创建自建的验证码生成器对象
        VerificationCode code = VerificationCode.getCodeInstance();
        //获取验证码文字
        String codeText = code.generateCodeText(4);
        //获取验证码图片
        BufferedImage image = code.outputImage(120, 40, codeText);

        //把内存中生成的验证码输出到客户端
        //通过ImageIO中的方法把内存中的图片输出到指定输出路径(写成文件\写入输出流)当中---write
        //static boolean
        //write(RenderedImage im, String formatName, OutputStream output)
        //RenderedImage--要写出的图片对象(RenderedImage是一个接口,实现类是BufferedImage)
        //formatName--要写出的图片文件格式(jpg)
        //OutputStream或File--输出的地方

        OutputStream output = response.getOutputStream();
        
        ImageIO.write(image, "jpg", output);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doGet方法和doPost方法相同时使用
        this.doGet(request, response);


    }


}
