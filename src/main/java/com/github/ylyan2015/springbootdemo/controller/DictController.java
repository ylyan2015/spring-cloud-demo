package com.github.ylyan2015.springbootdemo.controller;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.DictDto;
import com.github.ylyan2015.springbootdemo.dto.DictItemDto;
import com.github.ylyan2015.springbootdemo.service.IDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据字典控制器
 */
@Tag(name = "字典管理", description = "数据字典及字典项的增删改查接口")
@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource
    private IDictService dictService;

    /**
     * 新增字典
     */
    @Operation(summary = "新增字典", description = "创建新的数据字典")
    @PostMapping("/add")
    public Result<DictDto> addDict(@Valid @RequestBody DictDto dictDto) {
        return dictService.addDict(dictDto);
    }

    /**
     * 修改字典
     */
    @Operation(summary = "修改字典", description = "更新字典信息")
    @PutMapping("/update")
    public Result<DictDto> updateDict(@Valid @RequestBody DictDto dictDto) {
        return dictService.updateDict(dictDto);
    }

    /**
     * 删除字典
     */
    @Operation(summary = "删除字典", description = "根据ID删除字典及其字典项")
    @Parameter(name = "id", description = "字典ID", required = true, example = "1")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteDict(@PathVariable Long id) {
        return dictService.deleteDict(id);
    }

    /**
     * 根据ID查询字典
     */
    @Operation(summary = "查询字典详情", description = "根据ID查询字典详细信息")
    @Parameter(name = "id", description = "字典ID", required = true, example = "1")
    @GetMapping("/get/{id}")
    public Result<DictDto> getDictById(@PathVariable Long id) {
        return dictService.getDictById(id);
    }

    /**
     * 根据字典编码查询字典（含字典项）
     */
    @Operation(summary = "根据编码查询字典", description = "根据字典编码查询字典及所有字典项")
    @Parameter(name = "dictCode", description = "字典编码", required = true, example = "user_status")
    @GetMapping("/getByCode/{dictCode}")
    public Result<DictDto> getDictByCode(@PathVariable String dictCode) {
        return dictService.getDictByCode(dictCode);
    }

    /**
     * 查询所有字典
     */
    @Operation(summary = "查询字典列表", description = "查询所有字典信息")
    @GetMapping("/list")
    public Result<List<DictDto>> getAllDicts() {
        return dictService.getAllDicts();
    }

    /**
     * 启用/禁用字典
     */
    @Operation(summary = "切换字典状态", description = "启用或禁用字典")
    @Parameter(name = "id", description = "字典ID", required = true, example = "1")
    @Parameter(name = "status", description = "状态：1-启用，0-禁用", required = true, example = "1")
    @PutMapping("/toggleStatus")
    public Result<String> toggleDictStatus(@RequestParam Long id, @RequestParam Integer status) {
        return dictService.toggleDictStatus(id, status);
    }

    /**
     * 新增字典项
     */
    @Operation(summary = "新增字典项", description = "为字典添加新的字典项")
    @PostMapping("/item/add")
    public Result<DictItemDto> addDictItem(@Valid @RequestBody DictItemDto dictItemDto) {
        return dictService.addDictItem(dictItemDto);
    }

    /**
     * 修改字典项
     */
    @Operation(summary = "修改字典项", description = "更新字典项信息")
    @PutMapping("/item/update")
    public Result<DictItemDto> updateDictItem(@Valid @RequestBody DictItemDto dictItemDto) {
        return dictService.updateDictItem(dictItemDto);
    }

    /**
     * 删除字典项
     */
    @Operation(summary = "删除字典项", description = "根据ID删除字典项")
    @Parameter(name = "id", description = "字典项ID", required = true, example = "1")
    @DeleteMapping("/item/delete/{id}")
    public Result<String> deleteDictItem(@PathVariable Long id) {
        return dictService.deleteDictItem(id);
    }

    /**
     * 根据字典ID查询字典项列表
     */
    @Operation(summary = "查询字典项列表", description = "根据字典ID查询所有字典项")
    @Parameter(name = "dictId", description = "字典ID", required = true, example = "1")
    @GetMapping("/item/list/{dictId}")
    public Result<List<DictItemDto>> getDictItemsByDictId(@PathVariable Long dictId) {
        return dictService.getDictItemsByDictId(dictId);
    }

    /**
     * 启用/禁用字典项
     */
    @Operation(summary = "切换字典项状态", description = "启用或禁用字典项")
    @Parameter(name = "id", description = "字典项ID", required = true, example = "1")
    @Parameter(name = "status", description = "状态：1-启用，0-禁用", required = true, example = "1")
    @PutMapping("/item/toggleStatus")
    public Result<String> toggleDictItemStatus(@RequestParam Long id, @RequestParam Integer status) {
        return dictService.toggleDictItemStatus(id, status);
    }
}
