package com.yzu.clp.DATA_PERFORM.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzu.clp.DATA_PERFORM.dao.HeiMaoSubDao;
import com.yzu.clp.DATA_PERFORM.dto.PeriodDTO;
import com.yzu.clp.DATA_PERFORM.dto.DateDTO;
import com.yzu.clp.DATA_PERFORM.entity.HeiMaoSub;
import com.yzu.clp.DATA_PERFORM.entity.WeeklyData;
import com.yzu.clp.DATA_PERFORM.service.HeiMaoSubService;
import com.yzu.clp.DATA_PERFORM.vo.ClassifyNameVO;
import com.yzu.clp.Util.FilterWordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class HeiMaoSubServiceImpl extends ServiceImpl<HeiMaoSubDao, HeiMaoSub> implements HeiMaoSubService {

    @Resource
    private HeiMaoSubDao heiMaoSubDao;
    @Resource
    private FilterWordUtil filterWordUtil;


    @Value("${file.csv.save-path}")
    private String filePath;

    @Override
    public Map<String, Integer> countAll(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 定义日期格式


        LocalDate currentDate = LocalDate.now(); // 获取当前日期
        Month currentMonth = currentDate.getMonth(); // 获取当前月份
        int year = currentDate.getYear(); // 获取当前年份
        LocalDate currentMonthDate = LocalDate.of(year, currentMonth, 1); // 构造当前月份的日期
        //当月
        String startMonthDate = formatter.format(currentMonthDate);
        String endMonthDate = formatter.format(currentDate);

        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // 当前周的星期一
        LocalDate endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); // 当前周的星期日
        //本周
        String startOfWeekFormatted = startOfWeek.format(formatter); // 格式化开始日期
        String endOfWeekFormatted = endOfWeek.format(formatter); // 格式化结束日期
        //当日
        String currentDayDate = formatter.format(currentDate);




        Map<String, Integer> dataMap = new HashMap<>();
        Integer sum = heiMaoSubDao.countAll();
        dataMap.put("sum",sum);
        Integer updateNumberMonthly = heiMaoSubDao.countByDate(startMonthDate,endMonthDate);
        Integer updateNumberWeekly = heiMaoSubDao.countByDate(startOfWeekFormatted,endOfWeekFormatted);
        Integer updateNumberDayly = heiMaoSubDao.countByDaylyDate(currentDayDate);
        if (updateNumberDayly == null){
            updateNumberDayly = 0;
        }
        if (updateNumberWeekly == null ){
            updateNumberWeekly =0;
        }
        if (updateNumberMonthly == null){
            updateNumberMonthly =0;
        }
        dataMap.put("dayly",updateNumberDayly);
        dataMap.put("weekly", updateNumberWeekly);
        dataMap.put("monthly", updateNumberMonthly);
        return dataMap;
    }
    @Override
        public List<ClassifyNameVO> classifyNameByDate(PeriodDTO periodDTO) {
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(periodDTO.getStartDate());
        dateDTO.setEndDate(periodDTO.getEndDate());
        List<ClassifyNameVO> classifyNameVOS = heiMaoSubDao.classifyNameByDate(dateDTO);
        return classifyNameVOS;
    }


    @Override
    public void getFileListRecursive(String filePath, List<String> fileList) {
        File directory = new File(filePath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                } else if (file.isDirectory()) {
                    getFileListRecursive(file.getAbsolutePath(), fileList);
                }
            }
        }
    }

    @Override
    public Integer countAllByDate(LocalDate startDate, LocalDate endDate) {
        return heiMaoSubDao.countAllByDate(startDate, endDate);
    }

    @Override
    public Map<String , Object> classifyNameToBarByDate(PeriodDTO periodDTO) {
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(periodDTO.getStartDate());
        dateDTO.setEndDate(periodDTO.getEndDate());
        Map<String, Object> dataMap = new HashMap<>();
        List<ClassifyNameVO> classifyNameVOList = heiMaoSubDao.classifyNameByDate(dateDTO);
        //数据处理
        classifyNameVOList.forEach(item->{
            if ("京东".equals(item.getName())){
                dataMap.put("jingdong", item.getValue());
            }else if ("淘宝".equals(item.getName())){
                dataMap.put("taobao", item.getValue());
            }else if ("美团".equals(item.getName())) {
                dataMap.put("meituan", item.getValue());
            }else if ("滴滴".equals(item.getName())){
                dataMap.put("didi", item.getValue());
            }else if ("抖音".equals(item.getName())){
                dataMap.put("douyin", item.getValue());
            }else if ("天猫".equals(item.getName())){
                dataMap.put("tianmao", item.getValue());
            }else if ("拼多多".equals(item.getName())){
                dataMap.put("pdd", item.getValue());
            }else if ("饿了么".equals(item.getName())){
                dataMap.put("eleme", item.getValue());
            }
        });
        //过去的时间段的数据
        Map<String, Object> dataMapPast = new HashMap<>();
        dateDTO.setStartDate(periodDTO.getStartDatePast());
        dateDTO.setEndDate(periodDTO.getEndDatePast());
        List<ClassifyNameVO> classifyNameVOListPast = heiMaoSubDao.classifyNameByDate(dateDTO);
        classifyNameVOListPast.forEach(item->{
            if ("京东".equals(item.getName())){
                dataMapPast.put("jingdong", item.getValue());
            }else if ("淘宝".equals(item.getName())){
                dataMapPast.put("taobao", item.getValue());
            }else if ("美团".equals(item.getName())) {
                dataMapPast.put("meituan", item.getValue());
            }else if ("滴滴".equals(item.getName())){
                dataMapPast.put("didi", item.getValue());
            }else if ("抖音".equals(item.getName())){
                dataMapPast.put("douyin", item.getValue());
            }else if ("天猫".equals(item.getName())){
                dataMapPast.put("tianmao", item.getValue());
            }else if ("拼多多".equals(item.getName())){
                dataMapPast.put("pdd", item.getValue());
            }else if ("饿了么".equals(item.getName())){
                dataMapPast.put("eleme", item.getValue());
            }
        });
        //柱状图同比增加的
        Map<String,Object> barMap = new HashMap<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");


        //计算行业同比增加
        if (dataMapPast.get("jingdong") != null && Double.parseDouble(dataMapPast.get("jingdong").toString()) >0 ){
            double increase1 = (Double.parseDouble(dataMap.get("jingdong").toString()) - Double.parseDouble(dataMapPast.get("jingdong").toString()))
                    / Double.parseDouble(dataMapPast.get("jingdong").toString()) ;
            //判断同比增加是否大于0 否则赋值为0
            if (increase1 >0.1 || increase1 < -0.1 ){
                barMap.put("京东",decimalFormat.format(increase1 * 100.00));
            }else {
                barMap.put("京东",0.00);
            }
        } else {
            barMap.put("京东",0.00);
        }
        if (dataMapPast.get("taobao") != null && Double.parseDouble(dataMapPast.get("taobao").toString()) >0 ){
            double increase2 = ((Double.parseDouble(dataMap.get("taobao").toString()) - Double.parseDouble(dataMapPast.get("taobao").toString())  )
                    / Double.parseDouble(dataMapPast.get("taobao").toString())) ;
            //判断同比增加是否大于0 否则赋值为0
            if ( increase2 >0.1 || increase2 < -0.1 ){
                barMap.put("淘宝",decimalFormat.format(increase2 * 100.00));
            }else {
                barMap.put("淘宝",0.00);
            }
        } else {
            barMap.put("淘宝",0.00);
        }

        if (dataMapPast.get("pdd") != null && Double.parseDouble(dataMapPast.get("pdd").toString()) >0 ){
            double increase3 = ((Double.parseDouble(dataMap.get("pdd").toString()) - Double.parseDouble(dataMapPast.get("pdd").toString())  )
                    / Double.parseDouble(dataMapPast.get("pdd").toString())) ;
            //判断同比增加是否大于0 否则赋值为0
            if (increase3 >0.1 || increase3 < -0.1 ){
                barMap.put("拼多多",decimalFormat.format(increase3 * 100.00));
            }else {
                barMap.put("拼多多",0.00);
            }
        }else {
            barMap.put("拼多多",0.00);
        }
        if (dataMapPast.get("meituan") != null && Double.parseDouble(dataMapPast.get("meituan").toString()) >0 ){
            double increase4 = ((Double.parseDouble(dataMap.get("meituan").toString()) - Double.parseDouble(dataMapPast.get("meituan").toString())  )
                    / Double.parseDouble(dataMapPast.get("meituan").toString())) ;
            //判断同比增加是否大于0 否则赋值为0
            if (increase4 >0.1 || increase4 < -0.1){
                barMap.put("美团",decimalFormat.format(increase4 * 100.00));
            }else {
                barMap.put("美团",0.00);
            }
        } else {
            barMap.put("美团",0.00);
        }
        if (dataMapPast.get("douyin") != null && Double.parseDouble(dataMapPast.get("douyin").toString()) >0 ){
            double increase5 = ((Double.parseDouble(dataMap.get("douyin").toString()) - Double.parseDouble(dataMapPast.get("douyin").toString())  )
                    / Double.parseDouble(dataMapPast.get("douyin").toString())) ;
            //判断同比增加是否大于0 否则赋值为0
            if (increase5 >0.1 || increase5 < -0.1 ){
                barMap.put("抖音",decimalFormat.format(increase5 * 100.00));
            }else {
                barMap.put("抖音",0.00);
            }
        } else {
            barMap.put("抖音",0.00);
        }
        if (dataMapPast.get("eleme") != null && Double.parseDouble(dataMapPast.get("eleme").toString()) >0 ){
            double increase6 = ((Double.parseDouble(dataMap.get("eleme").toString()) - Double.parseDouble(dataMapPast.get("eleme").toString())  )
                    / Double.parseDouble(dataMapPast.get("eleme").toString())) ;
            //判断同比增加是否大于0 否则赋值为0
            if (increase6 >0.1 || increase6 < -0.1){
                barMap.put("饿了么",decimalFormat.format(increase6 * 100.00));
            }else {
                barMap.put("饿了么",0.00);
            }
        } else {
            barMap.put("饿了么",0.00);
        }
        if (dataMapPast.get("didi") != null && Double.parseDouble(dataMapPast.get("didi").toString()) >0 ){
            double increase7 = ((Double.parseDouble(dataMap.get("didi").toString()) - Double.parseDouble(dataMapPast.get("didi").toString())  )
                    / Double.parseDouble(dataMapPast.get("didi").toString())) ;
            //判断同比增加是否大于0 否则赋值为0
            if (increase7 >0.1 || increase7 < -0.1){
                barMap.put("滴滴",decimalFormat.format(increase7 * 100.00));
            }else {
                barMap.put("滴滴",0.00);
            }
        } else {
            barMap.put("滴滴",0.00);
        }

        if (dataMapPast.get("tianmao") != null && Double.parseDouble(dataMapPast.get("tianmao").toString()) >0 ){
            double increase8 = ((Double.parseDouble(dataMap.get("tianmao").toString()) - Double.parseDouble(dataMapPast.get("tianmao").toString())  )
                    / Double.parseDouble(dataMapPast.get("tianmao").toString())) ;
            //判断同比增加是否大于0 否则赋值为0
            if (increase8 >0.1 || increase8 < -0.1){
                barMap.put("天猫",decimalFormat.format(increase8 * 100.00));
            }else {
                barMap.put("天猫",0.00);
            }
        }else {
            barMap.put("天猫",0.00);
        }
        return barMap;
    }

    @Override
    public List<ClassifyNameVO> getWordle() {
        LocalDate currentDate = LocalDate.now(); // 获取当前日期
        LocalDate oneMonthAgo = currentDate.minusMonths(1); // 计算半年前的日期
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(oneMonthAgo);
        dateDTO.setEndDate(currentDate);
        List<String> list = heiMaoSubDao.getProblem(dateDTO).stream().filter(item-> StringUtils.isNotBlank(item) && item!=null ).collect(Collectors.toList());
        if (list.size() == 0){
            return null;
        }
        Map<String, Integer>  wordMap = filterWordUtil.filterKeywords(list, 25);
        //classifyNameVo 是用来统计投诉对象的name和value 太懒了不想改名字了
        List<ClassifyNameVO> classifyNameVOS = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : wordMap.entrySet()){
            ClassifyNameVO classifyNameVO = new ClassifyNameVO();
            String word = entry.getKey();
            Integer value = entry.getValue();
            if (",".equals(word) || "/".equals(word) ||"。 ".equals(word) || ";".equals(word) || "?" .equals(word) ){
                continue;
            }
            classifyNameVO.setValue(value);
            classifyNameVO.setName(word);
            classifyNameVOS.add(classifyNameVO);
        }
        return classifyNameVOS;
    }

    @Override
    public List<WeeklyData> getWeeklyData(DateDTO dateDTO) {
        List<WeeklyData> weeklyDataList = heiMaoSubDao.getWeeklyData(dateDTO);
        return weeklyDataList;
    }


}
