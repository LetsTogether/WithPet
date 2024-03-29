<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>헤더</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
	
	<!-- Noto sans 글꼴 -->
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" rel="stylesheet">
    
    <style>
        div {box-sizing:border-box;} 

        #header {
            width:100%;
            height:130px;
            background-color: rgb(73, 166, 112);
            color:white;
            position:relative;
            z-index:2;
        }
        #header a {text-decoration:none; color:white;}

        #header_1 {width: 10%; height:100%; float:left;}
        #header_2 {width: 15%; height:100%; float:left;}
        #header_3 {width: 15%; height:100%; float:left;}
        #header_4 {width: 21%; height:100%; float:left;}
        #header_5 {width: 12%; height:100%; float:left;}
        #header_6 {width: 12%; height:100%; float:left;}
        #header_7 {width: 15%; height:100%; float:left;}

        #header_1>img {height:100%; position:absolute; margin:auto; top:0px; bottom:0px; right:0px; left:0px;}

        #header_2{text-align:center; line-height:60px; font-size:17px; padding-top:55px;}
        @media (max-width: 470px) { #header_2 { font-size: 8px; } }
        #header_2_a{padding-bottom: 40px; transform : scale(1);}
        #header_2_detail{width:80%; line-height:35px; font-size:13px; background-color:white; color:black; margin-left:10%; display:none; border-radius:10%; border:black solid 1px;}
        @media (max-width: 470px) { #header_2_detail { font-size: 13px; } }
        #header_2>a:hover{cursor:pointer;}
        #header_2>a:hover + div{display:block;}
        #header_2>a~div:hover{cursor:pointer; display:block;}

        #header_3{text-align:center; line-height:60px; font-size:17px; padding-top:55px; position:relative;}
        @media (max-width: 470px) { #header_3 { font-size: 8px; } }
        #header_3_a{padding-bottom: 50px; transform : scale(1);}
        #header_3_detail{width:80%; line-height:35px; font-size:13px; background-color:white; color:black; position:absolute; display:none; margin-left:10%; border-radius:10%; border:black solid 1px;}
        @media (max-width: 470px) { #header_3_detail { font-size: 13px; } }
        #header_3>a:hover{cursor:pointer;}
        #header_3>a:hover + div{display:block;}
        #header_3>a~div:hover{cursor:pointer; display:block;}

        #header_4{text-align:center; line-height:60px; font-size:17px; padding-top:55px; position:relative;}
        @media (max-width: 470px) { #header_4 { font-size: 8px; } }
        #header_4_a{padding-bottom: 50px; transform : scale(1);}
        #header_4_detail{width:100%; line-height:35px; font-size:13px; background-color:white; color:black; position:absolute; display:none; border-radius:10%; border:black solid 1px;}
        @media (max-width: 470px) { #header_4_detail { font-size: 13px; } }
        #header_4>a:hover{cursor:pointer;}
        #header_4>a:hover + div{display:block;}
        #header_4>a~div:hover{cursor:pointer; display:block;}

        #header_5{text-align:center; line-height:60px; font-size:17px; padding-top:55px; position:relative;}
        @media (max-width: 470px) { #header_5 { font-size: 8px; } }
        #header_5_a{padding-bottom: 50px; transform : scale(1);}
        #header_5_detail{width:100%; line-height:35px; font-size:13px; background-color:white; color:black; position:absolute; display:none; border-radius:10%; border:black solid 1px;}
        @media (max-width: 470px) { #header_5_detail { font-size: 13px; } }
        #header_5>a:hover{cursor:pointer;}
        #header_5>a:hover + div{display:block;}
        #header_5>a~div:hover{cursor:pointer; display:block;}

        #header_6{text-align:center; line-height:60px; font-size:17px; padding-top:55px; position:relative;}
        @media (max-width: 470px) { #header_6 { font-size: 8px; } }
        #header_6_a{padding-bottom: 50px; transform : scale(1);}
        #header_6_detail{width:100%; line-height:50px; font-size:17px; background-color:white; color:black; position:absolute; display:none; border-radius:10%; border:black solid 1px;}
        @media (max-width: 470px) { #header_6_detail { font-size: 13px; } }
        #header_6>a:hover{cursor:pointer;}
        #header_6>a:hover + div{display:block;}
        #header_6>a~div:hover{cursor:pointer; display:block;}

        #header_7 {text-align:center; line-height:70px; font-size:15px; padding-right:2%;}
        @media (max-width: 940px) { #header_7 { font-size: 10px; } }
        #header_7>a:hover {cursor:pointer;}
        #header_2_detail a{text-decoration:none; color:black;}
        
    </style>

</head>
<body>	
		<c:if test="${ not empty alertMsg }">
		<script>
			alert('${alertMsg}');
		</script>
		<c:remove var="alertMsg" scope="session"/>
		</c:if>
			<c:if test="${ not empty errorMsg }">
		<script>
			alert('${errorMsg}');
		</script>
		<c:remove var="errorMsg" scope="session"/>
		</c:if>
		
	    <div id="header">
        <div id="header_1"></div>
        <div id="header_2"><a id="header_2_a">Trip</a>
            <div id="header_2_detail">
                <div id="header_2_detail_1">내 주변 조회</div>
                <div id="header_2_detail_2"><a href="transReservation">교통 예약하기</a></div>
                <div id="header_2_detail_3">숙소 예약하기</div>
                <div id="header_2_detail_4"><a href="placeList">여행코스 추천</a></div>
            </div>
        </div>
        <div id="header_3"><a id="header_3_a">Community</a>
            <div id="header_3_detail">
                <div id="header_3_detail_1" onclick="location.href='list.review'">리뷰</div>
                <div id="header_3_detail_2" onclick="location.href='list.free'">자유</div>
                <div id="header_3_detail_3" onclick="location.href='list.qna'">질문</div>
            </div>
        </div>
        <div id="header_4"  onclick="location.href='list.creator'"><a>Creator</a></div>
        <div id="header_5"  onclick="location.href='list.share'"><a>Share</a></div>
        <div id="header_6"  onclick="location.href='list.notice'"><a>Notice</a></div>
        <div id="header_7">
        <c:choose>
            <c:when test="${ empty loginMember }">
        		<a data-bs-toggle="modal" data-bs-target="#loginModal">Login</a>
        	</c:when>
        	<c:otherwise>
        		<c:choose>
        			<c:when test="${ loginMemo == 0 }">
        			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bell" viewBox="0 0 16 16">
  					<path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4.002 4.002 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4.002 4.002 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5.002 5.002 0 0 1 13 6c0 .88.32 4.2 1.22 6z"/>
					</svg>
					</c:when>
					<c:otherwise>
					<button type="button" class="btn btn-primary position-relative" style="background-color:transparent; border:none; padding:revert; margin-right:10px; margin-top:3px">
					  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bell-fill" viewBox="0 0 16 16" style="vertical-align:baseline;">
					  <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zm.995-14.901a1 1 0 1 0-1.99 0A5.002 5.002 0 0 0 3 6c0 1.098-.5 6-2 7h14c-1.5-1-2-5.902-2-7 0-2.42-1.72-4.44-4.005-4.901z" class="position-relative" style="padding-bottom:12px"/>
					  </svg>
					  <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size:10px; width:30px; height:18px;">
					   ${ loginMemo }
					   <span class="visually-hidden">unread messages</span>
					  </span>
					</button>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${ loginMember.memGrade eq 'ADMIN'}">
						<a href="logout.me">Logout</a> / <a href="adminPage">AdminPage</a>
        			</c:when>
        			<c:otherwise>
        				<a href="logout.me">Logout</a> / <a id="myPage" onclick="myPage();">MyPage</a>
        			</c:otherwise>
				</c:choose>
        		
        	</c:otherwise>        	
        </c:choose>
        
        <form action="" method="post" id="postForm">
        	<input type="hidden" name="memberId" value="${ loginMember.memId }"/>
        </form>
        
        <p id="token-result"></p>
        </div>
             
    </div>
    <jsp:include page="../member/modal/memberModal.jsp" />
	<jsp:include page="../member/modal/userFindmodal.jsp"/>
    <script>
  	function myPage(){
		$('#postForm').attr("action", 'myPage').submit();
  	};
    
        $('#header a').on({'mouseenter' : function(){
            var tag = $(this).text();
            var tagcompare = {'Trip' : '여행', 'Community' : '커뮤니티', 'Creator' : '크리에이터', 'Share' : '공유', 'Notice' : '공지'};

            for(var key in tagcompare){
                if(tag == key){
                    $(this).text(tagcompare[key]);
                }
            }
            }
        })

        $('#header a').on({'mouseout' : function(){
            var tag = $(this).text();
            var tagcompare = {'여행' : 'Trip', '커뮤니티' : 'Community' , '크리에이터' : 'Creator', '공유' : 'Share', '공지' : 'Notice'};

            for(var key in tagcompare){
                if(tag == key){
                    $(this).text(tagcompare[key]);
                }
            }
            }
        })

        $('#header a+div div').on({'mouseenter' : function(){
            var tag = $(this).parent().parent().children().eq(0).text();
            var tagcompare = {'Trip' : '여행', 'Community' : '커뮤니티', 'Creator' : '크리에이터', 'Share' : '공유', 'Notice' : '공지'};

            for(var key in tagcompare){
                if(tag == key){
                    $(this).parent().parent().children().eq(0).text(tagcompare[key]);
                }
            }
            }
        })

        $('#header a+div div').on({'mouseout' : function(){
            var tag = $(this).parent().parent().children().eq(0).text();
            var tagcompare = {'여행' : 'Trip', '커뮤니티' : 'Community' , '크리에이터' : 'Creator', '공유' : 'Share', '공지' : 'Notice'};

            for(var key in tagcompare){
                if(tag == key){
                    $(this).parent().parent().children().eq(0).text(tagcompare[key]);
                }
            }
            }
        })
    </script>
    
 
 <!--  
    <c:if test="${ not empty alertMsg }">
		<script>
			alert('${alertMsg}');
		</script>
		<c:remove var="alertMsg" scope="session" />
	</c:if>
   --> 
  
</body>
<meta name="viewport" content="width=device-width,initial-scale=1">
</html>

