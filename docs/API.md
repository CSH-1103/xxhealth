# 熙心健康体检 · 用户端接口文档 v1

> 前后端联调以此文档为准。所有字段名**小写**，与数据库实体保持一致。
> 负责人：曲奇饼（账号/医院/套餐）、怿安（预约/报告/日历）

## 通用约定

| 项 | 约定 |
|---|---|
| Base URL | `http://localhost:8080` |
| 请求方式 | 全部 `POST`，`Content-Type: application/json` |
| 返回结构 | `{ "code": 200, "message": "操作成功", "data": ... }` |
| 成功 | `code = 200` |
| 失败 | `code = 500`，`message` 为中文提示 |
| 日期格式 | `yyyy-MM-dd`（如 `2026-11-06`） |
| 金额单位 | 元（整数） |
| 空结果 | `data` 为 `[]` 或 `null`，不返回 500 |

前端 axios 统一封装后，业务数据取 `res.data.data`。

---

## 1. 账号模块（曲奇饼）

### 1.1 登录

`POST /api/users/login`

请求：

```json
{ "userid": "13800000001", "password": "123456" }
```

成功返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userid": "13800000001",
    "password": "123456",
    "realname": "张伟",
    "sex": 1,
    "identitycard": "210102199001011234",
    "birthday": "1990-01-01",
    "usertype": 1
  }
}
```

失败：`{"code":500,"message":"手机号或密码错误","data":null}`

### 1.2 手机号查重

`POST /api/users/check`

请求：`{ "userid": "13800000001" }`

返回：`data` 为该用户对象；**未注册时 `data` 为 `null`**。

### 1.3 注册

`POST /api/users/register`

请求：

```json
{
  "userid": "13800000003",
  "password": "123456",
  "realname": "王强",
  "sex": 1,
  "identitycard": "210102199801011234",
  "birthday": "1998-01-01",
  "usertype": 1
}
```

返回：`data = 1` 表示成功；`code=500, message="该手机号已注册"` 表示重复。

---

## 2. 医院模块（曲奇饼）

### 2.1 医院列表

`POST /api/hospital/list`

请求：`{ "state": 1 }`（只查正常营业的医院）

返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "hpid": 1,
      "name": "熙心健康体检中心·和平院区",
      "picture": "",
      "telephone": "024-88880001",
      "address": "沈阳市和平区南京北街 1 号",
      "businesshours": "07:30-11:30",
      "deadline": "10:00",
      "rule": "每天限20人",
      "state": 1
    }
  ]
}
```

### 2.2 医院详情

`POST /api/hospital/detail`

请求：`{ "hpid": 1 }`

返回：`data` 为单个医院对象。

---

## 3. 套餐模块（曲奇饼）

### 3.1 套餐列表

`POST /api/setmeal/list`

请求：`{ "type": 1 }`（1 男士 / 0 女士；传 `null` 查全部）

返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    { "smid": 2, "name": "中青年体检套餐", "type": 1, "price": 599 }
  ]
}
```

### 3.2 套餐详情（含检查项）

`POST /api/setmeal/detail`

请求：`{ "smid": 2 }`

返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "smid": 2,
    "name": "中青年体检套餐",
    "type": 1,
    "price": 599,
    "items": [
      { "ciid": 1, "ciname": "一般检查", "cicontent": "身高、体重、血压、心率", "meaning": "了解身体基本指标" },
      { "ciid": 6, "ciname": "血脂", "cicontent": "总胆固醇、甘油三酯、低密度脂蛋白", "meaning": "评估心血管风险" }
    ]
  }
}
```

---

## 4. 预约订单模块（怿安）

### 4.1 查询是否已有未完成预约

`POST /api/orders/check`

请求：`{ "userid": "13800000001" }`

返回：`data` 为**未归档订单数量**（`0` 表示可以预约）。

### 4.2 创建预约

`POST /api/orders/save`

请求：

```json
{
  "userid": "13800000001",
  "hpid": 1,
  "smid": 2,
  "orderdate": "2026-11-08"
}
```

返回：`data = 1` 成功。

失败：

- `{"code":500,"message":"您有未完成的预约，不能重复预约","data":null}`
- `{"code":500,"message":"预约日期不能早于今天","data":null}`

