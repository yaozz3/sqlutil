package com.yupi.sqlfather.core.builder.sql;

import com.yupi.sqlfather.common.ErrorCode;
import com.yupi.sqlfather.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL 方言工厂
 * 工厂 + 单例模式，降低开销
 *
 * @author https://github.com/liyupi
 */
public class SQLDialectFactory {

    /**
     * className => 方言实例映射
     */
    private static final Map<String, SQLDialect> DIALECT_POOL = new ConcurrentHashMap<>();

    private SQLDialectFactory() {
    }

    /**
     * 获取方言实例
     *
     * @param className 类名
     * @return
     */
    public static SQLDialect getDialect(String className) {
        SQLDialect dialect = DIALECT_POOL.get(className);
        if (null == dialect) {
            synchronized (className.intern()) {
                dialect = DIALECT_POOL.computeIfAbsent(className,
                        key -> {
                            try {
                                return (SQLDialect) Class.forName(className).newInstance();
                            } catch (Exception e) {
                                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                            }
                        });
            }
        }
        return dialect;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("yao","姚");
        map.put("zheng","政");
        map.put("zuo","作");
        String values=map.computeIfAbsent("ya",key->"y"
        );
        System.out.println(map.toString());
    }
}
