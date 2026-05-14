package com.github.ylyan2015.dao;

import com.github.ylyan2015.entity.DictItemEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据字典子项数据访问层
 */
@Repository
public interface DictItemRepository extends JpaRepository<DictItemEO, Long> {

    /**
     * 根据字典ID查询字典项列表
     */
    List<DictItemEO> findByDictId(Long dictId);

    /**
     * 根据字典ID删除所有字典项
     */
    void deleteByDictId(Long dictId);
}
