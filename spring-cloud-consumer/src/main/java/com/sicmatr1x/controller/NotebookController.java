package com.sicmatr1x.controller;

import com.sicmatr1x.common.Constant;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.pojo.ArticleSource;
import com.sicmatr1x.service.ArticleService;
import com.sicmatr1x.service.SpiderService;
import com.sicmatr1x.vo.CommonVo;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sicmatr1x
 */
@RestController
@RequestMapping("notebook")
@Api(value = "/notebook")
public class NotebookController {

    @Autowired
    private Constant constant;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private ArticleService articleService;

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = Logger.getLogger(NotebookController.class);

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

    /**
     * 添加知乎回答
     *
     * @return is add job success
     */
    @RequestMapping(value = "/zhihu/question/{questionId}/answer/{answerId}", method = RequestMethod.GET)
    @ApiOperation(
            value = "添加知乎回答",
            notes = "爬取并添加知乎回答到数据库",
            httpMethod = "GET",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "questionId",value = "知乎问题ID",dataType = "String",defaultValue = "389055663",required = true),
            @ApiImplicitParam(paramType = "path",name = "answerId",value = "知乎回答ID",dataType = "String",defaultValue = "1229966539",required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found")
    })
    public CommonVo spiderZhihuAnswer(@PathVariable String questionId, @PathVariable String answerId) {
        CommonVo response = new CommonVo(false);
        Article article = new Article();
        article.setUrl(spiderService.getDomainURLByName("zhihu") + "question/" + questionId + "/answer/" + answerId);
        article.setSource(ArticleSource.ZHIHU_ANSWER);
        Article resultArticle = null;
        try {
            resultArticle = spiderService.spiderZhihuAnswer(article);
            response.setSuccess(true);
            // 避免返回body过大
            resultArticle.setBody(resultArticle.getBody().substring(0, 400) + "......");
            response.setData(resultArticle);
        } catch (IOException e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    private boolean isZhihuZhuanlan(String url) {
        return url.contains("zhuanlan");
    }

    /**
     * 添加知乎回答
     *
     * @return is add job success
     */
    @RequestMapping(value = "/add/zhihu/answer", method = RequestMethod.POST)
    @ApiOperation(
            value = "添加知乎回答",
            notes = "爬取并添加知乎回答到数据库",
            httpMethod = "POST",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "url",value = "知乎问题URL",dataType = "String",defaultValue = "https://www.zhihu.com/question/389055663/answer/1229966539",required = true)
    })
    public CommonVo spiderZhihuAnswer2(@RequestParam String url) {
        CommonVo response = new CommonVo(false);
        Article article = new Article();
        String[] work = url.split("\\?");
        article.setUrl(work[0]);
        Article resultArticle = null;
        try {
            if (isZhihuZhuanlan(url)) {
                article.setSource(ArticleSource.ZHIHU_ZHUANLAN);
                resultArticle = spiderService.spiderZhihuZhuanLan(article);
            } else {
                article.setSource(ArticleSource.ZHIHU_ANSWER);
                resultArticle = spiderService.spiderZhihuAnswer(article);
            }
            response.setSuccess(true);
            // 避免返回body过大
            if (resultArticle.getBody() != null) {
                resultArticle.setBody(resultArticle.getBody().substring(0, 400) + "......");
            } else {
                response.setSuccess(false);
                response.setErrorMessage("Article body is empty. 文章内容为空。");
            }
            response.setData(resultArticle);
        } catch (IOException e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 添加文字笔记
     *
     * @return is add job success
     */
    @RequestMapping(value = "/add/textNote", method = RequestMethod.POST)
    @ApiOperation(
            value = "添加文字笔记",
            notes = "添加文字笔记到数据库",
            httpMethod = "POST",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "text",value = "知乎问题URL",dataType = "String",defaultValue = "hello test test test",required = true)
    })
    public CommonVo addTextNote(@RequestParam String text) {
        CommonVo response = new CommonVo(false);
        Article article = new Article();
        if (StringUtils.isEmpty(text)) {
            response.setErrorMessage("textContent is empty");
            return response;
        }
        Article resultArticle = null;
        try {
            List<Article> list = articleService.searchArticles(text, "body", 0, 1);
            if (!list.isEmpty()) {
                response.setErrorMessage("textContent already exist!");
                response.setData(list.get(0));
                return response;
            }

            article.setCategory("TextNote");
            String body = "<p>" + text + "</p>";
            article.setBody(body);
            String title = text.length() > 100 ? text.substring(0, 100) + "..." : text;
            article.setTitle(title);
            article.setCreatedTime(new Date());
            boolean isSuccess = articleService.addSave(article);
            response.setSuccess(isSuccess);
            response.setData(article);
        } catch (IOException e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 添加知乎文章
     *
     * @return is add job success
     */
    @RequestMapping(value = "/add/zhihu/p", method = RequestMethod.POST)
    @ApiOperation(
            value = "添加知乎文章",
            notes = "爬取并添加知乎文章到数据库",
            httpMethod = "POST",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "url",value = "知乎文章URL",dataType = "String",defaultValue = "https://zhuanlan.zhihu.com/p/136254608",required = true)
    })
    public CommonVo spiderZhihuZhuanLan(@RequestParam String url) {
        CommonVo response = new CommonVo(false);
        Article article = new Article();
        String[] work = url.split("\\?");
        article.setUrl(work[0]);
        article.setSource(ArticleSource.ZHIHU_ZHUANLAN);
        Article resultArticle = null;
        try {
            resultArticle = spiderService.spiderZhihuZhuanLan(article);
            response.setSuccess(true);
            // 避免返回body过大
            resultArticle.setBody(resultArticle.getBody().substring(0, 400) + "......");
            response.setData(resultArticle);
        } catch (IOException e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 添加虎嗅文章
     *
     * @return is add job success
     */
    @RequestMapping(value = "/add/huxiu/article", method = RequestMethod.POST)
    @ApiOperation(
            value = "添加虎嗅文章",
            notes = "爬取并添加虎嗅文章到数据库",
            httpMethod = "POST",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "url",value = "虎嗅文章URL",dataType = "String",defaultValue = "https://www.huxiu.com/article/378159.html",required = true)
    })
    public CommonVo spiderHuxiu(@RequestParam String url) {
        CommonVo response = new CommonVo(false);
        Article article = new Article();
        String[] work = url.split("\\?");
        article.setUrl(work[0]);
        article.setSource(ArticleSource.HUXIU);
        Article resultArticle = null;
        try {
            resultArticle = spiderService.spiderHuxiu(article);
            response.setSuccess(true);
            // 避免返回body过大
            resultArticle.setBody(resultArticle.getBody().substring(0, 400) + "......");
            response.setData(resultArticle);
        } catch (IOException e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 根据URL查找article
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    @ApiOperation(
            value = "根据URL查找article",
            notes = "根据URL查找article",
            httpMethod = "GET",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "url",value = "URL",dataType = "String",defaultValue = "https://www.huxiu.com/article/378159.html",required = true)
    })
    public String findOneArticleByUrl(@RequestParam String url) throws IOException {
        Article article = null;
        String[] work = url.split("\\?");
        article = articleService.findOneArticleByURL(work[0]);
        if (article == null) {
            CommonVo response = new CommonVo(true);
            response.setErrorMessage("Not found any result in DB");
            return objectMapper.writeValueAsString(response);
        } else {
            return "<html>\n" + article.getBody() + "\n</html>";
        }
    }

    @RequestMapping(value = "/article/delete", method = RequestMethod.GET)
    @ApiOperation(
            value = "根据URL删除第一个匹配的article",
            notes = "根据URL删除第一个匹配的article",
            httpMethod = "GET",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "url",value = "URL",dataType = "String",defaultValue = "https://www.huxiu.com/article/378159.html",required = true)
    })
    public CommonVo deleteOneArticleByUrl(@RequestParam String url) throws IOException {
        CommonVo response = new CommonVo(false);
        Article article = null;
        String[] work = url.split("\\?");
        article = articleService.deleteOneArticleByURL(work[0]);
        if (article == null) {
            response.setErrorMessage("Not found any result in DB");
            return response;
        } else {
            response.setData(article);
            response.setSuccess(true);
            return response;
        }
    }

    /**
     * 返回最近创建的n个笔记
     * @param number 正整数若为空则默认为3
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/recently/articles", method = RequestMethod.GET)
    @ApiOperation(value = "查询最近创建的n个笔记", notes = "查询并返回最近创建的n个笔记")
    @ApiImplicitParam(paramType = "query", name = "number", value = "3", required = false, dataType = "Integer")
    @ApiResponse(code = 200, message = "最近创建的n个笔记", response = String.class)
    public CommonVo findRecentlyArticles(@RequestParam(required = false) Integer number) throws IOException {
        CommonVo response = new CommonVo(false);
        List<Article> list = articleService.findRecentlyArticles(number);
        if (list == null) {
            response.setErrorMessage("Not found any result in DB");
        } else {
            response.setSuccess(true);
            response.setData(list);
        }
        return response;
    }

    /**
     * 模糊搜索笔记
     * @param keyword 关键字
     * @param type 关键字
     * @param pageBegin
     * @param pageSize
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/articles/search", method = RequestMethod.GET)
    @ApiOperation(value = "查询最近创建的n个笔记", notes = "查询并返回最近创建的n个笔记")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "房价", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "title", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "number", value = "3", required = false, dataType = "Integer")
    })
    @ApiResponse(code = 200, message = "最近创建的n个笔记", response = String.class)
    public CommonVo searchArticles(@RequestParam(required = true) String keyword,
                                   @RequestParam(required = true) String type,
                                   @RequestParam(required = false) Integer pageBegin,
                                   @RequestParam(required = false) Integer pageSize) throws IOException {
        CommonVo response = new CommonVo(false);
        List<Article> list = articleService.searchArticles(keyword, type, pageBegin, pageSize);
        if (list == null) {
            response.setErrorMessage("Not found any result in DB");
        } else {
            response.setSuccess(true);
            response.setData(list);
        }
        return response;
    }

}
