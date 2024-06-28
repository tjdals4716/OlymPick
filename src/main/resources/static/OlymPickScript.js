// 로그인
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault(); // 기본 폼 제출 동작을 막음
            const uid = document.getElementById('uid').value;
            const password = document.getElementById('password').value;

            const data = { uid, password };

            fetch('http://localhost:8080/users/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('로그인 실패');
                    }
                })
                .then(data => {
                    localStorage.setItem('userId', data.id); // 로그인된 사용자 ID 저장
                    localStorage.setItem('nickname', data.nickname); // 사용자 닉네임 저장
                    alert('로그인 성공');
                    window.location.href = 'OlymPick 메인페이지.html'; // 로그인 성공 시 메인 페이지로 이동
                })
                .catch(error => {
                    console.error('로그인 에러:', error);
                    alert('아이디나 비밀번호가 틀렸거나 존재하지 않습니다');
                });
        });
    }

    // 회원가입
    const signupForm = document.getElementById('signup-form');
    if (signupForm) {
        signupForm.addEventListener('submit', function(event) {
            event.preventDefault(); // 기본 폼 제출 동작을 막음
            const uid = document.getElementById('uid').value;
            const password = document.getElementById('password').value;
            const nickname = document.getElementById('nickname').value;
            const phoneNumber = document.getElementById('phoneNumber').value;
            const gender = document.getElementById('gender').value;
            const age = document.getElementById('age').value;
            const mbti = document.getElementById('mbti').value;

            const data = { uid, password, nickname, phoneNumber, gender, age, mbti };

            console.log('회원가입 데이터:', data); // 요청 전 데이터 확인

            fetch('http://localhost:8080/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            })
                .then(response => {
                    console.log('응답 상태 코드:', response.status); // 응답 상태 코드 확인
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('회원가입 실패');
                    }
                })
                .then(data => {
                    console.log('회원가입 성공 데이터:', data); // 성공 시 응답 데이터 확인
                    alert('회원가입이 완료되었습니다!');
                    window.location.href = 'OlymPick 로그인.html'; // 회원가입 성공 시 로그인 페이지로 이동
                })
                .catch(error => {
                    console.error('회원가입 에러:', error); // 에러 메시지 확인
                    alert('회원가입 실패');
                });
        });
    }

    // 사용자 이름 설정
    const usernameElement = document.querySelector('.username');
    const nickname = localStorage.getItem('nickname');
    if (usernameElement && nickname) {
        usernameElement.textContent = `안녕하세요, ${nickname}님!`;
    }

    // 상품 등록
    const productRegisterForm = document.getElementById('product-register-form');
    if (productRegisterForm) {
        productRegisterForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const userId = localStorage.getItem('userId'); // 로그인된 사용자 ID 사용
            console.log('userId:', userId); // userId 확인용 로그

            if (!userId) {
                alert('로그인이 필요합니다.');
                return;
            }

            const productData = {
                name: document.getElementById('product-name').value,
                content: document.getElementById('product-content').value,
                price: document.getElementById('product-price').value,
                quantity: document.getElementById('product-quantity').value,
                category: document.getElementById('product-category').value,
                userId: userId // 로그인된 사용자 ID를 사용
            };

            console.log('productData:', productData); // productData 확인용 로그

            const mediaFile = document.getElementById('product-media').files[0];
            const formData = new FormData();
            formData.append('productData', JSON.stringify(productData));
            formData.append('mediaData', mediaFile);

            fetch('http://localhost:8080/products', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('상품 등록 실패');
                    }
                })
                .then(data => {
                    alert('상품 등록 성공!');
                    // 폼 초기화
                    productRegisterForm.reset();
                    // 메인 페이지로 이동
                    window.location.href = 'OlymPick 메인페이지.html';
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('상품 등록 실패');
                });
        });
    }
});

// 로그아웃
function logout() {
    localStorage.removeItem('userId');
    localStorage.removeItem('nickname');
    alert('로그아웃되었습니다.');
    window.location.href = 'OlymPick 로그인.html'; // 로그아웃 후 로그인 페이지로 이동
}

// 경매 입찰
function placeBid() {
    const bidAmount = document.getElementById('bid-amount').value;
    alert(`입찰 금액: ₩${bidAmount}`);
}

// 장바구니로 이동
function goToCart() {
    window.location.href = 'OlymPick 장바구니.html'; // 장바구니 페이지로 이동
}

// 상품 목록 조회
function loadProducts() {
    fetch('http://localhost:8080/products')
        .then(response => response.json())
        .then(data => {
            const goodsList = document.getElementById('goods-list');
            data.forEach(product => {
                const goodsItem = document.createElement('div');
                goodsItem.className = 'goods-item';
                goodsItem.innerHTML = `
                    <img src="${product.mediaUrl}" alt="${product.name}">
                    <h3>${product.name}</h3>
                    <p>가격 : ₩${product.price}</p>
                    <button onclick="viewProduct(${product.id})">상품 보기</button>
                `;
                goodsList.appendChild(goodsItem);
            });
        })
        .catch(error => console.error('상품 목록 로드 에러:', error));
}

// 상품 보기 페이지로 이동
function viewProduct(productId) {
    window.location.href = `OlymPick 상품정보.html?productId=${productId}`;
}

