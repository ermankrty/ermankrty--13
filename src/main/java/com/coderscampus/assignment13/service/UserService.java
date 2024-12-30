package com.coderscampus.assignment13.service;


import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accountRepo;

	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	public User saveUser(User user) {
		return userRepo.save(user);
	}


	public void delete(Long userId) {
		Optional<User> userOptional = userRepo.findById(userId);
		if (userOptional.isPresent()) {
		User user = userOptional.get();
        accountRepo.deleteAll(user.getAccounts());
		userRepo.delete(user);
		} else {
			System.out.println("User not found");
		}
	}

	public Set<User> findAllUsersWithAccountsAndAddresses(){
		return userRepo.findAllUsersWithAccountsAndAddresses();
	}

}


