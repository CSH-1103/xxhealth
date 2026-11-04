import axios from 'axios'

// 开发环境通过 vue.config.js 的 proxy 转发到 http://localhost:8080
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 统一处理 Result：code !== 200 视为业务失败
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res && typeof res.code !== 'undefined' && res.code !== 200) {
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return response
  },
  error => {
    if (error.response && error.response.data && error.response.data.message) {
      return Promise.reject(new Error(error.response.data.message))
    }
    return Promise.reject(error)
  }
)

export default request
