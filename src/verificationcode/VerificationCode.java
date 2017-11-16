package verificationcode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 生成web验证码有关的方法的类
 * @author sqm
 * @version 1.2
 */
public class VerificationCode {
    //懒汉式单例类,不调用不实例化自己
    /**
     *<p>
     *默认验证码的取值范围(字符集),包括部分数字和部分大写字母；验证码文字要排除:0,o,O,1,i,I,l
     *</p>
     */
    private final String VERIFICATION_CODE_TEXTS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";

    /**
     *<p>
     *提供一个Random对象以便之后生成随机数等功能的调用
     *</p>
     */
    private static Random random = new Random();

    /**
     *<p>
     *验证码图片的宽度
     *</p>
     */
    private int w;

    /**
     *<p>
     *验证码图片的高度
     *</p>
     */
    private int h;

    /**
     *<p>
     *私有化无参构造
     *</p>
     */
    private VerificationCode() { }

    /**
     *<p>
     *创建一个私有的静态内部类,静态是为了之后的方法直接调用,内部类实现延迟加载,不调用不执行,保证线程安全
     *</p>
     */
    private static class LazyHolder {
        private static final VerificationCode CODE = new VerificationCode();
    }

    /**
     *<p>
     *提供一个对外调取此类的实例对象的方法
     *</p>
     *@return   返回一个验证码类的实例
     */
    public static VerificationCode getCodeInstance() {
        return LazyHolder.CODE;
    }

    /**
     *<p>
     *在内存中创建一个验证码图像
     *</p>
     *@param    w   验证码图像的宽度
     *@param    h   验证码图像的高度
     *@param    codeText   验证码文字
     *@return   返回一个指定的验证码图像
     */
    public BufferedImage outputImage(int w,int h,String codeText) {
        //设定验证码图像的尺寸
        this.w = w;
        this.h = h;
        //生成图像
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        //设定此图像的绘画器
        Graphics2D g = image.createGraphics();
        //设置绘画器算法:抗锯齿开启
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //生成背景边框
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, w, h);

        //生成背景颜色
        Color backColor = getRandColor(210, 255);
        g.setColor(backColor);
        g.fillRect(2, 2, w - 4, h - 4);

        //随机生成干扰线
        drawRandLine(g, (int)(h * 0.35), (int)(h * 0.65));

        //扭曲图片
        shear(g, backColor);

        //把文字加在图片上
        generateCode(g, codeText);

        //添加图片噪点
        drawNoises(image);

