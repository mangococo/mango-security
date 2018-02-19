package stu.mango.security.core.validate.code.image;

import stu.mango.security.core.validate.code.ValidateCode;

import java.awt.image.BufferedImage;

/**
 * 图片验证码
 */
public class ImageCode extends ValidateCode {
    private static final long serialVersionUID = -459472802770528046L;

    /**
     * 验证码容器
     */
    private BufferedImage image;

    /**
     * @param expireIn 过期时间
     */
    ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

