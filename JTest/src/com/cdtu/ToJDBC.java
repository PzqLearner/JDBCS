package com.cdtu;

import java.sql.*;
public class ToJDBC {
    public static void main(String[] args) throws SQLException {
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");//注册驱动
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/netweek","root","111111");
            st= con.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql="select * from nameword";
        try {
            rs=st.executeQuery(sql);
            while (rs.next()){
                String name=rs.getString("name");
                String password=rs.getString("password");
                System.out.println(name+" : "+password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (rs!=null){
                rs.close();
            }
            if (st!=null){
                st.close();
            }
            if (con!=null){
                con.close();
            }
        }
    }
}
