package stu.mango.security.rbac.repository.spec;

import stu.mango.security.rbac.domain.Admin;
import stu.mango.security.rbac.dto.AdminCondition;
import stu.mango.security.rbac.repository.support.MangoSpecification;
import stu.mango.security.rbac.repository.support.QueryWrapper;

public class AdminSpec extends MangoSpecification<Admin, AdminCondition> {

	public AdminSpec(AdminCondition condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWrapper<Admin> queryWrapper) {
		addLikeCondition(queryWrapper, "username");
	}

}
