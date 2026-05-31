package com.github.ylyan2015.springbootdemo.repository;

import com.github.ylyan2015.springbootdemo.entity.UserRoleEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEO, Long> {

    List<UserRoleEO> findByUserId(Long userId);

    List<UserRoleEO> findByRoleId(Long roleId);

    void deleteByUserId(Long userId);

    void deleteByRoleId(Long roleId);

    void deleteByUserIdAndRoleId(Long userId, Long roleId);
}