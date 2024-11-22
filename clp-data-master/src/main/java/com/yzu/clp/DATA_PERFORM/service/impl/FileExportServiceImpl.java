package com.yzu.clp.DATA_PERFORM.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deepoove.poi.data.PictureRenderData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzu.clp.DATA_PERFORM.dto.DateDTO;
import com.yzu.clp.DATA_PERFORM.dto.HeiMaoSubDTO;
import com.yzu.clp.DATA_PERFORM.dto.PeriodDTO;
import com.yzu.clp.DATA_PERFORM.entity.HeiMaoSub;
import com.yzu.clp.DATA_PERFORM.service.FileExportService;
import com.yzu.clp.DATA_PERFORM.service.HeiMaoSubService;
import com.yzu.clp.DATA_PERFORM.service.PromptService;
import com.yzu.clp.DATA_PERFORM.vo.ClassifyNameVO;
import com.yzu.clp.Util.ChartUtil;
import com.yzu.clp.Util.WordUtil;
import io.swagger.models.auth.In;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileExportServiceImpl implements FileExportService {
    @Resource
    private HeiMaoSubService heiMaoSubService;

    @Value("${file.csv.save-path}")
    private String filePath;
    @Value("${file.save-path}")
    private String pdfFilePath;
    @Value("${file.template.html.save-path}")
    private String htmlTemplatePath;

    @Value("${file.image.save-path}")
    private String imageFilePath;

    @Value(("${file.template.word.save-path}"))
    private String wordTemplatePath;

    @Resource
    private PromptService promptService;

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Override
    public void exportToCSV(HeiMaoSubDTO heiMaoSubDTO) {
        try{
            if(heiMaoSubDTO == null){
                return;
            }
            // 设置响应头
            File file = new File(filePath+ DateTime.now()+ ".csv");
            Writer writer = new FileWriter(file);
            QueryWrapper<HeiMaoSub> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("time1", heiMaoSubDTO.getStartTime());
            queryWrapper.le("time1", heiMaoSubDTO.getEndTime());
            List<HeiMaoSub> heiMaoSubList = heiMaoSubService.list(queryWrapper);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            heiMaoSubList.forEach(item->{
                try {
                    csvPrinter.printRecord(item);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createBulletinPdf() throws IOException, FontFormatException {
        Map<String, Object> dataMapPast = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        DefaultPieDataset defaultPieDataset = new DefaultPieDataset();
        String content ="";
        String content1 = "";
        String content3 = "";
        String content4 = "";
        String content5 = "";
        String content2 = "";
        //一个月的区间
        LocalDate currentDate = LocalDate.now(); // 获取当前日期
        LocalDate sixMonthsAgo = currentDate.minusMonths(1); // 计算一个月前的日期
        LocalDate oneYearAgo = sixMonthsAgo.minusMonths(1); // 计算一个月前的日期

//        LocalDate currentDate = LocalDate.of(2024,2,22); // 获取当前日期
//        LocalDate sixMonthsAgo = LocalDate.of(2024,2,1); // 计算半年前的日期
//        LocalDate oneYearAgo = LocalDate.of(2024,1,1); // 计算半年前的日期
        PeriodDTO periodDTO = new PeriodDTO();
        periodDTO.setStartDate(sixMonthsAgo);
        periodDTO.setEndDate(currentDate);

        //获得京东美团等的数据--现在
        List<ClassifyNameVO> classifyNameVOList = heiMaoSubService.classifyNameByDate(periodDTO);

        if (classifyNameVOList.size() == 0){
            content2 = "当月无数据";
        }else {
            //过去同时间段的数据
            periodDTO.setStartDate(oneYearAgo);
            periodDTO.setEndDate(sixMonthsAgo);
            List<ClassifyNameVO> classifyNameVOListPast = heiMaoSubService.classifyNameByDate(periodDTO);
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
            //数据处理
            classifyNameVOList.stream().map(item->{
                if (item.getValue() == null){
                    item.setValue(0);
                }
                return item;
            });
            classifyNameVOList.forEach(item->{
                if ("京东".equals(item.getName())){
                    defaultPieDataset.setValue("京东",item.getValue());
                    dataMap.put("jingdong", item.getValue());
                }else if ("淘宝".equals(item.getName())){
                    defaultPieDataset.setValue("淘宝",item.getValue());
                    dataMap.put("taobao", item.getValue());
                }else if ("美团".equals(item.getName())) {
                    defaultPieDataset.setValue("美团",item.getValue());
                    dataMap.put("meituan", item.getValue());
                }else if ("滴滴".equals(item.getName())){
                    defaultPieDataset.setValue("滴滴",item.getValue());
                    dataMap.put("didi", item.getValue());
                }else if ("抖音".equals(item.getName())){
                    defaultPieDataset.setValue("抖音",item.getValue());
                    dataMap.put("douyin", item.getValue());
                }else if ("天猫".equals(item.getName())){
                    defaultPieDataset.setValue("天猫",item.getValue());
                    dataMap.put("tianmao", item.getValue());
                }else if ("拼多多".equals(item.getName())){
                    defaultPieDataset.setValue("拼多多",item.getValue());
                    dataMap.put("pdd", item.getValue());
                }else if ("饿了么".equals(item.getName())){
                    defaultPieDataset.setValue("饿了么",item.getValue());
                    dataMap.put("eleme", item.getValue());
                }
            });
            //上半年的获得总数数据
            Integer currentSum = heiMaoSubService.countAllByDate(sixMonthsAgo, currentDate);
            dataMap.put("sum", currentSum);
            //上上个半年的总数数据
            Integer pastSum = heiMaoSubService.countAllByDate(oneYearAgo, sixMonthsAgo);
            double increase = 0;
            if (pastSum > 0){
                //计算同比增加
                increase = ((double) (currentSum - pastSum) / pastSum) * 100;
                //同比增加率
            }
            dataMap.put("increase", increase);
            content1 = String.format("上个月,舆情检测系统共收集消费者投诉%d文件, 同比增长%.2f%%。\n",currentSum,increase );
            //第二段内容
            content2 = getIncreaseForName(dataMap, dataMapPast);

            //生成第一第二段内容的饼状图
            createChartCompanyImage(defaultPieDataset, dataMap);
        }

        //获得第三段内容
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(sixMonthsAgo);
        dateDTO.setEndDate(currentDate);
        PeriodDTO periodDTO1 = new PeriodDTO();
        periodDTO1.setStartDate(sixMonthsAgo);
        periodDTO1.setEndDate(currentDate);
        periodDTO1.setStartDatePast(oneYearAgo);
        periodDTO1.setEndDatePast(sixMonthsAgo);
//        PeriodDTO periodForProblem = new PeriodDTO();
//        periodForProblem.setStartDate(sixMonthsAgo);
//        periodForProblem.setEndDate(currentDate);
//        periodForProblem.setStartDatePast(oneYearAgo);
//        periodForProblem.setEndDatePast(sixMonthsAgo);
        content3 = this.getContent3(dateDTO, periodDTO1, dataMap);

        content4 = this.getContent4(dateDTO, periodDTO1, dataMap);
//        //同步传给算法端拿到总结
        content5 = requestPy(content1 + content2 + content3 + content4);
        dataMap.put("content1", content1);
        dataMap.put("content2", content2);
        dataMap.put("content3", content3);
        dataMap.put("content4", content4);
        dataMap.put("content5", content5);
        String date = LocalDate.now().getYear() + "年" + Integer.valueOf(LocalDate.now().getMonthValue()) + "月专报内容";
        dataMap.put("title", date);
        if (StringUtils.isBlank(content2)){
            dataMap.put("nameImage",null);
        }
        if (StringUtils.isBlank(content3)){
            dataMap.put("problemImage",null);
        }
        if (StringUtils.isBlank(content4)){
            dataMap.put("industryImage",null);
        }
        createWord(dataMap);

    }


    /**
     * 拿到第四段内容
     * @param dateDTO
     * @param periodDTO
     * @return
     */
    private String getContent4(DateDTO dateDTO, PeriodDTO periodDTO, Map<String, Object> dataMap) throws IOException, FontFormatException {
        //第四段内容
        String finalFourthContent2 = "";
        String finalFourthContent1 = "";
        Map<String, Integer> industryDataMap = promptService.getIndustryTagChart(dateDTO);
        //生成饼状图
        DefaultPieDataset defaultPieDataset = new DefaultPieDataset();
        // 对 industryDataMap 中的值进行排序（假设值是 Integer 类型）
        List<Map.Entry<String, Integer>> sortedEntriesChart = industryDataMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // 按值降序排序
                .limit(8) // 仅保留前8个值
                .collect(Collectors.toList());

        // 将排序后的前8个数据加入到 dataset 中
        for (Map.Entry<String, Integer> entry : sortedEntriesChart) {
            defaultPieDataset.setValue(entry.getKey(), entry.getValue());
        }
//        industryDataMap.forEach((industry,value)->{
//            defaultPieDataset.setValue(industry, value);
//        });
        createChartIndustryImage(defaultPieDataset, dataMap);

        // 判断是否存在值大于1000的条目
        boolean hasValuesGreaterThan1000 = industryDataMap.values().stream()
                .anyMatch(value -> value > 1000);

        if (hasValuesGreaterThan1000) {
            Map<String, Integer> filteredMap = industryDataMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > 1000)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            StringBuilder thirdContent1 = new StringBuilder("从投诉行业分类来看，投诉量大于1000的行业分类有:");
            filteredMap.forEach((problem, value) -> thirdContent1.append(problem).append(" (").append(value).append("件)、 "));
            finalFourthContent1 = thirdContent1.toString().replaceAll("、 $", "。");
        }
        Map<String, Object>  increaseIndustryDataMap = promptService.getIndustryTagBar(periodDTO);
        // 判断是否存在值大于25%的条目
        //最终的第三段的第二句话
        boolean hasValuesGreaterThan25 = increaseIndustryDataMap.values().stream()
                .anyMatch(value -> {
                    if (value instanceof Double) {
                        return (double) value >= 50 || (double) value <-50;
                    }
                    return false;
                });
        if (hasValuesGreaterThan25) {
            Map<String, Double> filterIncreaseIndustryDataMap = increaseIndustryDataMap.entrySet()
                    .stream()
                    .filter(entry -> {
                        Object value = entry.getValue();
                        if (value instanceof Double) {
                            return (double) value >= 50 || (double) value <-50;
                        }
                        return false;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> (Double) entry.getValue()));
            StringBuilder thirdContent2 = new StringBuilder("同比变化明显的为:");
            filterIncreaseIndustryDataMap.forEach((industry, value) -> thirdContent2.append(industry).append(" (").append(value).append("%)、 "));
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // 对 filterIncreaseIndustryDataMap 中的值进行排序（假设值是 Double 类型）
            List<Map.Entry<String, Double>> sortedEntries = filterIncreaseIndustryDataMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // 按值降序排序
                    .limit(8) // 仅保留前8个值
                    .collect(Collectors.toList());

            // 将排序后的前8个数据加入到 dataset 中
            for (Map.Entry<String, Double> entry : sortedEntries) {
                dataset.addValue(entry.getValue(), entry.getKey(), entry.getKey());
            }
//            filterIncreaseIndustryDataMap.forEach((industry,value)->{
//                dataset.addValue(value,industry, industry);
//            });
            createBarIndustryImage(dataMap,dataset);
            finalFourthContent2 = thirdContent2.toString().replaceAll("、 $", "。");
        }

        return  finalFourthContent1 + finalFourthContent2;
    }

    private String getContent3(DateDTO dateDTO, PeriodDTO periodDTO, Map<String, Object> dataMap) throws IOException, FontFormatException {
        //第三段内容
        String finalThirdContent2 = "";
        String finalThirdContent1 = "";
        Map<String, Integer> problemSumDataMap = promptService.getProblemTagChart(dateDTO);
        //生成饼状图
        DefaultPieDataset defaultPieDataset = new DefaultPieDataset();
        problemSumDataMap.forEach((problem,value)->{
            defaultPieDataset.setValue(problem, value);
        });
        createChartProblemImage(defaultPieDataset, dataMap);
        // 判断是否存在值大于1000的条目
        boolean hasValuesGreaterThan1000 = problemSumDataMap.values().stream()
                .anyMatch(value -> value > 1000);
        if (hasValuesGreaterThan1000) {
            Map<String, Integer> filteredMap = problemSumDataMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > 1000)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            StringBuilder thirdContent1 = new StringBuilder("从投诉问题分类来看，投诉量大于1000的问题分类有:");
            filteredMap.forEach((problem, value) -> thirdContent1.append(problem).append(" (").append(value).append("件)、 "));
            finalThirdContent1 = thirdContent1.toString().replaceAll("、 $", "。");
        }

        Map<String, Object>  increaseProblemDataMap = promptService.getProblemTagBar(periodDTO);
        // 判断是否存在值大于25的条目
        //最终的第三段的第二句话
        boolean hasValuesGreaterThan25 = increaseProblemDataMap.values().stream()
                .anyMatch(value -> {
                    if (value instanceof Double) {
                        return (double) value >= 10 || (double) value < -10;
                    }
                    return false;
                });
        if (hasValuesGreaterThan25) {
            Map<String, Double> filterIncreaseProblemDataMap = increaseProblemDataMap.entrySet()
                    .stream()
                    .filter(entry -> {
                        Object value = entry.getValue();
                        if (value instanceof Double) {
                            return (double) value >= 10 || (double) value < -10;
                        }
                        return false;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> (Double) entry.getValue()));
            StringBuilder thirdContent2 = new StringBuilder("同比变化明显的为:");
            filterIncreaseProblemDataMap.forEach((problem, value) -> thirdContent2.append(problem).append(" (").append(value).append("%)、 "));
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            filterIncreaseProblemDataMap.forEach((problem, value) -> {
                dataset.addValue(value,problem, problem);
            });
            createBarProblemImage(dataMap, dataset);
            finalThirdContent2 = thirdContent2.toString().replaceAll("、 $", "。");
        }
        return  finalThirdContent1 + finalThirdContent2;
    }
    public void  createWord(Map<String, Object> dataMap){
        String fileName = "zb-" + new SimpleDateFormat("yyyyMMdd").format(DateTime.now());
        String templatePath = wordTemplatePath + "template-word.docx";
//        ClassLoader classLoader = getClass().getClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream("templates/template-word.docx");
        String wordPath = WordUtil.createWord(templatePath, pdfFilePath, fileName, dataMap);
    }



    //生成投诉对象饼状图
    public void createChartCompanyImage(DefaultPieDataset defaultPieDataset, Map<String, Object> dataMap) throws IOException, FontFormatException {
        //图片路径和图片名称
        String chartImageName =  imageFilePath+"company-chart"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        //生成图片
        ChartUtil.createPieChart(defaultPieDataset,chartImageName,"饼状图");
        dataMap.put("nameChartImage",new PictureRenderData(600, 350, chartImageName));
    }

    //生成投诉对象柱状图
    public void createBarCompanyImage(Map<String, Object> dataMap, CategoryDataset dataset) throws IOException, FontFormatException {
        //图片路径和图片名称
        String chartImageName =  imageFilePath+"company-bar"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        //生成图片
        ChartUtil.createBarChart(dataset,chartImageName,"柱状图");
        dataMap.put("nameBarImage",new PictureRenderData(600, 350, chartImageName));
    }
    //生成投诉问题分类饼状图
    public void createChartProblemImage(DefaultPieDataset defaultPieDataset, Map<String, Object> dataMap) throws IOException, FontFormatException {
        //图片路径和图片名称
        String chartImageName =  imageFilePath+"problem-chart"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        //生成图片
        ChartUtil.createPieChart(defaultPieDataset,chartImageName,"饼状图");
        dataMap.put("problemChartImage",new PictureRenderData(600, 350, chartImageName));
    }

    //生成投诉问题分类柱状图
    public void createBarProblemImage(Map<String, Object> dataMap, CategoryDataset dataset) throws IOException, FontFormatException {
        //图片路径和图片名称
        String chartImageName =  imageFilePath+"problem-bar"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        //生成图片
        ChartUtil.createBarChart(dataset,chartImageName,"柱状图");
        dataMap.put("problemBarImage",new PictureRenderData(600, 350, chartImageName));
    }

    //生成投诉行业分类饼状图
    public void createChartIndustryImage(DefaultPieDataset defaultPieDataset, Map<String, Object> dataMap) throws IOException, FontFormatException {
        //图片路径和图片名称
        String chartImageName =  imageFilePath+"industry-chart"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        //生成图片
        ChartUtil.createPieChart(defaultPieDataset,chartImageName,"饼状图");
        dataMap.put("industryChartImage",new PictureRenderData(600, 350, chartImageName));
    }
    //生成投诉行业分类柱状图
    public void createBarIndustryImage( Map<String, Object> dataMap, CategoryDataset dataset) throws IOException, FontFormatException {
        //图片路径和图片名称
        String chartImageName =  imageFilePath+"industry-bar"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        //生成图片
        ChartUtil.createBarChart(dataset,chartImageName,"饼状图");
        dataMap.put("industryBarImage",new PictureRenderData(600, 350, chartImageName));
    }

    /**
     * 拿到投诉行业的同比增加率 第二段内容
     * @param dataMap
     * @param dataMapPast
     * @return
     */
    public String getIncreaseForName( Map<String, Object> dataMap,  Map<String, Object> dataMapPast) throws IOException, FontFormatException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String nameContent = "";
        //计算行业同比增加
        //拼接字符串
        String jingdongIncrease ="";
        String taobaoIncrease ="";
        String pddIncrease ="";
        String tianmaoIncrease ="";
        String didiIncrease ="";
        String meituanIncrease ="";
        String elemeIncrease ="";
        String douyinIncrease ="";
        if (dataMapPast.get("jingdong") != null && dataMap.get("jingdong")!= null && Double.parseDouble(dataMapPast.get("jingdong").toString()) >0 ){
            double increase1 = (Double.parseDouble(dataMap.get("jingdong").toString()) - Double.parseDouble(dataMapPast.get("jingdong").toString()))
                    / Double.parseDouble(dataMapPast.get("jingdong").toString()) * 100;
            if (increase1 >10 || increase1 <-10 ){
                jingdongIncrease = String.format("，京东同比变化为%.2f%%",increase1  );
                dataMap.put("jingdongincrease", jingdongIncrease);
                dataset.addValue(increase1, "京东", "同比变化率");
            }
        }
        if (dataMapPast.get("taobao") != null && dataMap.get("taobao")!= null && Double.parseDouble(dataMapPast.get("taobao").toString()) >0 ){
            double increase2 = ((Double.parseDouble(dataMap.get("taobao").toString()) - Double.parseDouble(dataMapPast.get("taobao").toString())  )
                    / Double.parseDouble(dataMapPast.get("taobao").toString())) * 100 ;
            if (increase2 >10 || increase2 <-10 ){
                taobaoIncrease = String.format("，淘宝同比变化为%.2f%%", increase2 );
                dataMap.put("taobaoincrease", taobaoIncrease);
                dataset.addValue(increase2, "淘宝", "同比变化率");
            }
        }
        if (dataMapPast.get("pdd") != null && dataMap.get("pdd")!= null && Double.parseDouble(dataMapPast.get("pdd").toString()) >0 ){
            double increase3 = ((Double.parseDouble(dataMap.get("pdd").toString()) - Double.parseDouble(dataMapPast.get("pdd").toString())  )
                    / Double.parseDouble(dataMapPast.get("pdd").toString())) * 100;
            if (increase3 >10 || increase3 <-10 ){
                pddIncrease = String.format("，拼多多同比变化为%.2f%%", increase3 );
                dataset.addValue(increase3, "拼多多", "同比变化率");
                dataMap.put("pddincrease", pddIncrease);
            }
        }
        if (dataMapPast.get("meituan") != null && dataMap.get("meituan")!= null && Double.parseDouble(dataMapPast.get("meituan").toString()) >0 ){
            double increase4 = ((Double.parseDouble(dataMap.get("meituan").toString()) - Double.parseDouble(dataMapPast.get("meituan").toString())  )
                    / Double.parseDouble(dataMapPast.get("meituan").toString()))  * 100;
            if (increase4 >10 || increase4 <-10 ){
                meituanIncrease = String.format("，美团同比变化为%.2f%%", increase4 );
                dataset.addValue(increase4, "美团", "同比变化率");
                dataMap.put("meituanincrease", meituanIncrease);
            }
        }
        if (dataMapPast.get("douyin") != null && dataMap.get("douyin")!= null && Double.parseDouble(dataMapPast.get("douyin").toString()) >0 ){
            double increase5 = ((Double.parseDouble(dataMap.get("douyin").toString()) - Double.parseDouble(dataMapPast.get("douyin").toString())  )
                    / Double.parseDouble(dataMapPast.get("douyin").toString())) * 100 ;
            if (increase5 >10 || increase5 <-10 ){
                douyinIncrease = String.format("，抖音同比变化为%.2f%%", increase5 );
                dataset.addValue(increase5, "抖音", "同比变化率");
                dataMap.put("douyinincrease", douyinIncrease);
            }
        }
        if (dataMapPast.get("eleme") != null && dataMap.get("eleme")!= null && Double.parseDouble(dataMapPast.get("eleme").toString()) >0 ){
            double increase6 = ((Double.parseDouble(dataMap.get("eleme").toString()) - Double.parseDouble(dataMapPast.get("eleme").toString())  )
                    / Double.parseDouble(dataMapPast.get("eleme").toString())) * 100;
            if (increase6 >10 || increase6 <-10  ){
                elemeIncrease = String.format("，饿了么同比变化为%.2f%%", increase6 );
                dataset.addValue(increase6, "饿了么", "同比变化率");
                dataMap.put("elemeincrease", elemeIncrease);
            }
        }
        if (dataMapPast.get("didi") != null && dataMap.get("didi")!= null && Double.parseDouble(dataMapPast.get("didi").toString()) >0 ){
            double increase7 = ((Double.parseDouble(dataMap.get("didi").toString()) - Double.parseDouble(dataMapPast.get("didi").toString())  )
                    / Double.parseDouble(dataMapPast.get("didi").toString())) * 100;
            if (increase7 >10 || increase7 <-10 ){
                didiIncrease = String.format("，滴滴同比变化为%.2f%%", increase7 );
                dataMap.put("didiincrease", didiIncrease);
                dataset.addValue(increase7, "滴滴", "同比变化率");
            }
        }
        if (dataMapPast.get("tianmao") != null && dataMap.get("tianmao")!= null && Double.parseDouble(dataMapPast.get("tianmao").toString()) >0 ){
            double increase8 = ((Double.parseDouble(dataMap.get("tianmao").toString()) - Double.parseDouble(dataMapPast.get("tianmao").toString())  )
                    / Double.parseDouble(dataMapPast.get("tianmao").toString())) * 100;
            if (increase8 >10 || increase8 <-10 ){
                tianmaoIncrease = String.format("，天猫同比变化为%.2f%%", increase8 );
                dataMap.put("tianmaoincrease", tianmaoIncrease);
                dataset.addValue(increase8, "天猫", "同比变化率");
            }
        }

        nameContent = String.format("从投诉行业看，其中京东为%d件，淘宝为%d件，拼多多为%d件，美团为%d件，抖音为%d件，饿了么为%d件，滴滴为%d件，天猫为%d件"
                        , Integer.parseInt(dataMap.get("jingdong") == null ? "0" : dataMap.get("jingdong").toString())
                ,Integer.parseInt(dataMap.get("taobao") == null ? "0": dataMap.get("taobao").toString())
                , Integer.parseInt(dataMap.get("pdd") == null ? "0": dataMap.get("pdd").toString())
                , Integer.parseInt(dataMap.get("meituan") == null ? "0": dataMap.get("meituan").toString())
                ,Integer.parseInt(dataMap.get("douyin") == null ? "0": dataMap.get("douyin").toString())
                ,Integer.parseInt(dataMap.get("eleme") == null ? "0": dataMap.get("eleme").toString())
                ,Integer.parseInt(dataMap.get("didi") == null ? "0": dataMap.get("didi").toString())
                ,Integer.parseInt(dataMap.get("tianmao") == null ? "0": dataMap.get("tianmao").toString())
        )
                + jingdongIncrease + taobaoIncrease + pddIncrease + meituanIncrease + douyinIncrease + elemeIncrease + didiIncrease + tianmaoIncrease + "。";

        //生成柱状图
        createBarCompanyImage(dataMap,dataset);
        return nameContent;
    }

    public String requestPy(String content){

        RestTemplate restTemplate=new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


    // 发送GET请求
        String url = "http://localhost:5000/summary_generation";

        // 设置请求体参数
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("input_content", content);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            // 创建JsonReader对象

            try {
                // 创建ObjectMapper对象
                ObjectMapper objectMapper = new ObjectMapper();
                // 解析JSON对象字符串
                JsonNode jsonObject = objectMapper.readTree(responseBody);

                // 获取"message"的值
                String message = jsonObject.get("message").asText();
                // 处理响应结果
                return message;
            }catch (Exception e){
                throw  new RuntimeException("错误");
            }
        } else {
            // 处理错误情况
            return null;
        }

    }

}
