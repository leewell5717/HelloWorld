package com.liwei;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyClass {
    public static void main(String args[]){
        /**
         * NO.1
         * Schema是GreenDAO框架中所有实体类的一个"容器"类,用于添加所有的实体类.
         * 参数一：版本号
         * 参数二：包名，主要用于存放自动生成的DAO文件
         */
        Schema schema = new Schema(1,"com.liwei.greendao");
        addUserAndPic(schema);
        try {
            /**
             * NO.2
             * 自动生成所有的DAO对象
             * 参数一：Schema对象
             * 参数二：指定目标项目地址，用于存放生成的DAO文件。
             * 其中NO.1中的参数二和NO.2中的参数二合起来就是：
             * MyStudy/app/src/main/java的包名为com.liwei.greendao的包下。
             */
            new DaoGenerator().generateAll(schema,"../MyGit/app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加用户和图片实体，并将其进行一对一关联
     */
    private static void addUserAndPic(Schema schema){
        /***************添加Customer单个实体*/

        //添加一个Entity对象。addEntity的参数将作为为Java类名和数据库的表名（关系名）
        Entity customer = schema.addEntity("Customer");
        //添加ID，自动增长，主键
        customer.addIdProperty().getProperty();
        //添加属性customerName，字符串类型，不能为空
        customer.addStringProperty("customerName").notNull();
        //添加属性customerPhone，字符串类型
        customer.addStringProperty("customerPhone");
        //添加属性customerAge，int类型
        customer.addIntProperty("customerAge");

        /***************添加一对一关系*/

        /**添加Picture实体*/
        Entity picture = schema.addEntity("Picture");
        picture.addIdProperty();
        picture.addStringProperty("imageUrl");

        /**添加User实体*/
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        //在用户实体中添加pictureId的属性，作为外键用于关联Picture实体
        Property pictureId = user.addLongProperty("pictureId").getProperty();
        //通过addToOne方法完成关联关系，之后在自动生成的User实体中会有一个getPicture方法用于获取用户关联的图片
        //参数一：picture是被关联的对象，参数二：pictureId是User实体中的一个作为外键的属性
        user.addToOne(picture,pictureId);


        /***************添加一对多关系*/


    }
}