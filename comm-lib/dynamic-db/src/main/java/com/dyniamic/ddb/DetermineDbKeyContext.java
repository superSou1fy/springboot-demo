package com.dyniamic.ddb;

import java.util.Stack;

public class DetermineDbKeyContext {

    private static final ThreadLocal<Stack<Object>> DB_KEY = ThreadLocal.withInitial(Stack::new);

    public static Object currentDbKey() {
        return DB_KEY.get().peek();
    }

    public static void routeTo(Object dbKey) {
        DB_KEY.get().push(dbKey);
    }

    public static void restoreDbKey() {
        DB_KEY.get().pop();
    }
}
