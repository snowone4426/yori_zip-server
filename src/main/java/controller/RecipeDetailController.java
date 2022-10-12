package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.RecipeDAO;
import dao.RecipeObj;
import dao.RepleDAO;
import dao.StarDAO;
import dao.UserDAO;
import dao.UserObj;

@WebServlet("/RecipeDetailController")
public class RecipeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RecipeDetailController() {
        super();
    }

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  PrintWriter out = response.getWriter();
	  
	  if(request.getParameter("rd") != null) {
        switch (request.getParameter("rd")) {
          case "recipe" : 
            getRecipe(request,response);
          default : 
            out.print("RECIPEDETAIL CONTROLLER");
        }
      } else {
        
        out.print("RECIPEDETAIL CONTROLLER");
      }
	}


	private void getRecipe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      RecipeDAO recipedao = new RecipeDAO();
      RecipeObj recipe = new RecipeObj();
      RepleDAO repledao = new RepleDAO();
      StarDAO stardao = new StarDAO();
      UserObj my_info = (UserObj) session.getAttribute("user_info");
      JSONParser json_parser = new JSONParser(request.getReader());
      
      try {
        Map<String, Object> json_data = json_parser.object();
        Integer recipe_id = Integer.parseInt((String) json_data.get("recipe_id"));
        
        recipe = recipedao.getRecipe(recipe_id);
        
        JSONObject result = new JSONObject();
        
        JSONObject recipe_info = new JSONObject();
        
        recipe_info.put("recipe_id", recipe.getRecipe_id());
        recipe_info.put("user_id", recipe.getUser_id());
        recipe_info.put("title",recipe.getTitle());
        recipe_info.put("thumbnail",recipe.getThumbnail());
        recipe_info.put("level",recipe.getLevel());
        recipe_info.put("time",recipe.getTime());
        recipe_info.put("subtitle",recipe.getSubtitle());
        recipe_info.put("discription",recipe.getDiscription());
        recipe_info.put("create_at",recipe.getCreate_at());
        recipe_info.put("update_at",recipe.getUpdate_at());
        recipe_info.put("state",recipe.getState());
        recipe_info.put("star_score",recipe.getStar_score());
        
        JSONArray recipe_detail = new JSONArray();
        
        for(Map<String,String> x : recipe.getDetail()) {
          JSONObject json_obj = new JSONObject(x);
          recipe_detail.put(json_obj);
        }
        
        JSONObject ingredient = new JSONObject();
        
        for (String x : recipe.getIngredient().keySet()) {
          ingredient.put(x, new JSONObject(recipe.getIngredient().get(x)));
        }
        
        JSONArray reple_list = new JSONArray();
        
        for (Map<String,Object> x : repledao.getReple(recipe_id.toString(), 0, 10, null)) {
          reple_list.put(new JSONObject(x));
        }
        
        
        
        Map<String,String> star = stardao.getStar(recipe_id.toString(), my_info.getUser_id());
        
        if (star != null) {
          result.put("my_star_score", star.get("score"));
        }
        
        result.put("recipe_info", recipe_info);
        result.put("recipe_detail", recipe_detail);
        result.put("ingredient", ingredient);
        result.put("reple_count", repledao.repleCount(recipe_id.toString()));
        result.put("reple_list", reple_list);
        
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	}

}
