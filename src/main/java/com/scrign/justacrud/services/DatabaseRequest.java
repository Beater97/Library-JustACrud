package com.scrign.justacrud.services;

import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DatabaseRequest {

    @Autowired
    private DataSource dataSource;

    public boolean executeUpdate(String query) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean result = false;
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> List<T> customQuery(String query, Class<T> clazz) {
        List<T> records = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
        
            while (rs.next()) {
                T record = clazz.getDeclaredConstructor().newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    setProperty(record, columnName, columnValue);
                }
                records.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public String customQuery(String query) throws JsonProcessingException {
		List<Map<String, Object>> records = new ArrayList<>();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Map<String, Object> record = new LinkedHashMap<>();
				ResultSetMetaData metaData = rs.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					record.put(metaData.getColumnName(i), rs.getString(i));
				}
				records.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(records);
	}

    public String customQuery(String query,int maxRow) throws JsonProcessingException {
		List<Map<String, Object>> records = new ArrayList<>();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

                ps.setMaxRows(maxRow);
			while (rs.next()) {
				Map<String, Object> record = new LinkedHashMap<>();
				ResultSetMetaData metaData = rs.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					record.put(metaData.getColumnName(i), rs.getString(i));
				}
				records.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(records);
	}
    
    public <T> List<T> customQuery(String query, int maxRow, Class<T> clazz) {
        List<T> records = new ArrayList<>();
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setMaxRows(maxRow);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    T record = clazz.getDeclaredConstructor().newInstance();
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = rs.getObject(i);
                        setProperty(record, columnName, columnValue);
                    }
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public <T> T findFirst(String query, int maxRow, Class<T> clazz) {
        List<T> records = new ArrayList<>();
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setMaxRows(maxRow);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    T record = clazz.getDeclaredConstructor().newInstance();
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = rs.getObject(i);
                        setProperty(record, columnName, columnValue);
                    }
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records.getFirst();
    }

    public <T> T findFirst(String query, Class<T> clazz) {
        List<T> records = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
        
            while (rs.next()) {
                T record = clazz.getDeclaredConstructor().newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    setProperty(record, columnName, columnValue);
                }
                records.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records.getFirst();
    }

    private <T> void setProperty(T record, String fieldName, Object fieldValue) {
        if (fieldValue == null)
            return;

        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = findCompatibleMethod(record.getClass(), methodName, fieldValue);

        if (method != null) {
            try {
                method.invoke(record, fieldValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Method findCompatibleMethod(Class<?> clazz, String methodName, Object fieldValue) {
        Method[] methods = clazz.getMethods();
        methodName = methodName.replaceAll("[^a-zA-Z0-9]", "");
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName) && method.getParameterTypes().length == 1) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (isCompatibleType(fieldValue, parameterType)) {
                    return method;
                }
            }
        }
        return null;
    }

    private boolean isCompatibleType(Object value, Class<?> parameterType) {
        if (parameterType.isInstance(value)) {
            return true;
        }
        if (value instanceof Integer
                && (parameterType == int.class || parameterType == long.class || parameterType == Long.class)) {
            return true;
        }
        if (value instanceof Long
                && (parameterType == int.class || parameterType == Integer.class || parameterType == long.class)) {
            return true;
        }
        if (value instanceof Timestamp && parameterType == LocalDateTime.class) {
            return true;
        }
        if (value instanceof Blob && parameterType == byte[].class) {
            return true;
        }
        if (value instanceof Clob && parameterType == String.class) {
            return true;
        }
        return false;
    }

}
