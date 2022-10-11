package dao;

import java.util.ArrayList;
import java.util.List;

import util.DBConnPool;

public class BannerDAO extends DBConnPool {
  public List<BannerObj> getBannerList () {
    List<BannerObj> banner_list = new ArrayList<>();
    String sql = "SELECT * FROM banner WHERE start_date <= sysdate AND end_date >= sysdate";
    
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql);
      
      while(rs.next()) {
        BannerObj banner = new BannerObj();
        
        banner.setBanner_id(rs.getString("banner_id"));
        banner.setPhoto(rs.getString("photo"));
        banner.setAlt(rs.getString("alt"));
        banner.setCreated_at(rs.getDate("created_at"));
        banner.setStart_date(rs.getDate("start_date"));
        banner.setEnd_date(rs.getDate("end_date"));
        banner.setSearch(rs.getString("search"));
        
        banner_list.add(banner);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    
    return banner_list;
  }
}
