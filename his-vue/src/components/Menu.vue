<template>
  <div>
    <el-menu
      background-color="#333744"
      text-color="#fff"
      active-text-color="#409EFF"
      unique-opened
      :collapse="isCollapse"
      :collapse-transition="false"
      router
      :default-active="activeurl"
    >
      <!-- 一级菜单 -->
      <el-submenu :index="item.id + ''" v-for="item in menulist" :key="item.id">
        <!-- 一级菜单的模板区域 -->
        <template slot="title">
          <!-- 图标 -->
          <i :class="iconsObj[item.id]"></i>
          <!-- 文本 -->
          <span>{{ item.name }}</span>
        </template>

        <!-- 二级菜单 -->
        <el-menu-item
          :index="'/' + subItem.url"
          v-for="subItem in item.children"
          :key="subItem.id"
          @click="saveNavState('/' + subItem.url)"
        >
          <template slot="title">
            <!-- 图标 -->
            <i class="el-icon-menu"></i>
            <!-- 文本 -->
            <span>{{ subItem.name }}</span>
          </template>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'Menu',
  data () {
    return {
      // 左侧导航栏菜单数据 可以通过token向服务器获取用户的菜单信息（根据角色权限不同）
      menulist: [],
      iconsObj: {
        1: 'el-icon-user',
        2: 'el-icon-setting',
        3: 'el-icon-s-order',
        4: 'el-icon-s-cooperation'
      },
      // 被激活的链接地址
      activeurl: ''
    }
  },
  created () {
    // this.getMenuList(); 用来获取列表信息
    this.activeurl = window.sessionStorage.getItem('activeurl')
    this.$axios.http.get('/permissions/userPermissionList').then(reponse => {
      // console.log(reponse.data.data)
      if (reponse.data.status !== 200) {
        this.$message.error(reponse.data.message)
        this.$router.push('/login')
      }
      this.menulist = reponse.data.data
    })
  },
  methods: {
    // 保存链接的激活状态
    saveNavState (activeurl) {
      window.sessionStorage.setItem('activeurl', activeurl)
      this.activeurl = activeurl
    }
  },
  computed: {
    ...mapState({
      isCollapse: 'isCollapse'
    })
  }
}
</script>

<style lang="less" scoped>
.el-menu {
  border-right: none;
}

</style>
