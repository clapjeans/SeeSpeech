package poly.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.persistence.redis.IRedisMapper;
import poly.service.IRedisService;

@Service("RedisService")
public class RedisService implements IRedisService {
	
	@Resource(name = "RedisMapper")
	private IRedisMapper redisMapper;
	
	
	private Logger log = Logger.getLogger(this.getClass());
	
	
	@Override
	public List<String> showUserRate() throws Exception {
		
		log.info(this.getClass().getName() + ".showUserRateService start");
		
		List<String> rList = null;
		
		String key = "UserRank";
		if(redisMapper.getExists(key)) {
			rList = redisMapper.getUserRate(key);
		} else {
			redisMapper.setUserRate(key);
			rList = redisMapper.getUserRate(key);
			redisMapper.setTimeOutHour(key, 1);
		}
		
		return rList;
	}

}
