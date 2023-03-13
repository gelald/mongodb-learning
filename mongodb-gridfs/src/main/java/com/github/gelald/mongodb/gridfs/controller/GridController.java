package com.github.gelald.mongodb.gridfs.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.whereFilename;

/**
 * @author WuYingBin
 * date: 2023/3/4
 */
@Slf4j
@RestController
@RequestMapping("/grid")
public class GridController {
    private GridFsOperations gridFsOperations;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, ObjectId> upload(@RequestPart(value = "file") MultipartFile file) {
        StopWatch stopWatch = new StopWatch("上传文件");
        NumberFormat numberFormat = new DecimalFormat("#.###");
        Map<String, ObjectId> map = new HashMap<>();
        try (InputStream streamForUpload = file.getInputStream()) {
            stopWatch.start();
            ObjectId objectId = gridFsOperations.store(streamForUpload, file.getOriginalFilename(), file.getContentType());
            stopWatch.stop();
            log.info("文件:{}上传成功，文件Id:{}，耗时:{}秒", file.getOriginalFilename(), objectId, numberFormat.format(stopWatch.getTotalTimeSeconds()));
            map.put(file.getOriginalFilename(), objectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //查询并下载文件
    @GetMapping("/download")
    public void download(String filename, HttpServletResponse response) {
        StopWatch stopWatch = new StopWatch("下载文件");
        NumberFormat numberFormat = new DecimalFormat("#.###");
        stopWatch.start();
        //查询文件
        GridFSFile result = gridFsOperations.findOne(query(whereFilename().is(filename)));
        if (result == null) {
            throw new RuntimeException("文件找不到");
        }
        GridFsResource gridFsResource = gridFsOperations.getResource(result);
        String contentType = gridFsResource.getContentType();
        log.info("找到文件:{}，contentType:{}", gridFsResource.getFilename(), contentType);
        response.reset();
        response.setContentType(contentType);
        //注意: 如果没有下面这行设置header, 结果会将文件的内容作为响应的 body 直接输出在页面上, 而不是下载文件
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);  //指定下载文件名
        try (ServletOutputStream outputStream = response.getOutputStream();
             InputStream is = gridFsResource.getInputStream()) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        log.info("文件:{}下载成功，耗时:{}秒", filename, numberFormat.format(stopWatch.getTotalTimeSeconds()));
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("filename") String filename) {
        StopWatch stopWatch = new StopWatch("下载文件");
        NumberFormat numberFormat = new DecimalFormat("#.###");
        stopWatch.start();
        this.gridFsOperations.delete(query(whereFilename().is(filename)));
        stopWatch.stop();
        log.info("文件:{}删除成功，耗时:{}秒", filename, numberFormat.format(stopWatch.getTotalTimeSeconds()));
        return "delete success";
    }

    @Autowired
    public void setGridFsOperations(GridFsOperations gridFsOperations) {
        this.gridFsOperations = gridFsOperations;
    }

}
