package stu.mango.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class ValidateCodeController {
    private final Map<String, ValidateCodeProcessor> processorMap;

    @Autowired
    public ValidateCodeController(Map<String, ValidateCodeProcessor> processorMap) {
        this.processorMap = processorMap;
    }

    /**
     * 处理验证码的RestAPI：创建验证码，存入session用于之后表单提交时验证，将生成的图片/发送给前端
     * @param request 将生成的图形验证码保存到该请求的session中
     * @param response 使用响应的输出流向前端传送生成的图形验证码
     */
    @GetMapping("/code/{type}")
    public void createImageCode(@PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        processorMap.get(type + "CodeProcessor").create(new ServletWebRequest(request, response));
    }
}