// 상품 정보 조회
function loadProductDetails() {
    const params = new URLSearchParams(window.location.search);
    const productId = params.get('productId');
    if (productId) {
        fetch(`http://localhost:8080/products/${productId}`)
            .then(response => response.json())
            .then(data => {
                const productDetails = document.getElementById('product-details');
                productDetails.innerHTML = `
                <div class="product-info">
                    <img src="${data.mediaUrl}" alt="${data.name}">
                    <h3>${data.name}</h3>
                    <p>${data.content}</p>
                    <p>가격 : ₩${data.price}</p>
                    <p>재고 : ${data.quantity}개</p>
                    <button class="review-button" onclick="goToReviewPage(${productId})">리뷰 작성하기</button>
                </div>
            `;
                loadReviews(productId); // 상품 정보를 로드한 후 리뷰도 로드
            })
            .catch(error => console.error('상품 정보 로드 에러:', error));
    }
}

// 리뷰 조회
function loadReviews(productId) {
    fetch(`http://localhost:8080/reviews/product/${productId}`)
        .then(response => response.json())
        .then(data => {
            const reviewList = document.getElementById('reviews');
            reviewList.innerHTML = ''; // 기존 리뷰 목록 초기화
            data.forEach(review => {
                const reviewItem = document.createElement('div');
                reviewItem.className = 'review-item';
                reviewItem.innerHTML = `
                <div class="review-header">
                    <h4>${review.title}</h4>
                    <p><strong>작성자 :</strong> ${review.user.nickname}</p>
                </div>
                ${review.mediaUrl ? `<img src="${review.mediaUrl}" alt="리뷰 이미지">` : ''}
                <p>${review.content}</p>
                <div class="review-footer">
                    <p>좋아요 : ${review.likes}</p>
                    <p>작성시간 : ${new Date(review.statusDateTime).toLocaleString()}</p>
                    <button class="like-button" onclick="toggleLike(${review.id})">좋아요</button>
                </div>
            `;
                reviewList.appendChild(reviewItem);
            });
        })
        .catch(error => console.error('리뷰 로드 에러:', error));
}

// 리뷰 작성 페이지로 이동
function goToReviewPage(productId) {
    window.location.href = `OlymPick 리뷰작성.html?productId=${productId}`;
}

// 리뷰 좋아요
function toggleLike(reviewId) {
    const userId = localStorage.getItem('userId'); // 로그인된 사용자 ID 사용
    fetch(`http://localhost:8080/reviews/likes/${userId}/${reviewId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            const productId = new URLSearchParams(window.location.search).get('productId');
            loadReviews(productId); // 좋아요 업데이트 후 리뷰 목록 다시 로드
        })
        .catch(error => console.error('좋아요 에러:', error));
}

// 리뷰 작성
document.addEventListener("DOMContentLoaded", function() {
    const reviewForm = document.getElementById('review-form');
    if (reviewForm) {
        reviewForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const userId = localStorage.getItem('userId'); // 로그인된 사용자 ID 사용
            const productId = new URLSearchParams(window.location.search).get('productId'); // URL에서 productId 추출
            const reviewData = {
                title: document.getElementById('review-title').value,
                content: document.getElementById('review-content').value,
                userId: userId, // 로그인된 사용자 ID를 사용
                productId: productId // URL에서 추출한 productId 사용
            };

            const mediaFile = document.getElementById('review-media').files[0];
            const formData = new FormData();
            formData.append('reviewData', JSON.stringify(reviewData));
            formData.append('mediaFile', mediaFile);

            fetch('http://localhost:8080/reviews', {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    alert('리뷰 작성 성공!');
                    window.location.href = `OlymPick 상품정보.html?productId=${productId}`; // 리뷰 작성 후 상품 정보 페이지로 이동
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('리뷰 작성 실패');
                });
        });
    }
});

// 프로필 조회
function loadProfile() {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        alert('로그인이 필요합니다.');
        window.location.href = 'OlymPick 로그인.html';
        return;
    }

    fetch(`http://localhost:8080/users/${userId}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('profile-uid').textContent = data.uid;
            document.getElementById('profile-nickname').textContent = data.nickname;
            document.getElementById('profile-phoneNumber').textContent = data.phoneNumber;
            document.getElementById('profile-gender').textContent = data.gender;
            document.getElementById('profile-age').textContent = data.age;
            document.getElementById('profile-mbti').textContent = data.mbti;
        })
        .catch(error => console.error('프로필 로드 에러 :', error));
}

// 회원 탈퇴
function confirmDeleteAccount() {
    const confirmation = confirm('정말 탈퇴하시겠습니까? 입력한 정보가 모두 삭제됩니다.');
    if (confirmation) {
        deleteAccount();
    }
}

function deleteAccount() {
    const userId = localStorage.getItem('userId');

    fetch(`http://localhost:8080/users/${userId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert('회원 탈퇴가 완료되었습니다.');
                localStorage.removeItem('userId');
                localStorage.removeItem('nickname');
                window.location.href = 'OlymPick 로그인.html';
            } else {
                throw new Error('회원 탈퇴 실패');
            }
        })
        .catch(error => console.error('회원 탈퇴 에러:', error));
}


