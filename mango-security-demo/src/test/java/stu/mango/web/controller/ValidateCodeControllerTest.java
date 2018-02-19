package stu.mango.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import stu.mango.security.core.validate.code.ValidateCode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidateCodeControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void whenValidateCodeCreateSendSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/code/sms?mobile=18823888811"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public ValidateCode foo(Object object){
        return (ValidateCode) object;
    }

    @Test
    public void testCastNull() {
        ValidateCode str = foo(null);
        System.out.println(str);
    }
}
