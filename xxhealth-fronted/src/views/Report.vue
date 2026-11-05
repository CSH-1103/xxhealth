<template>
  <div class="page">
    <header class="nav">
      <span class="back" @click="go('/reportList')"><i class="fa fa-angle-left"></i></span>
      <h1>体检报告详情</h1>
      <span class="placeholder"></span>
    </header>

    <div v-if="loading" class="tip">加载中...</div>
    <div v-else-if="!order" class="tip">报告不存在</div>

    <template v-else>
      <section class="card head">
        <div class="name">{{ order.setmealname }}</div>
        <div class="sub">{{ order.hospitalname }}</div>
        <div class="sub">体检日期：{{ order.orderdate }}</div>
      </section>

      <section v-if="overall.length" class="card">
        <h2>总检结论</h2>
        <div v-for="item in overall" :key="item.orid" class="overall">
          <div class="overall-title">{{ item.title }}</div>
          <div class="overall-content">{{ item.content }}</div>
        </div>
      </section>

      <section class="card">
        <h2>检查项</h2>
        <div v-for="item in checkItems" :key="item.cirid" class="item">
          <div class="item-title">
            {{ item.ciname }}
            <span v-if="hasError(item)" class="err-tag">异常</span>
          </div>
          <div v-for="d in item.details" :key="d.cidrid" class="detail" :class="{ error: d.iserror === 1 }">
            <span class="d-name">{{ d.name }}</span>
            <span class="d-value">{{ d.value }}<em v-if="d.unit"> {{ d.unit }}</em></span>
            <span class="d-range">{{ d.normalvaluestring || rangeText(d) }}</span>
          </div>
        </div>
        <div v-if="checkItems.length === 0" class="empty">暂无检查项</div>
      </section>
    </template>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Report',
  data() {
    return {
      loading: true,
      order: null,
      overall: [],
      checkItems: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    async load() {
      const orderid = Number(this.$route.query.orderid)
      if (!orderid) {
        this.loading = false
        return
      }
      try {
        const [orderRes, overallRes, ciRes] = await Promise.all([
          request.post('/orders/getOrdersById', { orderid }),
          request.post('/overallResult/listOverallResultByOrderId', { orderid }),
          request.post('/ciReport/listCiReport', { orderid })
        ])
        this.order = orderRes.data
        this.overall = overallRes.data || []
        this.checkItems = ciRes.data || []
      } catch (e) {
        this.order = null
      } finally {
        this.loading = false
      }
    },
    hasError(item) {
      return (item.details || []).some(d => d.iserror === 1)
    },
    rangeText(d) {
      if (d.minrange != null && d.maxrange != null) {
        return d.minrange + ' - ' + d.maxrange
      }
      return ''
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
.card { background: #fff; border-radius: 10px; margin: 12px; padding: 14px; }
.card h2 { font-size: 15px; color: #222; margin-bottom: 10px; }
.head .name { font-size: 18px; font-weight: 600; color: #222; }
.sub { font-size: 13px; color: #888; margin-top: 6px; }
.overall { margin-bottom: 12px; }
.overall-title { font-size: 14px; font-weight: 600; color: #2f7cf6; margin-bottom: 4px; }
.overall-content { font-size: 14px; color: #444; line-height: 1.7; }
.item { border-top: 1px solid #f2f2f2; padding: 10px 0; }
.item:first-of-type { border-top: none; }
.item-title { font-size: 15px; font-weight: 600; color: #222; margin-bottom: 6px; }
.err-tag { font-size: 12px; color: #e64545; background: #ffecec; padding: 1px 6px; border-radius: 8px; margin-left: 6px; font-weight: 400; }
.detail { display: flex; justify-content: space-between; font-size: 13px; color: #666; padding: 4px 0; }
.detail.error .d-value, .detail.error .d-name { color: #e64545; font-weight: 600; }
.d-name { flex: 1; }
.d-value { flex: 1; text-align: center; }
.d-value em { font-style: normal; color: #999; font-size: 12px; }
.d-range { flex: 1; text-align: right; color: #aaa; }
.tip, .empty { text-align: center; color: #999; font-size: 14px; padding: 40px 0; }
</style>