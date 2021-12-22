package poly.persistence.mongo.impl;


import com.mongodb.client.ClientSession;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import poly.persistence.mongo.IWriteMapper;
import poly.persistence.mongo.comm.AbstractMongoDBCommon;
import poly.util.CmmUtil;

import java.util.*;
import java.util.function.Consumer;


@Component("WriteMapper")
public class WriteMapper extends AbstractMongoDBCommon implements IWriteMapper {


    @Autowired
    private MongoTemplate mongodb;

    private Logger log = Logger.getLogger(this.getClass());

    //게시글 저장하기
    @Override
    public void insertwrite(String colNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".insertwrite Start!");


        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        collection.insertOne(new Document(pMap));

        log.info(this.getClass().getName() + ".insertUser End!");




    }
    //게시물 목록 가져오는 쿼리
    @Override
    public List<Map<String ,String>>getUserNoticeList(String colNm, String SS_USER_EMAIL) throws Exception {
        log.info(this.getClass().getName() + ".getUserQuizTitleList start!");

        // 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
        List<Map<String ,Object>> rList = new ArrayList<>();

        // 데이터를 가져올 컬렉션 선택
        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        // 쿼리 만들기
        Document query = new Document();
        query.append("user_email", SS_USER_EMAIL);


        // 조회결과중 출력할 컬럼들
        Document projection = new Document();
        projection.append("title", "$title");
        projection.append("date","$date");
        projection.append("comment","$comment");
        projection.append("num","$num");
        projection.append("_id", "$_id");

        //objectId사용안할것
        //projection.append("_id", 0);

        Consumer<Document> processBlock = doc -> {
            //조회잘되나 출력
            String date =CmmUtil.nvl(doc.getString("date"));
            String title =CmmUtil.nvl(doc.getString("title"));
            String comment =CmmUtil.nvl(doc.getString("comment"));
            ObjectId obj = doc.getObjectId("_id"); ; //pk로 대신사용할것 ID 는 objectID라 형변환 string으로 해줄것.


            Map<String,Object>rMap = new LinkedHashMap<>();

            rMap.put("title", title);
            rMap.put("comment", comment);
            rMap.put("date", date);
            rMap.put("obj", obj);



            rList.add(rMap);


        };

        collection.find(query).projection(projection).forEach(processBlock);

        log.info(this.getClass().getName() + ".getNoticList end");

        return rList;
    }

    //사용자 게시물 정보가져오는 쿼리
    @Override
    public List<Map<String, String>> getUserNoticeInfo(String colNm, String num2) throws Exception {

        log.info(this.getClass().getName() + ".getUserNotice start!");
        log.info(this.getClass().getName() + num2);

        // 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
        List<Map<String ,String>> rList = new ArrayList<>();

        // 데이터를 가져올 컬렉션 선택
        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        // 쿼리 만들기
        Document query = new Document();
        query.append("num", num2);


        // 조회결과중 출력할 컬럼들
        Document projection = new Document();
        projection.append("title", "$title");
        projection.append("comment","$comment");
        projection.append("num","$num");
        //objectId사용안할것
        projection.append("_id", 0);

        Consumer<Document> processBlock = doc -> {

            //연속해서 담을것
            String title =CmmUtil.nvl(doc.getString("title"));
            String comment =CmmUtil.nvl(doc.getString("comment"));
            String num = CmmUtil.nvl(doc.getString("num"));
            ObjectId idc = doc.getObjectId("_id"); ; //pk로 대신사용할것 ID 는 objectID라 형변환 string으로 해줄것.

            log.info(this.getClass().getName()+title);

            Map<String,String>rMap = new LinkedHashMap<>();

            rMap.put("title", title);
            rMap.put("comment", comment);
            rMap.put("num",num);
            //rMap.put("id",idc);


            rList.add(rMap);


        };

        collection.find(query).projection(projection).forEach(processBlock);

        log.info(this.getClass().getName() + ".getNoticList end");

        return rList;
    }

    @Override
    public int updateNotice(String colNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".updateUserPwForFind start");

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        Document findQuery = new Document();
        findQuery.append("num", pMap.get("num"));



        Document updateQuery = new Document();
        updateQuery.append("title", pMap.get("title"));
        updateQuery.append("comment", pMap.get("comment"));

        log.info(pMap.get("title"));
        log.info(pMap.get("comment"));


        UpdateResult updateResults = collection.updateOne(findQuery, new Document("$set", updateQuery));
        int res = (int) updateResults.getMatchedCount();
        log.info("res : " + res);

        log.info(this.getClass().getName() + ".updateUserList end");

        return res;
    }
    //게시글 삭제하기
    @Override
    public int deleNotice(String colNm, Map<String, Object> pMap) {

        log.info(this.getClass().getName() + ".deleteNotice start");


        MongoCollection<Document> collection = mongodb.getCollection(colNm);




        DeleteResult deleteResult = collection.deleteOne();
        int res = (int) deleteResult.getDeletedCount();

        log.info(this.getClass().getName() + ".deleteNotice end");

        return res;
    }
}
