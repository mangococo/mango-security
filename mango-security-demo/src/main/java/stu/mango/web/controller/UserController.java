package stu.mango.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.bean.User;
import stu.mango.bean.UserQueryCondition;
import stu.mango.exception.UserNotExistException;
import stu.mango.security.core.properties.SecurityProperties;
import stu.mango.security.rbac.dto.AdminInfo;
import stu.mango.security.rbac.service.AdminService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController // 声明此 Controller 提供RestAPI, 在RestController中，相当于给所有的xxxMapping端点都添加了@ResponseBody注解，不返回视图，只返回数据。
@RequestMapping("/user")
public class UserController {

    private final ProviderSignInUtils providerSignInUtils;

    private final SecurityProperties securityProperties;

    private final AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserController(ProviderSignInUtils providerSignInUtils,
                          SecurityProperties securityProperties,
                          AdminService adminService) {
        this.providerSignInUtils = providerSignInUtils;
        this.securityProperties = securityProperties;
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public User register(User user, HttpServletRequest request) throws Exception {
        Map<String, String[]> stringMap = request.getParameterMap();
        stringMap.keySet().forEach(key -> logger.info(key + " = " + Arrays.toString(new String[]{Arrays.toString(stringMap.get(key))})));
        
        String requestType = request.getParameter("type");
        logger.info("请求类型：" + requestType);

        // 无论是注册用户还是绑定用户动作，都会拿到一个用户唯一标识
        String username = user.getUsername();

        logger.info("用户" + username + "请求注册");

        if (StringUtils.equalsIgnoreCase("register", requestType)) {
            AdminInfo info = new AdminInfo();
            BeanUtils.copyProperties(user, info);
            info.setRoleId((long) 3);
            adminService.create(info);
        } else if (StringUtils.equalsIgnoreCase("binding", requestType)) {
            // 将userId作为用户唯一标识插到user_connection表中，将user_info与 social_info 关联起来
            providerSignInUtils.doPostSignUp(username, new ServletWebRequest(request));
        }

        logger.info("用户" + username + "注册成功");

        return user;
    }

    @GetMapping("/me")
    public Object whoAmI(Authentication user, HttpServletRequest request) throws UnsupportedEncodingException {

        String header = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(header, "bearer ");

        logger.info(token);

        /*
         * 解析JWT token
         */
        Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();

        String company = (String) claims.get("company");

        logger.info("----> company:" + company);

        return user;
    }

    /**
     * JsonView 使用：3、在Controller的方法上指定视图
     *
     * @param userCondition 用于查询的“条件”
     * @param pageable 前台的分页信息（查询页码、每页数目、sort规则）
     *
     * @return 符合条件的用户列表
     */
    @GetMapping // 等价于 @RequestMapping(method = RequestMethod.GET)
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务") // swagger-ui.html对该方法的描述
    public List<User> query(UserQueryCondition userCondition,//@RequestParam注解映射请求参数到java方法参数上去
                              @PageableDefault(page = 2, size = 12, sort = "age,asc") Pageable pageable) { //
        List<User> userList = new ArrayList<>();
        System.out.println(userCondition);

        System.out.println("Pageable：" + pageable.getPageNumber());
        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getSort());
        System.out.println(pageable.getOffset());

        userList.add(new User("001", "001"));
        userList.add(new User("002", "001"));
        userList.add(new User("003", "001"));

        return userList;
    }

    /**
     * JsonView 使用：3、在Controller的方法上指定视图
     *
     * 利用正则表达式(\d+)区分资源信息
     *      {id:\\d+} 表示该方法仅以id(数字格式)作为查询依据
     *
     * @param userId 利用 @PathVariable 将 url（/user/001）中指定部分（001）作为查询依据（资源）
     *               注解 @ApiParam swagger-ui 生成文档：传入字段的描述信息
     */
    @JsonView(User.UserDetailView.class)
    @GetMapping("/{id:\\d+}")
    public User getInfo(@PathVariable(name = "id") @ApiParam(value = "用户 id") String userId) {
        if ("001".equals(userId)) {
            throw new UserNotExistException(userId);
        }
        System.out.println("调用getInfo服务, userID: " + userId);
        return new User("tom", "000");
    }

    /**
     *
     * @param user @RequestBody将前端发来的 json字符串对象 解析装入model,<br/>
     *             注解 @Valid 与字段注解搭配完成校验逻辑，若校验失败返回400错误（提交格式错误）
     *
     * @param errors BindingResult与@Valid 搭配使用，可以将错误信息传进方法体内部（处理的可能）
     */
    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> System.out.println(e.getDefaultMessage()));
        }

        System.out.println(user);
        user.setId("001");

        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@PathVariable(name = "id") String id
            , @Valid @RequestBody User user
            , BindingResult errors) {

        System.out.println(id);
        if (errors.hasErrors()) {
            System.out.println("错误信息：");
            errors.getAllErrors().forEach(e -> System.out.println("\t" + e.getDefaultMessage()));
        }
        System.out.println(user);
        user.setId(id);
        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable(name = "id") String id) {
        logger.info("Delete " + id +" Success!!!");
    }
}
