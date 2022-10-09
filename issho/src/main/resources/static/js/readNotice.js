$(document).ready(function() {
//	alert("call changetoBR!!!");
	changetoBR();	
	$('#toggleArea').hide();

});


function changetoBR(){
//		alert(text);	
		// 공지사항 내용 안의 CR을 <br>로 change
		let html = $("#notice_contents").html().replace(/(?:\r\n|\r|\n)/g, '<br>');
		// class를 이용하여 여러 개의 댓글들의 내용 안의 CR을 <br>로 change
		$("#notice_contents").html(html);
		$(".noticeCmt_contents").each(function(){
			let str = $(this).html().replace(/(?:\r\n|\r|\n)/g, '<br>')	;		
			$(this).html(str);		
		});
};

//전역변수로 수정 시 사용할 코멘트 번호임
//let changeCmt_seq;
function changeMode(noticeCmt_seq){
	$('#toggleArea').show();
	//변경할 댓글 번호 전역변수에 저장
	$('#noticeCmt_seq').val(noticeCmt_seq);
	
	let contentsId = 'contents_' + noticeCmt_seq;
//	alert('|'+ contentsId + '|');
	let strContents = $('#'+contentsId).html();
//	alert(strContents);
	
	strContents = strContents.replace(/(<br>|<br\/>|<br \/>)/g, '\r\n');		
//	alert(strContents);
//	changeCmt_seq = Cmt_seq;
//	alert(changeCmt_seq);

	//변경할 장소로 인동
	location.href = '#commentForm';
	$('#submitComment').val('수정');
	$('#noticeCmt_contents').html(strContents);
	//현재 내용 읽고 채워 넣기
	$('#noticeCmt_contents').focus();
	
	return false;
}

function showWriteForm(fShow){
	$('#CommentForm')[0].reset();
	$('#submitComment').val('댓글저장');

	if(fShow == 1){
//		alert('showWriteForm');
//		$('#CommentForm')[0].reset();
		$('#toggleArea').show();
	} else {
//		alert('hideWriteForm');
//		$('#CommentForm')[0].reset();
		$('#toggleArea').hide();
//		$('#submitComment').val('댓글저장');
	}
};
	
//공지사항 삭제
function deleteNotice(notice_seq) {
	if (confirm('삭제하시겠습니까? 만약 삭제하신다면 댓글도 삭제됩니다.')==false) {
		return false;
	}
	
	location.href = 'deleteNotice?notice_seq=' + notice_seq;
	return false;
}

function writeORupdate() {
	let mode = $('#submitComment').val();
//	alert(mode);
	if(mode =='수정')
		return updateComment();
	else 
		return writeComment();		
}

function writeComment() {

	let noticeCmt_contents = $('#noticeCmt_contents').val();
	let notice_seq = $('#notice_seq').val();
	
	if (noticeCmt_contents.length < 5 || noticeCmt_contents.length > 1000) {
		alert('댓글을 5자 이상 1,000자 이하로 내용을 입력하세요.');
		return false;
	}
	
	$.ajax({
		url: 'writeComment',
		type: 'post',
		data: { notice_seq: notice_seq, noticeCmt_contents: noticeCmt_contents },
		success: function(n) {
			if(n == 0) {
				alert('저장이 실패였습니다.');
				return false;			
			}else
				return true;
		},
		error: function() {
			alert('저장이 실패였습니다.');
			return false;
		}
	});
}

function updateComment() {
	let noticeCmt_seq = $('#noticeCmt_seq').val();
	let noticeCmt_contents = $('#noticeCmt_contents').val();
	let notice_seq = $('#notice_seq').val();
	
	if (noticeCmt_contents.length < 5 || noticeCmt_contents.length > 1000) {
		alert('댓글을 5자 이상 1,000자 이하로 내용을 입력하세요.');
		return false;
	}
	
	$.ajax({
		url: 'updateComment',
		type: 'post',
		data: { noticeCmt_seq: noticeCmt_seq, noticeCmt_contents: noticeCmt_contents },
		success: function(n) {
			if(n == 0) {
				alert('저장이 실패였습니다.');
				return false;			
			}else
				return true;
		},
		error: function() {
			alert('저장이 실패였습니다.');
			return false;
		}
	});
}


function deleteComment(noticeCmt_seq) {
	
	if (confirm('삭제하시겠습니까?')==false) {
		return false;
	}
	
	$.ajax({
		url: 'deleteComment',
		type: 'get',
		data: {noticeCmt_seq: noticeCmt_seq},
		success: function(n) {
			if(n == 0) {
				alert('삭제가 실패였습니다.');
				return false;			
			}else
				return true;
		},
		error: function(e) {
//			alert(e);
			return false;
		}
	});
}

function showPop() {

         console.log('showPop 실행');

         window.open("multiRoom/chatHome", "메신저",
               "width=500, height=700, top=10, left=10");
}
