Jersey2 - DbUtils - BackboneJS 
=================
* [Jersey](https://jersey.java.net) 2.13 ([MOXy](http://www.eclipse.org/eclipselink/moxy.php))
* [DbUtils](https://commons.apache.org/proper/commons-dbutils/) 1.6
* [BackboneJS](http://backbonejs.org) 1.1.2

####The "JNDI DataSource / JDBC" setting
----
Please confirm the following files about JDBC setting.

* [src/main/webapp/META-INF/context.xml](/src/main/webapp/META-INF/context.xml)
* [main.properties](/main.properties)
* [dist.properties](/dist.properties)

####The 'message' table in the database
----
The following "message" table is required.

    create table message (id INT auto_increment, content VARCHAR(255), createTime TIMESTAMP, index(id));

####Reference
----
* [Jersey 2.13, DbUtils 1.6, Backbone 1.1.2](https://sites.google.com/site/memo0x000000/jersey2dbutilsbackbone)

#### 利用条件
----
* いかなる種類の使用, 改造, 転載を行っても構いません.
* 改変, 部分的な利用したものはあなたのものになります. 公開するときはあなたの名前の元に公開してください.
* これによるいかなる損害も作者は責任を負わないものとします.
