package com.huxwd.card.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建日期：2021/10/11 16:03
 *
 * @author zhang_
 * @version 1.0
 * 包名： com.huxwd.card.bean
 * 类说明：
 */

public class Bean00 implements Serializable {
    //
    public int expHeightDt = 0, t6HeightDt = 0; // 详情页面的高度
    public int expHeight = 0, t6Height = 0;
    public String expName = "收起";
    //
    public String dateTime;
    public String teacher;
    public String roomName;
    public String className;
    public String bookName;
    public String studentYear;
    public List<String> classCode;
    //
    public String courseName;
    public String courseNo;
    public Integer roomId;
    public Integer knowledgeNo;
    public byte checkFlag;
    public String courseId;

    public Integer quorum;
    public Integer attended;


    public boolean isChecked = false;


    //反抵
    public String checkTime;
    public String studentCardNo; // 学生卡号
    public String teacherCardNo;
    public String oldCardNo; // 抵押卡号
    public int oprFlag = 1;

    public boolean isBackVerified = false;
}
