// 메인 페이지로 돌아가기 함수
function goToMainPage() {
    window.location.href = 'OlymPick 메인페이지.html'; // 메인 페이지로 이동
}

document.addEventListener('DOMContentLoaded', function() {
    // 로그인 폼 처리
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
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
                    localStorage.setItem('userId', data.id);
                    localStorage.setItem('nickname', data.nickname);
                    alert('로그인 성공');
                    window.location.href = 'OlymPick 메인페이지.html';
                })
                .catch(error => {
                    console.error('로그인 에러:', error);
                    alert('아이디나 비밀번호가 틀렸거나 존재하지 않습니다');
                });
        });
    }

    // 회원가입 폼 처리
    const signupForm = document.getElementById('signup-form');
    if (signupForm) {
        signupForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const uid = document.getElementById('uid').value;
            const password = document.getElementById('password').value;
            const nickname = document.getElementById('nickname').value;
            const phoneNumber = document.getElementById('phoneNumber').value;
            const gender = document.getElementById('gender').value;
            const age = document.getElementById('age').value;
            const mbti = document.getElementById('mbti').value;
            const data = { uid, password, nickname, phoneNumber, gender, age, mbti };

            console.log('회원가입 데이터:', data);

            fetch('http://localhost:8080/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            })
                .then(response => {
                    console.log('응답 상태 코드:', response.status);
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('회원가입 실패');
                    }
                })
                .then(data => {
                    console.log('회원가입 성공 데이터:', data);
                    alert('회원가입이 완료되었습니다!');
                    window.location.href = 'OlymPick 로그인.html';
                })
                .catch(error => {
                    console.error('회원가입 에러:', error);
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

    // 장바구니 페이지 제목 업데이트
    const cartTitle = document.getElementById('cart-title');
    if (nickname && cartTitle) {
        cartTitle.textContent = `${nickname}님의 장바구니`;
    }

    // 상품 등록 폼 처리
    const productRegisterForm = document.getElementById('product-register-form');
    if (productRegisterForm) {
        productRegisterForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const userId = localStorage.getItem('userId');
            console.log('userId:', userId);

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
                userId: userId
            };

            console.log('productData:', productData);

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
                    productRegisterForm.reset();
                    window.location.href = 'OlymPick 메인페이지.html';
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('상품 등록 실패');
                });
        });
    }

    // 전체 상품 목록 로드
    loadAllProducts();

    // 카테고리 링크 클릭 이벤트 추가
    const categoryLinks = document.querySelectorAll('.main-nav ul li a');
    categoryLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            const category = this.textContent.trim().toUpperCase();
            if (category === 'ALL') {
                loadAllProducts();
            } else {
                loadProductsByCategory(category);
            }
        });
    });

    // 프로필 페이지 로드
    if (window.location.pathname.includes('OlymPick 프로필.html')) {
        loadProfile();
    }

    // 장바구니 페이지 로드
    if (window.location.pathname.includes('OlymPick 장바구니.html')) {
        loadBasketItems();
    }
});

// 로그아웃
function logout() {
    localStorage.removeItem('userId');
    localStorage.removeItem('nickname');
    alert('로그아웃되었습니다.');
    window.location.href = 'OlymPick 로그인.html';
}

// 경매 입찰
function placeBid() {
    const bidAmount = document.getElementById('bid-amount').value;
    alert(`입찰 금액: ₩${bidAmount}`);
}

// 장바구니로 이동
function goToCart() {
    window.location.href = 'OlymPick 장바구니.html';
}

// 전체 상품 목록 조회
function loadAllProducts() {
    const goodsListSection = document.querySelector('.goods-list-section h2');
    if (goodsListSection) {
        goodsListSection.textContent = '전체 상품 목록';
    }
    fetch('http://localhost:8080/products')
        .then(response => response.json())
        .then(data => {
            const goodsList = document.getElementById('goods-list');
            if (!goodsList) return;
            goodsList.innerHTML = '';
            if (data.length === 0) {
                goodsList.innerHTML = '<p>상품이 존재하지 않습니다</p>';
            } else {
                data.forEach(product => {
                    const goodsItem = document.createElement('div');
                    goodsItem.className = 'goods-item';
                    goodsItem.innerHTML = `
                        <img src="${product.mediaUrl}" alt="${product.name}">
                        <h3>${product.name}</h3>
                        <p>가격 : ₩${product.price}</p>
                        <div class="button-container">
                            <button onclick="viewProduct(${product.id})">상품 보기</button>
                        </div>
                    `;
                    goodsList.appendChild(goodsItem);
                });
            }
        })
        .catch(error => console.error('상품 목록 로드 에러:', error));
}

