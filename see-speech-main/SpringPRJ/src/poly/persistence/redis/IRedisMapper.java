package poly.persistence.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IRedisMapper {
	
	public boolean getExists(String key) throws Exception;
	
	
	public List<String> getUserRate(String key) throws Exception;
	
	public int setUserRate(String key)throws Exception;
	
	public boolean setTimeOutHour(String key, int hours) throws Exception;

	
}
