package poly.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.persistence.mongo.IWriteMapper;
import poly.service.IWriteService;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("WriteService")
public class WriteService implements IWriteService {

    private final Logger log = Logger.getLogger(this.getClass());

    // mongoDB collection name
    private final String colNm = "NoticeCollection";

    @Resource(name = "WriteMapper")
    private IWriteMapper writeMapper;

    @Override
    public void insertwrite(Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".insertUser start");

        writeMapper.insertwrite(colNm, pMap);

        log.info(this.getClass().getName() + ".insertUser end");

    }
    @Override
    public List<Map<String,String>> getwriteList(String SS_USER_EMAIL) throws Exception {

        log.info(this.getClass().getName() + ".getNoticList start!");
        List<Map<String,String>> NoticList = writeMapper.getUserNoticeList(colNm, SS_USER_EMAIL);


        if (NoticList == null) {
            NoticList = new ArrayList<>();
        }

        log.info(this.getClass().getName() + ".getUserQuizTitleList end!");

        return NoticList;

    }
    //게시물 상세보기
    @Override
    public List<Map<String, String>> getNoticEdit(String title) throws Exception {

        log.info(this.getClass().getName() + ".getNoticList start!");
        List<Map<String,String>> NoticInfoList = writeMapper.getUserNoticeInfo(colNm, title);

        if (NoticInfoList == null) {
            NoticInfoList = new ArrayList<>();
        }

        log.info(this.getClass().getName() + ".getUserNoticInfo end!");



        return NoticInfoList;
    }

    //게시물 수정 등록하기
    @Override
    public int updateNotice(Map<String, Object> pMap) throws Exception {
        log.info(this.getClass().getName() + ".updateUserPw start");

        int res = writeMapper.updateNotice(colNm, pMap);

        log.info(this.getClass().getName() + ".updateUserPw end");

        return res;
    }

    @Override
    public int deleteNotice(Map<String, Object> pMap) throws Exception {
        log.info(this.getClass().getName() + ".deleteNotice start");

        int res = writeMapper.deleNotice(colNm, pMap);

        log.info(this.getClass().getName() + ".deleteNotice end");

        return res;
    }
}