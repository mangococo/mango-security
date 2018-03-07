package stu.mango.security.rbac.repository;

import org.springframework.stereotype.Repository;
import stu.mango.security.rbac.domain.Resource;


@Repository
public interface ResourceRepository extends MangoRepository<Resource> {

	Resource findByName(String name);

}
