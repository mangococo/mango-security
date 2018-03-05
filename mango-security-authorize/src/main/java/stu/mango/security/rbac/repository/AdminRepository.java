package stu.mango.security.rbac.repository;

import org.springframework.stereotype.Repository;
import stu.mango.security.rbac.domain.Admin;

/**
 * @author zhailiang
 *
 */
@Repository
public interface AdminRepository extends MangoRepository<Admin> {

	Admin findByUsername(String username);

}
