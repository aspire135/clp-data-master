package com.yzu.clp.DATA_PERFORM.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzu.clp.DATA_PERFORM.dto.TaskQueryDTO;
import com.yzu.clp.DATA_PERFORM.entity.Task;
import com.yzu.clp.DATA_PERFORM.vo.TaskVO;

import java.util.List;

public interface TaskService extends IService<Task> {
    TaskVO getAllTask(TaskQueryDTO taskQueryDTO);
}
