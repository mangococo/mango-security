package stu.mango.security.core.properties;

public class ImageCodeProperties extends SmsCodeProperties {
    /**
     * 图片验证码图片的宽度
     */
    private int width = 67;

    /**
     * 图片验证码图片的高度
     */
    private int height = 23;

    ImageCodeProperties() {
        setLength(4);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
