
## Spring Version

- Spring Boot: 1.5.9.RELEASE
- Spring Cloud: Edgware.SR1

## Project Structure

Server:
1. spring-cloud-eureka
  - port: 8091
  - description: Spring Cloud eureka发现中心
2. spring-cloud-common
  - port: N/A
  - description: 放一些common的类与POJO等
3. spring-cloud-mongo-producer
  - port: 8092
  - description: 用于连接mongoDB
4. spring-cloud-consumer
  - port: 8090
  - description: 提供控制层访问
5. spring-cloud-spider
  - port: 8093
  - description: 提供网页爬取服务
6. spring-cloud-mongo-sync
  - port: 8094
  - description: 用于连到待备份的该项目注册中心上开始同步mongoDB数据到本地mongoDB

## install

### Init MongoDB

STEP 1: Download and install [MongoDB Community](https://www.mongodb.com/try/download/community)

STEP 2: Run MongoDB service: cd to your mongo/bin folder

```
mongod  --dbpath "D:\Program Files\mongodb-windows-x86_64-4.4.4\data\db"
```

STEP 3: Test MongoDB service status, open `localhost:27017` in browser.

STEP 4: Create Collection and documents which this project is need: cd to your mongo/bin folder

```
mongo
> use NoteBookServer
> db.createUser({user:'NoteBookServer_appln',pwd:'Friday5',roles:[{role:'readWrite',db:'NoteBookServer'}]})
```

STEP 5: Run spring-cloud-mongo-producer service, it will init document after springboot is start up

## How to use



### webpage

- Spring Cloud eureka 发现中心: http://localhost:8091/
- Swagger2 UI: http://localhost:8090/swagger-ui.html

### API

#### General API

- Test NoteBookServer running status or check version
  - URL: /notebook/version
  - Method: GET
  - examples: http://localhost:8090/notebook/version


#### Zhihu website com.sicmatr1x.spider

- (RESTful) add zhihu answer to notebook
  - URL: /notebook/zhihu/question/{questionId}/answer/{answerId}
  - Method: GET
  - examples: http://localhost:8090/notebook/zhihu/question/389055663/answer/1229966539

- add zhihu answer to notebook
  - URL: /notebook/add/zhihu/answer
  - Method: POST
  - body:
    - url: zhihu answer url, like: https://www.zhihu.com/question/389055663/answer/1229966539
  - examples: http://localhost:8090/notebook/add/zhihu/answer
  
- add zhihu zhuanlan article to notebook
  - URL: /notebook/add/zhihu/p
  - Method: POST
  - body:
    - url: zhihu answer url, like: https://zhuanlan.zhihu.com/p/136254608
  - examples: http://localhost:8090/notebook/add/zhihu/p
  
- add huxiu article to notebook
  - URL: /notebook/add/huxiu/article
  - Method: POST
  - body:
    - url: zhihu answer url, like: https://www.huxiu.com/article/375386.html
  - examples: http://localhost:8090/notebook/add/huxiu/article
  
- get note by url
  - URL: /notebook/article
  - Method: GET
  - params:
    - url: note url, like: https://www.zhihu.com/question/279255504/answer/1234844708
  - examples: http://localhost:8090/notebook/article?url=https://www.zhihu.com/question/279255504/answer/1234844708
  
- find recently articles
  - URL: /notebook/recently/articles
  - Method: GET
  - params:
    - number: recently number notes, like: http://localhost:8090/notebook/recently/articles?number=2
  - examples: http://localhost:8090/notebook/recently/articles?number=2
 