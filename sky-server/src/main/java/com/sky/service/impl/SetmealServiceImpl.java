package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增套餐
     * @param setmealDTO
     */
    public void save(SetmealDTO setmealDTO) {
        //创建Setmeal对象并赋值
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        Long id = setmeal.getId();
        setmeal.setStatus(StatusConstant.DISABLE);
        //新增Setmeal
        setmealMapper.insert(setmeal);

        //创建SetmealDish对象并赋值
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        //新增SetmealDish
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        //select * from setmeal limit 0,10
        //开始分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        Long total = page.getTotal();
        List<Setmeal> records= page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    public void deleteBatch(Long[] ids){
        //判断当前套餐能否被删除--是否存在起售中的套餐
        for(Long id: ids){
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //根据套餐id集合批量删除套餐
        setmealMapper.deleteById(ids);
        //根据套餐id集合批量删除套餐菜品关系数据
        setmealDishMapper.deleteBySetmealIds(ids);

    }
}
