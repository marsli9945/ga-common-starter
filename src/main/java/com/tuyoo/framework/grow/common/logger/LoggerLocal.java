package com.tuyoo.framework.grow.common.logger;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@EnableAsync
@AllArgsConstructor
public class LoggerLocal
{
    private final File file;

    public void flash()
    {
        file.delete();
    }

    public void record(List<LoggerEntities> logList)
    {
        try
        {
            ArrayList<LoggerEntities> tmp = new ArrayList<>(logList);
            BufferedWriter tmpBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

            String fileLine;
            for (LoggerEntities loggerEntities : tmp)
            {
                fileLine = JSON.toJSONString(loggerEntities);
                tmpBuffer.write(fileLine);
                tmpBuffer.newLine();
                tmpBuffer.flush();
            }
            tmpBuffer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<LoggerEntities> read()
    {
        ArrayList<LoggerEntities> list = new ArrayList<>();
        if (!file.exists())
        {
            file.mkdir();
            return list;
        }

        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String fileLine;
            while ((fileLine = bufferedReader.readLine()) != null)
            {
                list.add(JSON.parseObject(fileLine, LoggerEntities.class));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }

}
