package dao;

import java.util.ArrayList;
import java.util.List;

import util.DBConnPool;

public class RepleDAO extends DBConnPool {
  public List<RepleObj> getReple(String recipe_id, Integer offset, Integer limit, String user_id) {
    List<RepleObj> reple_list = new ArrayList<>();
    String sql = "SELECT ROWNUM AS num, re.* FROM reple re "
        + "WHERE recipe_id = " + recipe_id + " AND num BETWEEN " + offset*limit + " AND " + (offset+1)*limit;
    
    if (user_id != null) {
      sql += " AND user_id = " + user_id;
    }
    
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql);
      
      while(rs.next()) {
        RepleObj reple = new RepleObj();
        
        reple.setReple_id(rs.getString("reple_id"));
        reple.setRecipe_id(rs.getString("recipe_id"));
        reple.setUser_id(rs.getString("user_id"));
        reple.setContents(rs.getString("contents"));
        reple.setCreate_at(rs.getDate("created_at"));
        reple.setUpdate_at(rs.getDate("update_at"));
        reple.setState(rs.getString("state"));
        
        reple_list.add(reple);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    
    return reple_list;
  }
  
  public Integer repleCount(String recipe_id) {
    Integer result = 0;
    String sql = "SELECT COUNT(*) AS count FROM reple WHERE recipe_id = " + recipe_id;
    
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql);
      
      if(rs.next()) {
        result = Integer.parseInt(rs.getString("count"));
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    
    return result;
  }
  
  public Boolean createReple (String user_id, String recipe_id, String contents) {
    Boolean result = false;
    String sql = "INSERT INTO reple(reple_id,user_id,recipe_id,contents,state) VALUES (seq_reple_id.nextval, " + user_id + ", " + recipe_id + ", " + contents + ", common)";
    
    try {
      stmt = con.createStatement();
      
      if(stmt.executeUpdate(sql) > 0) {
        result = true;
      };
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    
    return result;
  }
  
  public Boolean updateReple (String reple_id, String contents) {
    Boolean result = false;
    String sql = "UPDATE reple SET contents = '" + contents + "' WHERE reple_id " + reple_id;
    
    try {
      stmt = con.createStatement();
      
      if (stmt.executeUpdate(sql) > 0) {
        result = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
     
    return result;
  }
  
  public Boolean deleteReple(String reple_id) {
    Boolean result = false;
    String sql = "DELETE FROM reple WHERE reple_id = " + reple_id;
    
    try {
      stmt = con.createStatement();
      
      if (stmt.executeUpdate(sql) > 0) {
        result = true;
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    
    
    return result;
  }
}
