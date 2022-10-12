package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import dao.BannerDAO;
import dao.BannerObj;
import dao.RecipeDAO;
import dao.RecipeObj;

@WebServlet("/MainController")
public class MainController extends HttpServlet {
  private static final long serialVersionUID = 1L;
       

  public MainController() {
      super();
  }
        
  public void service (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (request.getParameter("m") != null) {
      switch (request.getParameter("m")) {
        case "banner" : 
          getBanner(request,response);
          break;
        case "theme" : 
          getThemeRecipe(request,response);
          break;
        case "hot" : 
          getHotRecipe(request,response);
          break;
        default : 
          PrintWriter out = response.getWriter();
          out.print("MAIN CONTROLLER");
      }
    }
  }
  
  private void getBanner(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    BannerDAO banner = new BannerDAO();
    List<BannerObj> list = banner.getBannerList();
    
    JSONArray json_array = new JSONArray();
    
    for (BannerObj obj:list) {
      JSONObject json_obj = new JSONObject();
      try {
        json_obj.put("banner_id", obj.getBanner_id());
        json_obj.put("photo", obj.getPhoto());
        json_obj.put("alt", obj.getAlt());
        json_obj.put("created_at", obj.getCreated_at());
        json_obj.put("start_date", obj.getStart_date());
        json_obj.put("end_date", obj.getEnd_date());
        json_obj.put("search", obj.getSearch());
        
        json_array.put(json_obj);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    
    out.print(json_array.toString());
  }
  
  private void getThemeRecipe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    RecipeDAO recipe = new RecipeDAO();
    List<RecipeObj> list = recipe.getThemeRecipeList(Integer.parseInt(request.getParameter("search_id")));
    
    JSONArray json_array = new JSONArray();
    
    for (RecipeObj obj:list) {
      JSONObject json_obj = new JSONObject();
      
      try {
        json_obj.put("recipe_id", obj.getRecipe_id());
        json_obj.put("title",obj.getTitle());
        json_obj.put("subtitle",obj.getSubtitle());
        json_obj.put("thumbnail",obj.getThumbnail());
        
        json_array.put(json_obj);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    
    out.print(json_array.toString());
  }
  
  private void getHotRecipe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    RecipeDAO recipe = new RecipeDAO();
    List<RecipeObj> list = recipe.getHotRecipeList();
    
    JSONArray json_array = new JSONArray();
    
    for (RecipeObj obj:list) {
      JSONObject json_obj = new JSONObject();
      
      try {
        json_obj.put("recipe_id", obj.getRecipe_id());
        json_obj.put("thumbnail", obj.getThumbnail());
        json_obj.put("title", obj.getTitle());
        json_obj.put("subtitle", obj.getSubtitle());
        json_obj.put("difficulty", obj.getLevel());
        json_obj.put("time", obj.getTime());
        json_obj.put("created_at", obj.getCreate_at());
        json_obj.put("updated_at", obj.getUpdate_at());
        json_obj.put("state", obj.getState());
        json_obj.put("star_score", obj.getStar_score());
        
        json_array.put(json_obj);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    
    out.print(json_array.toString());
  }
  
  

}
