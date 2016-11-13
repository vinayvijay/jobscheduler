package com.jobscheduler.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jobscheduler.model.User;

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findOneByUsername(String username);
    
    List<User> findAllBySkill(String skill);
    
}
