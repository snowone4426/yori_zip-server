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
            break;
          case "bookmark" : 
            setBookmark(request,response);
            break;
          case "deleterecipe" :
            deleteRecipe(request,response);
          case "star" : 
            setStar(request,response);
          case "reple" : 
            getReple(request,response);
          case "setrecple" :
            setReple(request,response);
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
        
        
        
        if (my_info != null) {
          Map<String,String> star = stardao.getStar(recipe_id.toString(), my_info.getUser_id());

          if (star != null) {
            result.put("my_star_score", star.get("score"));
          }
          
          result.put("my_bookmark", recipedao.isBookmark(recipe_id.toString(), my_info.getUser_id()));
        } else {
          result.put("my_bookmark", false);
          result.put("my_star_score", "0");
        }
        
        result.put("recipe_info", recipe_info);
        result.put("recipe_detail", recipe_detail);
        result.put("ingredient", ingredient);
        result.put("reple_count", repledao.repleCount(recipe_id.toString()));
        result.put("reple_list", reple_list);
        
        out.print(result.toString());
        
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      }
	}
	
	private void setBookmark (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      RecipeDAO recipedao = new RecipeDAO();
      UserObj my_info = (UserObj) session.getAttribute("user_info");
      
      String recipe_id = request.getParameter("recipe_id");
      
      if (my_info != null && recipe_id != null) {
        
        String user_id = my_info.getUser_id();
        
        out.print(recipedao.setBookmark(recipe_id, user_id));
      }
	}
	
	private void deleteRecipe (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      RecipeDAO recipedao = new RecipeDAO();
      UserObj my_info = (UserObj) session.getAttribute("user_info");
      
      String recipe_id = request.getParameter("recipe_id");
      
      if (my_info != null && recipe_id != null) {
        
        String user_id = my_info.getUser_id();
        
        out.print(recipedao.deleteRecipe(recipe_id, user_id));
      }
	}

	private void setStar (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      StarDAO recipedao = new StarDAO();
      UserObj my_info = (UserObj) session.getAttribute("user_info");
      String score = request.getParameter("score");
      
      String recipe_id = request.getParameter("recipe_id");
      
      if (my_info != null && recipe_id != null && score != null) {
        
        String user_id = my_info.getUser_id();
        
        
        out.print(recipedao.setStar(recipe_id, user_id,score));
      }
	}
	
	private void getReple (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      RepleDAO repledao = new RepleDAO();
      UserObj my_info = (UserObj) session.getAttribute("user_info");
      String recipe_id = request.getParameter("recipe_id");
      JSONObject json_obj = new JSONObject();
      
      if(my_info != null && recipe_id != null) {
        List<Map<String,Object>> reple_list = repledao.getReple(recipe_id, Integer.parseInt(request.getParameter("offset")), Integer.parseInt(request.getParameter("limit")), null);
        
        JSONArray json_arr = new JSONArray();
        
        for (Map<String,Object> x : reple_list) {
          json_arr.put(new JSONObject(x));
        }
        
        try {
          json_obj.put("reple_list", json_arr.toString());
          json_obj.put("reple_count", repledao.repleCount(recipe_id));
        } catch (JSONException e) {
          e.printStackTrace();
        }
        
        
        out.print(json_obj.toString());
      }
      
	}
	
	private void setReple (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      RepleDAO repledao = new RepleDAO();
      UserObj my_info = (UserObj) session.getAttribute("user_info");
      String recipe_id = request.getParameter("recipe_id");
      JSONParser jsonparser = new JSONParser(request.getReader());
      
      try {
        Map<String,Object> json_data = jsonparser.object();
        
        if(repledao.createReple(my_info.getUser_id(), recipe_id,(String)json_data.get("contents"))) {
          
        }
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      
      
      
      
	}
}
