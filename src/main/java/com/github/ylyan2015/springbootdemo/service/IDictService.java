package com.github.ylyan2015.springbootdemo.service;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.DictDto;
import com.github.ylyan2015.springbootdemo.dto.DictItemDto;

import java.util.List;

/**
 * 数据字典服务接口
 */
public interface IDictService {

    /**
     * 新增字典
     */
    Result<DictDto> addDict(DictDto dictDto);

    /**
     * 修改字典
     */
    Result<DictDto> updateDict(DictDto dictDto);

    /**
     * 删除字典
     */
    Result<String> deleteDict(Long id);

    /**
     * 根据ID查询字典
     */
    Result<DictDto> getDictById(Long id);

    /**
     * 根据字典编码查询字典（含字典项）
     */
    Result<DictDto> getDictByCode(String dictCode);

    /**
     * 查询所有字典
     */
    Result<List<DictDto>> getAllDicts();

    /**
     * 启用/禁用字典
     */
    Result<String> toggleDictStatus(Long id, Integer status);

    /**
     * 新增字典项
     */
    Result<DictItemDto> addDictItem(DictItemDto dictItemDto);

    /**
     * 修改字典项
     */
    Result<DictItemDto> updateDictItem(DictItemDto dictItemDto);

    /**
     * 删除字典项
     */
    Result<String> deleteDictItem(Long id);

    /**
     * 根据字典ID查询字典项列表
     */
    Result<List<DictItemDto>> getDictItemsByDictId(Long dictId);

    /**
     * 启用/禁用字典项
     */
    Result<String> toggleDictItemStatus(Long id, Integer status);
}
