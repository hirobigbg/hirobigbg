/**
 * 
 */

//어떤 컨테이너에 떨어졌는지
let where = null;

//각 카드 요소들
const draggables = document.querySelectorAll(".card");
console.log(draggables);

//컨테이너들
const containers = document.querySelectorAll(".containerBox");

const requested = document.querySelector(".requested");
const inprogress = document.querySelector(".inprogress");
const done = document.querySelector(".done");
const pending = document.querySelector(".pending");

containers.forEach((containerBox) => {
	new Sortable(containerBox, {
		group: "shared",
		animation: 150
	})
})

//태스크에 이벤트 리스너 달아주기
draggables.forEach(draggable => {
	//dragstart : 드래그를 시작할 때 발생하는 이벤트
	draggable.addEventListener("dragstart", () => {
		console.log('드래그 시작');
		//드래그가 시작되면 해당 요소에 dragging이라는 클래스명을 추가시킨다.
		draggable.classList.add("dragging");

	});

	//dragend : 드래그하다가 마우스를 놓는 순간 발생하는 이벤트
	draggable.addEventListener("dragend", () => {
		console.log('드래그 종료');
		//드래그가 끝나면 dragging 클래스 삭제
		draggable.classList.remove("dragging");
	});
});

//진행전 이벤트
requested.addEventListener("dragover", (e) => {
	e.preventDefault();
});

requested.addEventListener("drop", (e) => {
	e.preventDefault();

	let dragging = $(".dragging").find('input').val();
	console.log(dragging);

	if ($(".dragging").hasClass("border-left-dark")) {
		$(".dragging").removeClass("border-left-dark");
		$(".dragging").addClass("border-left-warning");
	} else if ($(".dragging").hasClass("border-left-success")) {
		$(".dragging").removeClass("border-left-success");
		$(".dragging").addClass("border-left-warning");
	} else if ($(".dragging").hasClass("border-left-info")) {
		$(".dragging").removeClass("border-left-info");
		$(".dragging").addClass("border-left-warning");
	}

	console.log("requested drop & ajax 호출");

	where = 0;

	$.ajax({
		url: "changeState",
		type: "POST",
		data: {
			task_state: where,
			task_seq: dragging
		},
		dataType: 'json',
		success: function() {
			alert('requested 변경 완료');
		}
	});
});

//진행중
inprogress.addEventListener("dragover", (e) => {
	e.preventDefault();
});

inprogress.addEventListener("drop", (e) => {
	e.preventDefault();

	let dragging = $(".dragging").find('input').val();
	console.log(dragging);

	if ($(".dragging").hasClass("border-left-dark")) {
		$(".dragging").removeClass("border-left-dark");
		$(".dragging").addClass("border-left-success");
	} else if ($(".dragging").hasClass("border-left-warning")) {
		$(".dragging").removeClass("border-left-warning");
		$(".dragging").addClass("border-left-success");
	} else if ($(".dragging").hasClass("border-left-info")) {
		$(".dragging").removeClass("border-left-info");
		$(".dragging").addClass("border-left-success");
	}

	console.log("inprogress drop & ajax 호출");

	where = 1;

	$.ajax({
		url: "changeState",
		type: "POST",
		data: {
			task_state: where,
			task_seq: dragging
		},
		dataType: 'json',
		success: function() {
			alert('requested 변경 완료');
		}
	});
});

//완료
done.addEventListener("dragover", (e) => {
	e.preventDefault();
});

done.addEventListener("drop", (e) => {
	e.preventDefault();

	let dragging = $(".dragging").find('input').val();
	console.log(dragging);

	if ($(".dragging").hasClass("border-left-dark")) {
		$(".dragging").removeClass("border-left-dark");
		$(".dragging").addClass("border-left-info");
	} else if ($(".dragging").hasClass("border-left-warning")) {
		$(".dragging").removeClass("border-left-warning");
		$(".dragging").addClass("border-left-info");
	} else if ($(".dragging").hasClass("border-left-dark")) {
		$(".dragging").removeClass("border-left-dark");
		$(".dragging").addClass("border-left-info");
	}

	console.log("done drop & ajax 호출");

	where = 2;

	$.ajax({
		url: "changeState",
		type: "POST",
		data: {
			task_state: where,
			task_seq: dragging
		},
		dataType: 'json',
		success: function() {
			alert('requested 변경 완료');
		}
	});
});

