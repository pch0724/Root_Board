package com.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;

@Controller
public class ReadController {
	
	@Autowired
	BoardDao boarddao;
	
	@RequestMapping(value = "/board/read.do", method = RequestMethod.GET)
	public String read(@RequestParam("seq") int seq, Model model){
		
		BoardVO readBoard = boarddao.read(seq);
		
		model.addAttribute("read", readBoard);
		
		boarddao.hitup(seq);
		
		return "read";
	}
	
	@RequestMapping(value = "/board/fileDownload.do")
	public void fileDownload4(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String fileName = request.getParameter("fileName");
	    String originalFileName = request.getParameter("originalFileName");

	    // 실제 파일 경로를 생성
	    String savePath = request.getSession().getServletContext().getRealPath("upload");
	    String filePath = savePath +"/"+ fileName;

	    // 파일을 읽고 응답 객체에 쓰기
	    File file = new File(filePath);
	    FileInputStream fileInputStream = new FileInputStream(file);
	    ServletOutputStream servletOutputStream = response.getOutputStream();

	    // 파일 다운로드 헤더 설정
	    String userAgent = request.getHeader("User-Agent");
	    boolean isIE = userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1; // IE인 경우
	    String downFileName = null;

	    if (isIE) {
	        downFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");
	        response.setHeader("Content-Disposition", "attachment;filename=\"" + downFileName + "\";");
	    } else {
	        downFileName = new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + downFileName + "\";");
	    }

	    response.setHeader("Content-Type", "application/octet-stream");
	    response.setHeader("Content-Transfer-Encoding", "binary;");
	    response.setHeader("Pragma", "no-cache;");
	    response.setHeader("Expires", "-1;");

	    // 파일 쓰기
	    byte[] buffer = new byte[1024 * 8];
	    while (true) {
	        int count = fileInputStream.read(buffer);
	        if (count == -1) {
	            break;
	        }
	        servletOutputStream.write(buffer, 0, count);
	    }

	    // 리소스 해제
	    fileInputStream.close();
	    servletOutputStream.close();
	}
	
}
