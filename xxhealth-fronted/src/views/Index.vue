<template>
  <div class="app">
    <div class="hero">
      <p class="hello">{{ greeting }}，{{ userName }}</p>
      <h2>今天也要关注健康</h2>
      <p class="sub">选好套餐，预约一次安心的体检</p>
    </div>

    <div class="quick">
      <router-link v-for="item in entries" :key="item.path" :to="item.path" class="quick-item">
        <i :class="['fa', item.icon]"></i>
        <span>{{ item.label }}</span>
      </router-link>
    </div>

    <div class="section-title">
      <h3>热门套餐</h3>
      <router-link to="/appointment" class="more">去预约 <i class="fa fa-angle-right"></i></router-link>
    </div>

    <div v-if="loading" class="empty">加载中…</div>
    <div v-else-if="!hotSetmeals.length" class="empty">暂无套餐</div>
    <div
      v-for="item in hotSetmeals"
      :key="item.smid"
      class="card setmeal"
      @click="goSetmeal(item)"
    >
      <div class="setmeal-main">
        <h4>{{ item.name }}</h4>
        <p>{{ item.type === 1 ? '男士套餐' : '女士套餐' }}</p>
      </div>
      <div class="setmeal-side">
        <span class="price">¥{{ item.price }}</span>
        <i class="fa fa-angle-right"></i>
      </div>
    </div>

    <TabBar />
  </div>
</template>

<script>
import request from '@/utils/request'
import { getSessionStorage } from '@/common'
import TabBar from '@/components/TabBar.vue'

export default {
  name: 'Index',
  components: { TabBar },
  data() {
    return {
      user: null,
      setmeals: [],
      loading: true,
      entries: [
        { path: '/appointment', label: '预约体检', icon: 'fa-calendar-plus-o' },
        { path: '/reportList', label: '报告查询', icon: 'fa-file-text-o' },
        { path: '/appointmentList', label: '我的预约', icon: 'fa-list-alt' },
        { path: '/personal', label: '个人中心', icon: 'fa-user-o' }
      ]
    }
  },
  computed: {
    userName() {
      return this.user && this.user.realname ? this.user.realname : '朋友'
    },
    greeting() {
      const h = new Date().getHours()
      if (h < 6) return '夜深了'
      if (h < 9) return '早上好'
      if (h < 12) return '上午好'
      if (h < 14) return '中午好'
      if (h < 18) return '下午好'
      return '晚上好'
    },
    hotSetmeals() {
      return this.setmeals.slice(0, 3)
    }
  },
  created() {
    this.user = getSessionStorage('users')
    if (!this.user) {
      this.$router.replace('/login')
      return
    }
    this.loadSetmeals()
  },
  methods: {
    async loadSetmeals() {
      this.loading = true
      try {
        const res = await request.post('/setmeal/listSetmealByType', {})
        this.setmeals = Array.isArray(res.data) ? res.data : []
      } catch (e) {
        this.setmeals = []
      } finally {
        this.loading = false
      }
    },
    goSetmeal(item) {
      this.$router.push({ path: '/setmeal', query: { smid: item.smid, type: item.type } })
    }
  }
}
</script>

<style scoped>
.hero {
  padding: 26px 20px 52px;
  border-radius: 0 0 22px 22px;
  background: linear-gradient(135deg, #12a874, #18b97f);
  color: #fff;
}
.hello {
  font-size: 13px;
  opacity: 0.9;
}
.hero h2 {
  margin-top: 6px;
  font-size: 21px;
  letter-spacing: 0.5px;
}
.sub {
  margin-top: 6px;
  font-size: 13px;
  opacity: 0.85;
}
.quick {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  margin: -34px 16px 0;
  padding: 16px 6px;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 4px 14px rgba(31, 45, 61, 0.06);
}
.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 7px;
  color: #1f2d3d;
  font-size: 12px;
}
.quick-item i {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: #e6f6ef;
  color: #10a06a;
  font-size: 18px;
}
.section-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 20px 16px 6px;
}
.section-title h3 {
  font-size: 16px;
}
.more {
  color: #8a94a6;
  font-size: 12px;
}
.setmeal {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.setmeal-main h4 {
  font-size: 15px;
}
.setmeal-main p {
  margin-top: 6px;
  color: #8a94a6;
  font-size: 12px;
}
.setmeal-side {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #c3cad4;
}
</style>
