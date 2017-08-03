20170803 重要：
1、bug：使用mybatis-generator时xml中的内容会重复生成，需要手工清理


类型选择：
1、id类型都使用Long，该封装类型可以使用Null
2、储存钱一类的必须精确的，用java.math.BigDecimal，而float和double都不是精确的

