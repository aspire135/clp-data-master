package com.yzu.clp.DATA_PERFORM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzu.clp.DATA_PERFORM.dao.TaskDao;
import com.yzu.clp.DATA_PERFORM.dto.TaskQueryDTO;
import com.yzu.clp.DATA_PERFORM.entity.Task;
import com.yzu.clp.DATA_PERFORM.service.TaskService;
import com.yzu.clp.DATA_PERFORM.vo.TaskVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements TaskService {
    @Override
    public  TaskVO getAllTask(TaskQueryDTO taskQueryDTO) {
        Page<Task> page = new Page<>(taskQueryDTO.getCurrent(), taskQueryDTO.getSize());

        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        if (ObjectUtils.isNotEmpty(taskQueryDTO) && taskQueryDTO.getTaskId() != null){
            queryWrapper.eq("id", taskQueryDTO.getTaskId());
        }
        queryWrapper.orderByDesc("start_time");
        this.page(page,queryWrapper);
        TaskVO taskVO = new TaskVO();
        taskVO.setTasks(page.getRecords());
        taskVO.setTotal(page.getTotal());
        return taskVO;
    }
}
