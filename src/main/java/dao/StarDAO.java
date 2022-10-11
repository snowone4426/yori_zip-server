package dao;

import java.util.HashMap;
import java.util.Map;

import util.DBConnPool;

public class StarDAO extends DBConnPool {
  public Map<String,String> getStar (String recipe_id, String user_id) {
    Map<String,String> star = null;
    String sql = "SELECT * FROM star WHERE recipe_id = " + recipe_id + " AND user_id = " + user_id;
    
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql);
      
      if (rs.next()) {
        star = new HashMap<>();
        star.put("user_id", user_id);
        star.put("recipe_id", recipe_id);
        star.put("score", rs.getString("score"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    
    return star;
  }
  
  public String setStar (String recipe_id, String user_id, String score) {
    String result = "";
    String sql = "SELECT * FROM star WHERE recipe_id = " + recipe_id + " AND user_id = " + user_id;
    
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql);
      
      if (rs.next()) {
        String star_set_sql = "UPDATE star SET score = " + score + "WHERE recipe_id = " + recipe_id + " AND user_id  = " + user_id;
        stmt.executeUpdate(star_set_sql);
        
      } else {
        String star_set_sql = "INSERT INTO star VALUES (" + user_id + ", " + recipe_id + ", " + score + ")";
        stmt.executeUpdate(star_set_sql);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    
    
    return result;
  }
}
