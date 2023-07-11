package com.board.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;

@Controller
public class DeleteController {

	@Autowired
	BoardDao boardDao;
	@Autowired
	SqlSession ss;
	
	@RequestMapping(value =  "/board/delete.do", method=RequestMethod.GET)
	public String delete(@RequestParam("seq") int seq, Model model){
		
		BoardVO tempDeleteVO = boardDao.beforeDelete(seq);
		model.addAttribute("delete", tempDeleteVO);
		
		System.out.println(tempDeleteVO.getSeq());
		
		return "delete";
	}
	
	@RequestMapping(value= "/board/delete_pro.do", method=RequestMethod.POST)
	public String delete_pro(int seq, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		BoardVO boardVO = boardDao.read(seq); // 게시물 정보를 가져옵니다.
	    String realPath = request.getSession().getServletContext().getRealPath("upload"); // 파일 저장 경로를 가져옵니다.

	    if (boardVO.getFileName() != null && !boardVO.getFileName().isEmpty()) {
	        Path path = Paths.get(realPath + "/" + boardVO.getFileName());
	        if (Files.exists(path)) {
	            Files.delete(path); // 저장되어 있는 파일을 삭제합니다.
	            boardDao.updateFileName(seq, "", ""); // 파일명을 빈 문자열로 업데이트합니다.
	            response.setStatus(HttpServletResponse.SC_OK);
	        }
	    }
		
		boardDao.delete(seq);
		return "redirect:list.do";
	}
	
}
