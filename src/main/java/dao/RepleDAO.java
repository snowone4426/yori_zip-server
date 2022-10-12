package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DBConnPool;

public class RepleDAO extends DBConnPool {
  public List<Map<String,Object>> getReple(String recipe_id, Integer offset, Integer limit, String user_id) {
    List<Map<String,Object>> reple_list = new ArrayList<>();
    String sql = "SELECT ROWNUM AS num, re.* FROM reple re "
        + "WHERE recipe_id = " + recipe_id + " AND ROWNUM BETWEEN " + offset*limit + " AND " + (offset+1)*limit;
    
    if (user_id != null) {
      sql += " AND user_id = " + user_id;
    }
    
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql);
      
      while(rs.next()) {
        Map<String,Object> reple = new HashMap<String,Object>();
        
        reple.put("reple_id",rs.getString("reple_id"));
        reple.put("recipe_id",rs.getString("recipe_id"));
        reple.put("user_id",rs.getString("user_id"));
        reple.put("contents",rs.getString("contents"));
        reple.put("created_at",rs.getDate("created_at"));
        reple.put("update_at",rs.getDate("update_at"));
        reple.put("state",rs.getString("state"));
        
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
