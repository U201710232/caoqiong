package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Api("套餐接口相关")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增套餐")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐");
        setmealService.save(setmealDTO);
        return Result.success(MessageConstant.SETMEAL_CREATE_SUCCESS);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询，参数为:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation(value = "删除套餐")
    public Result delete(@RequestParam("ids") Long[] ids){
        log.info("删除套餐，ids:{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success(MessageConstant.SETMEAL_DELETE_SUCCESS);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询套餐")
    public Result<SetmealDTO> getById(@PathVariable Long id){
        log.info("根据id查询套餐:{}", id);
        SetmealDTO setmealDTO = setmealService.getByIdWithDish(id);
        return Result.success(setmealDTO);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐:{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success(MessageConstant.SETMEAL_UPDATE_SUCCESS);
    }
}
