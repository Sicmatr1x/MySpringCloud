package com.sicmatr1x.controller;

import com.sicmatr1x.common.Constant;
import com.sicmatr1x.service.JobService;
import com.sicmatr1x.vo.CommonVo;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sicmatr1x
 */
@RestController
@RequestMapping("job")
@Api(value = "/job")
public class JobController {

    @Autowired
    private Constant constant;

    @Autowired
    private JobService jobService;

    private static final Logger logger = Logger.getLogger(JobController.class);

    /**
     * 检查版本号
     *
     * @return application version
     */
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ApiOperation(
            value = "测试Server接口",
            notes = "获取当前NoteBookServer版本",
            httpMethod = "GET",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    public CommonVo version() {
//        logger.info("/notebook/version");
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("version", constant.getVersion());
        CommonVo response = new CommonVo(true, dataMap);
//        logger.info("response: " + response);
        return response;
    }



}
