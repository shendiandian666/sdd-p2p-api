package com.sdd.httpclient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {

	public static void main(String[] args) {
		/*Connection conn = Jsoup.connect("https://www.jspfanli.com/first");
		try {
			// 10秒超时时间,发起get请求，也可以是post
            Document doc = conn.timeout(10000).get();
         // 1. 只要我们关心的信息数据,这里使用css类选择器
            Element ul = doc.select(".h_list.i_list.hover_ul").get(0);
            // 2. 得到所有的li,排除个别不是同种类型的数据
            Elements lis = ul.getElementsByClass("transition_a li_o");
            
            Element page = doc.select(".page dt").get(0);
            System.out.println(page.text());
            for(int i = 0; i < lis.size(); i++){
            	Element li = lis.get(i);
            	Element link = li.select("a").first();
            	
            	String href = link.attr("abs:href");
            	getActivity(href);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		getActivity("https://www.jspfanli.com/a/237");
	}
	
	public static void getActivity(String url){
		Connection conn = Jsoup.connect(url);
		try {
			Document doc = conn.timeout(10000).get();
			//Element table = doc.select(".list_table tbody").get(0);
			
			Elements lis = doc.select(".display_data.clearfix").get(0).getElementsByClass("clearfix li_o");
			//System.out.println(table.toString());
			//System.out.println(lis.toString());
			Elements e1 = lis.get(1).select(".text_box_g.round_s");
			System.out.println(e1.get(0).text().substring(5));
			System.out.println(e1.get(1).text().substring(5));
			Element e2 = lis.get(2).select(".text_box_g.round_s").get(0);
			System.out.println(e2.text());
			Element e3 = lis.get(3).select(".text_box_g.round_s").get(0);
			System.out.println(e3.text());
			Element e4 = lis.get(4).select(".text_box_g.round_s").get(0);
			System.out.println(e4.text());
			Elements e5 = lis.get(5).select(".text_box_g.round_s");
			System.out.println(e5.get(0).text());
			System.out.println(e5.get(1).text().substring(3));
			System.out.println(e5.get(2).text().substring(5));
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * // 数据1，得到图片的url，通过img标签的src属性获得
                Element img = li.getElementsByTag("img").first();
                // 获取标签的属性值，参数为属性名称
                String imageUrl = img.attr("src");
                // 数据2，得到文章的标题
                Element h2 = li.getElementsByTag("h2").first();
                // 取h2元素下面的第一个a标签的文本即为标题
                String title = h2.getElementsByTag("a").first().text();
                // 数据3，得到文章的发表时间,取h2元素下面的第一个span标签的文本即为文章发表时间
                String postime = h2.getElementsByTag("span").first().text();
                // 数据4，得到文章内容简介,取li下面第一个p标签的文本
                String summary = li.getElementsByTag("p").first().text();
                // 数据5，得到文章的关键字,取li下面的class为tags的第一个元素的所有的a标签文本
                Element tagsSpan = li.getElementsByClass("tags").first();
	 */
}
