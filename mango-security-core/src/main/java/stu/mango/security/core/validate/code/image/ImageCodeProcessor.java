package stu.mango.security.core.validate.code.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.security.core.validate.code.ValidateCodeGenerator;
import stu.mango.security.core.validate.code.impl.AbstractValidateCodeProcessor;

import javax.imageio.ImageIO;
import java.util.Map;

@Component("imageCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {
    
    @Autowired
    public ImageCodeProcessor(Map<String, ValidateCodeGenerator> generatorMap) {
        super(generatorMap);
    }

    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }
}