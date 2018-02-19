package stu.mango.security.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateCodeProcessorHolder {

    private final Map<String, ValidateCodeProcessor> processorMap;

    @Autowired
    public ValidateCodeProcessorHolder(Map<String, ValidateCodeProcessor> processorMap) {
        this.processorMap = processorMap;
    }

    ValidateCodeProcessor getValidateCodeProcessor(ValidateCodeType type) {
        return getValidateCodeProcessor(type.toString());
    }

    private ValidateCodeProcessor getValidateCodeProcessor(String type) {
        String name = type.toLowerCase() + StringUtils.substringAfter(ValidateCodeProcessor.class.getSimpleName(), "Validate");
        ValidateCodeProcessor processor = processorMap.get(name);
        if (processor == null) {
            throw new ValidateCodeException("校验码处理器[" + name + "]不存在");
        }

        return processor;
    }
}
