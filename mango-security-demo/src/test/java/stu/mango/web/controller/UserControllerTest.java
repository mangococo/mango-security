package stu.mango.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void step() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 测试用例（查询，GET）
     * @throws Exception any exception
     */
    @Test
    public void whenQuerySuccess() throws Exception {
        String jsonStr = mockMvc.perform(get("/user") // 模拟发送HTTP请求
                .param("username", "mango")

                .param("age", "18") //查询条件
                .param("ageTo", "45")

                .param("page", "3") // 查询页信息
                .param("size", "15")
                .param("sort", "age,desc")

                .contentType(MediaType.APPLICATION_JSON_UTF8)) // Http content-type
                .andExpect(status().isOk()) // 期望数据(状态码：200)
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn().getResponse().getContentAsString(); //返回数据为长度为3的集合

        System.out.println("++++++++++Query++++++++++:" + jsonStr);
    }

    @Test
    public void whenGetInfoSuccess() throws Exception {
        String jsonStr = mockMvc.perform(get("/user/001").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("GetInfoSuccess:" + jsonStr);
    }

    @Test
    public void whenGetInfoFail() throws Exception {
        mockMvc.perform(get("/user/a").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError()); // Http状态码为4xx,错误
        System.out.println("GetInfoFail");
    }


    @Test
    public void whenCreateSuccess() throws Exception {
        Date date = new Date();
        System.out.println(date.getTime()); //时间戳
        String content = "{\"username\":\"tom\",\"password\":null,\"birthday\":" + date.getTime() + "}";
        String jsonReturn = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("001"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("New Info:\n\t" + jsonReturn);
    }

    @Test
    public void whenUpdateSuccess() throws Exception {
        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        System.out.println(date.getTime()); //时间戳
        String content = "{\"username\":\"tom\",\"password\":\"password\",\"birthday\":" + date.getTime() + "}";
        String jsonReturn = mockMvc.perform(put("/user/001").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("001"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("New Info:\n\t" + jsonReturn);
    }

    @Test
    public void whenDeleteSuccess() throws Exception {
        mockMvc.perform(delete("/user/001").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUploadSuccess() throws Exception {
        String abPath = mockMvc.perform(fileUpload("/file")
                .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "Hello file upload".getBytes("UTF-8"))))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        System.out.println(abPath);
    }


}
