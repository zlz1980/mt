package com.nantian.nbp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Lianghaizhen
 * @date 2025/3/6
 */
public class SerializationUtil {
    /**
     * 序列化对象为 byte 数组
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的 byte 数组
     */
    public static <T extends Serializable> byte[] serialize(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    /**
     * 反序列化 byte 数组为对象
     *
     * @param data  字节数组
     * @param clazz 目标对象的 Class 类型
     * @return 反序列化后的对象
     */
    public static <T extends Serializable> T deserialize(byte[] data, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(bis);
            return clazz.cast(ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
