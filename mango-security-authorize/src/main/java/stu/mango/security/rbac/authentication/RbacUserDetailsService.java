/**
 * 
 */
package stu.mango.security.rbac.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import stu.mango.security.rbac.domain.Admin;
import stu.mango.security.rbac.repository.AdminRepository;

@Component
@Transactional
public class RbacUserDetailsService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final AdminRepository adminRepository;

	@Autowired
	public RbacUserDetailsService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名:" + username);
		Admin admin = adminRepository.findByUsername(username);

		logger.info(admin + "");

		return admin;
	}

}
