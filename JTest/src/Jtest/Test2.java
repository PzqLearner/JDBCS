package Jtest;

import com.cdtu.Tools;
import java.sql.*;

public class Test2 {
    public static void main(String[] args) {
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet re=null;
        try {
            con= Tools.getConnection();
            String sql="select Dept_no from department where Dept_na= ?";
            ps=con.prepareStatement(sql);
            ps.setString(1,"陈飞");
            re= ps.executeQuery();
            while (re.next()){
                System.out.println(re.getString("Dept_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Tools.close(ps,con,re);
        }
    }
}