        return image;
    }

    /**
     *<p>
     *得到随机的rgb区间的Color对象
     *</p>
     *@param    min Color对象rgb值的区间的最小数值
     *@param    max Color对象rgb值的区间的最大数值
     *@return   返回一个随机的rgb区间的Color对象
     */
    private Color getRandColor(int min, int max) {
        //把取值范围限定在0-255
        int range =255;
        if (min < 0) {
            min = 0;
        }
        if (min > range) {
            min = range;
        }
        if (max < 0) {
            max = 0;
        }
        if (max > range) {
            max = range;
        }

        int r = min + random.nextInt(max - min);
        int g = min + random.nextInt(max - min);
        int b = min + random.nextInt(max - min);

        return new Color(r, g, b);
    } 

    /**
     * <p>
     * 得到一个随机的rgb整数值,例如:0xFFFFFFFF
     * </p>
     * @return 返回一个随机的rgb整数值
     */
    private int getRandIntRGB() {
        //int rgb的rgb值为Color.getRGB()
        // 24-31 位表示 alpha(可忽略)，16-23 位表示红色，8-15 位表示绿色，0-7 位表示蓝色
        //先获得三个rgb数,再把这3个数字进行操作,第一个向左位移八位,拼接第二个,新数字再向左位移八位,拼接第三个可得到rgb的整数表示;
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        //rgb = (((r<<8)|g)<<8)|b
        int rgb = r << 8;
        rgb = rgb | g;
        rgb = rgb << 8;
        rgb = rgb | b;

        return rgb;
    }

    /**
     * <p>
     * 在指定矩形验证码中绘制随机数目的线段
     * </p>
     * @param   g   指定的图像控制对象
     * @param   min   随机线段数目的最小值
     * @param   max   随机线段数目的最大值
     */
    private void drawRandLine(Graphics g,int min, int max) {
        /* 绘制几条直线作为干扰 */
        //设定线段随机坐标点
        //循环多次话多条线
        int lineNum = min + random.nextInt(max-min);
        for (int i = 0; i < lineNum; i++) {
            int x1 = random.nextInt(this.w - 4);
            int y1 = random.nextInt(this.h - 4);
            int x2 = random.nextInt(this.w - 4);
            int y2 = random.nextInt(this.h - 4);
            //使用Graphics中的drawLine(int x1, int y1, int x2, int y2)
            //画出多条干扰线，颜色随机，比背景颜色深（推荐150-210）
            Color color = getRandColor(150, 210);
            g.setColor(color);
            //循环多次话多条线
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     *<p>
     *在指定验证码图像上添加噪点(改变随机坐标像素的rgb值
     *</p>
     *@param    image   指定的验证码图像
     */
    private void drawNoises(BufferedImage image) {
        //使用BUfferedImage中的setRGB()方法来改变图片中指定坐标的rgb值
        //setRGB(int x, int y, int rgb)
        //设定一个噪点个数的公式
        //噪点个数noises = 图片面积 * 噪点率
        float noisesRate = 0.05f;
        int noises = (int) (noisesRate * this.w * this.h);

        for (int i = 0; i < noises; i++) {
            int x = 2 + random.nextInt(this.w - 4);
            int y = 2 + random.nextInt(this.h - 4);
            int rgb = getRandIntRGB();
            //setRGB(int x, int y, int rgb)
            image.setRGB(x, y, rgb);
        }
    }
    /**
     *<p>
     *在指定的矩形图形中,x方向和y方向分别按照正弦曲线扭曲
     *</p>
     *@param    g   指定的图像控制对象
     *@param    color   指定的扭曲后的图形填充空白的颜色
     */
    private void shear(Graphics g,Color color){
        shearX(g, color);
        shearY(g, color);
    }

    /**
     *<p>
     *指定的矩形图形中使图形沿X坐标按照正弦曲线扭曲
     *</p>
     *@param    g   指定的图像控制对象
     *@param    color   指定的扭曲后的图形填充空白的颜色
     */
    private void shearX(Graphics g, Color color) {
        //指定一个变化周期(单次扭曲Y坐标的长度)
        int period = random.nextInt(3);

        //边框间隙
        boolean borderGap = true;
        //框架
        int frames = 1;
        //代表一个角度,用于设置开始扭曲的sin坐标
        int phase = random.nextInt(3);

        for (int i = 0; i < this.h; i++) {
            double periodDouble = (double) (period >> 1);
            double temp1 = (double) i / (double) period;
            double temp2 = 2 * Math.PI * (double) phase;
            double temp3 = temp2/(double) frames;
            double temp4 = temp1 + temp3;
            double d = periodDouble * Math.sin(temp4);
            //图片水平方向像素上的偏移量
            int dRate = (int)d;
            //复制垂直坐标为i时的水平方向像素的位置到偏移处
            g.copyArea(0, i, this.w, 1, dRate, 0);

            //填充图片扭曲后水平方向的空白
            if (borderGap) {
                g.setColor(color);
                g.drawLine(dRate, i, 0, i);
                g.drawLine(dRate + this.w, i, this.w, i);
            }
        }
    }

    /**
     *<p>
     *指定的矩形图形中使图形沿Y坐标按照正弦曲线扭曲
     *</p>
     *@param    g   指定的图像控制对象
     *@param    color   指定的扭曲后的图形填充空白的颜色
     */
    private void shearY(Graphics g, Color color) {
        //指定一个变化周期(单次扭曲X坐标的长度)
        int period = random.nextInt(this.w / 20) + this.w / 20;

        boolean borderGap = true;
        int frames = 20;
        //代表一个角度,用于设置开始扭曲的sin坐标
        int phase = random.nextInt(this.w / 20);

        for (int i = 0; i < this.w; i++) {

            double periodDouble = (double) (period >> 1);
            double temp1 = (double) i / (double) period;
            //小数+D结尾代表double数值
            //temp2 = 2πr; r=7;
            double temp2 = 2 * Math.PI * (double) phase;
            //temp3 = 2πr/frames;
            double temp3 = temp2/(double) frames;

            //当前x坐标的弧度
            double temp4 = temp1 + temp3;
            //当前x坐标对应的y坐标,也就是之后图片竖直方向像素要偏移的值
            double d = periodDouble * Math.sin(temp4);
            //图片竖直方向像素上的偏移量
            int dRate = (int)d;
            //复制水平坐标为i时的竖直方向像素的位置到偏移处
            g.copyArea(i, 0, 1, this.h, 0, dRate);

            //填充图片扭曲后垂直方向的空白
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, dRate, i, 0);
                g.drawLine(i, dRate + this.h, i, this.h);
            }
        }
    }

    /**
     *<p>
     * 使用默认字符集生成验证码的字符
     *</p>
     * @param codeLength    验证码长度
     * @return  返回指定长度默认字符集的字符串
     */
    public String generateCodeText(int codeLength){
        return generateCodeText(codeLength, VERIFICATION_CODE_TEXTS);
    }

    /**
     *<p>
     *生成验证码的字符
     *</p>
     *@param    codeLength  生成的验证码字符的个数
     *@param    codeSources  生成的验证码字符的z字符集
     *@return   返回指定长度指定字符集的字符串
     *
     */
    public String generateCodeText(int codeLength,String codeSources) {
        //判断字符来源是否给出,如果没有给出直接使用默认字符集
        if (codeSources == null || codeSources.length() == 0) {
            codeSources = VERIFICATION_CODE_TEXTS;
        }

        //生成一个字符缓冲区,长度为验证码字符的长度
        StringBuilder codeTexts = new StringBuilder(codeLength);
        //生成一个固定种子的随机数生成器对象，用于随机选择生成的字符
        //保证了同一次调用此方法时的验证码文字相同
        Random indexRand = new Random(System.currentTimeMillis());

        //获得源字符集的长度
        int len = codeSources.length();
        //取得指定个数的拼接字符串
        for (int i = 0; i < codeLength; i++) {
            codeTexts.append(codeSources.charAt(indexRand.nextInt(len)));
        }

        return codeTexts.toString();
    }

    /**
     * <p>
     * 设置指定验证码文字,使其生成在指定的矩形验证码图像上
     * </p>
     * @param   g   指定的图像控制对象
     * @param   codeText    指定的验证码文字
     */
    private void generateCode(Graphics2D g, String codeText) {


        /*设定每个字的字体范围*/
        //在给定字体范围内随机获取一个验证码的字体
        String[] fontNames = new String[] {"Algerian","Arial","sans serif"};
        
        //获取字体的style样式，0---普通1---黑体，2---斜体
        int[] styles = new int[]{Font.PLAIN,Font.BOLD,Font.ITALIC};

        for (int i = 0; i < codeText.length() ; i++) {
            //设定验证码文字的大小在0.6h-0.72h之间
            int fontSize = (int)(0.62 * this.h) + random.nextInt((int) (0.12 * this.h));
            //设定每个文字的颜色的取值区间
            Color codeColor = getRandColor(90, 140);
            g.setColor(codeColor);

            //随机给出此字的字体
            int nameIndex = random.nextInt(fontNames.length);
            String fontName = fontNames[nameIndex];
            //随机给出此字的样式                                                                      
            int styleIndex = random.nextInt(styles.length);
            int style = styles[styleIndex];
            //给定此字的字体
            Font font = new Font(fontName,style,fontSize);
            g.setFont(font);
            //给定此字字符
            String singleText = codeText.substring(i, i + 1);

            //设定每个字的绘制点基线坐标（就当文字左上角坐标）
            int textX = (this.w / codeText.length() - fontSize) / 2 + 5 + this.w / codeText.length() * i;
            int textY = (int)((fontSize / 2 + this.h / 2) * 0.90);

            //设定每个字符旋转的角度(正负30度）
            int angle = random.nextInt(60) - 30;
            //将角度转换成弧度（角度转弧度 π/180×角度）
            double theta = Math.PI / 180 * angle;
            //void rotate(double theta, double x, double y）
            //将当前的 Graphics2D Transform 与平移后的旋转转换连接
            g.rotate(theta, textX, textY);
            //绘制单个文字
            g.drawString(singleText, textX, textY);
            ////再把旋转弧度归位，防止下次旋转弧度叠加上次
            g.rotate(-theta, textX, textY);
        }
    }



}

