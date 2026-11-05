<template>
  <div class="page">
    <header class="nav">
      <span class="back" @click="go('/index')"><i class="fa fa-angle-left"></i></span>
      <h1>体检报告</h1>
      <span class="placeholder"></span>
    </header>

    <div v-if="loading" class="tip">加载中...</div>
    <div v-else-if="list.length === 0" class="tip">暂无体检报告</div>

    <ul v-else class="list">
      <li v-for="item in list" :key="item.orderid" class="card" @click="openReport(item)">
        <div class="row">
          <span class="name">{{ item.setmealname }}</span>
          <span class="tag">已归档</span>
        </div>
        <div class="sub">{{ item.hospitalname }}</div>
        <div class="sub">体检日期：{{ item.orderdate }}</div>
        <div class="row bottom">
          <span class="price">¥{{ item.price }}</span>
          <span class="link">查看报告 <i class="fa fa-angle-right"></i></span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import request from '@/utils/request'
import { getSessionStorage } from '@/common'

export default {
  name: 'ReportList',
  data() {
    return {
      list: [],
      loading: true
    }
  },
  created() {
    this.load()
  },
  methods: {
    async load() {
      const user = getSessionStorage('users')
      if (!user) {
        this.$router.push('/login')
        return
      }
      try {
        const res = await request.post('/orders/listOrdersByUserId', {
          userid: user.userid,
          state: 2
        })
        this.list = res.data || []
      } catch (e) {
        this.list = []
      } finally {
        this.loading = false
      }
    },
    openReport(item) {
      this.$router.push({ path: '/report', query: { orderid: item.orderid } })
    },
    go(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
.page { min-height: 100%; background: #f5f6fa; padding-bottom: 24px; }
.nav { display: flex; align-items: center; justify-content: space-between; height: 48px; padding: 0 12px; background: #2f7cf6; color: #fff; }
.nav h1 { font-size: 17px; font-weight: 500; }
.back, .placeholder { width: 24px; font-size: 20px; cursor: pointer; }
.list { padding: 12px; }
.card { background: #fff; border-radius: 10px; padding: 14px; margin-bottom: 10px; box-shadow: 0 1px 4px rgba(0,0,0,.04); cursor: pointer; }
.row { display: flex; align-items: center; justify-content: space-between; }
.name { font-size: 16px; font-weight: 600; color: #222; }
.tag { font-size: 12px; color: #2f7cf6; background: #eaf2ff; padding: 2px 8px; border-radius: 10px; }
.sub { font-size: 13px; color: #888; margin-top: 6px; }
.bottom { margin-top: 10px; border-top: 1px solid #f0f0f0; padding-top: 10px; }
.price { color: #ff6b35; font-size: 16px; font-weight: 600; }
.link { color: #2f7cf6; font-size: 14px; }
.tip { text-align: center; color: #999; font-size: 14px; padding: 60px 0; }
</style>