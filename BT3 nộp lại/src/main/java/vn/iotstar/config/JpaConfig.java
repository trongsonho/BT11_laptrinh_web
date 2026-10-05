package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JpaConfig {

    private static volatile EntityManagerFactory factory;

    private static EntityManagerFactory getFactory() {
        if (factory == null) {
            synchronized (JpaConfig.class) {
                if (factory == null) {
                    Map<String, Object> properties = new HashMap<>();
                    override(properties, "DB_URL", "jakarta.persistence.jdbc.url");
                    override(properties, "DB_USERNAME", "jakarta.persistence.jdbc.user");
                    override(properties, "DB_PASSWORD", "jakarta.persistence.jdbc.password");
                    factory = Persistence.createEntityManagerFactory("jpa-hibernate-mysql", properties);
                }
            }
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    private static void override(Map<String, Object> properties, String key, String jpaKey) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key);
        }
        if (value != null) {
            properties.put(jpaKey, value);
        }
    }
}
