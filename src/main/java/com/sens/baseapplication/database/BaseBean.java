package com.sens.baseapplication.database;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by SensYang on 2017/9/2 0002.
 */

public class BaseBean implements Serializable{
    // 设置为主键,自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_pid")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
