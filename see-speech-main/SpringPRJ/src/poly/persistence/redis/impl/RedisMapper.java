package poly.persistence.redis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;

import poly.persistence.redis.IRedisMapper;
import poly.util.CmmUtil;


@Component("RedisMapper")
public class RedisMapper implements IRedisMapper {
	
	@Autowired
	public RedisTemplate<String, Object> redisDB;
	
	@Autowired //MongoTemplate가 메모리에 올라와 있으면 변수에 넣어라
	private MongoTemplate mongodb;
	
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean getExists(String key) throws Exception {
		
		log.info(this.getClass().getName() + ".getExists start");
		
		return redisDB.hasKey(key);
		
	}

	@Override
	public List<String> getUserRate(String key) throws Exception {
		log.info(this.getClass().getName() + ".getUserRate start");
		
		HashMap<String, String> pMap = new HashMap<String, String>();
		
		List<String> rList = new ArrayList<String>();
		
		//redis 저장 타입 지정
		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new StringRedisSerializer());
		
		Set rSet = (Set) redisDB.opsForZSet().range(key, 0, -1); //모든 데이터 가져오기
		
		Iterator<String> it = rSet.iterator();
		while(it.hasNext()) {
			rList.add(it.next());
		}
		
		return rList;
	}

	@Override
	public int setUserRate(String key) throws Exception {
		log.info(this.getClass().getName() + ".setUserRate start");
		
		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new StringRedisSerializer());
		
		// 데이터를 가져올 컬렉션 선택
		MongoCollection<Document> collection = mongodb.getCollection("UserCollection");
		
        
        // 쿼리 만들기
        Document query = new Document();

        // 조회결과중 출력할 컬럼들
        Document projection = new Document();
        projection.append("user_name", "$user_name");
        projection.append("user_rate", "$user_rate");
        //objectId사용안할것
        projection.append("_id", 0);

        Document sort = new Document();

        sort.append("user_rate", -1);

        Consumer<Document> processBlock = document -> {

            String user_name = CmmUtil.nvl(document.getString("user_name"));
            int user_rate = document.getInteger("user_rate", 0);
            
            redisDB.opsForZSet().add(key, user_name, user_rate);
        };
        
        collection.find(query).projection(projection).sort(sort).forEach(processBlock);
        
		return 0;
	}

	@Override
	public boolean setTimeOutHour(String key, int hours) throws Exception {
		log.info(this.getClass().getName() + ".setTimeOutHour start");
		
		
		return redisDB.expire(key, hours, TimeUnit.HOURS);
	}

}
