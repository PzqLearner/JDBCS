package Jtest;

import java.sql.*;

public class Test1 {
    public static void main(String[] args) throws SQLException {
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");//注册驱动
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital","root","111111");
            st= con.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql1="delete from department where Dept_no=3";
        try {
            int row=st.executeUpdate(sql1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql="select * from department";
        try {
            rs=st.executeQuery(sql);
            while (rs.next()){
                String name=rs.getString("Dept_na");
                String number=rs.getString("Dept_no");
                System.out.println(number+" : "+name);
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
