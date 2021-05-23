package com.sicmatr1x.util;

import com.sicmatr1x.dao.ArticleDao;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.pojo.ArticleSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 在Springboot启动之后运行特定代码
 */
@Component
public class InitAfterRunUtil implements ApplicationRunner {

    @Autowired
    private ArticleDao articleDao;

    /**
     *
     * @param applicationArguments
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        String url = "127.0.0.1";
        Article testArticle = articleDao.findOneArticleByURL(url);
        if (testArticle == null) {
            testArticle = new Article();
            testArticle.setUrl(url);
            testArticle.setTitle("Test");
            testArticle.setCategory("Test");
            testArticle.setSource(ArticleSource.ZHIHU_ANSWER);
            articleDao.saveArticle(testArticle);
        }
        System.out.println("InitAfterRunUtil:已初始化数据库");
    }
}
