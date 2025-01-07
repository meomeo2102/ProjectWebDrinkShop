package dao;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnectionPool {
	private static HikariDataSource dataSource;

	static {
		try {
			// Cấu hình HikariCP
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl("jdbc:sqlserver://LAPTOP-HQR0AC60\\ADMIN:1433;databaseName=SkibidiDrink;encrypt=true;trustServerCertificate=true;");
			config.setUsername("sa");
			config.setPassword("123");

			// Các cấu hình Connection Pool
			config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			config.setMaximumPoolSize(20); // Số lượng kết nối tối đa
			config.setMinimumIdle(5); // Số lượng kết nối tối thiểu
			config.setIdleTimeout(30000); // Thời gian idle tối đa (ms)
			config.setMaxLifetime(1800000); // Thời gian tồn tại tối đa của một connection (ms)
			config.setConnectionTimeout(20000); // Thời gian chờ để lấy một connection (ms)

			// Tạo DataSource
			dataSource = new HikariDataSource(config);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing HikariCP connection pool", e);
		}
	}

	// Phương thức để lấy DataSource
	public static DataSource getDataSource() {
		return dataSource;
	}

	// Optional: Đóng Connection Pool khi không sử dụng nữa
	public static void shutdown() {
		if (dataSource != null) {
			dataSource.close();
		}
	}

	public static void main(String[] args) {
		try (Connection connection = getDataSource().getConnection()) {
	        if (connection != null) {
	            System.out.println("Kết nối cơ sở dữ liệu thành công!");
	        } else {
	            System.out.println("Kết nối cơ sở dữ liệu thất bại!");
	        }
	    } catch (SQLException e) {
	        System.out.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


}
