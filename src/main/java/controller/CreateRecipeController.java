package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.RecipeDAO;
import dao.RecipeObj;

@WebServlet("/CreateRecipeController")
public class CreateRecipeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateRecipeController() {
        super();
    }

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  if(request.getParameter("cr") != null) {
        switch (request.getParameter("cr")) {
          case "create" : 
            createRecipe(request,response);
          case "category" :
            getCategory(request,response);
          default : 
            PrintWriter out = response.getWriter();
            out.print("CREATE RECIPE CONTROLLER");
        }
      } else {
        PrintWriter out = response.getWriter();
        out.print("CREATE RECIPE CONTROLLER");
      }
	}
	
	@SuppressWarnings("unchecked")
  private void createRecipe (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      JSONParser json_parser = new JSONParser(request.getReader());
      RecipeObj recipe = new RecipeObj();
      RecipeDAO recipedao = new RecipeDAO();
      
      try {
        Map<String, Object> json_data = json_parser.object();
        
        Map<String, Object> recipe_info = (Map<String, Object>) json_data.get("recipe_info");
        recipe.setUser_id((String)recipe_info.get("user_id"));
        recipe.setTitle((String) recipe_info.get("title"));
        recipe.setThumbnail((String) recipe_info.get("thumbnail"));
        recipe.setLevel((String) recipe_info.get("lever"));
        recipe.setTime((String) recipe_info.get("time"));
        recipe.setSubtitle((String) recipe_info.get("subtitle"));
        recipe.setDiscription((String) recipe_info.get("discription"));
        
        List<Map<String,String>> detail = (List<Map<String,String>>) json_data.get("recipe_detail");
        recipe.setDetail(detail);
        
        Map<String,Map<String,String>> ingredient = (Map<String,Map<String,String>>) json_data.get("ingredient");
        recipe.setIngredient(ingredient);
        
        out.print(recipedao.createRecipe(recipe));
        
      } catch (ParseException e) {
        e.printStackTrace();
      }
	}
	
	private void getCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      RecipeDAO recipe = new RecipeDAO();
      Map<String,List<String>> map = recipe.getCategory();
      
      JSONObject json_obj = new JSONObject();
      
      for (String x : map.keySet()) {
        List<String> sub_category = map.get(x);
        JSONArray json_array = new JSONArray(sub_category);
        
        try {
          json_obj.put(x, json_array);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
      
      out.print(json_obj.toString());
    }

}
