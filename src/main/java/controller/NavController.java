package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.RecipeDAO;
import dao.RecipeObj;


@WebServlet("/NavController")
public class NavController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public NavController() {
        super();
    }

	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  if(request.getParameter("n") != null) {
	    switch (request.getParameter("n")) {
	      case "category" : 
	        getCategory(request,response);
	        break;
	      case "viewrecipe" : 
	        setViewRecipe(request,response);
	        break;
	      default : 
	        PrintWriter out = response.getWriter();
	          out.print("MAIN CONTROLLER");
	    }
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
	
	private void setViewRecipe (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      @SuppressWarnings("unchecked")
      List<RecipeObj> recent_view = (ArrayList<RecipeObj>) session.getAttribute("recent_view");
      RecipeObj recipe = new RecipeObj();
      
      recipe.setRecipe_id(request.getParameter("recipe_id"));
      recipe.setThumbnail(request.getParameter("thumbnail"));
      recipe.setTitle(request.getParameter("title"));
      
      if(recent_view.size() < 8) {
        recent_view.add(recipe);
      } else {
        List<RecipeObj> subList = new ArrayList<>(recent_view.subList(1, recent_view.size()));
        subList.add(recipe);
        recent_view = subList;
      }
	}

}
