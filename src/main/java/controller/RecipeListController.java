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
import org.json.JSONObject;

import dao.RecipeDAO;

@WebServlet("/RecipeListController")
public class RecipeListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public RecipeListController() {
        super();
    }

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  if(request.getParameter("rl") != null) {
        switch (request.getParameter("rl")) {
          case "list" : 
            getRecipeList(request,response);
          case "search" : 
            getSearchLIst(request,response);
          default : 
            PrintWriter out = response.getWriter();
            out.print("RECIPELIST CONTROLLER");
        }
      } else {
        PrintWriter out = response.getWriter();
        out.print("RECIPELIST CONTROLLER");
      }
	}

	   @SuppressWarnings("unchecked")
	   private void getRecipeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	       response.setContentType("application/json"); 
	       response.setCharacterEncoding("utf-8");
	       request.setCharacterEncoding("utf-8");
	       PrintWriter out = response.getWriter();
	       RecipeDAO recipe = new RecipeDAO();
	       JSONParser json_parser = new JSONParser(request.getReader());
	       List<Map<String,String>> recipe_list = null;
	       Map<String,String> category = null;
	       
	       try {
	         Map<String, Object> json_data = json_parser.object();
	         String limit = (String) json_data.get("limit");
	         String offset = (String) json_data.get("offset");
	         
	         if (json_data.get("category") != null ) {
	           category = (Map<String, String>) json_data.get("category");
	         }
	         
	         recipe_list = recipe.getRecipeList(Integer.parseInt(offset), Integer.parseInt(limit), category);
	         System.out.println(recipe_list);
	         
	         JSONArray json_array = new JSONArray();
	         
	         for (Map<String,String> x :recipe_list) {
	           JSONObject json_obj = new JSONObject(x);
	           
	           json_array.put(json_obj);
	         }
	         
	         out.print(json_array.toString());

	       } catch (ParseException e) {
	         e.printStackTrace();
	       }
	   }
	   
	   private void getSearchLIst(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     response.setContentType("application/json"); 
         response.setCharacterEncoding("utf-8");
         request.setCharacterEncoding("utf-8");
         PrintWriter out = response.getWriter();
         RecipeDAO recipe = new RecipeDAO();
         JSONParser json_parser = new JSONParser(request.getReader());
         List<Map<String,Object>> recipe_list = null;
         
         try {
          Map<String, Object> json_data = json_parser.object();
          
          String search = (String) json_data.get("search");
          Integer offset = Integer.parseInt((String) json_data.get("offset"));
          Integer limit = Integer.parseInt((String) json_data.get("limit"));
          
          recipe_list = recipe.getSearchList(offset, limit, search);
          
          JSONArray json_array = new JSONArray();
          
          for(Map<String,Object> x :recipe_list) {
            JSONObject json_obj = new JSONObject(x);
            
            json_array.put(json_obj);
          }
          
          out.print(json_array.toString());
          
        } catch (ParseException e) {
          e.printStackTrace();
        }
	   }

}
