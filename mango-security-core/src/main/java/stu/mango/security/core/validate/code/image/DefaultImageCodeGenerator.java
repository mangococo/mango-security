package stu.mango.security.core.validate.code.image;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.security.core.properties.ImageCodeProperties;
import stu.mango.security.core.properties.SecurityProperties;
import stu.mango.security.core.validate.code.ValidateCodeGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DefaultImageCodeGenerator implements ValidateCodeGenerator {
    private final SecurityProperties securityProperties;

    public DefaultImageCodeGenerator(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }


    @Override
    public ImageCode createValidateCode(ServletWebRequest request) {
        ImageCodeProperties codeProperties = securityProperties.getCode().getImage();

        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", codeProperties.getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", codeProperties.getHeight());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = image.getGraphics();
        Random random = new Random();

        graphics.setColor(getRandColor(200, 250));
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        graphics.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            graphics.drawLine(x, y, x + xl, y + yl);
        }

        // 生成四位随机数、并画到图片上
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < codeProperties.getLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            graphics.drawString(rand, 13 * i + 6, 16);
        }

        graphics.dispose();

        return new ImageCode(image, sRand.toString(), codeProperties.getExpireIn());
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc 前景色
     * @param bc 背景色
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }

        int r = fc + random.nextInt(bc-fc);
        int g = fc + random.nextInt(bc-fc);
        int b = fc + random.nextInt(bc-fc);

        return new Color(r, g, b);
    }
}
