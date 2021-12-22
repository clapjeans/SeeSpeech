package poly.persistence.mongo;



import java.util.List;
import java.util.Map;


public interface IWriteMapper {


    // 게시물 등록하기
    void insertwrite( String colNm,Map<String, Object> pMap) throws Exception;

    // 사용자 게시물 목록 가져오기
    List<Map<String,String>> getUserNoticeList(String colNm, String SS_USER_EMAIL) throws Exception;

    //사용자 게시물 정보 가져오기
    List<Map<String,Object>> getUserNoticeInfo(String colNm, String title) throws Exception;

    //사용자 게시물 수정하기
    int updateNotice(String colNm, Map<String, Object> pMap) throws Exception;

    //사용자 게시글 삭제하기
    int deleNotice(String colNm, Map<String, Object> pMap);
}
