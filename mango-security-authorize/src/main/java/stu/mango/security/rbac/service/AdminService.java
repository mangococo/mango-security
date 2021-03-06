package stu.mango.security.rbac.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stu.mango.security.rbac.dto.AdminCondition;
import stu.mango.security.rbac.dto.AdminInfo;

/**
 * 管理员服务
 */
public interface AdminService {

	/**
	 * 创建管理员
	 * @param adminInfo
	 * @return
	 */
	AdminInfo create(AdminInfo adminInfo);
	/**
	 * 修改管理员
	 * @param adminInfo
	 * @return
	 */
	AdminInfo update(AdminInfo adminInfo);
	/**
	 * 删除管理员
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 获取管理员详细信息
	 * @param id
	 * @return
	 */
	AdminInfo getInfo(Long id);
	/**
	 * 获取管理员详细信息
	 * @param username
	 * @return
	 */
	AdminInfo getInfo(String username);
	/**
	 * 分页查询管理员
	 * @param condition
	 * @param pageable 根据参数装入分页的信息：一是分页的信息（page、size），二是排序的信息。
	 * @return
	 */
	Page<AdminInfo> query(AdminCondition condition, Pageable pageable);

}
