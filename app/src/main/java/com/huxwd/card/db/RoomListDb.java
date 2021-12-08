package com.huxwd.card.db;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 创建日期：2021/10/22 10:47
 *
 * @author zhang_
 * @version 1.0
 * 包名： com.huxwd.card.db
 * 类说明：
 */
public class RoomListDb extends LitePalSupport {

    public Integer roomId;
    public String roomName;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public static void saveALL(List<RoomListDb> roomListDbList) {
        LitePal.deleteAll(RoomListDb.class);
        if (roomListDbList != null) {
            RoomListDb roomListDb = new RoomListDb();
            roomListDb.roomName = "请选择教室";
            roomListDb.roomId = -1;
            roomListDbList.add(0, roomListDb);
        }
        LitePal.saveAll(roomListDbList);
    }

    public static List<RoomListDb> selectAll() {
        return LitePal.findAll(RoomListDb.class); //  不会为null
    }

    public static RoomListDb selectOne(String roomName) {
        List<RoomListDb> roomListDbs = LitePal.where("roomName = ?", roomName + "").find(RoomListDb.class);
        if (roomListDbs != null && roomListDbs.size() > 0) {
            return roomListDbs.get(0);
        } else {
            return null;
        }
    }
}
