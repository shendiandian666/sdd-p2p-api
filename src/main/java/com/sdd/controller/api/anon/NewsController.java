package com.sdd.controller.api.anon;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.controller.api.ApiController;
import com.sdd.service.NewsService;
import com.sdd.util.JSONResult;

@RestController("anonNews")
@RequestMapping("/api/anon/news")
public class NewsController extends ApiController {

	@Autowired
	private NewsService newsService;
	
	@RequestMapping(value = "/getContent")
	public String getContent() throws Exception {
		String id = getString("id");
		Map<String, Object> newsObj = newsService.getContent(id);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, newsObj);
	}
	
	@RequestMapping(value = "/getNews")
	public String getNews() throws Exception {
		List<Map<String, Object>> newsList = newsService.getNews(getPageNum(), getPageSize(), "'1','2'");
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, newsList);
	}
	
}
