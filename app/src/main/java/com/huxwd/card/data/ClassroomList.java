package com.huxwd.card.data;

import com.huxwd.card.db.RoomListDb;

import java.util.List;

/**
 * 创建日期：2021/10/22 10:43
 *
 * @author zhang_
 * @version 1.0
 * 包名： com.huxwd.card.data
 * 类说明：
 */
public class ClassroomList {

    private String success;
    private String msg;
    private List<RoomListDb> roomList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<RoomListDb> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomListDb> roomList) {
        this.roomList = roomList;
    }


}
