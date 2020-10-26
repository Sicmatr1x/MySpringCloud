
## install

### MongoDB

```
use NoteBookServer
db.createUser({user:'NoteBookServer_appln',pwd:'Friday5',roles:[{role:'readWrite',db:'NoteBookServer'}]})
```

## How to use

spring-cloud-consumer: 8090
spring-cloud-eureka: 8091
spring-cloud-mongo-producer: 8092

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
 