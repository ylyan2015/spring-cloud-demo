package com.github.ylyan2015.springbootdemo.repository;

import com.github.ylyan2015.springbootdemo.entity.MenuEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单数据访问层
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuEO, Long> {

    /**
     * 根据父菜单ID查询
     */
    List<MenuEO> findByParentId(Long parentId);

    /**
     * 根据状态查询
     */
    List<MenuEO> findByStatus(Integer status);
}
