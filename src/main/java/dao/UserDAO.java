package dao;

import models.User;
import java.sql.*;
public class UserDAO {
	
    private final Connection con;

    public UserDAO(Connection connection) {
    	this.con = connection;
    }
    
    public User getUserRs (ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setAddress(rs.getString("address"));
        user.setPhone(rs.getString("phone_number"));
        user.setImg(rs.getString("images"));
        return user;
    }
    public int editProfile(User user) {

        return -1;
    }
    public void saveImg (String path  , int id ) {
        String sql = "UPDATE ListUser set img = ? where user_id = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, path);
            ps.setString(2, id+"");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getUserImg (int id ) throws SQLException {
        String sql = "SELECT img FROM ListUser WHERE user_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id+"");
            ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               return rs.getString(1);
           }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public User getUser(int id) {
        String sql = "select * from ListUser where user_id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id+"");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return getUserRs(rs);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updatePassword (String email , String password) throws SQLException {
        String query = "UPDATE users SET password=? WHERE email=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, password);
            ps.setString(2, email);
            int row = ps.executeUpdate();
            System.out.println(row);
            return row > 0;
    }
    
    public boolean registerUser(User user) throws SQLException {
        String sql = "insert into users  (username, password, email, phone_number) " +
                "VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return true;
    }
    
    public boolean checkEmailExist(String email) {
        String sql = "select email from users where email=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("email").equals(email)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    return false;
    
    
    }
  //public User getLogin(String email, String password) throws SQLException {
        //String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        //try (PreparedStatement ps = con.prepareStatement(sql)) {
            //ps.setString(1, email);
            //ps.setString(2, password);

            //try (ResultSet rs = ps.executeQuery()) {
                //if (rs.next()) {
                    //User user = getUserRs(rs); // Chuyển ResultSet thành User
                    //int isAdmin = rs.getInt("is_admin"); // Lấy giá trị is_admin từ cơ sở dữ liệu
                    //user.setIsAdmin(isAdmin); // Gán quyền admin cho user
                    
                    // Trả về đối tượng User với quyền admin hoặc user
                    //return user;
                //}
            //}
        //}
        //return null;
    //}

    
    public User getLogin(String email, String password) throws SQLException {
    	PreparedStatement ps = con.prepareStatement("select * from users where email = ? and password = ?");
    	ps.setString(1, email);
    	ps.setString(2, password);
    	try (ResultSet rs = ps.executeQuery()) {
    		if (rs.next()) {
    			return getUserRs(rs); // Hàm này xử lý logic để chuyển ResultSet thành User
    		}
    	}
    	return null;
    }
    
    public static void main(String[] args) throws SQLException {
		UserDAO dao = new UserDAO(DBConnectionPool.getDataSource().getConnection());
		User s = dao.getLogin("admin@example.com", "123");
		System.out.println(s.toString());
		
	}
	public boolean checkUsername(String username) {
		 String sql = "select username from users where username=?";
	        try {
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setString(1, username);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                if (rs.getString("username").equals(username)) {
	                    return true;
	                }
	            }
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    return false;
	    
	    
	}
	public User findByEmail(String email) {
		try {
        	PreparedStatement ps = con.prepareStatement("select * from users where email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getUserRs(rs); // Hàm này xử lý logic để chuyển ResultSet thành User
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		return null;
	}
}
