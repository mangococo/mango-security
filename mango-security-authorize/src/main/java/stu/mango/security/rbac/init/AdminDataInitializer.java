package stu.mango.security.rbac.init;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import stu.mango.security.rbac.domain.Admin;
import stu.mango.security.rbac.domain.Resource;
import stu.mango.security.rbac.domain.ResourceType;
import stu.mango.security.rbac.domain.Role;
import stu.mango.security.rbac.domain.RoleAdmin;
import stu.mango.security.rbac.repository.AdminRepository;
import stu.mango.security.rbac.repository.ResourceRepository;
import stu.mango.security.rbac.repository.RoleAdminRepository;
import stu.mango.security.rbac.repository.RoleRepository;

/**
 * 默认的系统数据初始化器，永远在其他数据初始化器之前执行
 */
@Component
public class AdminDataInitializer extends AbstractDataInitializer {

	private final PasswordEncoder passwordEncoder;

	private final RoleRepository roleRepository;

	private final AdminRepository adminRepository;

	private final RoleAdminRepository roleAdminRepository;

	private final ResourceRepository resourceRepository;

	@Autowired
	public AdminDataInitializer(PasswordEncoder passwordEncoder, RoleRepository roleRepository, AdminRepository adminRepository, RoleAdminRepository roleAdminRepository, ResourceRepository resourceRepository) {
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.adminRepository = adminRepository;
		this.roleAdminRepository = roleAdminRepository;
		this.resourceRepository = resourceRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idea.core.spi.initializer.DataInitializer#getIndex()
	 */
	@Override
	public Integer getIndex() {
		return Integer.MIN_VALUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idea.core.spi.initializer.AbstractDataInitializer#doInit()
	 */
	@Override
	protected void doInit() {
		initResource();
		Role role = initRole();
		initAdmin(role);
	}

	/**
	 * 初始化用户数据
	 * 
	 * @param role
	 */
	private void initAdmin(Role role) {
		Admin admin = new Admin();
		admin.setUsername("admin");
		admin.setPassword(passwordEncoder.encode("123456"));
		adminRepository.save(admin);

		RoleAdmin roleAdmin = new RoleAdmin();
		roleAdmin.setRole(role);
		roleAdmin.setAdmin(admin);
		roleAdminRepository.save(roleAdmin);
	}

	/**
	 * 初始化角色数据
	 * 
	 * @return
	 */
	private Role initRole() {
		Role role = new Role();
		role.setName("超级管理员");
		roleRepository.save(role);
		return role;
	}

	/**
	 * 初始化菜单数据
	 */
	protected void initResource() {
		Resource root = createRoot("根节点");

		createResource("首页", "", "home", root);

		Resource menu1 = createResource("平台管理", "", "desktop", root);

//		createResource("资源管理", "resource", "", menu1);
		createResource("角色管理", "role", "", menu1);
		createResource("管理员管理", "admin", "", menu1);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idea.core.spi.initializer.AbstractDataInitializer#isNeedInit()
	 */
	@Override
	protected boolean isNeedInit() {
		return adminRepository.count() == 0;
	}

	/**
	 * @param name
	 * @return
	 */
	protected Resource createRoot(String name) {
		Resource node = new Resource();
		node.setName(name);
		resourceRepository.save(node);
		return node;
	}

	/**
	 * @param name
	 * @param parent
	 * @return
	 */
	protected Resource createResource(String name, Resource parent) {
		return createResource(name, null, null, parent);
	}

	/**
	 * @param name
	 * @param link
	 * @param iconName
	 * @param parent
	 * @return
	 */
	protected Resource createResource(String name, String link, String iconName, Resource parent) {
		Resource node = new Resource();
		node.setName(name);
		node.setIcon(iconName);
		node.setParent(parent);
		node.setType(ResourceType.MENU);
		if (StringUtils.isNotBlank(link)) {
			node.setLink(link + "Manage");
			Set<String> urls = new HashSet<>();
			urls.add(link + "Manage");
			urls.add("/" + link + "/**");
			node.setUrls(urls);
		}
		resourceRepository.save(node);
		return node;
	}
}
