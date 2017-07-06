##注意事项
###布局使用tiles3还是thymeleaf layout？
tiles用在jsp解析上可以，但html模板还是用thymeleaf,
结论：推荐使用thymeleaf layout

###thymeleaf的布局使用offical layout 还是 layout-dialect?
都可以，但要注意layout-dialect对于thymeleaf3的支持，当前存在警告，部分语法后续版本会失效


##版本关注

servlet-api待升级到4.0