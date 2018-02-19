package stu.mango.web.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stu.mango.bean.FileInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {
    public static final String path = "D:\\董进\\CODE\\Java\\IdeaProjects\\mango-security\\mango-security-demo\\src\\main\\java\\stu\\mango\\web\\controller";
    /**
     * 文件上传示例
     *
     * @param file 表单提交的multipart/form-data, 参数名称要与提交的表单参数一致
     * @return 包含路径信息的对象
     */
    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());


        File localFile = new File(path, new Date().getTime()+".txt");
        file.transferTo(localFile); // 写到本地
        // file.getInputStream(); 可将其写到其他云服务平台上去

        return new FileInfo(localFile.getAbsolutePath());
    }

    /**
     * 文件下载实例
     *
     * @param id
     */
    @GetMapping("/{id}")
    public void download(HttpServletResponse response, @PathVariable String id) {
        try ( // try(...)中打开的流，jdk会自动关闭
                InputStream is = new FileInputStream(new File(path, id+".txt"));
                OutputStream os = response.getOutputStream();
                ) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt"); // 虽然本地文件的实际文件名为时间戳，但可以通过指定的文件名下载出去

            IOUtils.copy(is, os); // 将文件的输入流copy到输出流中，即，将文件的内容写入相应。
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
