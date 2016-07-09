# dingdingisv

这是一个钉钉 ISV 套件 token 管理工具, 可同时维护多份账号.

## 如何获取一个回调地址

一个回调地址类似 http://youdomain.com/isvReceive/demo  (demo) 为账号名

操作步骤如下

* 注册一个账号
```

curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'Authorization: Bearer null' -d '{
  "password": "demo",
  "username": "demo"
}' 'http://youdomain.com:7001/dingding/reg'

```
现在你现在已拥有一个名叫 demo 的账号.

* 配置 ISV 秘钥

钉钉提供了一些参数用于验证和加密,下面来看如何通过钉钉的  回调URL 验证.

要设置秘钥前,先获取认证秘钥.

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'Authorization: Bearer null' -d '{
  "password": "demo",
  "rememberMe": true,
  "username": "demo"
}' 'http://youdomain.com:7001/dingding/authenticate'

```

```

output:
{
  "errcode": 0,
  "errmsg": "success",
  "data": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6dyIsImF1dGgtZGluZ2RpbmctaXZzMjAxNiMkI0AiOiJST0xFX1VTRVIiLCJleHAiOjE0OTM5NTA4ODh9.XnLUmpCgtNrq46whrt-x5QnXaSglhhINDvG6HPtewtnUIcHNVeAyxpK7NowMzWlZJRTc2DNFrWzpPByPnpv2qQ"
}

```

* 调用设置套件基本信息接口

```
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6dyIsImF1dGgtZGluZ2RpbmctaXZzMjAxNiMkI0AiOiJST0xFX1VTRVIiLCJleHAiOjE0OTM5NTA4ODh9.XnLUmpCgtNrq46whrt-x5QnXaSglhhINDvG6HPtewtnUIcHNVeAyxpK7NowMzWlZJRTc2DNFrWzpPByPnpv2qQ' -d '{
  "suiteEncodingAesKey": "填写自己的",
  "suiteKey": "suite4xxxxxxxxxxxxxxx",
  "suiteSecret": "填写自己的",
  "suiteTicket": "",
  "token": "填写自己的"
}' 'http://youdomain.com:7001/dingding/isvapps'

```

全部设置完成, 点击页面上 验证回到 URL 有效性

## Building for production

To optimize the dingdingisv client for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod


