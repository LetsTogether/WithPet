package com.project.withpet.board.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.project.withpet.board.common.model.vo.PageInfo;
import com.project.withpet.board.model.vo.Board;
import com.project.withpet.board.model.vo.Comments;
import com.project.withpet.board.model.vo.Tag;
import com.project.withpet.board.model.vo.TagBridge;

@Repository
public class BoardDao {

	public ArrayList<Board> selectFrList(SqlSessionTemplate sqlSession, PageInfo pi) {
		int offset = (pi.getCurrentPage()-1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectFrList", null, rowBounds);
	
	}

	public int selectFrListCount(SqlSessionTemplate sqlSession) {
		return sqlSession.selectOne("boardMapper.selectFrListCount");
	}

	public int insertFrBoard(SqlSessionTemplate sqlSession, Board b) {
		return sqlSession.insert("boardMapper.insertFrBoard", b);
	}

	public int increaseCount(SqlSessionTemplate sqlSession, int boardNo) {
		return sqlSession.update("boardMapper.increaseCount", boardNo);
	}

	public Board selectBoard(SqlSessionTemplate sqlSession, int boardNo) {
		return sqlSession.selectOne("boardMapper.selectBoard", boardNo);
	}

	public int deleteBoard(SqlSessionTemplate sqlSession, int boardNo) {
		return sqlSession.update("boardMapper.deleteBoard", boardNo);
	}

	public ArrayList<Comments> selectCommentsList(SqlSessionTemplate sqlSession, int boardNo) {
		return (ArrayList)sqlSession.selectList("boardMapper.selectCommentsList", boardNo);
	}

	public int insertComments(SqlSessionTemplate sqlSession, Comments c) {
		return sqlSession.insert("boardMapper.insertComments", c);
	}

	public int addTag(SqlSessionTemplate sqlSession, String tagName) {
		return sqlSession.insert("boardMapper.addTag",tagName);
	}

	public int removeTag(SqlSessionTemplate sqlSession, String tagName) {
		return sqlSession.delete("boardMapper.removeTag",tagName);
	}

	public int searchTagId(SqlSessionTemplate sqlSession, String tagName) {
		return sqlSession.selectOne("boardMapper.searchTagId", tagName);
	}

	public String selectTag(SqlSessionTemplate sqlSession, String tagName) {
		return sqlSession.selectOne("boardMapper.selectTag", tagName);
	}
	
	public void insertTagBridge(SqlSessionTemplate sqlSession,int boardNo, int tagId) {
		Map<String, Object> params = new HashMap<>();
	    params.put("boardNo", boardNo);
	    params.put("tagId", tagId);
	    sqlSession.insert("boardMapper.insertTagBridge", params);
	}

	public ArrayList<Tag> selectTagName(SqlSessionTemplate sqlSession, int boardNo) {
		return (ArrayList)sqlSession.selectList("boardMapper.selectTagName", boardNo);
	}


	public void insertTagBridge(SqlSessionTemplate sqlSession, List<TagBridge> tagBridges) {
		sqlSession.insert("boardMapper.insertTagBridge", tagBridges);
		
	}

	public int selectTagId(SqlSessionTemplate sqlSession, String tagName) {
		return sqlSession.selectOne("boardMapper.selectTagId", tagName);
	}

	
}
