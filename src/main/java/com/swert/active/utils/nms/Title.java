package com.swert.active.utils.nms;

import com.swert.active.utils.nms.reflections.NMSReflections;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Title extends NMSReflections {
    private static Class<?> packetClass = getNMSClass("PacketPlayOutTitle");
    private static Class<?> chatClass = getNMSClass("IChatBaseComponent");

    private Object titlePacket;
    private Object subtitlePacket;

    public Title(String title) {
        setTitle(title, null, 20, 40, 20);
    }

    public Title(String title, int fadeInTime, int showTime, int fadeOutTime) {
        setTitle(title, null, fadeInTime, showTime, fadeOutTime);
    }

    public Title(String title, String subtitle) {
        setTitle(title, subtitle, 20, 40, 20);
    }

    public Title(String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        setTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
    }

    public void setTitle(String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime){
        try {
            if (title != null) {
                Object titleChat = chatClass.getDeclaredClasses()[0].getMethod("a", String.class)
                        .invoke(null, "{\"text\": \"" + title + "\"}");
                Constructor<?> titleConstructor = packetClass.getConstructor(
                        packetClass.getDeclaredClasses()[0],
                        chatClass,
                        int.class,
                        int.class,
                        int.class
                );
                this.titlePacket = titleConstructor.newInstance(
                        packetClass.getDeclaredClasses()[0].getField("TITLE").get(null),
                        titleChat,
                        fadeInTime,
                        showTime,
                        fadeOutTime
                );
            }

            if (subtitle != null){
                Object subtitleChat = chatClass.getDeclaredClasses()[0].getMethod("a", String.class)
                        .invoke(null, "{\"text\": \"" + subtitle + "\"}");
                Constructor<?> subtitleConstructor = packetClass.getConstructor(
                        packetClass.getDeclaredClasses()[0],
                        chatClass,
                        int.class,
                        int.class,
                        int.class
                );
                this.subtitlePacket = subtitleConstructor.newInstance(
                        packetClass.getDeclaredClasses()[0].getField("SUBTITLE").get(null),
                        subtitleChat,
                        fadeInTime,
                        showTime,
                        fadeOutTime
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(Player player){
        setTitle("", "", 20, 20 ,20);
        sendPacket(player, this.titlePacket);
        sendPacket(player, this.subtitlePacket);
    }

    public void send(Player player) {
        if (this.titlePacket != null) {
            sendPacket(player, this.titlePacket);
        }
        if (this.subtitlePacket != null) {
            sendPacket(player, this.subtitlePacket);
        }
    }

}