// 특정 카테고리의 상품 목록 조회
function loadProductsByCategory(category) {
    const goodsListSection = document.querySelector('.goods-list-section h2');
    if (goodsListSection) {
        goodsListSection.textContent = `${category} 상품 목록`;
    }
    fetch(`http://localhost:8080/products/category/${category}`)
        .then(response => response.json())
        .then(data => {
            const goodsList = document.getElementById('goods-list');
            if (!goodsList) return;
            goodsList.innerHTML = '';
            if (data.length === 0) {
                goodsList.innerHTML = '<p class="no-products">상품이 존재하지 않습니다</p>';
            } else {
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
            }
        })
        .catch(error => console.error('카테고리별 상품 목록 로드 에러:', error));
}

// 상품 보기 페이지로 이동
function viewProduct(productId) {
    window.location.href = `OlymPick 상품정보.html?productId=${productId}`;
}

// 상품 장바구니에 추가
function addToBasket() {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        alert('로그인이 필요합니다.');
        window.location.href = 'OlymPick 로그인.html';
        return;
    }

    const params = new URLSearchParams(window.location.search);
    const productId = params.get('productId');
    const quantity = parseInt(document.getElementById('quantity').value);

    fetch(`http://localhost:8080/products/basket/${userId}/${productId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ quantity: quantity })
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('장바구니 추가 실패');
            }
        })
        .then(data => {
            alert('상품이 장바구니에 담겼습니다.');
        })
        .catch(error => console.error('장바구니 추가 에러:', error));
}

// 장바구니 목록 조회
function loadBasketItems() {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        alert('로그인이 필요합니다.');
        window.location.href = 'OlymPick 로그인.html';
        return;
    }

    fetch(`http://localhost:8080/products/basket/${userId}`)
        .then(response => response.json())
        .then(data => {
            const pendingPurchasesList = document.getElementById('pending-purchases-list');
            const shippingPurchasesList = document.getElementById('shipping-purchases-list');
            const completedPurchasesList = document.getElementById('completed-purchases-list');

            pendingPurchasesList.innerHTML = ''; // 기존 장바구니 목록 초기화
            shippingPurchasesList.innerHTML = ''; // 기존 배송중인 목록 초기화
            completedPurchasesList.innerHTML = ''; // 기존 구매 완료 목록 초기화

            let hasPendingItems = false;
            let hasShippingItems = false;
            let hasCompletedItems = false;

            data.forEach(item => {
                const basketItem = document.createElement('li');
                basketItem.className = 'basket-item';
                basketItem.innerHTML = `
                    <img src="${item.product.mediaUrl}" alt="${item.product.name}">
                    <h3>${item.product.name}</h3>
                    <p>가격 : ₩${item.product.price}</p>
                    <p>수량 : ${item.count}</p>
                    <div class="button-container">
                        <button onclick="decreaseQuantity(${item.product.id})">하나만 빼기</button>
                        <button onclick="removeAllFromBasket(${item.product.id})">전체 수량 빼기</button>
                    </div>
                `;

                if (item.basketStatus === '배송준비중') {
                    pendingPurchasesList.appendChild(basketItem);
                    hasPendingItems = true;
                } else if (item.basketStatus === '배송중') {
                    shippingPurchasesList.appendChild(basketItem);
                    hasShippingItems = true;
                } else if (item.basketStatus === '구매완료') {
                    completedPurchasesList.appendChild(basketItem);
                    hasCompletedItems = true;
                }
            });

            if (!hasPendingItems) {
                pendingPurchasesList.innerHTML = '<p class="no-items">구매 대기중인 상품이 없습니다</p>';
            }
            if (!hasShippingItems) {
                shippingPurchasesList.innerHTML = '<p class="no-items">배송중인 상품이 없습니다</p>';
            }
            if (!hasCompletedItems) {
                completedPurchasesList.innerHTML = '<p class="no-items">구매 완료된 상품이 없습니다</p>';
            }
        })
        .catch(error => console.error('장바구니 목록 로드 에러:', error));
}

