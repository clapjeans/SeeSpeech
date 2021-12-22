package poly.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.service.IRedisService;

@Controller
public class RedisController {
	
	@Resource(name="RedisService")
    private IRedisService redisService;
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	
	@RequestMapping(value = "rank.do")
	  public String rank(Model model) throws Exception {
	
	      log.info(this.getClass().getName() + ".rank start");
	
	  List<String> rList = redisService.showUserRate();
	
	
	  model.addAttribute("rList", rList);
	
	  log.info(this.getClass().getName() + ".rank end");
	
	  return "/user/rank";
	  }
	
}
