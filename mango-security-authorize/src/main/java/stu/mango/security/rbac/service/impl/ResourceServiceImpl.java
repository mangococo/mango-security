package stu.mango.security.rbac.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import stu.mango.security.rbac.domain.Admin;
import stu.mango.security.rbac.domain.Resource;
import stu.mango.security.rbac.dto.ResourceInfo;
import stu.mango.security.rbac.repository.AdminRepository;
import stu.mango.security.rbac.repository.ResourceRepository;
import stu.mango.security.rbac.service.ResourceService;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final ResourceRepository resourceRepository;

	private final AdminRepository adminRepository;

	@Autowired
	public ResourceServiceImpl(ResourceRepository resourceRepository, AdminRepository adminRepository) {
		this.resourceRepository = resourceRepository;
		this.adminRepository = adminRepository;
	}

	/* (non-Javadoc)
	 * @see com.idea.ams.service.ResourceService#getResourceTree(java.lang.Long, com.idea.ams.domain.Admin)
	 */
	@Override
	public ResourceInfo getTree(Long adminId) {
		Admin admin = adminRepository.findOne(adminId);

		Resource resource = resourceRepository.findByName("根节点");

		logger.info(resource + "");

		return resource.toTree(admin);
	}
	@Override
	public ResourceInfo getInfo(Long id) {
		Resource resource = resourceRepository.findOne(id);
		ResourceInfo resourceInfo = new ResourceInfo();
		BeanUtils.copyProperties(resource, resourceInfo);
		return resourceInfo;
	}

	@Override
	public ResourceInfo create(ResourceInfo info) {
		Resource parent = resourceRepository.findOne(info.getParentId());
		if(parent == null){
			parent = resourceRepository.findByName("根节点");
		}
		Resource resource = new Resource();
		BeanUtils.copyProperties(info, resource);
		parent.addChild(resource);
		info.setId(resourceRepository.save(resource).getId());
		return info;		
	}

	@Override
	public ResourceInfo update(ResourceInfo info) {
		Resource resource = resourceRepository.findOne(info.getId());
		BeanUtils.copyProperties(info, resource);
		return info;
	}

	@Override
	public void delete(Long id) {
		resourceRepository.delete(id);
	}
	/* (non-Javadoc)
	 * @see stu.mango.security.rbac.service.ResourceService#move(java.lang.Long, boolean)
	 */
	@Override
	public Long move(Long id, boolean up) {
		Resource resource = resourceRepository.findOne(id);
		int index = resource.getSort();
		List<Resource> childs = resource.getParent().getChilds();
		for (int i = 0; i < childs.size(); i++) {
			Resource current = childs.get(i);
			if(current.getId().equals(id)) {
				if(up){
					if(i != 0) {
						Resource pre = childs.get(i - 1);
						resource.setSort(pre.getSort());
						pre.setSort(index);
						resourceRepository.save(pre);
					}
				}else{
					if(i != childs.size()-1) {
						Resource next = childs.get(i + 1);
						resource.setSort(next.getSort());
						next.setSort(index);
						resourceRepository.save(next);
					}
				}
			}
		}
		resourceRepository.save(resource);
		return resource.getParent().getId();
	}

}
