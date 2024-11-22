package com.yzu.clp.DATA_PERFORM.controller;


import com.yzu.clp.DATA_PERFORM.common.R;
import com.yzu.clp.DATA_PERFORM.dto.*;
import com.yzu.clp.DATA_PERFORM.service.HeiMaoSubService;
import com.yzu.clp.DATA_PERFORM.service.PromptService;
import com.yzu.clp.DATA_PERFORM.service.TaskService;
import com.yzu.clp.DATA_PERFORM.service.TemporaryPromptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/heiMaoSub")
@Api(tags = "爬虫数据")
public class HeiMaoSubController {
    @Resource
    private HeiMaoSubService heiMaoSubService;
    @Value("${file.save-path}")
    private String filePath;

    @Resource
    private TemporaryPromptService temporaryPromptService;
    @Resource
    private PromptService promptService;
    @Resource
    private TaskService taskService;
    @PostMapping("/total")
    public R countAll(){
        return R.ok().data(heiMaoSubService.countAll());
    }
    @PostMapping("/classifyName")
    @ApiOperation("投诉对象分类统计-饼图")
    public R classifyNameByDate(@RequestBody PeriodDTO periodDTO){
        return R.ok().data(heiMaoSubService.classifyNameByDate(periodDTO));
    }

    @PostMapping("/classifyName-bar")
    @ApiOperation("投诉对象分类统计-柱状图")
    public R classifyNameToBarByDate(@RequestBody PeriodDTO periodDTO){
        return R.ok().data(heiMaoSubService.classifyNameToBarByDate(periodDTO));
    }

    @PostMapping("/temporary-prompt")
    @ApiOperation("prompt列表-新建任务")
    public void promptByDate( @RequestBody TemporaryPromptDTO temporaryPromptDTO) {
        temporaryPromptService.temporaryPrompt(temporaryPromptDTO);
    }

    @PostMapping("/prompt")
    @ApiOperation("prompt列表")
    public R temproraryPrompt( @RequestBody PromptDTO promptDTO) {
        return R.ok().success(promptService.savePrompt(promptDTO));
    }

    @GetMapping("/getPrompt")
    @ApiOperation("得到最新prompt")
    public R getPrompt(){
        return R.ok().data(promptService.getAllPrompt());
    }


    @GetMapping("/fileList")
    @ApiOperation("获得文件名列表")
    public R getFileList() {
        //目录路径
        String directoryPath = filePath;
        List<String> fileList = new ArrayList<>();
        heiMaoSubService.getFileListRecursive(directoryPath, fileList);
        return R.ok().data(fileList);
    }
    /**
     * 拿task任务列表
     * @param taskQueryDTO
     * @return
     */
    @PostMapping("/task/all")
    @ApiOperation("task'任务表")
    public R getAllTask(@ApiParam @RequestBody TaskQueryDTO taskQueryDTO){
         return R.ok().data(taskService.getAllTask(taskQueryDTO));
    }
    @PostMapping("/problem-tag/chart")
    @ApiOperation("问题分类饼状图")
    public R getProblemTagChart(@RequestBody  DateDTO dateDTO){
        return R.ok().data(promptService.getProblemTagChart(dateDTO));
    }

    @PostMapping("/problem-tag/bar")
    @ApiOperation("问题分类柱状图")
    public R getProblemTagBar(@RequestBody PeriodDTO periodDTO){
        return R.ok().data(promptService.getProblemTagBar(periodDTO));
    }

    @PostMapping("/industry-tag/chart")
    @ApiOperation("行业分类饼状图")
    public R getIndustryTagChart(@RequestBody  DateDTO dateDTO){
        return R.ok().data(promptService.getIndustryTagChart(dateDTO));
    }

    @PostMapping("/industry-tag/bar")
    @ApiOperation("行业分类柱状图")
    public R getIndustryTagBar(@RequestBody PeriodDTO periodDTO){
        return R.ok().data(promptService.getIndustryTagBar(periodDTO));
    }
    @PostMapping("/wordle")
    @ApiOperation("词云")
    public R getWordle(){
        return R.ok().data(heiMaoSubService.getWordle());
    }

    @PostMapping("/weeklyData")
    @ApiOperation("得到每周的记录条数")
    public R getWeeklyData(@RequestBody DateDTO dateDTO ){
        return R.ok().data(heiMaoSubService.getWeeklyData(dateDTO));
    }

    @PostMapping("/temporary-prompt/get-data")
    @ApiOperation("接收参数")
    public R getTemporaryPromptData(@RequestBody List<Map<String, Object>> jsonList
    ) throws IOException, FontFormatException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        Map<String, Object> map3 = new HashMap<>();
        Map<String, Object> map4 = new HashMap<>();
        if (jsonList.size() == 5 ){
            map = jsonList.get(0);
            map1 = jsonList.get(1);
            map2 = jsonList.get(2);
            map3 = jsonList.get(3);
            map4 = jsonList.get(4);
        }
        temporaryPromptService.createTemporaryTaskBulletin(map, map1, map2, map3, map4);
        return R.ok().data(1);
    }


}
