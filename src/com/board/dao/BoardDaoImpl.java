package com.board.dao;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {
	
	public List<BoardVO> list(Map<String, Object> map) {
		List<BoardVO> list = getSqlSession().selectList("boardList", map);
		return list;
	}

	public int getCount(Map<String, Object> map) {
		return ((Integer) getSqlSession().selectOne("boardCount", map)).intValue();
	}
	
	public void hitup(int seq){
		getSqlSession().update("hitup",seq);
	}
	
	public BoardVO read(int seq){
		return (getSqlSession().selectOne("read", seq));
	}
	
	public void delete(int seq){
		getSqlSession().delete("delete", seq);
	}
	
	public int checkPassword(int seq, String pass) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("seq", seq);
        paramMap.put("pass", pass);
        return getSqlSession().selectOne("checkPassword", paramMap);
    }
	
	public BoardVO beforeDelete(int seq){
		return (getSqlSession().selectOne("beforeDelete", seq));
	}
	
	public void updateFileName(int seq, String fileName, String originalFileName){
		
	}

	public void updateReplyInfo(Map<String, Object> paramMap) {
		getSqlSession().update("updateReplyInfo", paramMap);
	}

	public void insertReply(BoardVO vo) {
		
	}

}
