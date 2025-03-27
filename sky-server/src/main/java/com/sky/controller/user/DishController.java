package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "C端菜品接口")
public class DishController {

    @Autowired
    private DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * 根据分类id查询菜品和关联的口味
     * @param categoryId
     * @return
     */
    @RequestMapping("/list")
    @ApiOperation("根据分类id查询菜品和关联的口味")
    public Result<List<DishVO>> list(Long categoryId){
        log.info("根据分类id查询菜品：{}", categoryId);
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<DishVO> list = dishService.listWithFlavor(dish);
        return Result.success(list);
    }
}
