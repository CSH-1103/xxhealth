<template>
  <div class="page">
    <header class="nav">
      <span class="back" @click="go('/index')"><i class="fa fa-angle-left"></i></span>
      <h1>预约体检</h1>
      <span class="placeholder"></span>
    </header>

    <p class="hint">请选择体检套餐类型</p>

    <div class="types">
      <div class="type-card" @click="choose(1)">
        <i class="fa fa-male"></i>
        <div class="t-name">男士套餐</div>
        <div class="t-desc">适合男性常规体检</div>
      </div>
      <div class="type-card female" @click="choose(0)">
        <i class="fa fa-female"></i>
        <div class="t-name">女士套餐</div>
        <div class="t-desc">适合女性常规体检</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Appointment',
  methods: {
    choose(type) {
      this.$router.push({ path: '/hospital', query: { type } })
    },
    go(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
.page { min-height: 100%; background: #f5f6fa; }
.nav { display: flex; align-items: center; justify-content: space-between; height: 48px; padding: 0 12px; background: #2f7cf6; color: #fff; }
.nav h1 { font-size: 17px; font-weight: 500; }
.back, .placeholder { width: 24px; font-size: 20px; cursor: pointer; }
.hint { font-size: 14px; color: #888; padding: 16px 16px 0; }
.types { padding: 16px; display: flex; flex-direction: column; gap: 14px; }
.type-card { background: #fff; border-radius: 12px; padding: 22px 18px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,.04); cursor: pointer; border: 1px solid transparent; }
.type-card i { font-size: 34px; color: #2f7cf6; }
.type-card.female i { color: #ff6b9d; }
.t-name { font-size: 17px; font-weight: 600; color: #222; margin-top: 10px; }
.t-desc { font-size: 13px; color: #999; margin-top: 6px; }
</style>
