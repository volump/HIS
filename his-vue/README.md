## 项目启动
+ IDE: 我这里使用的vs-code
+ 导入后首先安装依赖
```
cnpm install
```
+ 启动项目
```
npm run serve
```
+ 生产阶段启动
```
npm run build
```

+ 使用eslint 检查
```
npm run lint
```

### 添加配置文件
+ 添加`vue.config.js`
+ 自定义`html-webpack-plugin`信息: 修改标题
+ [options参考]()
```js
module.exports = {
  // 修改或新增html-webpack-plugin的值，在index.html里面能读取htmlWebpackPlugin.options.title
  chainWebpack: config => {
    config.plugin('html')
      .tap(args => {
        args[0].title = '嘟嘟云医院后台管理系统'
        return args
      })
  }
}
```
### 文件说明
+ `assets`: 放置静态资源文件x
+ `views`： 放的`<router-view/>` 显示的视图页面
+ `components`: 放置个人模块所需要的组件
+ `store`: 放置`vuex`状态管理文件，并且单独写在一个文件，这里还没有进行模块管理，内容也不多
+ `utils`: 放置一些工具类
  + `axiosUtil`: 对`axios`的请求进行封装，并使用`element-ui`组件进行提示 并在 `main.js` 添加到原型上
+ `main.js`： 入口文件

### 登录

+ router/index.js 定义路由 并把'/'重定向 登录页
```js
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login')
  },
```
+ 在router/index.js 添加路由守卫 , 对于本地未存储token 的路径访问强制跳转到 login
```js
// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to 将要访问的路径
  // from 代表从哪个路径跳转而来
  // next 是一个函数，表示放行
  //     next()  放行    next('/login')  强制跳转
  if (to.path === '/login') return next()
  // 获取token
  const tokenStr = window.sessionStorage.getItem('token')
  if (!tokenStr) return next('/login')
  next()
})
```
+ axios 添加拦截器 在请求头里放入 token 值
```js
axios.interceptors.request.use(config => {
  config.headers.Authorization = window.sessionStorage.getItem('token')
  // 在最后必须 return config
  return config
})
```
+ 登录成功后把token存储起来
```js
window.sessionStorage.setItem('token', res.data.token)
```

### 使用props在父子组件传值
+ 在子组件 使用props来接收父组件传过来的值

``` html
      <el-breadcrumb-item>{{msg2}}</el-breadcrumb-item>

<script>
export default {
  props: ['msg1', 'msg2']
}
</script>
```
+ 在父组件 导入子组件并加载到组件中，使用时传参数时 对与非字符串和变量应使用数据绑定`:`

```html
    <Breadcrumb msg1="基本信息管理" :msg2="常数类别列表" />

<script>
import Breadcrumb from '@/components/Breadcrumb'
export default {
   components: {
    Breadcrumb
  },
}
</script>
```

