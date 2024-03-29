package com.project.withpet.member.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.project.withpet.board.common.model.vo.PageInfo;
import com.project.withpet.board.common.template.Pagination;
import com.project.withpet.board.model.vo.Board;
import com.project.withpet.board.model.vo.Comments;
import com.project.withpet.member.model.service.MemberService;
import com.project.withpet.member.model.vo.CertVO;
import com.project.withpet.member.model.vo.Friend;
import com.project.withpet.member.model.vo.Inquiry;
import com.project.withpet.member.model.vo.Member;
import com.project.withpet.member.model.vo.Passward;
import com.project.withpet.member.model.vo.Schedule;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@RequestMapping("sendBack.me")
	public void sendBack(HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print("http://localhost:8787/withpet");
	}
	
	@RequestMapping(value="idCheck.me", produces="application/json; charset=UTF-8")
	public void idCheck(String checkId, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(memberService.idCheck(checkId));
	}
	
	@RequestMapping(value="nickCheck.me", produces="application/json; charset=UTF-8")
	public void nickCheck(String checkNick, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(memberService.nickCheck(checkNick));
	}
	
	@RequestMapping(value="phoneCheck.me", produces="application/json; charset=UTF-8")
	public void phoneCheck(String checkPhone, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(memberService.phoneCheck(checkPhone));
	}
	
	@RequestMapping(value="emailCheck.me", produces="application/json; charset=UTF-8")
	public void emailCheck(String checkEmail, HttpServletResponse response) throws ServletException, IOException {
		
		String email = checkEmail.substring(0, checkEmail.lastIndexOf("@")) + checkEmail.substring(checkEmail.lastIndexOf("@")).toLowerCase();
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(memberService.emailCheck(email));
	}
	
	@RequestMapping("insert.me")
	public ModelAndView insertUser(Member member, ModelAndView mv, HttpServletRequest request) throws ServletException, IOException{
		
		String encPwd = bcryptPasswordEncoder.encode(member.getMemPwd());
		member.setMemPwd(encPwd);
		
		String email = member.getMemEmail().substring(0, member.getMemEmail().lastIndexOf("@")) + member.getMemEmail().substring(member.getMemEmail().lastIndexOf("@")).toLowerCase();
		member.setMemEmail(email);
		
		HttpSession session = request.getSession();
		session.removeAttribute("kakaoId");
		
		if(memberService.insertMember(member) > 0) {
			mv.setViewName("common/main");
		} else { 
			mv.addObject("errorMsg", "에러 발생").setViewName("common/errorPage");
		}
		
		return mv;
		
	}
	
	@RequestMapping("login.me")
	public ModelAndView selectMember(Member member, ModelAndView mv, HttpSession session) throws ServletException, IOException{
				
		Member loginMember = memberService.selectMember(member);
		int loginMemo = memberService.selectMemoCount(member.getMemId());
		
		if(loginMember != null && (member.getMemPwd().equals(loginMember.getMemPwd())) /* && bcryptPasswordEncoder.matches(member.getMemPwd(), loginMember.getMemPwd())*/) {
			session.setAttribute("loginMember", loginMember);
			session.setAttribute("loginMemo", loginMemo);
			mv.setViewName("redirect:/");
		} else {
			mv.addObject("errorMsg", "응 안돼~");
			mv.setViewName("common/main");
		}
		
		return mv;
		
	}
	
	@RequestMapping("logout.me")
	public ModelAndView deleteMember(String memId, ModelAndView mv, HttpSession session) throws ServletException, IOException{
				
		session.removeAttribute("loginMember");
		session.removeAttribute("loginMemo");
		
		mv.setViewName("redirect:/");
		
		return mv;
		
	}
	
	
	@RequestMapping("kakaoGetCodeUrl")
	public void kakaoGetCodeUrl(HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("kakaoGetCodeUrl 진입");
		
		String getCodeUrl = "https://kauth.kakao.com/oauth/authorize"
				        + "?client_id=" + "261f64346cb2da405692b542c232a7d3&"
				        + "&redirect_uri=" + "http://localhost:8787/withpet/kakaoLoginJump"
				        + "&response_type=code";
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(getCodeUrl);
	}
	
	@RequestMapping("kakaoLogin.me")
	public void selectKakaoMember(String kakaoId, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		
		System.out.println("kakaoLogin 진입");
		
		Member TempMember = memberService.selectKakaoMember(kakaoId);
		
		if(TempMember != null) {
			session.setAttribute("loginMember", TempMember);
			session.setAttribute("loginMemo", memberService.selectMemoCount(TempMember.getMemId()));
			session.removeAttribute("kakaoId");			
			
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("1");
		} else {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("0");
		}
	}
	
	@RequestMapping("/kakaoLoginJump")
	public String kakaoLoginJump() throws ServletException, IOException {
		System.out.println("kakaoLoginJump 진입");
		return "member/kakaoLogin";
	}
	
	@RequestMapping("kakaoGetTokenUser")
	public void kakaoGetTokenUser(String code, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		
		System.out.println("kakaoGetTokenUser 진입");
		
		String accessToken = getAccessToken(code);
		HashMap<String, Object> userInfo = getUserInfo(accessToken);
		
		HttpSession session = request.getSession();
		session.setAttribute("accessToken", userInfo.get("accessToken"));
		session.setAttribute("kakaoId", userInfo.get("id"));
		
		if(memberService.selectKakaoCount((String)userInfo.get("id")) == 0){			
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("http://localhost:8787/withpet");
		} else {
        response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print("http://localhost:8787/withpet");
		}
		
	}
	
	@RequestMapping("deleteKakaoId")
	public String deleteKakaoId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("kakaoId");
		
		return "common/main";
	}
	
	public String getAccessToken(String code) throws ServletException, IOException, ParseException {
		
		System.out.println("getAccessToken 진입");
		
		String GetTokenUrl = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code"
				   + "&client_id=" + "261f64346cb2da405692b542c232a7d3&"
		           + "&redirect_uri=" + "http://localhost:8787/withpet/kakaoLoginJump"
		           + "&code=" + code;
		
		URL requestUrl = new URL(GetTokenUrl);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoOutput(true);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String responseText = "";
		String access_Token = "";
		String line;
		
		while((line = br.readLine()) != null) {
			responseText += line;
		}
		
		if(!responseText.equals("")) {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject)parser.parse(responseText);
			
			access_Token = (String)jsonObject.get("access_token");
		}
		
		br.close();
		urlConnection.disconnect();
		
		return access_Token;
	}
	
	public HashMap<String, Object> getUserInfo(String accessToken) throws ServletException, IOException, ParseException {
	
		System.out.println("getUserInfo 진입");
		
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		String getUserURL = "https://kapi.kakao.com/v2/user/me";
		
		URL requestUrl = new URL(getUserURL);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String responseText = "";
		String line;
		
		while((line = br.readLine()) != null) {
			responseText += line;
		}
		
		if(!responseText.equals("")) {
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(responseText);
			String id = element.getAsJsonObject().get("id").getAsString();
            
            userInfo.put("accessToken", accessToken);
            userInfo.put("id", id);
         
		}
		
		return userInfo;
	}
	
	@RequestMapping(value="memberDiaryMain.me")
	public ModelAndView memberDiaryMain(Schedule schedule, ModelAndView mv){
		
		mv.setViewName("member/diary/memberDiary");
		
		return mv;
	}
	
	@RequestMapping(value="insertSchedule.me")
	public ModelAndView insertSchedule(Schedule schedule, ModelAndView mv){
		
		int schedulecount = memberService.insertSchedule(schedule);

		mv.setViewName("member/diary/memberDiary");
		
		return mv;
		
	}
	
	@ResponseBody
	@RequestMapping(value="selectSchedules.me", produces="application/json; charset=UTF-8")
	public String selectSchedules(Schedule schedule, ModelAndView mv){

		ArrayList<Schedule> schedules = memberService.selectSchedules(schedule); 
		
		return new Gson().toJson(schedules);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 박민성
	
	// 작성글 보기
	
	
	// 메일 보내기
	@Autowired
	private JavaMailSenderImpl sender;
	
	@ResponseBody
	@PostMapping("sendMail.bo")
	public int sendMail(String email, HttpServletRequest request) throws MessagingException {
		System.out.println("오냐?");
		System.out.println(email);
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		String ip = request.getRemoteAddr();
		String secret = generateSecret();
		
		CertVO certvo = CertVO.builder()
				  .who(ip)
				  .secret(secret)
				  .build();
		
		int result = memberService.sendMail(certvo);
		
		helper.setTo(email);
		helper.setSubject("인증번호 보내드립니다");
		helper.setText("인증번호 : " + secret);
		
		sender.send(message);
		
		return result;
		
	}
	
	public String generateSecret() {
	Random r = new Random();
	int i = r.nextInt(100000);
	Format f= new DecimalFormat("000000");
	String secret = f.format(i);
	
	return secret;
	}

	
	@ResponseBody
	@PostMapping("check")
	public int checkCode(String code, HttpServletRequest request) {
		CertVO certVo = CertVO.builder().who(request.getRemoteAddr()).secret(code).build();
		return memberService.validata(certVo);
	}
	
	@ResponseBody
	@PostMapping(value="idFind", produces="application/json; charset=UTF-8")
	public String idFind(String email, HttpServletRequest request) {
		return new Gson().toJson(memberService.idFind(email));
	}
	
	@ResponseBody
	@PostMapping("pwdMail.bo")
	public int pwdMail(String email, HttpServletRequest request) throws MessagingException {
		System.out.println("오냐?");
		System.out.println(email);
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		String ip = request.getRemoteAddr();
		String secret = generateSecret();
		
		CertVO certvo = CertVO.builder()
				  .who(ip)
				  .secret(secret)
				  .build();
		
		int result = memberService.sendMail(certvo);
		
		helper.setTo(email);
		helper.setSubject("인증번호 보내드립니다");
		helper.setText("인증번호 : " + secret);
		
		sender.send(message);
		
		return result;
		
	}
	
	@ResponseBody
	@PostMapping("pwdCheck")
	public int pwdCheck(String code, HttpServletRequest request) {
		CertVO certVo = CertVO.builder().who(request.getRemoteAddr()).secret(code).build();
		return memberService.validata(certVo);
	}

	@ResponseBody
	@PostMapping("pwdFind")
	public ModelAndView pwdFind(Passward p, HttpServletRequest request, ModelAndView mv) {
		if(memberService.pwdFind(p) > 0) {
			mv.addObject("alertMsg", "비밀번호가 변경되었습니다!");
			mv.setViewName("common/main");
		}else {
			mv.addObject("errorMsg", "비밀번호변경에 실패하였습니다!");
			mv.setViewName("common/main");
		}
		return mv;
	}
	
	@PostMapping("myPage")
	public String myPage(@RequestParam(value="mPage", defaultValue="1") int currentPage, String memberId, HttpServletRequest request, Model m) {
		
		PageInfo pi = Pagination.getPageInfo(memberService.boardCount(memberId), currentPage, 10, 10);
		pi.setMemberId(memberId);
		ArrayList<Board> list = memberService.myPage(pi);
		if(list.isEmpty()) {
			m.addAttribute("boardList", list);
			return "member/myPageMain";

		}else {
			m.addAttribute("number", 1);
			m.addAttribute("pi", pi);
			m.addAttribute("boardList", list);
			return "member/myPageMain";
		}
	}
	
	@PostMapping("myPageReply")
	public String myPageReply(@RequestParam(value="mPage", defaultValue="1") int currentPage, String memberId, HttpServletRequest request, Model m) {
		
		PageInfo pi = Pagination.getPageInfo(memberService.replyCount(memberId), currentPage, 10, 10);
		pi.setMemberId(memberId);
		ArrayList<Comments> list = memberService.myPageReply(pi);
		if(list.isEmpty()) {
			m.addAttribute("boardList", list);
			return "member/myPageMain";

		}else {
			m.addAttribute("number", 2);
			m.addAttribute("pi", pi);
			m.addAttribute("ReplyList", list);
			return "member/myPageMain";
		}
	}
	@PostMapping("myPageLike")
	public String myPageLike(@RequestParam(value="mPage", defaultValue="1") int currentPage, String memberId, HttpServletRequest request, Model m) {
		
		PageInfo pi = Pagination.getPageInfo(memberService.likeCount(memberId), currentPage, 10, 10);
		pi.setMemberId(memberId);
		ArrayList<Board> list = memberService.myPageLike(pi);
		if(list.isEmpty()) {
			m.addAttribute("boardList", list);
			return "member/myPageMain";

		}else {
			m.addAttribute("number", 3);
			m.addAttribute("pi", pi);
			m.addAttribute("boardList", list);
			return "member/myPageMain";
		}
	}
	
	
	@PostMapping("myPageDelete")
	public String myPageDelete(@RequestParam(value="mPage", defaultValue="1") int currentPage, String memberId, HttpServletRequest request, Model m) {
		
		PageInfo pi = Pagination.getPageInfo(memberService.deleteCount(memberId), currentPage, 10, 10);
		pi.setMemberId(memberId);
		ArrayList<Board> list = memberService.myPageDelete(pi);
		if(list.isEmpty()) {
			m.addAttribute("boardList", list);
			return "member/myPageMain";

		}else {
			m.addAttribute("number", 5);
			m.addAttribute("pi", pi);
			m.addAttribute("boardList", list);
			return "member/myPageMain";
		}
		
	}
	
	@RequestMapping("myPageFriend.me")
	public String myPageFriend(@RequestParam(value="mPage", defaultValue="1") int currentPage, HttpServletRequest request, Model m) {
		HttpSession session = request.getSession();
		String memberId = (String)((Member)session.getAttribute("loginMember")).getMemId();
		PageInfo pi = Pagination.getPageInfo(memberService.friendCount(memberId), currentPage, 6, 10);
		pi.setMemberId(memberId);
		
		ArrayList<Member> list = memberService.myPageFriend(pi);
		if(list.isEmpty()) {
			m.addAttribute("friendList", list);
			return "member/friend/myPageFriend";

		}else {
			m.addAttribute("pi", pi);
			m.addAttribute("friendList", list);
			return "member/friend/myPageFriend";
		}
	}
	
	@PostMapping("friendDelete.me")
	public String friendDelete(String memberId, String friendId, Model m){
		Friend fri = new Friend(memberId, friendId);
		
		if(memberService.friendDelete(fri) > 0) {
			m.addAttribute("alertMsg", "친구 삭제에 성공하셨습니다.");
			return "redirect:myPageFriend.me";
		}else {
			m.addAttribute("alertMsg", "친구 삭제에 실패하셨습니다.");
			return "redirect:myPageFriend.me";
		}
	}
	
	@PostMapping("freindSharing.me")
	public String freindSharing(String memberId, String friendId, Model m) {
		Friend fri = new Friend(memberId, friendId);
		
		if(memberService.freindSharing(fri) > 0) {
			m.addAttribute("alertMsg", "친구 일정 공유에 성공하셨습니다.");
			return "redirect:myPageFriend.me";
		} else {
			m.addAttribute("alertMsg", "친구 일정 공유에 실패하셨습니다.");
			return "redirect:myPageFriend.me";
		}
	}
	
	@PostMapping("sharingCancellation.me")
	public String sharingCancellation(String memberId, String friendId, Model m) {
		Friend fri = new Friend(memberId, friendId);
		
		if(memberService.sharingCancellation(fri) > 0) {
			m.addAttribute("alertMsg", "친구 일정 공유 취소에 성공하셨습니다.");
			return "redirect:myPageFriend.me";
		} else {
			m.addAttribute("alertMsg", "친구 일정 공유 취소에 실패하셨습니다.");
			return "redirect:myPageFriend.me";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="friendSearch.me", produces="application/json; charset=UTF-8")
	public String friendSearch(String friendSearch, String memberId, Model m) {
		Friend fri = new Friend(memberId, friendSearch);
		Member member = memberService.friendSearch(fri);
		System.out.println(member);
		if(member != null) {
			return new Gson().toJson(member);
		}else {
			return new Gson().toJson("없음");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="liveSearch.me", produces="application/json; charset=UTF-8")
	public String liveSearch(String keyword) {
		ArrayList<Member> list = memberService.liveSearch(keyword +"%");
		if(list != null) {
			return new Gson().toJson(list);
		}else {
			return new Gson().toJson("없음");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="findMember.me", produces="application/json; charset=UTF-8")
	public String findMember(String findMember) {
		Member member = memberService.findMember(findMember);
		if(member != null) {
			return new Gson().toJson(member);
		}else {
			return new Gson().toJson("없음");
		}
	}
	
	@PostMapping("friendInsert.me")
	public String friendInsert(String friendId, String memberId, Model m) {
		return null;
	}
	
	@RequestMapping("inquiry.me")
	public String inquiry(@RequestParam(value="iPage", defaultValue="1") int currentPage, HttpServletRequest request, Model m) {
		HttpSession session = request.getSession();
		String memberId = ((Member)session.getAttribute("loginMember")).getMemId();
		PageInfo pi = Pagination.getPageInfo(memberService.inquiryCount(memberId), currentPage, 10, 10);
		pi.setMemberId(memberId);
		ArrayList<Inquiry> list = memberService.inquiry(pi);
		if(list.isEmpty()) {
			m.addAttribute("inquiryList", list);
			return "member/inquiry/memberInquiry";
		}else {
			m.addAttribute("pi", pi);
			m.addAttribute("inquiryList", list);
			return "member/inquiry/memberInquiry";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="inquiryDetail.me", produces="application/json; charset=UTF-8")
	public String inquiryDetail(int inquiryNo, String memberId) {
		System.out.println(memberId);
		Inquiry i = new Inquiry(memberId, inquiryNo);
		Inquiry inquiry = memberService.inquiryDetail(i);
		if(inquiry != null) {
			if(inquiry.getInquiryAnswer() == null) {
				inquiry.setInquiryAnswer("답변 내용이 아직 작성되지 않았습니다.");
			}
			return new Gson().toJson(inquiry);
		}else {
			return new Gson().toJson("없음");
		}
	}
	
	@RequestMapping("inuqiryDelete.me")
	public String inquiryDelete(int ino, Model m) {
		if(memberService.inquiryDelete(ino) > 0) {
			m.addAttribute("alertMsg", "삭제에 성공하셨습니다.");
			return "redirect:inquiry.me";
		}else {
			m.addAttribute("alertMsg", "삭제에 실패하셨습니다.");
			return "redirect:inquiry.me";
		}
	}
	
	@RequestMapping("inquiryInsertPage.me")
	public String inquiryInsertPage() {
		return "member/modal/memberinquiryModal";
	}
	
	@RequestMapping("inquiryInsert.me")
	public String inquiryInsert(String memberId, String inquiryTitle, String inquiryContent, Model m) {
		Inquiry i = new Inquiry();
		i.setMemberId(memberId);
		i.setInquiryTitle(inquiryTitle);
		i.setInquiryContent(inquiryContent);
		System.out.println(i);
		if(memberService.inquiryInsert(i) > 0) {
			m.addAttribute("alertMsg", "작성에 성공하셨습니다.");
			return "redirect:inquiry.me";
		}else {
			m.addAttribute("alertMsg", "작성에 실패하셨습니다.");
			return "redirect:inquiry.me";
		}
		
	}
	
}