// 장바구니 상품 전체 제거
function removeAllFromBasket(productId) {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        alert('로그인이 필요합니다.');
        window.location.href = 'OlymPick 로그인.html';
        return;
    }

    const confirmation = confirm('해당 상품을 장바구니에서 모두 빼시겠습니까?');
    if (!confirmation) {
        return;
    }

    fetch(`http://localhost:8080/products/basket/${userId}/${productId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => {
            if (response.ok) {
                loadBasketItems();
            } else {
                throw new Error('장바구니 제거 실패');
            }
        })
        .catch(error => console.error('장바구니 제거 에러:', error));
}

// 장바구니에서 상품 제거
function decreaseQuantity(productId) {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        alert('로그인이 필요합니다.');
        window.location.href = 'OlymPick 로그인.html';
        return;
    }

    fetch(`http://localhost:8080/products/basket/${userId}/${productId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                loadBasketItems();
            } else {
                throw new Error('장바구니 제거 실패');
            }
        })
        .catch(error => console.error('장바구니 제거 에러:', error));
}

// 리뷰 조회
function loadReviews(productId) {
    fetch(`http://localhost:8080/reviews/product/${productId}`)
        .then(response => response.json())
        .then(data => {
            const reviewList = document.getElementById('reviews');
            reviewList.innerHTML = '';
            if (data.length === 0) {
                reviewList.innerHTML = '<p class="no-reviews">아직 리뷰가 없습니다</p>';
            } else {
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
            }
        })
        .catch(error => console.error('리뷰 로드 에러:', error));
}

// 리뷰 좋아요
function toggleLike(reviewId) {
    const userId = localStorage.getItem('userId');
    fetch(`http://localhost:8080/reviews/likes/${userId}/${reviewId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            const productId = new URLSearchParams(window.location.search).get('productId');
            loadReviews(productId);
        })
        .catch(error => console.error('좋아요 에러:', error));
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
                </div>
                <div class="quantity-action-container">
                    <div class="quantity-container">
                        <label for="quantity">수량 : </label>
                        <input type="number" id="quantity" name="quantity" min="1" value="1">
                    </div>
                    <div class="action-buttons">
                        <button class="cart-button" onclick="addToBasket(${productId})">장바구니에 담기</button>
                        <button class="purchase-button" onclick="purchaseProduct(${productId})">구매하기</button>
                    </div>
                </div>
            `;
                loadReviews(productId); // 상품 정보를 로드한 후 리뷰도 로드
            })
            .catch(error => console.error('상품 정보 로드 에러:', error));
    }
}

function goToReviewPage() {
    const params = new URLSearchParams(window.location.search);
    const productId = params.get('productId');
    if (!productId) {
        console.error('상품 ID를 URL에서 찾을 수 없습니다.');
        alert('상품 ID를 URL에서 찾을 수 없습니다.');
        return;
    }
    window.location.href = `OlymPick 리뷰작성.html?productId=${productId}`;
}

// 리뷰 작성
document.addEventListener("DOMContentLoaded", function() {
    const reviewForm = document.getElementById('review-form');
    if (reviewForm) {
        reviewForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const userId = localStorage.getItem('userId'); // 로그인된 사용자 ID 사용
            const params = new URLSearchParams(window.location.search);
            const productId = params.get('productId'); // URL에서 productId 추출

            if (!productId) {
                console.error('상품 ID를 URL에서 찾을 수 없습니다.');
                alert('상품 ID를 URL에서 찾을 수 없습니다.');
                return;
            }

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

            fetch(`http://localhost:8080/reviews`, {
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
        .catch(error => console.error('프로필 로드 에러:', error));
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

// 경매 목록 조회
function loadAuctions() {
    fetch('http://localhost:8080/auctions')
        .then(response => response.json())
        .then(data => {
            const auctionList = document.getElementById('auction-list');
            if (!auctionList) return;
            auctionList.innerHTML = '';
            if (data.length === 0) {
                auctionList.innerHTML = '<p class="no-auctions">현재 진행 중인 경매가 없습니다</p>';
            } else {
                data.forEach(auction => {
                    const auctionItem = document.createElement('div');
                    auctionItem.className = 'auction-item';
                    auctionItem.innerHTML = `
                        <h3>${auction.name}</h3>
                        <p>현재 입찰가: ₩${auction.currentBid}</p>
                        <input type="number" placeholder="입찰 금액 입력">
                        <button onclick="placeBid(${auction.id})">입찰하기</button>
                    `;
                    auctionList.appendChild(auctionItem);
                });
            }
        })
        .catch(error => console.error('경매 목록 로드 에러:', error));
}
