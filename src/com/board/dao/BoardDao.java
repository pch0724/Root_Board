package com.board.dao;

import com.board.VO.BoardVO;

import java.util.List;
import java.util.Map;

public abstract interface BoardDao {
	
	public abstract List<BoardVO> list(Map<String, Object> paramMap);

	public abstract int getCount(Map<String, Object> paramMap);
	
	public abstract void hitup(int seq);
	
	public abstract BoardVO read(int seq);
	
	public abstract void delete(int seq);
	
	public abstract int checkPassword(int seq, String pass);
	
	public abstract BoardVO beforeDelete(int seq);
	
	public abstract void updateFileName(int seq, String fileName, String originalFileName);
	
	public abstract void updateReplyInfo(Map<String, Object> paramMap);
	
	public abstract void insertReply(BoardVO vo);

}
