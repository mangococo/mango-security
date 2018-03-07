package stu.mango.security.rbac.repository;

import org.springframework.stereotype.Repository;
import stu.mango.security.rbac.domain.Role;


@Repository
public interface RoleRepository extends MangoRepository<Role> {

}
