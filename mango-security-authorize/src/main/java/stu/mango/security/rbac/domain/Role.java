/**
 * 
 */
package stu.mango.security.rbac.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;

/**
 * 角色信息
 */
@Entity
public class Role implements Serializable{

	private static final long serialVersionUID = 4139498977082646704L;
	/**
	 * 数据库表主键
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 审计日志，记录条目创建时间，自动赋值，不需要程序员手工赋值
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdTime;
	/**
	 * 角色名称
	 */
	@Column(length = 20, nullable = false)
	private String name;
	/**
	 * 角色拥有权限的资源集合
	 */
	@OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonBackReference
	private Set<RoleResource> resources  = new HashSet<>();
	/**
	 * 角色的用户集合
	 */
	@OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonBackReference
	private Set<RoleAdmin> admins = new HashSet<>();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the resources
	 */
	public Set<RoleResource> getResources() {
		return resources;
	}
	/**
	 * @param resources the resources to set
	 */
	public void setResources(Set<RoleResource> resources) {
		this.resources = resources;
	}
	/**
	 * @return the admins
	 */
	public Set<RoleAdmin> getAdmins() {
		return admins;
	}
	/**
	 * @param admins the admins to set
	 */
	public void setAdmins(Set<RoleAdmin> admins) {
		this.admins = admins;
	}
	
}
