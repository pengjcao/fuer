package org.example.fuer_xitong.pojo.entity;

public class BaseContext {

    private static final ThreadLocal<String> currentId  = new ThreadLocal<>();
    private static final ThreadLocal<Integer> currentRole = new ThreadLocal<>();

    public static void setCurrentId(String id) {
        currentId.set(id);
    }

    public static String getCurrentId() {
        return currentId.get();
    }

    public static void setCurrentRole(Integer role) {
        currentRole.set(role);
    }

    public static Integer getCurrentRole() {
        return currentRole.get();
    }

    public static void clear() {
        currentId.remove();
        currentRole.remove();
    }
}
