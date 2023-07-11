package com.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;

@Controller
public class ModifyController {
	@Autowired
	private BoardDao boardDao;
	@Autowired
	SqlSession ss;
	
	
	@RequestMapping(value = "/board/modify.do", method=RequestMethod.GET)
	public String modify(@RequestParam("seq") int seq, Model model){
		
		BoardVO tempContentVO  = boardDao.read(seq);
		model.addAttribute("modify", tempContentVO);
		
		return "modify";
	}
	
	@RequestMapping({"/board/checkPassword.do"})
	public void checkPassword(HttpServletRequest request, HttpServletResponse response) {
	    System.out.println("통신 확인");
	    String seq = request.getParameter("seq");
	    String pass = request.getParameter("pass");
	    System.out.println(seq);
	    System.out.println(pass);
	    
	    int chkPass = boardDao.checkPassword(Integer.parseInt(seq), pass);
	    
	    System.out.println(chkPass);
	    // 응답 데이터를 생성하고 클라이언트에 보냅니다.
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    try (PrintWriter out = response.getWriter()) {
	        out.print(chkPass);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value="/board/modify_pro.do", method=RequestMethod.POST)
	public String updateProc(@RequestParam("seq") int seq, @RequestParam("file-remove") boolean fileRemove, BoardVO vo, HttpServletRequest request) throws IllegalStateException, IOException{
		System.out.println(fileRemove);
		System.out.println(request.getParameter("${modify.fileName}"));
		System.out.println(vo.getFileName());
		System.out.println(fileRemove);
		
		System.out.println();
		System.out.println("===>Controller로 insertBoard 접속");
		//MultipartFile로 파일 정보를 받음
		MultipartFile uploadFile = vo.getUploadFile();
		System.out.println("uploadFile : "+uploadFile);
		//randomUUID로 랜덤값 받음
		String genId = UUID.randomUUID().toString().substring(5, 12);
		// realPath로 프로젝트에 fileSave 폴더에 값을 저장함
		String realPath = request.getSession().getServletContext().getRealPath("upload");
		System.out.println("경로 : "+realPath);
		
		// 만약 uploadFile이 비어있지않다면 다음과 같이 코드 실행
		if (!uploadFile.isEmpty()) {
	        if (vo.getOriginalFileName() != null && !vo.getOriginalFileName().isEmpty()) { // fileRemove 사용
	            File oldFile = new File(realPath + "/" + vo.getFileName());
	            if (oldFile.exists()) {
	                oldFile.delete();
	            }
	        }
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
		}else {
			if (fileRemove == true && (vo.getOriginalFileName() == null || vo.getOriginalFileName().isEmpty())) { // fileRemove 사용
	            File oldFile = new File(realPath + "/" + vo.getFileName());
	            if (oldFile.exists()) {
	                oldFile.delete();
	                vo.setFileName("");
	                vo.setOriginalFileName("");
	            }
	        }
		}
		
		ss.update("modify",vo);
		return "redirect:list.do";
		// /board/list.jsp로 이동한다.
		
	}
	@RequestMapping(value="/board/deleteFile.do", method=RequestMethod.POST)
	public void deleteFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("seq") int seq) throws IOException {
	    BoardVO boardVO = boardDao.read(seq); // 게시물 정보를 가져옵니다.
	    String realPath = request.getSession().getServletContext().getRealPath("upload"); // 파일 저장 경로를 가져옵니다.

	    if (!boardVO.getFileName().isEmpty()) {
	        Path path = Paths.get(realPath + "/" + boardVO.getFileName());
	        if (Files.exists(path)) {
	            Files.delete(path); // 저장되어 있는 파일을 삭제합니다.
	            boardDao.updateFileName(seq, "", ""); // 파일명을 빈 문자열로 업데이트합니다.
	            response.setStatus(HttpServletResponse.SC_OK);
	        }
	    }
	}
	
}
