package com.board.controller;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;

import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WriteController {
	@Autowired
	private BoardDao boardDao;
	@Autowired
	SqlSession ss;

	@RequestMapping(value = "/board/write.do", method=RequestMethod.GET)
	public ModelAndView process(){
	System.out.println("글쓰기 페이지");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("write");

		return mav;
	}
	
	@RequestMapping(value="/board/write_pro.do", method=RequestMethod.POST)
	public String insertProc(BoardVO vo, HttpServletRequest request) throws IllegalStateException, IOException{

		System.out.println("===>Controller로 insertBoard 접속");
		//MultipartFile로 파일 정보를 받음
		MultipartFile uploadFile = vo.getUploadFile();
		System.out.println("uploadFile : "+uploadFile);
		//randomUUID로 랜덤값 받음
		String genId = UUID.randomUUID().toString().substring(5, 12);
		System.out.println("fileName : "+vo.getFileName());
		// realPath로 프로젝트에 fileSave 폴더에 값을 저장함
		String realPath = request.getSession().getServletContext().getRealPath("upload");
		System.out.println("경로 : "+realPath);
		
	    // realPath에 해당하는 디렉토리가 존재하지 않는 경우 생성합니다.
	    File dir = new File(realPath);
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }
		
		// 만약 uploadFile이 비어있지않다면 다음과 같이 코드 실행
		if(!uploadFile.isEmpty()) {
			System.out.println("여기 접속?");
			// originalFileName에 원본 파일명을 저장한다.
			String originalFileName = uploadFile.getOriginalFilename();
			System.out.println("originalFileName : "+originalFileName);
			// saveFileName에 랜덤값이 저장된 genID와 파일명을 저장한다.
			String saveFileName = genId + "." + FilenameUtils.getExtension(originalFileName);
			System.out.println("saveFileName : "+saveFileName);
			// 아래 함수로 파일을 업로드한다.
			uploadFile.transferTo(new File(realPath+"/"+saveFileName));
			// 다음 함수로 파일명을 vo에 저장한다.
			vo.setFileName(saveFileName);
			vo.setOriginalFileName(originalFileName);
		}

		ss.insert("write",vo);

		return "redirect:list.do";
		// /board/list.jsp로 이동한다. 			
	}
	
	@RequestMapping(value = "/board/reply.do", method = RequestMethod.GET)
	public String reply(@RequestParam("seq") int seq, Model model) {
	    BoardVO parentBoard = boardDao.read(seq);
	    model.addAttribute("parentBoard", parentBoard);
	    return "reply";
	}
	
	@RequestMapping(value = "/board/reply_pro.do", method = RequestMethod.POST)
    public String insertReplyProc(BoardVO vo, HttpServletRequest request) throws IllegalStateException, IOException {
        // 답글 정보 업데이트
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("ref", vo.getRef());
		paramMap.put("step", vo.getStep());
		boardDao.updateReplyInfo(paramMap);

		vo.setStep(vo.getStep() + 1);
		vo.setDepth(vo.getDepth() + 1);
        // 파일 업로드 및 처리 과정은 기존 insertProc 메소드와 동일하게 구현할 수 있습니다.
        // 아래 코드는 insertProc과 동일한 파일 처리 부분입니다.
        MultipartFile uploadFile = vo.getUploadFile();
        String genId = UUID.randomUUID().toString().substring(5, 12);
        String realPath = request.getSession().getServletContext().getRealPath("upload");

        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

		// 만약 uploadFile이 비어있지않다면 다음과 같이 코드 실행
		if(!uploadFile.isEmpty()) {
			System.out.println("여기 접속?");
			// originalFileName에 원본 파일명을 저장한다.
			String originalFileName = uploadFile.getOriginalFilename();
			System.out.println("originalFileName : "+originalFileName);
			// saveFileName에 랜덤값이 저장된 genID와 파일명을 저장한다.
			String saveFileName = genId + "." + FilenameUtils.getExtension(originalFileName);
			System.out.println("saveFileName : "+saveFileName);
			// 아래 함수로 파일을 업로드한다.
			uploadFile.transferTo(new File(realPath+"/"+saveFileName));
			// 다음 함수로 파일명을 vo에 저장한다.
			vo.setFileName(saveFileName);
			vo.setOriginalFileName(originalFileName);
		}

        // 답글 추가
        ss.insert("insertReply", vo);
        return "redirect:list.do";
    }
	
}