#### .sync 修饰符
+ [文档位置](https://cn.vuejs.org/v2/guide/components-custom-events.html#sync-%E4%BF%AE%E9%A5%B0%E7%AC%A6)
+ 对于constant-type 和 edit.vue 组件
+ constant-type/index.vue


```html
<el-dialog :title="title" :visible.sync="show" :close-on-click-modal="false" width="500px">
  <ConstantTypeEdit v-if="show" :show.sync="show" @getData="getData()" :editid="editid"></ConstantTypeEdit>
</el-dialog>
```

+ edit.vue 

```html
<script>
export default {
  name: 'ConstantTypeEdit',
  props: ['editid'],
  methods: {
    save () {
        // 方法表达对其赋新值  :show.sync="show" 修改父组件show = false
        this.$emit('update:show', false)
      }
    }
  }
}
</script>
```

### vuex使用
+ 原理图
[![dR7KWd.png](https://s1.ax1x.com/2020/08/26/dR7KWd.png)](https://imgchr.com/i/dR7KWd)

+ 将vuex不同属性定义再不同的文件里，这里举一个左侧菜单栏的折叠功能

```js
//state.js
export default {
  isCollapse: false
}
// 推荐把mutation里的方法采用变量定义，且将方法名定义到另外一个常量文件里 比如mutation-types.js
export const TOGGLE_COLLAPSE = 'toggleCollapse' // 折叠左侧菜单栏
// mutation.js
import { TOGGLE_COLLAPSE } from './mutation-types'

export default {
  [TOGGLE_COLLAPSE] (state) {
    state.isCollapse = !state.isCollapse
  }
}
// action

import { TOGGLE_COLLAPSE } from './mutation-types'

export default {
  toggle_collapse ({ commit }) {
    commit(TOGGLE_COLLAPSE)
  }
}

```

+ 在组件里的使用
+ `Menu.vue`
```js
import { mapState } from 'vuex'

export default {
  /* 
  从 store 实例中读取状态最简单的方法就是在计算属性中返回某个状态
  当 状态 变化的时候, 都会重新求取计算属性，并且触发更新相关联的 DOM。
  获取方式一： this.$store.state.isCollapse  
  通过在根实例中注册 store 选项，该 store 实例会注入到根组件下的所有子组件中，且子组件能通过 this.$store 访问到。
  获取方式二： 使用 mapState 辅助函数帮助生成计算属性 并且使用了对象展开运算符 传对象的时候方便自己定义名称
  */
  computed: {
    ...mapState({
      isCollapse: 'isCollapse'
    })
  }
}
```

+ `Home.vue`
```js
import { mapState, mapActions } from 'vuex'

export default {
  computed: {
    ...mapState({
      isCollapse: 'isCollapse'
    })
  },
  methods: {
    /*
   在组件中使用  this.$store.dispatch('xxx') 分发 action
    mapActions 是action的辅助函数 ，同时可以传数组，选择自己需要的action 函数
    */
    ...mapActions([
      'toggleCollapse'
    ])
  },
}
```

### 批量删除

+ 利用post方法
```java
@Override
public boolean batchdel(Integer[] ids) {

    ConstantType constantType = new ConstantType();
    constantType.setActive(0);

    UpdateWrapper<ConstantType> wrapper = new UpdateWrapper<>();
    List<Integer> list = new ArrayList<>(ids.length);
    Collections.addAll(list,ids);
    wrapper.in("id",list);
    return this.update(constantType,wrapper);
}
```
+ 对于使用delete 方法 直接传参时 url为 `http://127.0.0.1:8088/constantTypes/batchdel?ids[]=6` 
  未对数据使用深度序列化，需要配置选择
+ 我采用字符串`,`分割的方式降低难度
```js
batchdel () {
  if (this.selectedrow.length === 0) {
    this.$message('没有任何被选中的数据')
  } else {
    const ids = []
    for (let i = 0; i < this.selectedrow.length; i++) {
      ids.push(this.selectedrow[i].id)
    }
    this.$axios.del('/constantTypes/batchdel',
      () => {
        if (this.total === (this.queryInfo.page - 1) * this.queryInfo.limit + ids.length) this.queryInfo.page -= 1
        this.getData()
      },
      {
        ids: ids.join(',')
      }
    )
  }
},
```
+ 后端
```java
@DeleteMapping("/batchdel")
public Result batchDel(@RequestParam String ids){
    String [] idList = ids.split(",");
    List<Integer> list = new ArrayList<>(idList.length);
    for (String id:idList){
        list.add(Integer.parseInt(id));
    }
    if(constantTypeService.removeByIds(list)) return ResultGenerator.getSuccessResult("","删除成功");
    return ResultGenerator.getFailResult("","删除失败");
}
```
+ 也可以使用mybatis-plus提供逻辑删除

### 表单添加自定义规则

```js
data () {
    // name 必须唯一的
    var checkName = (rule, value, cb) => {
      // console.log(value)
      // value 传入的校验值
      const promise = this.$axios.http.get('/constantTypes/check', { params: { name: value } })
      promise.then(
        response => {
          if (response.data.status !== 200) {
            // 返回ERROR 表示 验证不符合要求
            cb(new Error('该常数类别代码存在'))
          }
          // 这是返回正确的
          cb()
        }
      )
    },
    rules: {
        code: [
          { required: true, message: '编码不能为空', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' },
          { validator: checkName, trigger: 'blur' }
        ]
      }
    }
  }
```

### ajax请求包axios
+ 对axios进行一些处理，包装一下
+ 定义自己的请求基路径`axios.defaults.baseURL`

+ 为了能够在全局使用把axios挂载到Vue函数对象的原型上`Vue.prototype.$axios = axios`，这样在组件里通过`this.$axios`访问

### 路径使用
+ `./`指当前目录
+ `../`指当前目录的上一层目录
+ vue的使用路径中的`@`代表“src目录”这个是webpack起的别名；在`webpack.base.conf.js`中有声明
+ `extensions`: 在导入语句没带文件后缀时，webpack会自动按照顺序添加后缀名查找
```js
// 连接路径并返回
function resolve(dir) {
  return path.join(__dirname, '..', dir)
}

module.exports = {
  
  resolve: {
    // 在导入语句没带文件后缀时，webpack会自动按照顺序添加后缀名查找
    extensions: ['.js', '.vue', '.json'],
    // 配置别名
    alias: {
      'vue$': 'vue/dist/vue.esm.js',
      // 将项目根目录中，src的路径配置为别名@
      '@': resolve('src'),
    }
  }
}
```
+ 在webpack中有这个配置`resolve.mainFiles: ["index"]` 解析目录时要使用的文件名。
+ 根据上面说的`() => import('@/views/constant-type')` 以及实际项目情况对应的路径是 `项目根路径/src/views/constant-type/index.vue` 
+ >> 在浏览器的中对'/home' 中/ 会解析为 协议://主机名:端口号/ 再拼接上home 作为路径

### 问题记录

+ 如果在删除的数据，当前页的数据刚好为删除数据的个数此时 应该修改当前页

```js
this.$axios.del(`/constantTypes/${id}`, () => {
  if (this.total === (this.queryInfo.page - 1) * this.queryInfo.limit + 1) this.queryInfo.page -= 1
  this.getData()
})
```

+ element-ui select 下拉框位置错乱

```html
<!-- 默认是插入到body -->
<el-select v-model="form.deptId" placeholder="请选择" :popper-append-to-body="false">
<!-- 添加自己样式 -->
<style scoped>
.el-select-dropdown {
  top: 38px !important;
  left: 0 !important;
}
</style>
```

+ 使用`Steps`步骤条和`Tabs` 选项卡组件的 使用 `:active="parseInt(activeIndex)"` 两者使用不同的类型
  + Steps active 是需要数值
  + Tab name 需要字符串