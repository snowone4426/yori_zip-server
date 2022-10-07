package dao;

import java.util.ArrayList;
import java.util.List;

import util.DBConnPool;

public class RecipeDAO extends DBConnPool {
	
	
	public RecipeDAO () {
		super();
	}
	
	public List<RecipeObj> getThemeRecipeList (String search) {
		List<RecipeObj> theme_list = new ArrayList<RecipeObj>();
		String sql = "SELECT ROWNUM as num, recipe_id, title, subtitle, thumbnail FROM ("
				+ "SELECT count(*) as popularity , a.recipe_id, a.title, a.subtitle, a.thumbnail "
				+ "FROM (SELECT r.recipe_id, r.title, r.subtitle, r.thumbnail FROM recipe "
				+ "INNER JOIN recipetag t ON r.recipe_id=t.recipe_id WHERE t.tag='" + search + "') a "
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
		}
		
		return theme_list;
	}
}
