
## Spring Version

- Spring Boot: 1.5.9.RELEASE
- Spring Cloud: Edgware.SR1

## Project Structure

WebPage: http://localhost:8090

Server:
1. spring-cloud-eureka
  - port: 8031
  - description: Spring Cloud eureka发现中心
2. spring-cloud-common
  - port: N/A
  - description: 放一些common的类与POJO等
3. [spring-cloud-mongo-producer](http://localhost:8092/swagger-ui.html)
  - port: 8032
  - description: 用于连接mongoDB
4. [spring-cloud-consumer](http://localhost:8090/swagger-ui.html)
  - port: 8030
  - description: 提供控制层访问
5. [spring-cloud-spider](http://localhost:8093/swagger-ui.html)
  - port: 8033
  - description: 提供网页爬取服务
  - 接入[Arthas](https://arthas.aliyun.com/doc/quick-start.html)
    - HTTP: http://127.0.0.1:8563/
6. [spring-cloud-mongo-sync](http://localhost:8094/swagger-ui.html)
  - port: 8034
  - description: 用于连到待备份的该项目注册中心上开始同步mongoDB数据到本地mongoDB
7. [spring-cloud-job](http://localhost:8095/swagger-ui.html)
  - port: 8035
  - description: 定时任务

## install

### Init MongoDB

STEP 1: Download and install [MongoDB Community](https://www.mongodb.com/try/download/community)

STEP 2: Run MongoDB service: cd to your mongo/bin folder

For windows
```cmd
mongod  --dbpath "D:\Program Files\MongoDB\Server\4.4\data"
```

For Mac
```bash
sudo chmod +x /Users/sicmatrix/dev/mongodb-macos-x86_64-4.4.26/bin/mongod
./mongod  --dbpath "/Users/sicmatrix/dev/MongoDB/Server/4.4/data"
```

STEP 3: Test MongoDB service status, open `localhost:27017` in browser.

STEP 4: Create Collection and documents which this project is need: cd to your mongo/bin folder

STEP 5: open MongoDBCompass.exe and connect to your db

```
mongodb://localhost:27017
```

STEP 6: create table

```shell
mongo
> use NoteBookServer
> db.createUser({user:'NoteBookServer_appln',pwd:'Friday5',roles:[{role:'readWrite',db:'NoteBookServer'}]})
```

STEP 7: Run spring-cloud-mongo-producer service, it will init document after springboot is start up

### Deploy in Docker

```
docker run -itd -p 8030:8030 -p 8031:8031 -p 8032:8032 -p 8033:8033 -p 8034:8034 -p 8035:8035 -v /Users/sicmatrix/dev/docker/MySpringCloudData:/data --name centOS-MySpringCloud centos:latest
```
参数说明：
- `-itd`
  - `i` 让 docker 分配一个伪终端并绑定到容器的标准输入上
  - `t` 让容器的标准输入保持打开
  - `d` 让容器后台运行
- `--name` 给容器命名，否则就是系统随机生成的一个名字，不好记，这里我们给他命名叫：centOS-MySpringCloud
- `-p [宿主机端口]:[容器端口]` 指定宿主机与容器之间的端口映射，我这里使用的是 9001 对应容器的 9000 端口
- `-v [宿主机目录]:[容器目录]` 挂载一个宿主机的目录，这里我挂载的是 php 代码，这样我们在宿主机修改 php 代码时，就可以同步到容器中了。其实 php-fpm 的配置文件相关的也可以使用 -v 参数来挂载，省的每次修改都进入容器里改了，这个示例我没加，感兴趣的同学可以下去自己加一下。


### run jar

```
java -jar spring-cloud-eureka.jar

Error: A JNI error has occurred, please check your installation and try again
Exception in thread "main" java.lang.SecurityException: Invalid signature file digest for Manifest main attributes
```



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
 

