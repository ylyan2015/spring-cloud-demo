package com.github.ylyan2015.dao;

import com.github.ylyan2015.entity.UserEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEO, Long> {
    // 自带CRUD，无需写SQL
}