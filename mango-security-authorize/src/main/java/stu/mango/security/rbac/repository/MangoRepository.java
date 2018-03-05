/**
 * 
 */
package stu.mango.security.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author zhailiang
 *
 */
@NoRepositoryBean
public interface MangoRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

}
