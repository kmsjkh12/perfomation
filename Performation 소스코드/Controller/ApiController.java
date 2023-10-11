package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.dto.ApiDTO;
import com.example.demo.dto.AppusersLockerDTO;
import com.example.demo.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ApiController {
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private AppusersLockerMapper appusersLockerMapper;

    @RequestMapping(value="/")
    public String Index(Model model) {
        model.addAttribute("username", "초기값");

        return "index";
    }

    private ExecutorService nonBlockingService = Executors
            .newCachedThreadPool();
    @GetMapping("/sse")
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter();
        List<ReviewDTO> list = reviewMapper.allreadreview();
        var item = movieMapper.readinfo();

        List array = new ArrayList<Object>();
        for(int i=0; i<list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", list.get(i).getId());
            List<ApiDTO> username = apiMapper.readappuser(list.get(i).getAppusers_id());
            map.put("username", username.get(0).getUsername());
            map.put("readinfo_id", list.get(i).getReadinfo_id());
            map.put("timestamp", list.get(i).getTimestamp());
            map.put("push", list.get(i).getPush());
            map.put("content", list.get(i).getContent());
            for(int j=0; j<item.size(); j++) {
                if(item.get(j).getId().equals(list.get(i).getReadinfo_id())) {
                    map.put("movie", item.get(j));
                }
            }
            array.add(map);
        }
        nonBlockingService.execute(() -> {
            try {
                // we could send more events
                emitter.send(array);
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @GetMapping("/ticket")
    public SseEmitter handleTicket() {
        SseEmitter emitter = new SseEmitter();
        List<AppusersLockerDTO> list = appusersLockerMapper.returnAppusers();

        nonBlockingService.execute(() -> {
            try {
                emitter.send(list);
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}


