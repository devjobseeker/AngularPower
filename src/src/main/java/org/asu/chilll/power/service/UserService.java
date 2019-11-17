package org.asu.chilll.power.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.asu.chilll.power.dataview.AppErrorMessage;
import org.asu.chilll.power.dataview.UserDataView;
import org.asu.chilll.power.dataview.search.PageResult;
import org.asu.chilll.power.dataview.search.SearchCriteria;
import org.asu.chilll.power.entity.security.AppUser;
import org.asu.chilll.power.enums.AppMsgType;
import org.asu.chilll.power.enums.UserType;
import org.asu.chilll.power.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private AppUserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public AppUser save(AppUser user) {
		return userRepo.saveAndFlush(user);
	}
	
	public AppUser update(AppUser user) {
		return userRepo.save(user);
	}
	
//	public AppUser find(Long id) {
//		return userRepo.findOneById(id);
//	}
	
	public AppUser findByUid(String uid) {
		return userRepo.findByUid(uid);
	}
	
	public AppUser findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	public PageResult findUsers(SearchCriteria searchCriteria){
		List<AppUser> appUsers = userRepo.findAll();
		List<UserDataView> userList = new ArrayList<UserDataView>();
		for(AppUser user: appUsers) {
			UserDataView dv = new UserDataView();
			dv.setUid(user.getUid());
			dv.setUsername(user.getUsername());
			dv.setRole(user.getRole());
			dv.setEnabled(user.isEnabled());
			userList.add(dv);
		}
		PageResult result = new PageResult();
		result.setUsers(userList);
		return result;
	}
	
	public UserDataView deactivateUser(String username) {
		UserDataView result = new UserDataView();
		AppUser dbUser = userRepo.findByUsername(username);
		if(dbUser != null) {
			dbUser.setEnabled(false);
			this.update(dbUser);
			
			result.setErrorMsg(new AppErrorMessage(AppMsgType.Success.toString()));
			return result;
		}
		result.setErrorMsg(new AppErrorMessage(AppMsgType.User_Not_Exist.toString()));
		return result;
	}
	
	public UserDataView addUser(UserDataView user) {
		//find user exist or not
		AppUser appUser = this.findByUsername(user.getUsername());
		if(appUser == null) {
			appUser = new AppUser();
			appUser.setUid(UUID.randomUUID().toString());
			appUser.setUsername(user.getUsername());
			appUser.setPassword(passwordEncoder.encode(user.getPassword()));
			appUser.setEnabled(true);
			appUser.setRole(UserType.USER.toString());
			appUser = this.save(appUser);
			AppErrorMessage error = new AppErrorMessage(AppMsgType.Success.toString());
			user.setErrorMsg(error);
		}else {
			//TODO: return user exist message
			AppErrorMessage error = new AppErrorMessage(AppMsgType.User_Exist.toString());
			user.setErrorMsg(error);
		}
		
		return user;
	}
}