<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>발신함</title>

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:400,500,600">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<style>

	body{
		padding: 0px! important;
	}
	
	#body{
		width: 85%;
		height: 1000px;
		background-color:white;
		box-sizing: border-box;
		position:absolute;
		float:left;
		margin-left:15%;
		
	}
	
	#diaryFooter{
		margin-top:1000px;
	}
	
	#body>p{
		margin-top:15px;
		text-align:center;
		font-size:35px;
	}

	#memomain{
		width : 100%;
		height : 1000px;
		padding-top : 15px;
		float:left;
	}
	
	#memomain p{
		font-size:25px;
		margin-left:60px;
	}
	
	#memomain a{
		color:black;
		text-decoration:none;
	}
	
	#memokind{
		width : 100%;
		text-align : center;
		font-size  : 20px;
	}
	
	#memokind td{
		border-left :  1px silver solid; 
		border-right : 1px silver solid; 
	}
	
	#memolist{
		width : 95%;
		text-align : center;
		margin : auto;
	}
	
	#memolist th{
		background-color : lightgray;
		border : 1px solid silver;
	}
	
	#memoetc{
		width : 95%;
		margin : auto;
	}
	
	@media (max-width: 930px) { #deleteButton { font-size: 12px; }

</style>
</head>
<body>
	<div id="memoSide"><jsp:include page="../../common/myPageSide.jsp" /></div>
	<div id="memoSendback"><jsp:include page="../../common/sendBack.jsp" /></div>
	
	<div id="body">	
		    <div id="memomain">
		    <br>
		    <p>[${ loginMember.memNick }] 님의 쪽지</p>
		    <br><br><br>
		    
		    <hr>
		    <table id="memokind">
		    	<tr>
		    		<td><a href="receiveMemo">받은쪽지함</a></td>
		    		<td><a href="sendMemo">보낸쪽지함</a></td>
		    		<td><a href="newMemo">새 쪽지</a></td>
		    	</tr>
		    </table>
		    <hr>
		    <br><br><br><br><br>
		    
		    <table id="memolist" >
		    	<tr>
		    		<th class="memolisthead" width="5%">
		    		<input id="sendMemoCheckMain" type="checkbox">
		    		<input type="hidden" id="memoModalButton" data-bs-toggle="modal" data-bs-target="#memoModal">
		    		</th>
		    		<th class="memolisthead" width="10%">받은사람</th>
		    		<th class="memolisthead" width="35%">제목</th>
		    		<th class="memolisthead" width="25%">발송시간</th>
		    		<th class="memolisthead" width="25%">수신확인</th>
		    	</tr>
		    	<c:choose>
		    	<c:when test="${empty list}">
			    	<tr>
			    		<td class="memotd" colspan="5">쪽지가 없어요</td>
			    	</tr>
		    	</c:when>
		    	<c:otherwise>
			    	<c:forEach items="${list}" var="memos" varStatus="status">
			    	<tr id="sendMemoMain${status.index}">
			    		<td class="memotd">
			    		<input type="checkbox" class="sendMemoCheck" id="sendMemoCheck${status.index}" name="receiveMemoCheck${status.index}" onclick="memoCheck(${status.index})">
			    		<input type="hidden" class="sendMemoNo" id="sendMemoNo${status.index}" name="receiveMemoNo${status.index}" value="${memos.memoNo}">
			    		</td>
					    <td class="memotd sendMemoMain" onclick="memoModal(${memos.memoNo})">${memos.memoReceiver}</td>
					    <td class="memotd sendMemoMain" onclick="memoModal(${memos.memoNo})">${memos.memoTitle}</td>   	
					    <td class="memotd sendMemoMain" onclick="memoModal(${memos.memoNo})">${memos.memoSendDate}</td>   	
					    <td class="memotd sendMemoMain" onclick="memoModal(${memos.memoNo})" id="memoRead${memos.memoNo}" >
					    <c:choose>
					    	<c:when test="${memos.memoCheck == 'N'}">읽지않음</c:when>
					    	<c:otherwise>${memos.memoCheckDate}</c:otherwise>
					    </c:choose>
			    		</td>
			    	</tr>
			    	</c:forEach>
		    	</c:otherwise>
		    	</c:choose>
		    </table>
		    
		    <script>
		    $(function(){
	    		$('.memotd').css("height", "40px").css("border", "1px solid silver");
	    		
				$('.sendMemoMain').on({
	    		  mouseenter: function() {
	    		    $(this).parent().children().slice(1).css("background-color", "lightgreen").css("cursor", "pointer");
	    		  },
	    		  mouseleave: function() {
	    			$(this).parent().children().slice(1).css("background-color", "").css("cursor", "");
	    		  }
	    		});
	    	})
		    </script>
		    
		    <br><br>
		    
		    <table id="memoetc">
		    	<tr>
		    		<td colspan="3">
		    		<button class="btn btn-danger" onclick="deleteSendMemos()"  style="width:8%; height:30px; display:flex; align-items: center; justify-content: center;" id="deleteButton">삭제</button>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td colspan="3" width="100%">
		    			<div id="pagingArea" style="text-align: center;">
				                <ul class="pagination" style="display: flex; justify-content: center;">
				                
				                	<c:choose>
				                		<c:when test="${ pi.currentPage eq 1 }">
					                   		<li class="page-item disabled"><a class="page-link" href="#" disabled>&lt;</a></li>
				    					</c:when>
				    					<c:otherwise>
					                    	<li class="page-item"><a class="page-link" href="sendMemo?cPage=${ pi.currentPage - 1 }">&lt;</a></li>  					
				    					</c:otherwise>            	
				                	</c:choose>
				                    
				                    <c:forEach begin="${ pi.startPage }" end="${ pi.endPage }" var="p">
				                    	<li class="page-item"><a class="page-link" href="sendMemo?cPage=${p}">${p}</a><li>
				                    </c:forEach>
				                    
				                    
				                    <c:choose>
				                    	<c:when test="${ pi.currentPage eq pi.maxPage }">
				                    		<li class="page-item disabled"><a class="page-link" href="#" disabled>&gt;</a></li>
				                    	</c:when>
				                    	<c:otherwise>
				                    		<li class="page-item"><a class="page-link" href="sendMemo?cPage=${ pi.currentPage + 1 }">&gt;</a></li>
				                  		</c:otherwise>
				                    </c:choose>
				                    
				                </ul>
				            </div>
		    		</td>
		    	</tr>
		    </table>
	</div>
	</div>
		        
		    
	<div id="diaryFooter"><jsp:include page="../../common/footer.jsp" /></div>
	
	
	<script>
		$(function(){
			$('#sendMemoCheckMain').on("click", function(){
				for(var i = 0; i < $('.sendMemoCheck').length; i++){
					$('#sendMemoCheck' + i).prop("checked", true);
				}
			})
		})
		
		
		function memoCheck(a){
			var check = true;
			
			if($('#sendMemoCheck' + a).prop("checked") == false){
				$('#sendMemoCheckMain').prop("checked", false);		
			} else{
				for(var i = 0; i < $('.sendMemoCheck').length; i++){
					if($('#sendMemoCheck' + i).prop("checked") == false){
						check = false;
					};
				}
				if(check){
					$('#sendMemoCheckMain').prop("checked", true);	
				}
			}
		}
		
		function deleteSendMemoOne(){
			
			deleteSendMemo($('#memoDetailNo').val(), 1);
	  	}
		
		function rollbackSendMemoOne(){
			
			rollbackSendMemo($('#memoDetailNo').val());
		}
		
		function deleteSendMemos(){
						
			var checkedCount = 0;
			
			for(var i = 0; i < $('.sendMemoCheck').length; i++){
				if($('#sendMemoCheck' + i).prop("checked") == true){
					checkedCount += 1;
					if(checkedCount != $('.sendMemoCheck:checked').length){
						deleteSendMemo($('#sendMemoNo' + i).val(), 0);
					} else{
						deleteSendMemo($('#sendMemoNo' + i).val(), 1);
					}
				}
			}
		}
		
		function deleteSendMemo(MemoNo, triger){
			$.ajax({
				url : "deleteSendMemo",
				type : "get",
				data : {deleteMemoNo : MemoNo},
				success : function(result){
					if(triger == 1){
						location.href="http://localhost:8787/withpet/sendMemo";
					}
				},
				error : function(error){}
			})	
		}
		
		function rollbackSendMemo(MemoNo){
			$.ajax({
				url : "rollbackSendMemo",
				type : "get",
				data : {deleteMemoNo : MemoNo},
				success : function(result){
					location.href="http://localhost:8787/withpet/sendMemo";
				},
				error : function(error){}
			})	
		}
		
	
	
	</script>
	
	<div class="modal fade" id="memoModal" aria-hidden="true" aria-labelledby="exampleModalToggleLabel" data-bs-backdrop="static" tabindex="-1">
 	   <div class="modal-dialog modal-dialog-centered">
	      <div class="modal-content">
	      
	        <div class="modal-header" style="height:15px; background-color:lightgray">
	          <h5 class="modal-title"></h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	        
	        <div class="modal-body">
	        
	          <p class="modal-title" style="font-size:15px; text-align:center; margin-top:15px; margin-bottom:30px">쪽지 확인</p>
	          
	          <input type="hidden" id="memoDetailNo" name="memoDetailNo">
	          
	          <table style="width:70%; margin:auto; font-size:14px;">
	          	<tr>
	          		<td>받은사람</td>
	          		<td><input id="memoDetailReceiver" style="width:100%;" disabled></td>
	          	</tr>
	          	<tr>
	          		<td>보낸시간</td>
	          		<td><input id="memoDetailSendDate" style="width:100%;" disabled></td>
	          	</tr>
	          	<tr>
	          		<td>제목</td>
	          		<td><input id="memoDetailTitle" style="width:100%;" disabled></td>
	          	</tr>
	          	<tr>
	          		<td>내용</td>
	          		<td><textarea id="memoDetailContent" style="width:100%; height:90px;; resize:none;" disabled></textarea></td>
	          	</tr>
	          	<tr>
	          		<td>확인시간</td>
	          		<td><input id="memoDetailCheckDate" style="width:100%;" disabled></td>
	          	</tr>
	          
	          
	          </table>
	          
	        </div>
	        
	        <div class="modal-footer">
	        <div style="margin:auto">
	          <button type="button" class="btn btn-secondary" onclick="rollbackSendMemoOne()" id="rollbackButton">회수</button>
	          <button type="button" class="btn btn-danger" onclick="deleteSendMemoOne()">삭제</button>
	        </div>
	        </div>
	       
	      
	      </div>
	    </div>
	  </div>
	  
	  <script>
	  
	  function memoModal(memoNoSub){
  		$('#memoModalButton').click();
  		
  		$.ajax({
  			url : "selectMemoDetail",
  			type : "get",
				data : {
					memoNo : memoNoSub
				},
				success : function(result){
					
					$('#memoDetailNo').val(result.memoNo);	
					$('#memoDetailReceiver').val(result.memoReceiver);	
					$('#memoDetailSendDate').val(result.memoSendDate);	
					$('#memoDetailTitle').val(result.memoTitle);	
					$('#memoDetailContent').val(result.memoContent);	
					$('#memoDetailCheckDate').val(result.memoCheckDate);
					
					if(result.memoCheckDate == null){
						$('#rollbackButton').prop("disabled", false)
					} else{
						$('#rollbackButton').prop("disabled", true)
					}
				},
				error : function(result){}
  		})
	  }
	  
	  
	  </script>
	  
</body>
</html>