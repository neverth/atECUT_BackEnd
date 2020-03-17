### atECUT后端

**API  getBooksByTitle    获得图书信息**

GET http://neverth.fun:8080/atecut/restfulApi/library/book/{title}/{pageCount}

EX  http://neverth.fun:8080/atecut/restfulApi/library/book/vue/1/

**API getBookDetailBymarcNo  获得图书馆藏地位置**

GET http://neverth.fun:8080/atecut/restfulApi/library/bookDetail/{marcNo}

EX  http://neverth.fun:8080/atecut/restfulApi/library/bookDetail/53716638667671457a6f4863776950753333756e63673d3d


**API 根据ISBN号获得豆瓣评分等信息**

GET http://neverth.fun:8080/atecut/restfulApi/douBan/bookIsbn/{isbn}

EX http://neverth.fun:8080/atecut/restfulApi/douBan/bookIsbn/9787532125944

> {marcNo}和{isbn}  在getBooksByTitle 这个api返回的参数里面都有，分别是isbn， marcRecNo

**API 成绩接口**

POST http://neverth.fun:8111/score/get

JSONBODY 
```json
{
    "username": "123",
    "password": "XXX",
    "XSBH": "", // 被查询学号
    "XN": "2019", // 学年
    "XQ": "1",  // 学期
    "pageSize": 10,
    "pageNumber": 1
}
```

**API 课表接口**

POST http://neverth.fun:8111/kb/get

JSONBODY 
```json
{
    "username": "123",
    "password": "XXX",
    "XN": "", // 学年
    "XQ": "", // 学期
    "ZC": "", // 周次
}
```

