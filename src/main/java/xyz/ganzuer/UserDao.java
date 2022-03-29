package xyz.ganzuer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDao {
    public User find(String name){
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try{
            conn=JdbcUtils.getConnection();
            stmt=conn.createStatement();
            String sql="select * from users where name='"+name+"'";
            rs=stmt.executeQuery(sql);
            while (rs.next()){
                User user=new User();
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtils.release(rs,stmt,conn);
        }
        return null;
    }
}
