package com.github.ylyan2015.dao;

import com.github.ylyan2015.entity.DictEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 数据字典数据访问层
 */
@Repository
public interface DictRepository extends JpaRepository<DictEO, Long> {

    /**
     * 根据字典编码查询
     */
    Optional<DictEO> findByDictCode(String dictCode);

    /**
     * 删除字典编码
     */
    void deleteByDictCode(String dictCode);
}
