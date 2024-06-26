document.addEventListener("DOMContentLoaded", function() {
	
	
    const sendDataButton = document.getElementById("like-btn");
    const notificationInsert = document.getElementById("notification-insert");
    const notificationDelete = document.getElementById("notification-delete");
    
	sendDataButton.addEventListener("click", handleClick);
	
	function handleClick(event) {
        const target = event.target;

        if (target.className === "far fa-heart") {
            insertLike();
        } else if (target.className === "fas fa-heart") {
            deleteLike();
        } else {
            console.error("알 수 없는 클래스입니다.");
        }
    }
    
    const storeIdElement = document.getElementById("storeStoreNo");
    const memberUsernameElement = document.getElementById("memberUsername");

    
    const storeStoreNo = storeIdElement.value;
    const memberUsername = memberUsernameElement.value;
    
    // storeIdElement와 memberUsernameElement가 존재하는지 확인
    if (!storeStoreNo || !memberUsername) {
        console.error("storeStoreId 또는 memberUsername 요소를 찾을 수 없습니다.");
        return;
    }
    
    console.log(storeStoreNo);
    console.log(memberUsername);

    const requestData = {
        storeStoreNo: storeStoreNo,
        memberUsername: memberUsername
    };
    
    isLike();
    
    function showNotificationInsert() {
        // 알림 문구를 보이도록 설정
        notificationInsert.classList.add("show");

        // 3초 후에 알림 문구를 서서히 사라지게 함
        setTimeout(function() {
            notificationInsert.style.transition = "opacity 1.5s linear"; // 희미해지게 하는 시간
            notificationInsert.style.opacity = 0;
        }, 3000);

        // 4.5초 후에 알림 문구를 완전히 숨김
        setTimeout(function() {
            notificationInsert.classList.remove("show");
            notificationInsert.style.opacity = 1; // opacity를 다시 초기값으로 설정
            notificationInsert.style.transition = "none"; // 초기화 후 transition 제거
        }, 4500);
    }
    
    function showNotificationDelete() {
        // 알림 문구를 보이도록 설정
        notificationDelete.classList.add("show");

        // 3초 후에 알림 문구를 서서히 사라지게 함
        setTimeout(function() {
            notificationDelete.style.transition = "opacity 1.5s linear"; // 희미해지게 하는 시간
            notificationDelete.style.opacity = 0;
        }, 3000);

        // 4.5초 후에 알림 문구를 완전히 숨김
        setTimeout(function() {
            notificationDelete.classList.remove("show");
            notificationDelete.style.opacity = 1; // opacity를 다시 초기값으로 설정
            notificationDelete.style.transition = "none"; // 초기화 후 transition 제거
        }, 4500);
    }

    function insertLike() {

        fetch('/store/like/insert', { // URL을 실제 API 엔드포인트로 변경
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response;
        })
        .then(data => {
            console.log('Success:', data);
            // 추가적인 성공 처리 로직
        	sendDataButton.className = "fas fa-heart";
			sendDataButton.style.color = "red";
			showNotificationInsert();
			
        })
        .catch(error => {
            console.error('Error:', error);
            // 추가적인 에러 처리 로직
        });
    }
    
    function deleteLike() {

        fetch('/store/like/delete', { // URL을 실제 API 엔드포인트로 변경
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response;
        })
        .then(data => {
            console.log('Success:', data);
            // 추가적인 성공 처리 로직
			sendDataButton.className = "far fa-heart";
			sendDataButton.style.color = "";
			showNotificationDelete();
			
        })
        .catch(error => {
            console.error('Error:', error);
            // 추가적인 에러 처리 로직
        });
    }
    
    function isLike() {
		console.log('isLike 함수 실행');
		console.log(requestData);
		
		// 쿼리스트링
		const queryString = new URLSearchParams(requestData).toString();
		const url = '/store/like/check?' + queryString;
		
		console.log(queryString);
		console.log(url);
		
        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            // 추가적인 성공 처리 로직
            if(data > 0){
				sendDataButton.className = "fas fa-heart";
				sendDataButton.style.color = "red";
			}
			
        })
        .catch(error => {
            console.error('Error:', error);
            // 추가적인 에러 처리 로직
        });
    }
});