package poly.service;



import java.util.List;
import java.util.Map;

public interface IWriteService {
    // 게시글 등록하기
    void insertwrite(Map<String, Object> pMap) throws Exception;

    // 사용자 게시글 목록가져오기
    List<Map<String,String>> getwriteList(String SS_USER_EMAIL) throws Exception;

    //사용자 게시글 가져오기
    List<Map<String,String>> getNoticEdit(String title) throws Exception;

    //사용자 게시글 수정하기
    int updateNotice(Map<String, Object> pMap) throws Exception;

    //사용자 게시글 삭제하기
    int deleteNotice(Map<String, Object> pMap )throws Exception;
}
