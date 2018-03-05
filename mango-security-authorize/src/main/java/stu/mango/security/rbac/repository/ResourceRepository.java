/**
 * 
 */
package stu.mango.security.rbac.repository;

import org.springframework.stereotype.Repository;
import stu.mango.security.rbac.domain.Resource;


/**
 * @author zhailiang
 *
 */
@Repository
public interface ResourceRepository extends MangoRepository<Resource> {

	Resource findByName(String name);

}
