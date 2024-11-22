DROP DATABASE IF EXISTS disclo;
CREATE DATABASE disclo;
USE disclo;

-- 사용자 테이블
CREATE TABLE User (
    user_id VARCHAR(36) PRIMARY KEY,                  -- UUID 사용
    nickname VARCHAR(255) NOT NULL,                -- 닉네임
    password_hash VARCHAR(255) NOT NULL,           -- 비밀번호 (해시 저장)
    email VARCHAR(255) NOT NULL,                    -- 이메일
    birth_date VARCHAR(7),                             -- 생년월일 (YYYY-MM-DD 형식, 뒷자리 첫 번째 자리까지)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 생성 날짜
    profile_color VARCHAR(20),                         -- 프로필 배경 색상 코드
    user_type ENUM('ADMIN', 'GUEST') NOT NULL      -- 유저 타입 추가 (ENUM)
);

-- 주식 정보 테이블
CREATE TABLE Stock (
    stock_id BIGINT AUTO_INCREMENT PRIMARY KEY,           -- 주식 정보 테이블
    market_cap BIGINT,                              -- 시가 총액(단위: 억)
    ticker VARCHAR(10) NOT NULL,                    -- 주식 티커
    market_type ENUM('KOSPI', 'KOSDAQ') NOT NULL,   -- 마켓 타입 추가 (ENUM)
    company_name VARCHAR(255) NOT NULL,             -- 회사 이름
    category VARCHAR(100),                          -- 카테고리
    company_overview TEXT                           -- 회사 개요
);

-- 일봉 데이터 테이블
CREATE TABLE StockPriceDay (
    stock_price_day_id BIGINT AUTO_INCREMENT PRIMARY KEY,           -- 일봉 데이터 테이블
    stock_id BIGINT NOT NULL,                       -- 주식과의 관계
    date DATE NOT NULL,                             -- 날짜
    open_price INT NOT NULL,                        -- 시가
    high_price INT NOT NULL,                        -- 고가
    low_price INT NOT NULL,                         -- 저가
    close_price INT NOT NULL,                       -- 종가
    volume INT,                                  -- 거래량
    change_rate FLOAT,                              -- 변동률
    FOREIGN KEY (stock_id) REFERENCES Stock(stock_id) ON DELETE CASCADE
);

-- 주봉 데이터 테이블
CREATE TABLE StockPriceWeek (
    stock_price_week_id BIGINT AUTO_INCREMENT PRIMARY KEY,           -- 주봉 데이터 테이블
    stock_id BIGINT NOT NULL,                       -- 주식과의 관계
    date DATE NOT NULL,                             -- 날짜
    open_price INT NOT NULL,                     -- 시가
    high_price INT NOT NULL,                     -- 고가
    low_price INT NOT NULL,                      -- 저가
    close_price INT NOT NULL,                    -- 종가
    volume INT,                                  -- 거래량
    change_rate FLOAT,                              -- 변동률
    FOREIGN KEY (stock_id) REFERENCES Stock(stock_id) ON DELETE CASCADE
);

-- 월봉 데이터 테이블
CREATE TABLE StockPriceMonth (
    stock_price_month_id BIGINT AUTO_INCREMENT PRIMARY KEY,           -- 월봉 데이터 테이블
    stock_id BIGINT NOT NULL,                       -- 주식과의 관계
    date DATE NOT NULL,                             -- 날짜
    open_price INT NOT NULL,                     -- 시가
    high_price INT NOT NULL,                     -- 고가
    low_price INT NOT NULL,                      -- 저가
    close_price INT NOT NULL,                    -- 종가
    volume INT,                                  -- 거래량
    change_rate FLOAT,                              -- 변동률
    FOREIGN KEY (stock_id) REFERENCES Stock(stock_id) ON DELETE CASCADE
);

-- 공시 테이블
CREATE TABLE Announcement (
    announcement_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 공시 테이블
    stock_id BIGINT NOT NULL,                          -- 주식과의 관계
    title VARCHAR(255) NOT NULL,                       -- 공시 제목
    content TEXT NOT NULL,                             -- 공시 내용
    announcement_date DATE NOT NULL,                   -- 공시 날짜
    submitter VARCHAR(255),                            -- 제출자
    original_announcement_url VARCHAR(255),            -- 원문 공시 URL
    announcement_type ENUM('정기공시', '주요사항보고', '외부감사관련', '발행공시', '지분공시', '자산유동화', '거래소공시', '기타공시', '공정위공시') NOT NULL, -- 공시 종류
    FOREIGN KEY (stock_id) REFERENCES Stock(stock_id) ON DELETE CASCADE
);

-- 댓글 테이블
CREATE TABLE Comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- 댓글 테이블
    announcement_id BIGINT NOT NULL,                   -- 공시와의 관계
    user_id VARCHAR(36),                                  -- 사용자 ID (UUID)
    content TEXT NOT NULL,                             -- 댓글 내용
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,     -- 생성 날짜
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 업데이트 시간
    FOREIGN KEY (announcement_id) REFERENCES Announcement(announcement_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- 피드백 테이블
CREATE TABLE Feedback (
    feedback_id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- 피드백 테이블
    announcement_id BIGINT NOT NULL,                   -- 공시와의 관계
    user_id VARCHAR(36),                                  -- 사용자 ID (UUID)
    type ENUM('POSITIVE', 'NEGATIVE') NOT NULL,        -- 피드백 유형
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,     -- 생성 날짜
    FOREIGN KEY (announcement_id) REFERENCES Announcement(announcement_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- 관심 종목 테이블
CREATE TABLE FavoriteStock (
    favorite_stock_id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- 관심 종목 테이블
    user_id VARCHAR(36),                                  -- 사용자 ID (UUID)
    stock_id BIGINT NOT NULL,                          -- 주식과의 관계
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (stock_id) REFERENCES Stock(stock_id) ON DELETE CASCADE
);

-- 관심 공시 테이블
CREATE TABLE FavoriteAnnouncement (
    favorite_announcement_id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- 관심 공시 테이블
    user_id VARCHAR(36),                                  -- 사용자 ID (UUID)
    announcement_id BIGINT NOT NULL,                   -- 공시와의 관계
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (announcement_id) REFERENCES Announcement(announcement_id) ON DELETE CASCADE
);