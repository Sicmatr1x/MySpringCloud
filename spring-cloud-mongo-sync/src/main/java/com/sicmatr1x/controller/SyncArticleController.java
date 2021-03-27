package com.sicmatr1x.controller;

import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.service.SyncArticleService;
import com.sicmatr1x.vo.CommonVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SyncArticleController {

    @Autowired
    SyncArticleService syncArticleService;

    @RequestMapping(value = "/sync/test/mongodbConnection", method = RequestMethod.POST)
    public CommonVo testConnectionToSourceDB(@RequestBody(required = false) Map<String, String> mongodbMap) {
        String result = syncArticleService.testConnectionToSourceDB(mongodbMap);
        return new CommonVo(true, result);
    }

    @RequestMapping(value = "/sync/article/loadArticleDocumentFromDB", method = RequestMethod.GET)
    public CommonVo loadArticleDocumentFromDB() {
        Map<String, String> result = syncArticleService.readArticlesFromDB();
        return new CommonVo(true, result);
    }

    @RequestMapping(value = "/sync/article/margeSourceToTargetDB", method = RequestMethod.GET)
    public CommonVo margeSourceArticlesMargeToTargetDB(@RequestBody Boolean additionalMode) {
        int affectRowNum = syncArticleService.margeSourceArticlesToTargetDB(additionalMode);
        Map<String, Integer> result = new HashMap<>();
        result.put("affectRowNum", affectRowNum);
        return new CommonVo(true, result);
    }

    @RequestMapping(value = "/sync/article/autoMarge", method = RequestMethod.POST)
    @ApiOperation(
            value = "自动备份数据到当前微服务连接数据库",
            notes = "POST body 为备份数据来源信息",
            httpMethod = "POST",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "additionalMode",value = "是否启用追加模式", dataType = "Boolean", defaultValue = "true", required = true),
            @ApiImplicitParam(paramType = "path",name = "mongodbMap",value = "mongoDB连接信息", dataType = "Map<String, String>",
                    defaultValue = "username:NoteBookServer_appln," +
                            "password:Friday5" +
                            "host:127.0.0.1" +
                            "port:27017" +
                            "database:NoteBookServer",
                    required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found")
    })
    public CommonVo autoMargeArticles(@RequestBody Boolean additionalMode, @RequestBody Map<String, String> mongodbMap) {
        if (additionalMode == null) {
            additionalMode = true;
        }
        Map<String, Object> result = syncArticleService.autoMargeArticles(additionalMode, mongodbMap);
        return new CommonVo(true, result);
    }
}
