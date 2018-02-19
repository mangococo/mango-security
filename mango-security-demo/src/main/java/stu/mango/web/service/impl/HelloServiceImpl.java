package stu.mango.web.service.impl;

import org.springframework.stereotype.Service;
import stu.mango.web.service.HelloService;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String greeting(String name) {
        System.out.println("Greeting!!!");

        return "Hello" + name;
    }
}
