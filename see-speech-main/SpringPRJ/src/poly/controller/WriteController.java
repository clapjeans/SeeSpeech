package poly.controller;

import org.apache.log4j.Logger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import poly.service.IWriteService;
import poly.util.CmmUtil;
import poly.util.DateUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WriteController {

    private final Logger log = Logger.getLogger(this.getClass());

    @Resource(name="WriteService")
    private IWriteService writeService;

    @RequestMapping(value= "Write.do")
    public String Write()throws Exception {
        log.info(this.getClass().getName() + "Write strat");

        return "write/write";
    }

//게시판 리스트로 이동
    @RequestMapping(value= "writelist.do")
    public String List(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                       ModelMap model)throws Exception {
        log.info(this.getClass().getName() + "quizeWrite List strat");

        try{
            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
            log.info(SS_USER_EMAIL+"가져오는가");
            //String user_email = EncryptUtil.encAES128CBC(SS_USER_EMAIL);

            List<Map<String,Object>> userList = writeService.getwriteList(SS_USER_EMAIL);

             model.addAttribute("userList", userList);



        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }


        log.info(this.getClass().getName() + ".UserWrite List end!");

        return "/write/list";

    }


//게시판 수정하기로 이동
@RequestMapping(value= "writeEdit.do",method=RequestMethod.GET)
public String writeEdit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
    ModelMap model)throws Exception {
    log.info(this.getClass().getName() + "WriteEdit strat");

    try{
        String num = CmmUtil.nvl(request.getParameter("num")); //공지글메세지 몰라서 못함 시퀀스 줄만한걸 찾지 못함 .

        log.info(this.getClass().getName()+num+"제대로찍히냐");

        List<Map<String,String>> NoticeInfoList = writeService.getNoticEdit(num);


        for(Map<String, String> pMap :NoticeInfoList){
            log.info(this.getClass().getName()+"title"+pMap.get("title"));
        }


        model.addAttribute("NoticeInfoList", NoticeInfoList);


    } catch (Exception e) {
        log.info(e.toString());
        e.printStackTrace();
    }


    log.info(this.getClass().getName() + ".UserWrite List end!");


    return "/write/writeEdit";
}

//게시판 수정쿼리 실행
@RequestMapping(value= "writeUpdate.do", method= RequestMethod.GET)
public String writeUpdate( HttpServletRequest request, HttpServletResponse response,
                        ModelMap model)throws Exception {
    log.info(this.getClass().getName() + ".updateNotice start");

    String msg = "";
    String url = "";

    try {

        String title = CmmUtil.nvl( request.getParameter("title"));
        String comment = CmmUtil.nvl( request.getParameter("comment"));
        String num = CmmUtil.nvl(request.getParameter("num"));


        log.info("id 찍히냐"+comment);
        log.info("id 찍히냐"+title);
        log.info("num"+num);


        Map<String, Object> pMap = new HashMap<>();
        pMap.put("title", title);
        pMap.put("comment", comment);
        pMap.put("num",num);

        int res = writeService.updateNotice(pMap);

        if (res == 1) {
            msg = "성공적으로 수정했습니다.";
            url = "writelist.do";
        } else {
            msg = "수정에 실패했습니다.";
            url = "writelist.do";
        }

    } catch (Exception e) {
        // 저장 실패 시
        msg = "서버 오류입니다.";
        url = "writelist.do";
        log.info(e.toString());
        e.printStackTrace();
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    log.info(this.getClass().getName() + ".updateNotice end");

    return "/redirect";
}

 //게시판 글 등록하기

    @RequestMapping(value="writeinsert.do", method= RequestMethod.GET)
    public String writeinsert(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                              ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".NoticeInsert start!");

        String msg = "";

        try{
            /*
             * 게시판 글 등록되기 위해 사용되는 form객체의 하위 input 객체 등을 받아오기 위해 사용함
             * */
            String user_email = CmmUtil.nvl((String)session.getAttribute("SS_USER_EMAIL")); //이메일 기준비교값
            String title = CmmUtil.nvl(request.getParameter("title")); //제목
            String comment = CmmUtil.nvl(request.getParameter("comment")); //내용


            /*
             * #######################################################
             * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
             * 						반드시 작성할 것
             * #######################################################
             * */

            log.info("title : "+ title);
            log.info("comment : "+ comment);


            Map<String, Object> pMap = new HashMap<>();

            //--------------------------------------------------------------------------------
          //시퀀스로 활용할 방법을 몰라요^^


            pMap.put("user_email", user_email); //구분값
            pMap.put("title", title); //제목
            pMap.put("comment", comment);  //내용
            pMap.put("date", DateUtil.getDateTime()); //날짜
            pMap.put("num", String.valueOf(Math.random())); //pK로활용할것


            /*
             * 게시글 등록하기위한 비즈니스 로직을 호출
             * */
           writeService.insertwrite(pMap);


            //저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다.";


            //변수 초기화(메모리 효율화 시키기 위해 사용함)
            pMap = null;

        }catch(Exception e){

            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다.";
            log.info(e.toString());
            e.printStackTrace();

        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", "/writelist.do");

        log.info(this.getClass().getName() + ".insertwrite end");

        return "/redirect";
    }

    //게시판 삭제
    @RequestMapping(value = "writedelet.do")
    public String writedelet(HttpSession session, HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        log.info(this.getClass().getName() + ".deleteboard start!");

        String msg = "";
        String url = "";

            try{
                String title = CmmUtil.nvl(request.getParameter("title")); //제목
                String comment = CmmUtil.nvl(request.getParameter("comment")); //내용
                String id = CmmUtil.nvl(request.getParameter("id")); //공지글메세지 몰라서 못함 시퀀스 줄만한걸 찾지 못함 .

                Map<String, Object> pMap = new HashMap<>();
                pMap.put("id", id);
                pMap.put("title", title);
                pMap.put("comment", comment);

                int res = writeService.deleteNotice(pMap);

                if (res == 1) {
                    msg = "성공적으로 삭제 되었습니다.";
                    url = "writelist.do";
                } else {
                    msg = "게시물삭제에 실패했습니다.";
                    url = "writelist.do";
                }

            } catch (Exception e) {
                // 유저 정보 삭제 실패 시
                msg = "서버 오류입니다.";
                url = "writelist.do";
                log.info(e.toString());
                e.printStackTrace();
            }


        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".deleteNotice end!");

        return "/redirect";
    }




}