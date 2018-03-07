package stu.mango.security.rbac.repository;

import org.springframework.stereotype.Repository;
import stu.mango.security.rbac.domain.Admin;

@Repository
public interface AdminRepository extends MangoRepository<Admin> {

	Admin findByUsername(String username);

}
