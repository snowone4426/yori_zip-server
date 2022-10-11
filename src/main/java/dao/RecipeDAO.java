package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DBConnPool;

public class RecipeDAO extends DBConnPool {
	
	
	public RecipeDAO () {
		super();
	}
	// 태그가 일치하는 레시피 중 상위 4개를 뽑아 배열로 만들어 줌
	public List<RecipeObj> getThemeRecipeList (Integer search_id) {
		List<RecipeObj> theme_list = new ArrayList<RecipeObj>();
		String sql = "SELECT ROWNUM as num, recipe_id, title, subtitle, thumbnail FROM ("
				+ "SELECT count(*) as popularity , a.recipe_id, a.title, a.subtitle, a.thumbnail "
				+ "FROM (SELECT r.recipe_id, r.title, r.subtitle, r.thumbnail FROM recipe "
				+ "INNER JOIN recipetag t ON r.recipe_id=t.recipe_id WHERE t.tag_id='" + search_id + "') a "
				+ "INNER JOIN bookmark b ON a.recipe_id = b.recipe_id "
				+ "GROUP BY a.recipe_id, a.title, a.subtitle, a.thumbnail "
				+ "ORDER BY popularity DESC "
				+ ")WHERE ROWNUM < 5;";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				RecipeObj recipe = new RecipeObj();
				
				recipe.setRecipe_id(rs.getString("recipe_id"));
				recipe.setTitle(rs.getString("title"));
				recipe.setSubtitle(rs.getString("subtitle"));
				recipe.setThumbnail(rs.getString("thumbnail"));
				
				theme_list.add(recipe);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		  close();
		}
		