//보류중
pending.addEventListener("dragover", (e) => {
	e.preventDefault();
});

pending.addEventListener("drop", (e) => {
	e.preventDefault();

	let dragging = $(".dragging").find('input').val();
	console.log(dragging);

	if ($(".dragging").hasClass("border-left-warning")) {
		$(".dragging").removeClass("border-left-warning");
		$(".dragging").addClass("border-left-dark");
	} else if ($(".dragging").hasClass("border-left-success")) {
		$(".dragging").removeClass("border-left-success");
		$(".dragging").addClass("border-left-dark");
	} else if ($(".dragging").hasClass("border-left-info")) {
		$(".dragging").removeClass("border-left-info");
		$(".dragging").addClass("border-left-dark");
	}


	console.log("pending drop & ajax 호출");

	where = 3;

	$.ajax({
		url: "changeState",
		type: "POST",
		data: {
			task_state: where,
			task_seq: dragging
		},
		dataType: 'json',
		success: function() {
			alert('requested 변경 완료');
		}
	});
});

$(".profile").hover(function() {
	let memb_mail = $(this).find("#m_mail").val();
	let memb_name = $(this).find("#m_name").val();

	$(this).find(".profileBox").css('border', '3px solid #727272');

	$(this).append(
		'<span class="membInfo">' + memb_name + '  ' + memb_mail + '</span>'
	);
}, function() {
	$(this).find(".profileBox").css('border', 'none');
	$(".membInfo").remove();
});
	/*
//컨테이너에 이벤트 리스너 달아주기
containers.forEach(containers => {
	//드래그하면서 마우스가 대상 객체의 영역 위에 자리 잡고 있을 때 발생하는 이벤트
	containers.addEventListener("dragover", e => {
		//기본으로 정의된 이벤트(다른 요소 위에 얹을 수 없다...?)가 작동하지 못하게 막는다.
		e.preventDefault();
		
		const afterElement = getDragAfterElement(containers, e.clientY)
		console.log(afterElement);
		
		//드래그하는 요소가 해당 컨테이너 위에 있을 경우 자식으로 이어붙인다.
		const draggable = document.querySelector(".dragging");
		containers.appendChild(draggable);
		
	});
});
	
//드래그하는 요소가 다른 요소 사이에 들어가도록 구현, 즉 정확한 위치를 잡아주기 위한 함수
function getDragAfterElement(containers, y){
			
	//draggableElements 배열에 현재 컨테이너에서 드래그하고 있는 모든 요소를 담는다.
	//draggable:not(.dragging) -> 현재 드래그하고 있는 요소도 포함시키고자 함.
	//... : array로 리턴해주기 위함.
	
	const draggableElements = [...containers.querySelectorAll('.card:not(.dragging)')]
	
	console.log(draggableElements);
	
	//reduce의 역할 : 배치시킬 정확한 요소 하나를 찾아내고자 함!
	return draggableElements.reduce(
		//closest : 현재 드래그하고 있는 요소와 가장 가까운 요소 하나, 어떤 요소가 마우스 커서 바로 뒤에 있나
		(closest, child) => {
			//DOM 엘리먼트의 위치를 구해 box 변수에 담는다
			const box = child.getBoundingClientRect();
			console.log(box);
			
			const offset = y - box.top - box.height / 2;
			console.log(offset);
			
			if(offset < 0 && offset > closest.offset) {
				return {offset : offset, element : child}
			} else {
				return closest
			}
		},
		{offset : Number.NEGATIVE_INFINITY},
	).element
}
*/