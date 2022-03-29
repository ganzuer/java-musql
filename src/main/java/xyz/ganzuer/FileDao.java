package xyz.ganzuer;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class FileDao {
    public boolean insert(File file){
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try{
            conn= JdbcUtils.getConnection();
            stmt=conn.createStatement();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uploadTime=sdf.format(file.getUploadTime());

            String sql="insert into files(id,filePath,fileName,fileSize,uploadTime)"+
                    "values("
                    +file.getId()
                    +",'"
                    +file.getFilePath()
                    +"','"
                    +file.getFileName()
                    +"','"
                    +file.getFileSize()
                    +"','"
                    +uploadTime
                    +"')";
            int num = stmt.executeUpdate(sql);
            if(num>0){
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtils.release(rs,stmt,conn);
        }
        return false;
    }
    public ArrayList<File> findAll(){
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        ArrayList<File> list=new ArrayList<File>();
        try{
            conn=JdbcUtils.getConnection();
            stmt=conn.createStatement();
            String sql="select * from files";
            rs=stmt.executeQuery(sql);
            while (rs.next()){
                File file = new File();
                file.setId(rs.getInt("id"));
                file.setFilePath(rs.getString("filePath"));
                file.setFileName(rs.getString("fileName"));
                file.setUploadTime(rs.getTimestamp("uploadTime"));
                file.setFileSize(rs.getLong("fileSize"));
                list.add(file);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtils.release(rs,stmt,conn);
        }
        return null;
    }
    public boolean delete(String id){
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try{
            conn=JdbcUtils.getConnection();
            stmt=conn.createStatement();
            String sql="delete from files where id='"+id+"'";
            int num=stmt.executeUpdate(sql);
            while (num>0){
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtils.release(rs,stmt,conn);
        }
        return false;
    }
    public File find(String id){
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try{
            conn=JdbcUtils.getConnection();
            stmt=conn.createStatement();
            String sql="select * from files where id='"+id+"'";
            rs=stmt.executeQuery(sql);
            while (rs.next()){
                File file=new File();
                file.setId(rs.getInt("id"));
                file.setFileName(rs.getString("fileName"));
                file.setFilePath(rs.getString("filePath"));
                file.setFileSize(rs.getLong("fileSize"));
                file.setUploadTime(rs.getDate("uploadTime"));
                return file;
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
