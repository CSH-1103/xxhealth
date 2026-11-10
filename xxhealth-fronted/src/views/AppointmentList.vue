<template>
  <div class="page">
    <header class="nav">
      <span class="back" @click="go('/index')"><i class="fa fa-angle-left"></i></span>
      <h1>我的预约</h1>
      <span class="placeholder"></span>
    </header>

    <div class="tabs">
      <span
        v-for="tab in tabs"
        :key="tab.label"
        class="tab"
        :class="{ active: state === tab.value }"
        @click="switchTab(tab.value)"
      >{{ tab.label }}</span>
    </div>

    <div v-if="loading" class="tip">加载中...</div>
    <div v-else-if="list.length === 0" class="tip">暂无预约记录</div>

    <ul v-else class="list">
      <li v-for="item in list" :key="item.orderid" class="card">
        <div class="row">
          <span class="name">{{ item.setmealname }}</span>
          <span class="tag" :class="item.state === 1 ? 'wait' : 'done'">
            {{ item.state === 1 ? '待体检' : '已归档' }}
          </span>
        </div>
        <div class="sub">{{ item.hospitalname }}</div>
        <div class="sub">预约日期：{{ item.orderdate }}</div>
        <div class="row bottom">
          <span class="price">¥{{ item.price }}</span>
          <span class="actions">
            <button v-if="item.state === 1" class="btn ghost" @click="cancel(item)">取消预约</button>
            <button v-if="item.state === 2" class="btn primary" @click="openReport(item)">查看报告</button>
          </span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import request from '@/utils/request'
import { getSessionStorage } from '@/common'

export default {
  name: 'AppointmentList',
  data() {
    return {
      loading: true,
      state: null,
      list: [],
      tabs: [
        { label: '全部', value: null },
        { label: '待体检', value: 1 },
        { label: '已归档', value: 2 }
      ]
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
      this.loading = true
      try {
        const res = await request.post('/orders/listOrdersByUserId', {
          userid: user.userid,
          state: this.state
        })
        this.list = res.data || []
      } catch (e) {
        this.list = []
      } finally {
        this.loading = false
      }
    },
    switchTab(value) {
      this.state = value
      this.load()
    },
    async cancel(item) {
      if (!window.confirm('确定要取消这条预约吗？')) {
        return
      }
      try {
        const res = await request.post('/orders/removeOrders', { orderid: item.orderid })
        if (res.data === 1) {
          window.alert('已取消')
          this.load()
        } else if (res.data === -1) {
          window.alert('报告已生成，不能取消')
        } else {
          window.alert('取消失败，订单不存在')
        }
      } catch (e) {
        window.alert('取消失败，请稍后重试')
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
.tabs { display: flex; background: #fff; border-bottom: 1px solid #eee; }
.tab { flex: 1; text-align: center; padding: 12px 0; font-size: 14px; color: #666; cursor: pointer; }
.tab.active { color: #2f7cf6; font-weight: 600; border-bottom: 2px solid #2f7cf6; }
.list { padding: 12px; }
.card { background: #fff; border-radius: 10px; padding: 14px; margin-bottom: 10px; }
.row { display: flex; align-items: center; justify-content: space-between; }
.name { font-size: 16px; font-weight: 600; color: #222; }
.tag { font-size: 12px; padding: 2px 8px; border-radius: 10px; }
.tag.wait { color: #ff8f1f; background: #fff4e6; }
.tag.done { color: #2f7cf6; background: #eaf2ff; }
.sub { font-size: 13px; color: #888; margin-top: 6px; }
.bottom { margin-top: 10px; border-top: 1px solid #f0f0f0; padding-top: 10px; }
.price { color: #ff6b35; font-size: 16px; font-weight: 600; }
.actions { display: flex; gap: 8px; }
.btn { border: none; border-radius: 16px; padding: 6px 14px; font-size: 13px; }
.btn.primary { background: #2f7cf6; color: #fff; }
.btn.ghost { background: #fff; color: #e64545; border: 1px solid #e64545; }
.tip { text-align: center; color: #999; font-size: 14px; padding: 60px 0; }
</style>
