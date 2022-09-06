module.exports = {
  // 修改或新增html-webpack-plugin的值，在index.html里面能读取htmlWebpackPlugin.options.title
  chainWebpack: config => {
    config.plugin('html')
      .tap(args => {
        args[0].title = '贵山河医院后台管理系统'
        return args
      })
  },
  devServer: {
    host: '127.0.0.1',
    port: 8888,
    open: true
  }
}