### 4.3 我的预约列表

`POST /api/orders/list`

请求：`{ "userid": "13800000001", "state": null }`（state 传 `1` 未归档 / `2` 已归档 / `null` 全部）

返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "orderid": 2,
      "orderdate": "2026-10-20",
      "userid": "13800000001",
      "hpid": 1,
      "smid": 2,
      "state": 2,
      "hospitalname": "熙心健康体检中心·和平院区",
      "setmealname": "中青年体检套餐",
      "price": 599
    }
  ]
}
```

### 4.4 预约详情

`POST /api/orders/detail`

请求：`{ "orderid": 2 }`

返回：`data` 为单个 `OrdersDto`（结构同上）。

### 4.5 取消预约

`POST /api/orders/remove`

请求：`{ "orderid": 1 }`

返回：`data = 1` 成功。

限制：**只有未归档（state=1）的订单可以取消**，已归档的返回
`{"code":500,"message":"报告已生成，不能取消","data":null}`。

---

## 5. 报告模块（怿安）

### 5.1 总检结论

`POST /api/overallResult/list`

请求：`{ "orderid": 2 }`

返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    { "orid": 1, "title": "总检结论", "content": "本次体检未见明显异常，血脂偏高……", "orderid": 2 }
  ]
}
```

### 5.2 检查项 + 明细

`POST /api/ciReport/list`

请求：`{ "orderid": 2 }`

返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "cirid": 6,
      "ciid": 6,
      "ciname": "血脂",
      "orderid": 2,
      "details": [
        {
          "cidrid": 14,
          "name": "总胆固醇",
          "unit": "mmol/L",
          "minrange": 0,
          "maxrange": 5.2,
          "normalvalue": null,
          "normalvaluestring": "<5.2",
          "type": 1,
          "value": "5.8",
          "iserror": 1,
          "ciid": 6,
          "orderid": 2
        }
      ]
    }
  ]
}
```

`iserror = 1` 表示异常，前端标红。

---

## 6. 预约日历（怿安）

`POST /api/calendar/list`

请求：

```json
{ "hpid": 1, "year": 2026, "month": 11 }
```

返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    { "date": "2026-11-01", "total": 20, "used": 0, "remaining": 20, "full": false },
    { "date": "2026-11-06", "total": 20, "used": 1, "remaining": 19, "full": false },
    { "date": "2026-11-07", "total": 20, "used": 20, "remaining": 0, "full": true }
  ]
}
```

说明：

- `total` 从 `hospital.rule` 里解析数字（"每天限20人" → 20），解析失败默认 20
- `used` 为该医院当天已有订单数
- `full = remaining <= 0`
- 前端对 `full=true` 或今天以前的日期置灰

---

## 7. 状态字典

| 字段 | 值 | 含义 |
|---|---|---|
| `orders.state` | 1 | 未归档（待体检） |
| `orders.state` | 2 | 已归档（报告已生成） |
| `setmeal.type` | 1 / 0 | 男士套餐 / 女士套餐 |
| `users.sex` | 1 / 0 | 男 / 女 |
| `users.usertype` | 1 / 2 / 3 | 普通用户 / 东软员工 / 其他 |
| `cidetailedreport.iserror` | 0 / 1 | 正常 / 异常 |
| `hospital.state` | 1 / 2 | 正常 / 其他 |

---

## 8. 前端调用示例

```js
import request from '@/utils/request'

// 登录
const res = await request.post('/users/login', { userid, password })
const user = res.data.data

// 我的预约
const res2 = await request.post('/orders/list', { userid: user.userid, state: null })
const orders = res2.data.data
```

---

## 9. 后端文件归属

| 模块 | Controller | 负责人 |
|---|---|---|
| users | `UsersController` | 曲奇饼 |
| hospital | `HospitalController` | 曲奇饼 |
| setmeal | `SetmealController` | 曲奇饼 |
| orders | `OrdersController` | 怿安 |
| overallResult | `OverallresultController` | 怿安 |
| ciReport | `CireportController` | 怿安 |
| calendar | `CalendarController` | 怿安 |

> 两人都只改自己名下的 Controller / Service / Mapper；`Result`、`WebMvcConfig`、`application.properties` 由怿安维护。
