package com.yzu.clp.DATA_PERFORM.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.data.PictureRenderData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzu.clp.DATA_PERFORM.dao.TemporaryPromptDao;
import com.yzu.clp.DATA_PERFORM.dto.TemporaryPromptDTO;
import com.yzu.clp.DATA_PERFORM.entity.HeiMaoSub;
import com.yzu.clp.DATA_PERFORM.entity.Task;
import com.yzu.clp.DATA_PERFORM.entity.TemporaryPrompt;
import com.yzu.clp.DATA_PERFORM.service.HeiMaoSubService;
import com.yzu.clp.DATA_PERFORM.service.TaskService;
import com.yzu.clp.DATA_PERFORM.service.TemporaryPromptService;
import com.yzu.clp.DATA_PERFORM.vo.PromptTaskVO;
import com.yzu.clp.Util.ChartUtil;
import com.yzu.clp.Util.WordUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemporaryPromptImpl extends ServiceImpl<TemporaryPromptDao,TemporaryPrompt> implements TemporaryPromptService {


    @Value(("${file.template.word.save-path}"))
    private String wordTemplatePath;
    @Value("${file.result.save-path}")
    private String pdfFilePath;
    @Resource
    private TaskService taskService;

    @Value("${file.csv.save-path}")
    private String filePath;

    @Value("${file.image.save-path}")
    private String imageFilePath;

    @Resource
    private HeiMaoSubService heiMaoSubService;


    /**
     * 保存临时任务列表到数据库 并且异步请求算法端
     * @param temporaryPromptDTO
     */
    @Override
    public void temporaryPrompt(TemporaryPromptDTO temporaryPromptDTO) {
        //保存任务列表 和临时任务
        if(temporaryPromptDTO == null){
            return;
        }
        Task task = new Task();
        task.setStatus(0);
        task.setStartTime(new DateTime());
        taskService.save(task);
//        List<TemporaryPrompt> temporaryPromptList = temporaryPromptDTO.getTemporaryPromptList();
//        temporaryPromptList.forEach(item->{
//            item.setTaskId(task.getId());
//            this.save(item);
//        });

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String fileName = now.format(formatter) + ".csv";
        //生成文件 获得文件名
        try{
            // 设置响应头
            File file = new File(filePath+ fileName);
            Writer writer = new FileWriter(file);
            QueryWrapper<HeiMaoSub> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("time1", temporaryPromptDTO.getStartTime());
            queryWrapper.le("time1", temporaryPromptDTO.getEndTime());
            List<HeiMaoSub> heiMaoSubList =  heiMaoSubService.list(queryWrapper);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            heiMaoSubList.forEach(item->{
                try {
                    csvPrinter.printRecord(item.getName(),item.getProblemTags(),item.getIndustryTags(),item.getD1());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PromptTaskVO promptTaskVO = new PromptTaskVO();
        promptTaskVO.setTemporaryPromptList(temporaryPromptDTO.getTemporaryPromptList());
        promptTaskVO.setTaskId(task.getId());
        promptTaskVO.setFileName(fileName);
        try{
            this.excute(promptTaskVO);

        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void createTemporaryTaskBulletin(Map<String, Object> map, Map<String, Object> map1, Map<String, Object> map2, Map<String, Object> map3, Map<String, Object> map4) throws IOException, FontFormatException {
        Map<String, Object> dataMap = new HashMap<>();
        String date = "临时专报内容";
        String content="";
        String content1="";
        String content2="";
        String content3="";
        String content4="";
        //第一段
            //字段填充
        if (map.containsKey("总数") && map.get("总数") != null) {
            // 使用String.valueOf方法将值转换为字符串，避免空指针异常
            content = String.format("本次临时专报任务投诉量总计%s件", String.valueOf(map.get("总数")));
        }
            // 在这里使用content变量进行后续操作
        //第二段
        StringBuilder thirdContent = new StringBuilder("从投诉对象分类来看，临时任务投诉对象情况:");
        map1.forEach((problem, value) -> thirdContent.append(problem).append(" (").append(value).append("件)、 "));
        content1 = thirdContent.toString().replaceAll("、 $", "。");
        createImage(map1,dataMap,"nameImage");
        //第三段
        StringBuilder thirdContent1 = new StringBuilder("从投诉问题分类来看，临时任务投诉问题情况:");
        map2.forEach((problem, value) -> thirdContent1.append(problem).append(" (").append(value).append("件)、 "));
        content2 = thirdContent1.toString().replaceAll("、 $", "。");
        createImage(map2,dataMap,"problemImage");
        //第四段
        StringBuilder thirdContent2 = new StringBuilder("从投诉行业分类来看，临时任务投诉行业情况:");
        map3.forEach((problem, value) -> thirdContent2.append(problem).append(" (").append(value).append("件)、 "));
        content3 = thirdContent1.toString().replaceAll("、 $", "。");
        createImage(map3,dataMap,"industryImage");

        content4 = requestPy(content +content1 + content2 + content3 );
        dataMap.put("title", date);
        dataMap.put("content" , content);
        dataMap.put("content1" , content1);
        dataMap.put("content2" , content2);
        dataMap.put("content3" , content3);
        dataMap.put("content4" , content4);


        String fileName = (String) map4.get("文件名");
        String templatePath = wordTemplatePath + "temp-template-word.docx";
//        String templatePath = "/templates/" + "temp-template-word.docx";

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("templates/temp-template-word.docx");
        String wordPath = WordUtil.createWord(templatePath, pdfFilePath, fileName, dataMap);

    }

    @Async
    public void excute(PromptTaskVO promptTaskVO) throws Exception {
        WebClient webClient = WebClient.create();
        String url = "localhost:5000/predict";
        webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promptTaskVO)
                .exchange()
                .subscribe();
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


    //生成投诉对象饼状图
    public void createImage(Map<String, Object> map, Map<String, Object> dataMap,  String imageKey) throws IOException, FontFormatException {
        DefaultPieDataset defaultPieDataset = new DefaultPieDataset();
        map.forEach((k,v)->{
            defaultPieDataset.setValue(k,(Integer)v);
        });
        //图片路径和图片名称
        String chartImageName =  imageFilePath + imageKey+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        //生成图片
        ChartUtil.createPieChart(defaultPieDataset,chartImageName,"饼状图");
        dataMap.put(imageKey ,new PictureRenderData(600, 350, chartImageName));
    }


}