		return theme_list;
	}
	
	// 북마크가 많은 상위 8개를 뽑아서 보여줌.
	public List<RecipeObj> getHotRecipeList () {
	  List<RecipeObj> hot_list = new ArrayList<RecipeObj>();
	  String sql = "SELECT ROWNUM, recipe_id, title, subtitle, thumbnail FROM ("
	      + "SELECT r.recipe_id, r.title, r.subtitle, r.thumbnail FROM recipe r "
	      + "INNER JOIN bookmark b ON r.recipe_id = b.recipe_id "
	      + "GROUP BY r.recipe_id, r.title, r.subtitle, r.thumbnail ORDER BY count(*) DESC) "
	      + "WHERE ROWNUM < 9";
	  
	  try {
        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
          RecipeObj recipe = new RecipeObj();
          
          recipe.setRecipe_id(rs.getString("recipe_id"));
          recipe.setTitle(rs.getString("title"));
          recipe.setSubtitle(rs.getString("subtitle"));
          recipe.setThumbnail(rs.getString("thumbnail"));
          
          hot_list.add(recipe);
        }
        
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
	  
	  return hot_list;
	}
	
	// 카테고리가 선택되어 있다면 그에 맞는 데이터에 limit만큼 offset페이지에 있는 정보를 보내줌.
	public List<RecipeObj> getRecipeList (Integer offset, Integer limit, Map<String,List<String>> category) {
	  List<RecipeObj> recipe_list = new ArrayList<RecipeObj>();
	  String sql = "SELECT ROWNUM, recipe_id, title, subtitle, thumbnail "
	      + "FROM (SELECT r.recipe_id, r.title, r.subtitle, r.thumbnail FROM recipe r ";
	  
	  if (category != null) {
	    sql += "INNER JOIN recipecategory rc ON r.recipe_id = rc.recipe_id "
	        + "INNER JOIN category c ON c.category_id = rc.category_id "
	        + "WHERE ";
	    
	    for (String name : category.keySet()) {
	      sql += "c.name = '" + name + "' OR ";
	    }
	    
	    for (String name : category.keySet()) {
	      for ( String subcategory : category.get(name))  {
	        sql += "rc.sub_category = '" + subcategory + "' OR ";
	      }
	    }
	    
	    sql = sql.substring(0,sql.length()-3);
	  }
	      
	  sql += "ORDER BY r.created_at DESC)";
	  
	  if (offset != null && limit != null) {
	    sql += " WHERE ROWNUM BETWEEN " + offset*limit + " AND " + (offset + 1)*limit;
	  }
	  
	  try {
	    stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
          RecipeObj recipe = new RecipeObj();
          
          recipe.setRecipe_id(rs.getString("recipe_id"));
          recipe.setTitle(rs.getString("title"));
          recipe.setSubtitle(rs.getString("subtitle"));
          recipe.setThumbnail(rs.getString("thumbnail"));
          
          recipe_list.add(recipe);
        }
        
        
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
	  
	  return recipe_list;
	}
	
	// 검색어가 tag, title, subtitle과 일치하면 페이지 네이션 된 값을 반환
	public List<RecipeObj> getSearchList(Integer offset, Integer limit, String search) {
	  List<RecipeObj> recipe_list = new ArrayList<RecipeObj>();
	  String sql ="SELECT ROWNUM, recipe_id, title, subtitle, thumbnail, difficulty, time, created_at, state FROM ("
	      + "SELECT r.recipe_id, r.title, r.subtitle, r.thumbnail, r.difficulty, r.time, r.created_at, r.state FROM recipe "
	      + "INNER JOIN recipetag rt ON r.recipe_id = rt.recipe_id "
	      + "INNER JOIN tag t ON rt.tag_id = t.tag_id "
	      + "WHERE t.name LIKE '%" + search + "%' OR r.title LIKE '%" + search + "%' OR r.subtitle LIKE '%" + search + "%' "
	      + "ORDER BY created_at DESC"
	      + ")";
	  
	  if (offset != null && limit != null) {
        sql += " WHERE ROWNUM BETWEEN " + offset*limit + " AND " + (offset + 1)*limit;
      }
	  
	  try {
        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
          RecipeObj recipe = new RecipeObj();
          
          recipe.setRecipe_id(rs.getString("recipe_id"));
          recipe.setTitle(rs.getString("title"));
          recipe.setSubtitle(rs.getString("subtitle"));
          recipe.setThumbnail(rs.getString("thumbnail"));
          recipe.setLevel(rs.getString("level"));
          recipe.setTime(rs.getString("time"));
          recipe.setCreate_at(rs.getDate("create_at"));
          recipe.setState(rs.getString("state"));
          
          recipe_list.add(recipe);
        }
        
        
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
	  
	  return recipe_list;
	}
	
	// 레시피 전체정보
	public RecipeObj getRecipe (Integer recipe_id) {
	  RecipeObj result = new RecipeObj();
	  
	  //---레시피 정보----------------------------------------------------------------
	  try {
	    String recipe_sql = "SELECT * FROM recipe WHERE recipe_id = " + recipe_id;
	    stmt = con.createStatement();
        rs = stmt.executeQuery(recipe_sql);
        
        if(rs.next()) {
          result.setRecipe_id(rs.getString("recipe_id"));
          result.setUser_id(rs.getString("user_id"));
          result.setTitle(rs.getString("title"));
          result.setThumbnail(rs.getString("thumbnail"));
          result.setLevel(rs.getString("difficulty"));
          result.setTime(rs.getString("time"));
          result.setSubtitle(rs.getString("subtitle"));
          result.setDiscription(rs.getString("discription"));
          result.setCreate_at(rs.getDate("created_at"));
          result.setUpdate_at(rs.getDate("updated_at"));
          result.setState(rs.getString("state"));
        }
        
        //---별점----------------------------------------------------------------
        String star_sql = "SELECT ROUND(AVG(score),1) as star_score FROM star "
            + "WHERE recipe_id = " + recipe_id + " "
            + "GROUP BY recipe_id";
        rs = stmt.executeQuery(star_sql);
        
        if(rs.next()) { 
          result.setStar_score(rs.getString("star_score"));
        }
        
        //---만드는 법----------------------------------------------------------------
        String detail_sql = "SELECT * FROM recipedetail WHERE recipe_id = " + recipe_id + " ORDER BY detail_id";
        rs = stmt.executeQuery(detail_sql);
        List<Map<String,String>> detailList = new ArrayList<Map<String,String>>();
        
        while (rs.next()) {
          Map<String,String> detail = new HashMap<String,String>();
          detail.put("detail_id", rs.getString("detail_id"));
          detail.put("description", rs.getString("description"));
          detail.put("photo", rs.getString("photo"));
          
          detailList.add(detail);
        }
        
        result.setDetail(detailList);
        
        //---카테고리----------------------------------------------------------------
        Map<String,String> category = new HashMap<String,String>();
        List<String> main_category = new ArrayList<String>();
        String category_sql = "SELECT c.category_id, c.name FROM category c "
            + "INNER JOIN recipecategory rc ON rc.category_id = c.category_id "
            + "WHERE rc.recipe_id = " + recipe_id;
        rs = stmt.executeQuery(category_sql);
        
        while (rs.next()) {
          main_category.add(rs.getString("name"));
        }
        
        for (String name : main_category) {
          String sub_category = "";
          String sub_category_sql = "SELECT rc.category_id, rc.recipe_id, rc.sub_category, c.name FROM recipecategory rc "
              + "INNER JOIN category c ON rc.category_id = c.category_id "
              + "WHERE rc.recipe_id = " + recipe_id + " AND c.name = '" + name + "' "
              + "ORDER BY category_id";
          
          rs = stmt.executeQuery(sub_category_sql);
          
          if (rs.next()) {
            sub_category = rs.getString("sub_category");
          }
          
          category.put(name, sub_category);
        }
        
        result.setCategory(category);
        
        //---재료----------------------------------------------------------------
        Map<String,Map<String,String>> ingredient = new HashMap<String,Map<String,String>>();
        List<String> ingredient_group = new ArrayList<String>();
        String ingredient_group_sql = "SELECT ingredient_group FROM ingredient WHERE recipe_id = " + recipe_id;
        rs = stmt.executeQuery(ingredient_group_sql);
        
        while (rs.next()) {
          ingredient_group.add(rs.getString("ingredient_group"));
        }
        
        for (String group_name : ingredient_group) {
          String ingredient_sql = "SELECT * FROM ingredient WHERE recipe_id = 1 AND ingredient_group = '" + group_name + "'";
          rs = stmt.executeQuery(ingredient_group_sql);
          
          while (rs.next()) {
            Map<String,String> sub_category = new HashMap<String,String>();
            
            sub_category.put("name", rs.getString("name"));
            sub_category.put("quantity", rs.getString("quantity"));
            
            ingredient.put(group_name, sub_category);
          }
        }
        
        result.setIngredient(ingredient);
        
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
	  
	  
	  
	  return result;
	}
	
	//찜 기능
	public Boolean setBookmark (String recipe_id, String user_id) {
	  Boolean result = false;
	  String sql = "SELECT * FROM bookmark WHERE recipe_id = " + recipe_id + " AND user_id = " + user_id;
	  
	  try {
	    stmt = con.createStatement();
	    rs = stmt.executeQuery(sql);
	    
	    if (rs.next()) {
	      String bookmark_sql = "DELETE FROM bookmark WHERE recipe_id = " + recipe_id +" AND user_id = " + user_id;
	      stmt.executeUpdate(bookmark_sql);
	    } else {
	      String bookmark_sql = "INSERT INTO bookmark VALUES (" + user_id + ", " + recipe_id + ")";
          stmt.executeUpdate(bookmark_sql);
          result = true;
	    }
        
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
	  
	  return result;
	}
	
	//레시피 삭제 기능
	public Boolean deleteRecipe (String recipe_id, String user_id) {
	  Boolean result = false;
	  String sql = "DELETE FROM recipe WHERE recipe_id = "+ recipe_id +" AND user_id = " + user_id;
	  
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
	
	// 레시피 등록 기능
	public Boolean createRecipe (RecipeObj recipe) {
	  Boolean result = false;
	  
	  try {
	    stmt = con.createStatement();
	    Boolean recipe_result = false;
	    String recipe_sql = "INSERT INTO recipe(recipe_id,user_id,title,thumbnail,difficulty,time,subtitle,discription,state) "
	          + "VALUES (seq_recipe_id.nextval,?,?,?,?,?,?,?,?);";
        psmt = con.prepareStatement(recipe_sql);
        
        psmt.setString(0, recipe.getUser_id());
        psmt.setString(1,recipe.getTitle());
        psmt.setString(2,recipe.getThumbnail());
        psmt.setString(3,recipe.getLevel());
        psmt.setString(4,recipe.getTime());
        psmt.setString(5,recipe.getSubtitle());
        psmt.setString(6,recipe.getDiscription());
        psmt.setString(7,recipe.getState());
        
        recipe_result = psmt.executeUpdate() > 0;
        
        Boolean detail_result = false;
        for (Map<String,String> detail: recipe.getDetail()) {
          String detail_sql = "INSERT INTO recipedetail(detail_id, recipe_id, description, photo) VALUES (?, ?, ?, ?)";
          psmt = con.prepareStatement(detail_sql);
          psmt.setString(0,detail.get("detail_id"));
          psmt.setString(1,detail.get("recipe_id"));
          psmt.setString(2,detail.get("description"));
          psmt.setString(3,detail.get("photo"));
          
          if(psmt.executeUpdate() > 0) {
            detail_result = true;
          } else {
            detail_result = false;
            break;
          }
        }
        
        Boolean category_result = false;
        for (String key : recipe.getCategory().keySet()) {
          String main_category_sql = "SELECT category_id FROM category WHERE name = '"+ key +"'";
          String category_id = "";
          rs = stmt.executeQuery(main_category_sql);
          
          if (rs.next()) {
            category_id = rs.getString("category_id");
          } else {
            break;
          }
          
          String sub_category_sql = "INSERT INTO recipecategory VALUES (?,?,?)";
          psmt = con.prepareStatement(sub_category_sql);
          psmt.setString(0, category_id);
          psmt.setString(1, recipe.getRecipe_id());
          psmt.setString(2, recipe.getCategory().get(key));
          
          if (psmt.executeUpdate(sub_category_sql) > 0) {
            category_result = true;
          } else {
            category_result = false;
            break;
          }
        }
        
        Boolean ingredient_result = false;
        for (String group_key : recipe.getIngredient().keySet()) {
            String ingredient_sql = "INSERT INTO ingredient VALUES (?,?,?,?)";
            psmt = con.prepareStatement(ingredient_sql);
            psmt.setString(0,recipe.getRecipe_id());
            psmt.setString(1, recipe.getIngredient().get(group_key).get("name"));
            psmt.setString(2, group_key);
            psmt.setString(3, recipe.getIngredient().get(group_key).get("quantity"));
            
            if (psmt.executeUpdate() > 0) {
              ingredient_result = true;
            } else {
              ingredient_result = false;
              break;
            }
        }
        
        
        if (recipe_result && detail_result && category_result && ingredient_result) {
          result = true;
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
	  
	  return result;
	}
	
	public Map<String,String> getCategory () {
	  Map<String,String> category = new HashMap<String,String>();
      List<String> main_category = new ArrayList<String>();
      String category_sql = "SELECT c.category_id, c.name FROM category";
      
      try {
        stmt = con.createStatement();
        rs = stmt.executeQuery(category_sql);
        
        while (rs.next()) {
          main_category.add(rs.getString("name"));
        }
        
        for (String name : main_category) {
          String sub_category_sql = "SELECT rc.category_id, rc.recipe_id, rc.sub_category, c.name FROM recipecategory rc "
              + "INNER JOIN category c ON rc.category_id = c.category_id "
              + "WHERE  c.name = '" + name + "'";
          
          rs = stmt.executeQuery(sub_category_sql);
          
          if (rs.next()) {
            category.put(name, rs.getString("sub_category"));
          }
          
        }
        
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
      
      return category;
	}
	
	public List<RecipeObj> getMyRecipe (String user_id, Integer offset, Integer limit) {
	  List<RecipeObj> my_recipe = new ArrayList<>();
	  String sql = "SELECT ROWNUM as num, a.* FROM ("
	      + "SELECT r.recipe_id, r.user_id, r.thumbnail, r.title, r.subtitle, r.created_at, r.updated_at, ROUND(AVG(s.score),1) AS star_score, COUNT(*) AS reple_count FROM recipe r "
	      + "INNER JOIN star s ON s.recipe_id = r.recipe_id "
	      + "INNER JOIN reple re ON re.recipe_id = r.recipe_id "
	      + "GROUP BY r.user_id, r.thumbnail, r.title, r.subtitle, r.created_at, r.updated_at) a "
	      + "WHERE user_id = "+ user_id +" AND ROWNUM BETWEEN " + offset*limit + " AND " + (offset+1)*limit;
	  
	  try {
        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
          RecipeObj recipe = new RecipeObj();
          
          recipe.setNum(rs.getString("num"));
          recipe.setRecipe_id(rs.getString("recipe_id"));
          recipe.setUser_id(rs.getString("user_id"));
          recipe.setThumbnail(rs.getString("thumbnail"));
          recipe.setTitle(rs.getString("title"));
          recipe.setSubtitle(rs.getString("subtitle"));
          recipe.setCreate_at(rs.getDate("creaeted_at"));
          recipe.setUpdate_at(rs.getDate("upadated_at"));
          recipe.setStar_score(rs.getString("star_score"));
          recipe.setReple_count(rs.getString("reple_count"));
          
          my_recipe.add(recipe);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        close();
      }
	  
	  return my_recipe;
	}
	
}
