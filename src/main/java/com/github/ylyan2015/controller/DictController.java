package com.github.ylyan2015.controller;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.DictDto;
import com.github.ylyan2015.dto.DictItemDto;
import com.github.ylyan2015.service.IDictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典控制器
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource
    private IDictService dictService;

    /**
     * 新增字典
     */
    @PostMapping("/add")
    public Result<DictDto> addDict(@RequestBody DictDto dictDto) {
        return dictService.addDict(dictDto);
    }

    /**
     * 修改字典
     */
    @PutMapping("/update")
    public Result<DictDto> updateDict(@RequestBody DictDto dictDto) {
        return dictService.updateDict(dictDto);
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteDict(@PathVariable Long id) {
        return dictService.deleteDict(id);
    }

    /**
     * 根据ID查询字典
     */
    @GetMapping("/get/{id}")
    public Result<DictDto> getDictById(@PathVariable Long id) {
        return dictService.getDictById(id);
    }

    /**
     * 根据字典编码查询字典（含字典项）
     */
    @GetMapping("/getByCode/{dictCode}")
    public Result<DictDto> getDictByCode(@PathVariable String dictCode) {
        return dictService.getDictByCode(dictCode);
    }

    /**
     * 查询所有字典
     */
    @GetMapping("/list")
    public Result<List<DictDto>> getAllDicts() {
        return dictService.getAllDicts();
    }

    /**
     * 启用/禁用字典
     */
    @PutMapping("/toggleStatus")
    public Result<String> toggleDictStatus(@RequestParam Long id, @RequestParam Integer status) {
        return dictService.toggleDictStatus(id, status);
    }

    /**
     * 新增字典项
     */
    @PostMapping("/item/add")
    public Result<DictItemDto> addDictItem(@RequestBody DictItemDto dictItemDto) {
        return dictService.addDictItem(dictItemDto);
    }

    /**
     * 修改字典项
     */
    @PutMapping("/item/update")
    public Result<DictItemDto> updateDictItem(@RequestBody DictItemDto dictItemDto) {
        return dictService.updateDictItem(dictItemDto);
    }

    /**
     * 删除字典项
     */
    @DeleteMapping("/item/delete/{id}")
    public Result<String> deleteDictItem(@PathVariable Long id) {
        return dictService.deleteDictItem(id);
    }

    /**
     * 根据字典ID查询字典项列表
     */
    @GetMapping("/item/list/{dictId}")
    public Result<List<DictItemDto>> getDictItemsByDictId(@PathVariable Long dictId) {
        return dictService.getDictItemsByDictId(dictId);
    }

    /**
     * 启用/禁用字典项
     */
    @PutMapping("/item/toggleStatus")
    public Result<String> toggleDictItemStatus(@RequestParam Long id, @RequestParam Integer status) {
        return dictService.toggleDictItemStatus(id, status);
    }
}
