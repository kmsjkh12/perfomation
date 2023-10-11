package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.dao.MoviegradeinfoMapper;
import com.example.demo.dao.ApiMapper;
import com.example.demo.dao.MovieMapper;
import com.example.demo.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class IndexController {
    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private FavoritesMapper favoritesMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private MoviegradeinfoMapper moviegradeinfoMapper;

    @Autowired
    private AppusersLockerMapper appusersLockerMapper;

    @Autowired
    private EventMapper eventMapper;

//    @RequestMapping(value="login", method = RequestMethod.GET)
//    public List<ApiDTO> login() {
//        List<ApiDTO> list = apiMapper.readuser();
//        return list;
//    }

    @RequestMapping(value="readinfo", method = RequestMethod.GET)           // 모든공연정보를 읽어온다
    public List<MovieDTO> readinfo() {
        List<MovieDTO> movie = movieMapper.readinfo();
        return movie;
    }

    @RequestMapping(value="pushappusersid", method = RequestMethod.GET)
    public boolean pushappusersid(@RequestParam(value = "currentUserId")String currentUser_id, @RequestParam(value ="reviewId")String review_id) {
        String str = "";
        String username = null;
        String[] push_username = null;
        List<ReviewDTO> list = reviewMapper.SpecificReview(review_id);
        List<ApiDTO> readuserAll = apiMapper.readuserAll();

        for(int i=0; i<readuserAll.size(); i++) {
            if(readuserAll.get(i).getId().equals(currentUser_id)) {
                username = readuserAll.get(i).getUsername();
            }
        }
        if(list.get(0).getPush_appusers_id().equals("")) {
            str += currentUser_id;
            String push;
            int count;
            push = list.get(0).getPush();
            count = Integer.parseInt(push);
            count += 1;
            push = Integer.toString(count);
            reviewMapper.CountUpPush(review_id, push);
            System.out.println(username + "님이 성공적으로 추천하셨습니다.");
            reviewMapper.Push_Appusers_id(str, review_id);
            return true;
        } else {
            push_username = list.get(0).getPush_appusers_id().split(",");
            for(int i=0; i<push_username.length; i++) {
                if(push_username[i].equals(currentUser_id)) {
                    System.out.println(username + "님은 이미 추천하셨습니다. / " + username + "님의 추천을 삭제합니다.");
                    List<String> copylist = new ArrayList<>(Arrays.asList(push_username));
                    copylist.remove(push_username[i]);
                    str = String.join(",", copylist);
                    reviewMapper.Push_Appusers_id(str, review_id);
                    System.out.println("성공적으로 삭제되었습니다.");

                    String push;
                    int count;
                    push = list.get(0).getPush();
                    count = Integer.parseInt(push);
                    count -= 1;
                    push = Integer.toString(count);
                    reviewMapper.CountUpPush(review_id, push);

                    return false;
                }
            }
            String push;
            int count;
            push = list.get(0).getPush();
            count = Integer.parseInt(push);
            count += 1;
            push = Integer.toString(count);
            reviewMapper.CountUpPush(review_id, push);
            str = list.get(0).getPush_appusers_id();
            str = str + "," + currentUser_id;
            reviewMapper.Push_Appusers_id(str, review_id);
        }
        return true;
    }


    @RequestMapping(value="insertreview", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public boolean insertreview(ReviewDTO reviewDTO) {
        try {
            reviewMapper.insertreview(reviewDTO);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("댓글을 성공적으로 추가하였습니다.");
        return true;
    }
    @RequestMapping(value="updatereview", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public boolean updatereview(ReviewDTO reviewDTO) {
        try {
            reviewMapper.updatereview(reviewDTO);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("댓글을 성공적으로 수정하였습니다.(modify)");
        return true;
    }
    @RequestMapping(value="updategrade", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public boolean updategrade(MoviegradeinfoDTO moviegradeinfoDTO) {
        List<MoviegradeinfoDTO> list =moviegradeinfoMapper.readMovieGradeInfo(moviegradeinfoDTO);
        var current_grade = list.get(0).getGrade();
        var id = list.get(0).getId();
        System.out.println(id);
        var grade = moviegradeinfoDTO.getGrade();
        moviegradeinfoDTO.setId(id);
        moviegradeinfoMapper.updateCurrentUserGrade(moviegradeinfoDTO);

        List<MovieDTO> item = movieMapper.readSpeGrade2(moviegradeinfoDTO.getInfo_id());
        var movie_grade = item.get(0).getGrade();
        System.out.println(movie_grade);
        int insert_grade = Integer.parseInt(movie_grade) - Integer.parseInt(current_grade) + Integer.parseInt(grade);
        System.out.println(insert_grade);

        movieMapper.updateGrade(Integer.toString(insert_grade), moviegradeinfoDTO.getInfo_id());
        return true;
    }
    @RequestMapping(value="increment_view", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public List<MovieDTO> increment_view(MovieDTO movieDTO) {
        List<MovieDTO> list = movieMapper.readSpeGrade(movieDTO);
        String view_count = list.get(0).getView_count();
        int count = Integer.parseInt(view_count) + 1;
        movieMapper.increment_view(Integer.toString(count), list.get(0).getId());
        list = movieMapper.readSpeGrade(movieDTO);
        return list;
    }
    @RequestMapping(value="increment_review", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public List<MovieDTO> increment_review(MovieDTO movieDTO) {
        List<MovieDTO> list = movieMapper.readSpeGrade(movieDTO);
        String review_count = list.get(0).getReview_count();
        int count = Integer.parseInt(review_count) + 1;
        movieMapper.increment_review(Integer.toString(count), list.get(0).getId());
        list = movieMapper.readSpeGrade(movieDTO);
        return list;
    }
    @RequestMapping(value="buythisticket", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public List<AppusersLockerDTO> buythisticket(AppusersLockerDTO appusersLockerDTO) {
        String appusers_id = appusersLockerDTO.getAppusers_id();
        List<ApiDTO> appusers = apiMapper.readappuser(appusers_id);
        int total_price = Integer.parseInt(appusers.get(0).getPoint());
        int current_price = Integer.parseInt(appusersLockerDTO.getPrice());
        total_price = total_price - current_price;
        apiMapper.decPrice(Integer.toString(total_price), appusers_id);

        appusersLockerMapper.insertTicket(appusersLockerDTO);
        List<AppusersLockerDTO> list = appusersLockerMapper.returnAppusers();
        return list;
    }
    @RequestMapping(value="appuserslocker", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public List<AppusersLockerDTO> appuserslocker(AppusersLockerDTO appusersLockerDTO) {
        List<AppusersLockerDTO> list = appusersLockerMapper.SpeAppusersLocker(appusersLockerDTO);
        return list;
    }
    @RequestMapping(value="deleteUserTicket", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public boolean deleteUserTicket(AppusersLockerDTO appusersLockerDTO) {
        List<AppusersLockerDTO> list = appusersLockerMapper.returnAppusers();
        for(var i=0; i<list.size(); i++) {
            if(list.get(i).getSerial_number().equals(appusersLockerDTO.getSerial_number())) {
                appusersLockerMapper.deleteUserTicket(appusersLockerDTO);
                return true;
            }
        }
        return  false;
    }

    @RequestMapping(value="allreadreview", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public List<Map<String, String>> allreadreview() {
        List<ReviewDTO> list = reviewMapper.allreadreview();
        List array = new ArrayList<Object>();
        for(int i=0; i<list.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", list.get(i).getId());
            List<ApiDTO> username = apiMapper.readappuser(list.get(i).getAppusers_id());
            map.put("username", username.get(0).getUsername());
            map.put("readinfo_id", list.get(i).getReadinfo_id());
            map.put("timestamp", list.get(i).getTimestamp());
            map.put("push", list.get(i).getPush());
            map.put("content", list.get(i).getContent());
            array.add(map);
        }
        return array;
    }
    @RequestMapping(value="readreview", method = RequestMethod.GET)             // 해당 공연의 리뷰를 읽어온다.
    public List<ReviewDTO> readreview(ReviewDTO reviewDTO) {
        List<ReviewDTO> list = reviewMapper.readreview(reviewDTO);
        List<ApiDTO> item = apiMapper.readuserAll();
        for(int i=0; i<list.size(); i++) {                                      // 해당 리뷰의 작성자 id의 정보를 가져와 사용자 정의(Appusers)에 넣어준다.
            for(int j=0; j<item.size(); j++) {
                if (list.get(i).getAppusers_id().equals(item.get(j).getId())) {
                    list.get(i).setAppusers(item.get(j));
                }
            }
        }
        return list;
    }
    @RequestMapping(value="readuser", method = RequestMethod.GET)               // 현재 로그인한 유저의 정보를 가져온다.
    public List<ApiDTO> readuser(ApiDTO apiDTO) {
        List<ApiDTO> list = apiMapper.readuser(apiDTO);
        return list;
    }
    @RequestMapping(value="readFavorites", method = RequestMethod.GET)          // 즐겨찾기 읽기
    public List<FavoritesDTO> readFavorites(FavoritesDTO favoritesDTO) {
        List<FavoritesDTO> list = favoritesMapper.readFavorites(favoritesDTO);
        return list;
    }
    @RequestMapping(value="insertFavorites", method = RequestMethod.GET)        // 즐겨찾기 추가
    public boolean insertFavorites(FavoritesDTO favoritesDTO) {
        favoritesMapper.insertFavorites(favoritesDTO);
        return true;
    }
    @RequestMapping(value="deleteFavorites", method = RequestMethod.GET)        // 즐겨찾기 삭제
    public boolean deleteFavorites(FavoritesDTO favoritesDTO) {
        favoritesMapper.deleteFavorites(favoritesDTO);
        return true;
    }
    @RequestMapping(value="CommentReviewDelete", method = RequestMethod.GET)        // 즐겨찾기 삭제
    public boolean CommentReviewDelete(ReviewDTO reviewDTO) {
        reviewMapper.Delete_review(reviewDTO);
        return true;
    }
    @RequestMapping(value="DeleteMovieGrade", method = RequestMethod.GET)        // 즐겨찾기 삭제
    public boolean DeleteMovieGrade(MovieDTO movieDTO) {
        List<MovieDTO> list = movieMapper.readSpeGrade(movieDTO);
        var current_grade = list.get(0).getGrade();                     // 현재 공연의 평점
        var count = Integer.parseInt(list.get(0).getAddcount());                          // 현재 공연에 평점을 매긴 사람의 수
        var appusers_id = movieDTO.getAppusers_id();                    // 현재 유저의 아이디
        List<MoviegradeinfoDTO> item = moviegradeinfoMapper.readMovieGradeInfo2(appusers_id, movieDTO.getId());
        var user_grade = item.get(0).getGrade();                        // 유저가 매긴 평점
        var insert_grade = Integer.parseInt(current_grade) - Integer.parseInt(user_grade);
        String str = null;
        count = count - 1;
        movieDTO.setAddcount(Integer.toString(count));
        if(insert_grade == 0) {
            movieMapper.updateGrade(str, movieDTO.getId());
            movieMapper.incAddCount(movieDTO);
            moviegradeinfoMapper.DeleteUserGrade(item.get(0).getId());
        } else {
            movieMapper.updateGrade(Integer.toString(insert_grade), movieDTO.getId());
            movieMapper.incAddCount(movieDTO);
            moviegradeinfoMapper.DeleteUserGrade(item.get(0).getId());
        }
        return true;
    }
    @RequestMapping(value="decrement_review", method = RequestMethod.GET)           // 해당 공연의 리뷰를 작성한다.
    public List<MovieDTO> decrement_review(MovieDTO movieDTO) {
        List<MovieDTO> list = movieMapper.readSpeGrade(movieDTO);
        String review_count = list.get(0).getReview_count();
        int count = Integer.parseInt(review_count) - 1;
        movieMapper.increment_review(Integer.toString(count), list.get(0).getId());
        list = movieMapper.readSpeGrade(movieDTO);
        return list;
    }
    @RequestMapping(value="login", method = RequestMethod.GET)          // 사용자가 login 한다.
    public Map<String, Object> login(HttpSession session, ApiDTO apiDTO, HttpServletResponse response, Model model, HttpServletRequest request) {

        if(session.getAttribute("login") != null) {     // login 이라는 이름을 가진 session 이 있다면
            session.removeAttribute("login");           // login Session 을 삭제한다.
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<ApiDTO> list = apiMapper.searchUser(apiDTO);   // axios로 전달받은 인자를 searchUser에 넘겨준다.
            map.put("list", list);
            if(list != null) {                                  // serach 가 되었다면,
                if(list.get(0).getPassword().equals(apiDTO.getPassword())) {    // 전달받은 인자와 list의 값을 비교한다.
                    session.setAttribute("login", apiDTO);
                    Cookie[] cookies = request.getCookies();
                    if(apiDTO.isRememberme()) {
                        String str = apiDTO.getEmail() + "/" + apiDTO.getPassword() + "/" + list.get(0).getUsername() + "/" + list.get(0).getId();
                        Cookie cookie = new Cookie("loginCookie", str);     // loginCookie 쿠키를 설정한다.
                        cookie.setPath("/");                                      // 특정 path(url) 의 요청만 받는다. @/ 은 모두 허용.
                        cookie.setMaxAge(60*60*24*7);
                        response.addCookie(cookie);
                        model.addAttribute("username", list.get(0).getUsername());
                    }
                    map.put("isSuccess", true);
                    return map;

                } else {
                    map.put("isSuccess", false);
                    return map;
                }
            } else {
                map.put("isSuccess", false);
                return map;
            }
        } catch (Exception e) {
            map.put("isSuccess", false);
            return map;
        }
    }
    @RequestMapping(value="logout", method = RequestMethod.GET)                // 사용자가 로그아웃을 한다.
    public Cookie login(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("loginCookie", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return cookie;
    }
    @RequestMapping(value="checkCookie", method = RequestMethod.GET)            // 앱이 실행될 때 cookie 가 있는지 확인한다.
    public Map checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Map result = new HashMap<String, Object>();
        try {
            for(int i=0; i<cookies.length; i++) {
                if(cookies[i].getName().equals("loginCookie") == true) {
                    System.out.println(cookies[i].getValue()+"님이 접속하셨습니다.");
                    result.put("loginCookie", cookies[i].getValue());
                    result.put("_isfail", false);
                    return result;
                } else {
                    result.put("_isfail", true);
                }
            }
            return result;
        } catch (Exception e) {
            result.put("_isfail", true);
            return result;
        }
    }

    @RequestMapping(value="signin", method = RequestMethod.GET)             // 회원가입
    public ApiDTO signin(ApiDTO apiDTO) {
        apiMapper.userInsert(apiDTO);

        return apiDTO;
    }
    @RequestMapping(value="changeusername", method = RequestMethod.GET)             // 회원가입
    public boolean changeusername(ApiDTO apiDTO) {
        List<ApiDTO> list = apiMapper.readuserAll();
        for(int i=0; i<list.size(); i++) {
            if(apiDTO.getUsername().equals(list.get(i).getUsername())) {
                System.out.println("닉네임이 이미 존재합니다.");
                return false;
            }
        }
        System.out.println("닉네임을 변경합니다.");
        apiMapper.changeusername(apiDTO);
        return true;
    }
    @RequestMapping(value="changepassword", method = RequestMethod.GET)             // 회원가입
    public boolean changepassword(ApiDTO apiDTO) {
        if(apiDTO.getPassword() != null) {
            apiMapper.changepassword(apiDTO);
            System.out.println("성공적으로 변경했습니다.");
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value="insertShow", method = RequestMethod.GET)         // 공연을 추가한다 (admin 메뉴)
    public boolean insertShow(MovieDTO movieDTO) {
        System.out.println(movieDTO.getMovie_id());
        try {
            List<MovieDTO> list = movieMapper.insertShow(movieDTO);
        } catch (Exception e) {
            System.out.println("insertShow Controller Error");
            System.out.println(e);
        }
        System.out.println("공연을 성공적으로 추가하였습니다.");
        return true;
    }

    @RequestMapping(value="addGrade", method = RequestMethod.GET)           // 공연의 평점을 합산한다.
    public void addGrade(MovieDTO movieDTO) {
        int sum = 0;
        List<MovieDTO> list = movieMapper.readSpeGrade(movieDTO);       // 현재 info 공연의 addcount 를 가져온다
        int i = Integer.parseInt(list.get(0).getAddcount());
        String str;
        if(i == 0) {                                                    // 만약  addcount = 0 이라면
            movieMapper.addGrade(movieDTO);
            i += 1;
            try {
                moviegradeinfoMapper.InsertUser(movieDTO);
                System.out.println("성공적으로 추가하였습니다. [Movie_grade_info_mapper.InsertUser]");
            } catch (Exception e) {
                System.out.println(e);
            }
            str = Integer.toString(i);
            movieDTO.setAddcount(str);
            movieMapper.incAddCount(movieDTO);
        } else {                                                        // i != 0 이라면
            moviegradeinfoMapper.InsertUser(movieDTO);
            sum = Integer.parseInt(list.get(0).getGrade()) + Integer.parseInt(movieDTO.getGrade());
            str = Integer.toString(sum);
            System.out.println(str);
            movieDTO.setGrade(str);
            movieMapper.addGrade(movieDTO);
            i += 1;
            str = Integer.toString(i);
            System.out.println(str);
            movieDTO.setAddcount(str);
            movieMapper.incAddCount(movieDTO);
        }
    }
    @RequestMapping(value="readdGrade", method = RequestMethod.GET)           // 공연을 재평가한다.
    public boolean readdGrade(MoviegradeinfoDTO moviegradeinfoDTO) {
        List<MoviegradeinfoDTO> list = moviegradeinfoMapper.readMovieGradeInfo(moviegradeinfoDTO);
        String id;
        int Usergrade = 0;
        if ( list == null) {
            System.out.println("평점을 입력하세요.");
            return false;
        } else {
            id = list.get(0).getId();
            Usergrade = Integer.parseInt(list.get(0).getGrade());
            MovieDTO movieDTO = new MovieDTO();
            String str = moviegradeinfoDTO.getInfo_id();
            movieDTO.setId(str);
            List<MovieDTO> item = movieMapper.readSpeGrade(movieDTO);
            int Currentgrade = Integer.parseInt(item.get(0).getGrade());
            int sum = Currentgrade - Usergrade + Integer.parseInt(moviegradeinfoDTO.getGrade());
            movieDTO.setGrade(Integer.toString(sum));
            movieMapper.addGrade(movieDTO);                 // 계산한 값을 다시 movieDTO에 넣는다.

            moviegradeinfoDTO.setId(id);
            moviegradeinfoMapper.updateCurrentUserGrade(moviegradeinfoDTO);
            return true;
        }


    }
    @RequestMapping(value="readMovieGradeInfo", method = RequestMethod.GET)
    public List<MoviegradeinfoDTO> readMovieGradeInfo(MoviegradeinfoDTO moviegradeinfoDTO) {
        List<MoviegradeinfoDTO> list = moviegradeinfoMapper.readMovieGradeInfo(moviegradeinfoDTO);
        System.out.println(list);
        return list;
    }

    @RequestMapping(value="addEvent", method = RequestMethod.GET)
    public List<EventDTO> addEvent(EventDTO eventDTO) {
        eventMapper.addEvent(eventDTO);
        List<EventDTO> list = eventMapper.readEventAll();
        return list;
    }
    @RequestMapping(value="readEventAll", method = RequestMethod.GET)
    public List<EventDTO> readEventAll(EventDTO eventDTO) {
        List<EventDTO> list = eventMapper.readEventAll();
        return list;
    }


    @RequestMapping(value="/distance")
    public String test1(@RequestParam(value = "a")String a, @RequestParam(value ="b")String b, @RequestParam(value ="c")String c, @RequestParam(value ="d")String d) throws IOException {
        System.out.print(a + "," + b +","+c+","+d);
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=transit&origins="+ a + "," + b + "&destinations=" + c + "," + d + "&region=KR&key=AIzaSyDKojS4nQ2I1bZkxtO2Yqu30RyyJPl4MjY"); /*URL*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }
}
