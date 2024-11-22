package com.yzu.clp.DATA_PERFORM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzu.clp.DATA_PERFORM.dao.PromptDao;
import com.yzu.clp.DATA_PERFORM.dto.PeriodDTO;
import com.yzu.clp.DATA_PERFORM.dto.DateDTO;
import com.yzu.clp.DATA_PERFORM.dto.PromptDTO;
import com.yzu.clp.DATA_PERFORM.entity.Prompt;
import com.yzu.clp.DATA_PERFORM.service.PromptService;
import com.yzu.clp.DATA_PERFORM.service.TaskService;
import com.yzu.clp.DATA_PERFORM.vo.IndustryTagsCountVO;
import com.yzu.clp.DATA_PERFORM.vo.ProblemTagsCountVO;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromptServiceImpl extends ServiceImpl<PromptDao, Prompt> implements PromptService {

    @Resource
    private  TaskService taskService;
    @Resource
    private PromptDao promptDao;

    @Override
    public Boolean savePrompt(PromptDTO promptDTO) {
//        Task task = new Task();
//        task.setStatus(0);
//        task.setStartTime(new DateTime());
        List<Prompt> promptList = promptDTO.getPromptList();
        promptList.forEach(item->{
            if (item.getId() == null){
                this.save(item);
            }else {
                this.updateById(item);
            }
        });
        return true;
    }

    @Override
    public List<Prompt> getAllPrompt() {
        QueryWrapper<Prompt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level",2).or().eq("level", 1);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, Integer> getProblemTagChart(DateDTO dateDTO){
        Integer other = 0;
        //拿到现在的prompt的数据
        List<Prompt> promptList = this.getAllPrompt();
        //拿到粗略分类的数据
        List<ProblemTagsCountVO> problemTagsCountVOS = promptDao.getPromblemTagsCount(dateDTO);
        Map<String, Integer> dataMap = new HashMap<>();
        for (Prompt prompt : promptList) {
            Integer sum = 0;
            for (ProblemTagsCountVO problemTagsCountVO : problemTagsCountVOS) {
                if (problemTagsCountVO.getProblemTags() == null ){
                    other = problemTagsCountVO.getValue();
                }else {
                    if (problemTagsCountVO.getProblemTags().contains(prompt.getTitle())){
                        sum +=  problemTagsCountVO.getValue();
                    }
                }
            }
            if ("其他".equals(prompt.getTitle())){
                sum = sum + other;
            }
            dataMap.put(prompt.getTitle(), sum);
        }
        return dataMap;
    }

    @Override
    public Map<String, Object> getProblemTagBar(PeriodDTO periodDTO) {
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(periodDTO.getStartDatePast());
        dateDTO.setEndDate(periodDTO.getEndDatePast());

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        //过去同一时间段的结果
        Map<String, Integer> dataMapPast  =   this.getProblemTagChart(dateDTO);
        dateDTO.setStartDate(periodDTO.getStartDate());
        dateDTO.setEndDate(periodDTO.getEndDate());
        //现在这个时间段的结果
        Map<String, Integer> dataMap  =   this.getProblemTagChart(dateDTO);
        //拿到现在的prompt的数据
        List<Prompt> promptList = this.getAllPrompt();
        Prompt prompt0 = new Prompt();
        prompt0.setTitle("其他");
        promptList.add(prompt0);
        //存柱状图的同比增长数据
        Map<String, Object> barMap = new HashMap<>();
        for (Prompt prompt : promptList) {
            double increase = 0;
            if (dataMapPast.get(prompt.getTitle()) == null || dataMapPast.get(prompt.getTitle()) == 0){
                barMap.put(prompt.getTitle(), 0.00);
            }else {
                increase =    ((double)dataMap.get(prompt.getTitle()) - (double)dataMapPast.get(prompt.getTitle()))  /  dataMapPast.get(prompt.getTitle()) * 100.00 ;
                if (increase >10 || increase < -10){
                    barMap.put(prompt.getTitle(), Double.valueOf(decimalFormat.format(increase)));
                }
            }
        }
        return barMap;

    }

    @Override
    public Map<String, Integer> getIndustryTagChart(DateDTO dateDTO) {
        Integer other = 0;
        Map<String, Integer> dataMap = new HashMap<>();
        List<IndustryTagsCountVO> industryTagsCountVOS = promptDao.getIndustryTagsCount(dateDTO);
        //拿到现在的industry的prompt的数据
        QueryWrapper<Prompt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level",3);
        List<Prompt> promptList = list(queryWrapper);
        for (Prompt prompt : promptList) {
            Integer sum = 0;
            for (IndustryTagsCountVO industryTagsCountVO : industryTagsCountVOS) {
                if (industryTagsCountVO.getIndustryTags() == null  ){
                    other = industryTagsCountVO.getValue();
                }else {
                    if (industryTagsCountVO.getIndustryTags().contains(prompt.getTitle())){
                        sum +=  industryTagsCountVO.getValue();
                    }
                }
            }
            if ("其他".equals(prompt.getTitle())){
                sum = sum + other;
            }
            dataMap.put(prompt.getTitle(), sum);
        }
        return dataMap;
    }

    @Override
    public Map<String, Object> getIndustryTagBar(PeriodDTO periodDTO) {
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(periodDTO.getStartDatePast());
        dateDTO.setEndDate(periodDTO.getEndDatePast());

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        //过去同一时间段的结果
        Map<String, Integer> dataMapPast  =   this.getIndustryTagChart(dateDTO);
        dateDTO.setStartDate(periodDTO.getStartDate());
        dateDTO.setEndDate(periodDTO.getEndDate());
        //现在这个时间段的结果
        Map<String, Integer> dataMap  =   this.getIndustryTagChart(dateDTO);
        //拿到现在的industry的prompt的数据
        QueryWrapper<Prompt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level",3);
        List<Prompt> promptList = list(queryWrapper);
        Prompt prompt0 = new Prompt();
        prompt0.setTitle("其他");
        promptList.add(prompt0);
        //存柱状图的同比增长数据
        Map<String, Object> barMap = new HashMap<>();

        for (Prompt prompt : promptList) {
            double increase = 0;
            if (dataMapPast.get(prompt.getTitle()) == null || dataMapPast.get(prompt.getTitle()) == 0){
                barMap.put(prompt.getTitle(), 0.00);
            }else {
                increase =  ((double)dataMap.get(prompt.getTitle()) - (double)dataMapPast.get(prompt.getTitle()))  /  dataMapPast.get(prompt.getTitle()) * 100.00 ;
                if (increase >10 || increase <-10){
                    barMap.put(prompt.getTitle(), Double.valueOf(decimalFormat.format(increase)));
                }else {
                    barMap.put(prompt.getTitle(), 0);
                }
            }
        }
        return barMap;
    }

}
