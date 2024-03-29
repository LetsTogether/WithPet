package com.project.withpet.board.model.service;

import java.util.ArrayList;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.withpet.board.common.model.vo.PageInfo;
import com.project.withpet.board.model.dao.BoardDao;
import com.project.withpet.board.model.vo.Board;
import com.project.withpet.board.model.vo.Comments;


@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public ArrayList<Board> selectFrList(PageInfo pi) {
		return boardDao.selectFrList(sqlSession,pi);
	}

	@Override
	public int selectFrListCount() {
		return boardDao.selectFrListCount(sqlSession);
	}

	@Override
	public int insertFrBoard(Board b) {
		return boardDao.insertFrBoard(sqlSession,b);
	}

	@Override
	public int increaseCount(int boardNo) {
		return boardDao.increaseCount(sqlSession,boardNo);
	}

	@Override
	public Board selectBoard(int boardNo) {
		return boardDao.selectBoard(sqlSession,boardNo);
	}

	@Override
	public int deleteBoard(int boardNo) {
		return boardDao.deleteBoard(sqlSession,boardNo);
	}

	@Override
	public ArrayList<Comments> selectCommentsList(int boardNo) {
		return boardDao.selectCommentsList(sqlSession,boardNo);
	}

	@Override
	public int insertComments(Comments c) {
		return boardDao.insertComments(sqlSession,c);
	}
	


}
