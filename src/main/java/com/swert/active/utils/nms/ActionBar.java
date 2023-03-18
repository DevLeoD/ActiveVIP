package com.swert.active.utils.nms;

import com.swert.active.utils.nms.reflections.NMSReflections;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ActionBar extends NMSReflections {
    private static Class<?> packetPlayOutChat = getNMSClass("PacketPlayOutChat");
    private static Constructor<?> constructor;
    static {
        try {
            constructor = packetPlayOutChat.getConstructor(
                    getNMSClass("IChatBaseComponent"), byte.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Object packet;

    public ActionBar(String message) {
        this.setMessage(message);
    }

    public void show(Player player) {
        sendPacket(player, this.packet);
    }

    // todo improve performace with static classes and constructors
    public void setMessage(String message){
        try {
            Class<?> iChatBaseComponent = getNMSClass("IChatBaseComponent");
            Class<?> chatSerializer = iChatBaseComponent.getClasses()[0];
            Method a = chatSerializer.getMethod("a", String.class);
            Object component = a.invoke(chatSerializer, "{\"text\": \"" + message + "\"}");

            this.packet = constructor.newInstance(component, (byte) 2);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
