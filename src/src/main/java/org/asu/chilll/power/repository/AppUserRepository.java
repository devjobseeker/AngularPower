package org.asu.chilll.power.repository;

import java.util.List;

import org.asu.chilll.power.entity.security.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String>{
	AppUser findByUsername(String username);
	
	AppUser findByUid(String uid);
	
	//AppUser findOneById(Long id);
	
	List<AppUser> findAll();
	
	//Page<AppUser> fetchUsers(Pageable pageable);
}
