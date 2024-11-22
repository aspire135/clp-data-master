package com.yzu.clp.task;

import com.yzu.clp.DATA_PERFORM.service.FileExportService;
import com.yzu.clp.DATA_PERFORM.service.HeiMaoSubService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BulletinTask {
    public static void run() {
        Timer timer = new Timer();
        // 设置任务的执行时间为每个月的固定日期和时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为每个月的第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 设置为上午9点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 如果当前日期已经过了执行时间，则将执行时间设置为下个月的相同日期和时间
        if (calendar.getTime().before(new Date())) {
            calendar.add(Calendar.MONTH, 1);
        }

        // 安排任务
        timer.schedule(new TimerTask() {
            @Resource
            private FileExportService fileExportService;
            @Override
            public void run() {
                try {
                    fileExportService.createBulletinPdf();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (FontFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }, calendar.getTime());
    }
}
