-- ===================================
-- ✅ [DATA] categories
-- ===================================

INSERT INTO categories (id, name)
VALUES (100, '패션'),
       (101, '예술');

-- ===================================
-- ✅ [DATA] members
-- ===================================

INSERT INTO members (id, name, email, created_at, modified_at)
VALUES (1000, 'test', 'test@test.com', '2025-05-05T06:10:11', '2025-05-05T06:10:11'),
       (1001, 'test2', 'test2@test.com', '2025-05-05T06:10:11', '2025-05-05T06:10:11'),
       (1, 'test3', 'test3@test.com', '2025-05-05T06:10:11', '2025-05-05T06:10:11');

-- ===================================
-- ✅ [DATA] popup_locations
-- ===================================

INSERT INTO popup_locations (id, address_name, region_1depth_name, region_2depth_name, region_3depth_name, longitude,
                             latitude)
VALUES (10, '경기도 성남시 분당구', '경기', '성남시 분당구', '', 127.423084873712, 37.0789561558879),
       (11, '서울특별시 강남구 압구정로', '서울', '강남구', '압구정동', 127.044520, 37.543401),
       (12, '서울특별시 강남구 논현로', '서울', '강남구', '논현동', 127.045630, 37.542890),
       (13, '서울특별시 강남구 강남대로', '서울', '강남구', '역삼동', 127.043890, 37.544200),
       (14, '서울특별시 강남구 선릉로', '서울', '강남구', '선릉동', 127.046710, 37.541900),
       (15, '서울특별시 강남구 테헤란로', '서울', '강남구', '삼성동', 127.045200, 37.542500),
       (16, '서울특별시 강남구 봉은사로', '서울', '강남구', '삼성동', 127.046800, 37.543100),
       (17, '서울특별시 강남구 압구정로32길', '서울', '강남구', '압구정동', 127.042890, 37.544450),
       (18, '서울특별시 강남구 언주로', '서울', '강남구', '압구정동', 127.043200, 37.542800),
       (19, '서울특별시 강남구 도산대로', '서울', '강남구', '신사동', 127.044100, 37.543800),
       (20, '서울특별시 강남구 학동로', '서울', '강남구', '논현동', 127.045900, 37.542100),
       (21, '서울특별시 강남구 압구정로42길', '서울', '강남구', '압구정동', 127.043600, 37.544000),
       (22, '서울특별시 강남구 선릉로158길', '서울', '강남구', '논현동', 127.046200, 37.542600),
       (23, '서울특별시 강남구 압구정로28길', '서울', '강남구', '압구정동', 127.042100, 37.544600),
       (24, '서울특별시 강남구 언주로152길', '서울', '강남구', '신사동', 127.043800, 37.543500),
       (25, '서울특별시 강남구 논현로32길', '서울', '강남구', '논현동', 127.045100, 37.542300),
       (26, '서울특별시 강남구 압구정로30길', '서울', '강남구', '압구정동', 127.042500, 37.544300),
       (27, '서울특별시 강남구 선릉로156길', '서울', '강남구', '논현동', 127.047200, 37.541700),
       (28, '서울특별시 강남구 압구정로38길', '서울', '강남구', '압구정동', 127.043400, 37.543900),
       (29, '서울특별시 강남구 도산대로45길', '서울', '강남구', '신사동', 127.044800, 37.543200),
       (30, '서울특별시 강남구 언주로148길', '서울', '강남구', '신사동', 127.044300, 37.542700),
       (31, '서울특별시 강남구 압구정로26길', '서울', '강남구', '압구정동', 127.041800, 37.544800),
       (32, '서울특별시 강남구 논현로36길', '서울', '강남구', '논현동', 127.045400, 37.542000),
       (33, '서울특별시 강남구 선릉로154길', '서울', '강남구', '논현동', 127.046900, 37.541800),
       (34, '서울특별시 강남구 압구정로40길', '서울', '강남구', '압구정동', 127.043100, 37.544100),
       (35, '서울특별시 강남구 도산대로47길', '서울', '강남구', '신사동', 127.044500, 37.543600),
       (36, '서울특별시 강남구 언주로146길', '서울', '강남구', '신사동', 127.043900, 37.543000),
       (37, '서울특별시 강남구 압구정로34길', '서울', '강남구', '압구정동', 127.042800, 37.544200),
       (38, '서울특별시 강남구 논현로34길', '서울', '강남구', '논현동', 127.045700, 37.541900),
       (39, '서울특별시 강남구 선릉로152길', '서울', '강남구', '논현동', 127.047000, 37.541600);

-- ===================================
-- ✅ [DATA] popups
-- ===================================

INSERT INTO popups (id, title, popup_location_id, type, start_date, end_date)
VALUES (1, '무신사 X 나이키 팝업스토어', 10, 'EXPERIENTIAL', '2025-06-01', '2025-06-25'),
       (2, '아디다스 오리지널스 스토어', 11, 'RETAIL', '2025-06-15', '2025-07-15'),
       (3, '코카콜라 여름 페스티벌', 12, 'EXPERIENTIAL', '2025-07-01', '2025-07-31'),
       (4, '삼성 갤럭시 체험존', 13, 'EXPERIENTIAL', '2025-06-10', '2025-06-30'),
       (5, '스타벅스 리저브', 14, 'RETAIL', '2025-06-20', '2025-08-20'),
       (6, '나이키 에어맥스 컬렉션', 15, 'RETAIL', '2025-07-05', '2025-08-05'),
       (7, '디즈니 캐릭터 굿즈샵', 16, 'RETAIL', '2025-06-25', '2025-07-25'),
       (8, '현대자동차 아이오닉 체험관', 17, 'EXPERIENTIAL', '2025-07-10', '2025-08-10'),
       (9, '포켓몬 센터 서울', 18, 'RETAIL', '2025-06-30', '2025-08-30'),
       (10, '애플 스토어 압구정', 19, 'RETAIL', '2025-07-15', '2025-09-15'),
       (11, '유니클로 컬래버레이션', 20, 'RETAIL', '2025-06-05', '2025-07-05'),
       (12, 'BTS 팝업스토어', 21, 'EXPERIENTIAL', '2025-08-01', '2025-08-31'),
       (13, '넷플릭스 스트레인저 띵스', 22, 'EXPERIENTIAL', '2025-07-20', '2025-08-20'),
       (14, '카카오프렌즈 스토어', 23, 'RETAIL', '2025-06-12', '2025-07-12'),
       (15, 'LG 올레드 갤러리', 24, 'EXPERIENTIAL', '2025-07-25', '2025-08-25'),
       (16, '라인프렌즈 카페', 25, 'RETAIL', '2025-06-18', '2025-07-18'),
       (17, '마블 슈퍼히어로 체험관', 26, 'EXPERIENTIAL', '2025-08-05', '2025-09-05'),
       (18, '구찌 팝업 부티크', 27, 'RETAIL', '2025-07-30', '2025-08-30'),
       (19, '롯데월드 테마파크 미니어처', 28, 'EXPERIENTIAL', '2025-06-08', '2025-07-08'),
       (20, '삼성 스마트홈 체험존', 29, 'EXPERIENTIAL', '2025-08-10', '2025-09-10'),
       (21, '하이네켄 펍', 30, 'EXPERIENTIAL', '2025-07-12', '2025-08-12'),
       (22, 'MLB 공식 스토어', 31, 'RETAIL', '2025-06-22', '2025-07-22'),
       (23, '던킨도너츠 한정 메뉴', 32, 'RETAIL', '2025-08-15', '2025-09-15'),
       (24, 'SK텔레콤 5G 체험관', 33, 'EXPERIENTIAL', '2025-06-28', '2025-07-28'),
       (25, '르세라핌 굿즈샵', 34, 'RETAIL', '2025-08-20', '2025-09-20'),
       (26, '맥도날드 해피밀 토이', 35, 'RETAIL', '2025-07-03', '2025-08-03'),
       (27, '아모레퍼시픽 뷰티 라운지', 36, 'EXPERIENTIAL', '2025-08-25', '2025-09-25'),
       (28, '현대백화점 럭셔리 팝업', 37, 'RETAIL', '2025-06-15', '2025-07-15'),
       (29, '페라리 전시관', 38, 'EXPERIENTIAL', '2025-09-01', '2025-09-30'),
       (30, 'CJ올리브영 뷰티 스테이션', 39, 'RETAIL', '2025-07-08', '2025-08-08');

-- ===================================
-- ✅ [DATA] popup_categories
-- ===================================

INSERT INTO popup_categories (id, popup_id, category_id, name)
VALUES (1, 1, 100, '패션'),
       (2, 1, 101, '예술'),
       (3, 2, 100, '패션'),
       (4, 3, 101, '예술'),
       (5, 4, 101, '예술'),
       (6, 5, 100, '패션'),
       (7, 6, 100, '패션'),
       (8, 7, 101, '예술'),
       (9, 8, 101, '예술'),
       (10, 9, 101, '예술'),
       (11, 10, 101, '예술'),
       (12, 11, 100, '패션'),
       (13, 12, 101, '예술'),
       (14, 13, 101, '예술'),
       (15, 14, 101, '예술'),
       (16, 15, 101, '예술'),
       (17, 16, 100, '패션'),
       (18, 17, 101, '예술'),
       (19, 18, 100, '패션'),
       (20, 19, 101, '예술'),
       (21, 20, 101, '예술'),
       (22, 21, 101, '예술'),
       (23, 22, 100, '패션'),
       (24, 23, 100, '패션'),
       (25, 24, 101, '예술'),
       (26, 25, 101, '예술'),
       (27, 26, 100, '패션'),
       (28, 27, 100, '패션'),
       (29, 28, 100, '패션'),
       (30, 29, 101, '예술'),
       (31, 30, 100, '패션');

-- ===================================
-- ✅ [DATA] popup_images (MAIN)
-- ===================================

INSERT INTO popup_images (id, popup_id, type, url, sort_order)
VALUES (1, 1, 'MAIN', 'https://example.com/images/popup1.jpg', 1),
       (2, 1, 'MAIN', 'https://example.com/images/popup2.jpg', 2),
       (3, 1, 'MAIN', 'https://example.com/images/popup3.jpg', 3),
       (7, 2, 'MAIN', 'https://example.com/images/adidas1.jpg', 1),
       (8, 2, 'MAIN', 'https://example.com/images/adidas2.jpg', 2),
       (9, 3, 'MAIN', 'https://example.com/images/cocacola1.jpg', 1),
       (10, 4, 'MAIN', 'https://example.com/images/samsung1.jpg', 1),
       (11, 5, 'MAIN', 'https://example.com/images/starbucks1.jpg', 1),
       (12, 6, 'MAIN', 'https://example.com/images/nike1.jpg', 1),
       (13, 7, 'MAIN', 'https://example.com/images/disney1.jpg', 1),
       (14, 8, 'MAIN', 'https://example.com/images/hyundai1.jpg', 1),
       (15, 9, 'MAIN', 'https://example.com/images/pokemon1.jpg', 1),
       (16, 10, 'MAIN', 'https://example.com/images/apple1.jpg', 1),
       (17, 11, 'MAIN', 'https://example.com/images/uniqlo1.jpg', 1),
       (18, 12, 'MAIN', 'https://example.com/images/bts1.jpg', 1),
       (19, 13, 'MAIN', 'https://example.com/images/netflix1.jpg', 1),
       (20, 14, 'MAIN', 'https://example.com/images/kakao1.jpg', 1),
       (21, 15, 'MAIN', 'https://example.com/images/lg1.jpg', 1),
       (22, 16, 'MAIN', 'https://example.com/images/line1.jpg', 1),
       (23, 17, 'MAIN', 'https://example.com/images/marvel1.jpg', 1),
       (24, 18, 'MAIN', 'https://example.com/images/gucci1.jpg', 1),
       (25, 19, 'MAIN', 'https://example.com/images/lotte1.jpg', 1),
       (26, 20, 'MAIN', 'https://example.com/images/samsung2.jpg', 1),
       (27, 21, 'MAIN', 'https://example.com/images/heineken1.jpg', 1),
       (28, 22, 'MAIN', 'https://example.com/images/mlb1.jpg', 1),
       (29, 23, 'MAIN', 'https://example.com/images/dunkin1.jpg', 1),
       (30, 24, 'MAIN', 'https://example.com/images/skt1.jpg', 1),
       (31, 25, 'MAIN', 'https://example.com/images/lesserafim1.jpg', 1),
       (32, 26, 'MAIN', 'https://example.com/images/mcdonald1.jpg', 1),
       (33, 27, 'MAIN', 'https://example.com/images/amorepacific1.jpg', 1),
       (34, 28, 'MAIN', 'https://example.com/images/hyundai_dept1.jpg', 1),
       (35, 29, 'MAIN', 'https://example.com/images/ferrari1.jpg', 1),
       (36, 30, 'MAIN', 'https://example.com/images/oliveyoung1.jpg', 1);

-- ===================================
-- ✅ [DATA] popup_images (DESCRIPTION)
-- ===================================

INSERT INTO popup_images (id, popup_id, type, url, sort_order)
VALUES (4, 1, 'DESCRIPTION', 'https://brand-story-image.com/1.jpg', 1),
       (5, 1, 'DESCRIPTION', 'https://brand-story-image.com/2.jpg', 2),
       (6, 1, 'DESCRIPTION', 'https://brand-story-image.com/3.jpg', 3),
       (37, 2, 'DESCRIPTION', 'https://brand-story-image.com/adidas1.jpg', 1),
       (38, 3, 'DESCRIPTION', 'https://brand-story-image.com/cocacola1.jpg', 1),
       (39, 4, 'DESCRIPTION', 'https://brand-story-image.com/samsung1.jpg', 1),
       (40, 5, 'DESCRIPTION', 'https://brand-story-image.com/starbucks1.jpg', 1),
       (41, 6, 'DESCRIPTION', 'https://brand-story-image.com/nike1.jpg', 1),
       (42, 7, 'DESCRIPTION', 'https://brand-story-image.com/disney1.jpg', 1),
       (43, 8, 'DESCRIPTION', 'https://brand-story-image.com/hyundai1.jpg', 1),
       (44, 9, 'DESCRIPTION', 'https://brand-story-image.com/pokemon1.jpg', 1),
       (45, 10, 'DESCRIPTION', 'https://brand-story-image.com/apple1.jpg', 1),
       (46, 11, 'DESCRIPTION', 'https://brand-story-image.com/uniqlo1.jpg', 1),
       (47, 12, 'DESCRIPTION', 'https://brand-story-image.com/bts1.jpg', 1),
       (48, 13, 'DESCRIPTION', 'https://brand-story-image.com/netflix1.jpg', 1),
       (49, 14, 'DESCRIPTION', 'https://brand-story-image.com/kakao1.jpg', 1),
       (50, 15, 'DESCRIPTION', 'https://brand-story-image.com/lg1.jpg', 1),
       (51, 16, 'DESCRIPTION', 'https://brand-story-image.com/line1.jpg', 1),
       (52, 17, 'DESCRIPTION', 'https://brand-story-image.com/marvel1.jpg', 1),
       (53, 18, 'DESCRIPTION', 'https://brand-story-image.com/gucci1.jpg', 1),
       (54, 19, 'DESCRIPTION', 'https://brand-story-image.com/lotte1.jpg', 1),
       (55, 20, 'DESCRIPTION', 'https://brand-story-image.com/samsung2.jpg', 1),
       (56, 21, 'DESCRIPTION', 'https://brand-story-image.com/heineken1.jpg', 1),
       (57, 22, 'DESCRIPTION', 'https://brand-story-image.com/mlb1.jpg', 1),
       (58, 23, 'DESCRIPTION', 'https://brand-story-image.com/dunkin1.jpg', 1),
       (59, 24, 'DESCRIPTION', 'https://brand-story-image.com/skt1.jpg', 1),
       (60, 25, 'DESCRIPTION', 'https://brand-story-image.com/lesserafim1.jpg', 1),
       (61, 26, 'DESCRIPTION', 'https://brand-story-image.com/mcdonald1.jpg', 1),
       (62, 27, 'DESCRIPTION', 'https://brand-story-image.com/amorepacific1.jpg', 1),
       (63, 28, 'DESCRIPTION', 'https://brand-story-image.com/hyundai_dept1.jpg', 1),
       (64, 29, 'DESCRIPTION', 'https://brand-story-image.com/ferrari1.jpg', 1),
       (65, 30, 'DESCRIPTION', 'https://brand-story-image.com/oliveyoung1.jpg', 1);

-- ===================================
-- ✅ [DATA] popup_weekly_schedules
-- ===================================

INSERT INTO popup_weekly_schedules (id, popup_id, day_of_week, open_time, close_time)
VALUES (1, 1, 'MONDAY', '11:00:00', '23:00:00'),
       (2, 1, 'TUESDAY', '11:00:00', '23:00:00'),
       (3, 2, 'MONDAY', '10:00:00', '22:00:00'),
       (4, 2, 'TUESDAY', '10:00:00', '22:00:00'),
       (5, 3, 'WEDNESDAY', '12:00:00', '20:00:00'),
       (6, 3, 'THURSDAY', '12:00:00', '20:00:00'),
       (7, 4, 'MONDAY', '09:00:00', '18:00:00'),
       (8, 4, 'FRIDAY', '09:00:00', '18:00:00'),
       (9, 5, 'MONDAY', '08:00:00', '22:00:00'),
       (10, 5, 'SUNDAY', '08:00:00', '22:00:00'),
       (11, 6, 'TUESDAY', '10:30:00', '21:30:00'),
       (12, 6, 'SATURDAY', '10:30:00', '21:30:00'),
       (13, 7, 'MONDAY', '10:00:00', '20:00:00'),
       (14, 7, 'WEDNESDAY', '10:00:00', '20:00:00'),
       (15, 8, 'THURSDAY', '09:30:00', '18:30:00'),
       (16, 8, 'FRIDAY', '09:30:00', '18:30:00'),
       (17, 9, 'MONDAY', '11:30:00', '20:30:00'),
       (18, 9, 'SUNDAY', '11:30:00', '20:30:00'),
       (19, 10, 'TUESDAY', '10:00:00', '21:00:00'),
       (20, 10, 'SATURDAY', '10:00:00', '21:00:00'),
       (21, 11, 'MONDAY', '10:00:00', '22:00:00'),
       (22, 11, 'FRIDAY', '10:00:00', '22:00:00'),
       (23, 12, 'WEDNESDAY', '12:00:00', '21:00:00'),
       (24, 12, 'THURSDAY', '12:00:00', '21:00:00'),
       (25, 13, 'MONDAY', '11:00:00', '19:00:00'),
       (26, 13, 'TUESDAY', '11:00:00', '19:00:00'),
       (27, 14, 'FRIDAY', '10:00:00', '20:00:00'),
       (28, 14, 'SATURDAY', '10:00:00', '20:00:00'),
       (29, 15, 'MONDAY', '09:00:00', '18:00:00'),
       (30, 15, 'WEDNESDAY', '09:00:00', '18:00:00'),
       (31, 16, 'TUESDAY', '08:30:00', '21:30:00'),
       (32, 16, 'SUNDAY', '08:30:00', '21:30:00'),
       (33, 17, 'THURSDAY', '10:00:00', '19:00:00'),
       (34, 17, 'FRIDAY', '10:00:00', '19:00:00'),
       (35, 18, 'MONDAY', '11:00:00', '20:00:00'),
       (36, 18, 'SATURDAY', '11:00:00', '20:00:00'),
       (37, 19, 'WEDNESDAY', '09:30:00', '17:30:00'),
       (38, 19, 'SUNDAY', '09:30:00', '17:30:00'),
       (39, 20, 'TUESDAY', '10:00:00', '18:00:00'),
       (40, 20, 'THURSDAY', '10:00:00', '18:00:00'),
       (41, 21, 'FRIDAY', '16:00:00', '22:00:00'),
       (42, 21, 'SATURDAY', '16:00:00', '23:00:00'),
       (43, 22, 'MONDAY', '10:00:00', '21:00:00'),
       (44, 22, 'WEDNESDAY', '10:00:00', '21:00:00'),
       (45, 23, 'TUESDAY', '08:00:00', '22:00:00'),
       (46, 23, 'THURSDAY', '08:00:00', '22:00:00'),
       (47, 24, 'MONDAY', '09:00:00', '17:00:00'),
       (48, 24, 'FRIDAY', '09:00:00', '17:00:00'),
       (49, 25, 'WEDNESDAY', '11:00:00', '20:00:00'),
       (50, 25, 'SATURDAY', '11:00:00', '20:00:00'),
       (51, 26, 'TUESDAY', '08:30:00', '21:30:00'),
       (52, 26, 'SUNDAY', '08:30:00', '21:30:00'),
       (53, 27, 'THURSDAY', '10:00:00', '19:00:00'),
       (54, 27, 'FRIDAY', '10:00:00', '19:00:00'),
       (55, 28, 'MONDAY', '10:30:00', '20:30:00'),
       (56, 28, 'SATURDAY', '10:30:00', '20:30:00'),
       (57, 29, 'TUESDAY', '09:00:00', '18:00:00'),
       (58, 29, 'WEDNESDAY', '09:00:00', '18:00:00'),
       (59, 30, 'MONDAY', '08:00:00', '22:00:00'),
       (60, 30, 'SUNDAY', '08:00:00', '22:00:00');

-- ===================================
-- ✅ [DATA] popup_contents
-- ===================================

INSERT INTO popup_contents (id, popup_id, content_text, sort_order)
VALUES (1, 1, '무신사와 나이키의 특별한 협업으로 탄생한 한정판 컬렉션을 만나보세요.', 1),
       (2, 1, '최신 나이키 스니커즈와 무신사 스트릿 패션의 완벽한 조화를 경험하실 수 있습니다.', 2),
       (3, 2, '아디다스 오리지널스의 클래식한 디자인과 혁신적인 기술이 만나는 공간입니다.', 1),
       (4, 3, '코카콜라와 함께하는 시원한 여름 페스티벌! 다양한 체험 활동과 이벤트가 준비되어 있습니다.', 1),
       (5, 4, '최신 갤럭시 스마트폰과 웨어러블 기기를 직접 체험해보세요.', 1),
       (6, 5, '스타벅스 리저브 원두로 만든 프리미엄 커피와 한정 메뉴를 즐기실 수 있습니다.', 1),
       (7, 6, '나이키 에어맥스 시리즈의 역사와 최신 컬렉션을 한 번에 만나보세요.', 1),
       (8, 7, '디즈니 캐릭터들의 귀여운 굿즈와 한정판 아이템들이 가득합니다.', 1),
       (9, 8, '친환경 전기차 아이오닉의 첨단 기술과 미래 모빌리티를 체험하세요.', 1),
       (10, 9, '포켓몬 공식 굿즈와 한국 한정 상품들을 만나볼 수 있는 특별한 공간입니다.', 1),
       (11, 10, '애플의 최신 제품들을 직접 체험하고 전문가 상담을 받으실 수 있습니다.', 1),
       (12, 11, '유명 디자이너와의 특별 컬래버레이션 제품을 만나보세요.', 1),
       (13, 12, 'BTS 공식 굿즈와 한정판 아이템, 특별한 포토존이 마련되어 있습니다.', 1),
       (14, 13, '넷플릭스 인기 시리즈 스트레인저 띵스의 세계관을 체험할 수 있습니다.', 1),
       (15, 14, '카카오프렌즈 캐릭터들의 다양한 굿즈와 시즌 한정 상품들이 있습니다.', 1),
       (16, 15, 'LG 올레드 TV의 놀라운 화질과 혁신적인 디스플레이 기술을 체험하세요.', 1),
       (17, 16, '라인프렌즈와 함께하는 특별한 카페 공간에서 귀여운 캐릭터 메뉴를 즐기세요.', 1),
       (18, 17, '마블 슈퍼히어로들의 세계에 직접 들어가 특별한 체험을 해보세요.', 1),
       (19, 18, '구찌의 럭셔리한 패션 아이템과 한정판 컬렉션을 만나보실 수 있습니다.', 1),
       (20, 19, '롯데월드의 인기 어트랙션을 미니어처로 재현한 특별한 전시 공간입니다.', 1),
       (21, 20, '삼성 스마트홈 솔루션으로 미래의 집을 미리 체험해보세요.', 1),
       (22, 21, '하이네켄 특별 레시피 맥주와 펍 분위기를 즐길 수 있는 공간입니다.', 1),
       (23, 22, 'MLB 공식 굿즈와 야구 관련 아이템들을 만나보실 수 있습니다.', 1),
       (24, 23, '던킨도너츠의 한정 메뉴와 시즌 특별 도넛을 맛보세요.', 1),
       (25, 24, 'SK텔레콤의 5G 기술과 미래 통신 서비스를 체험할 수 있습니다.', 1),
       (26, 25, '르세라핌 공식 굿즈와 한정판 아이템들을 만나보세요.', 1),
       (27, 26, '맥도날드 해피밀 한정 토이와 특별 메뉴를 즐기실 수 있습니다.', 1),
       (28, 27, '아모레퍼시픽의 프리미엄 뷰티 제품을 체험하고 개인 맞춤 상담을 받아보세요.', 1),
       (29, 28, '현대백화점이 선별한 럭셔리 브랜드들의 특별한 팝업 컬렉션입니다.', 1),
       (30, 29, '페라리의 역사와 최신 모델들을 한 눈에 볼 수 있는 전시관입니다.', 1),
       (31, 30, 'CJ올리브영의 뷰티 전문가와 함께하는 맞춤형 뷰티 서비스를 경험하세요.', 1);

-- ===================================
-- ✅ [DATA] popup_socials
-- ===================================

INSERT INTO popup_socials (id, popup_id, icon_url, link_url, sort_order)

VALUES (1, 1, 'Instagram', 'https://instagram.com/musinsa_official', 1),
       (2, 1, 'Facebook', 'https://facebook.com/musinsa', 2),
       (3, 2, 'Instagram', 'https://instagram.com/adidas_originals', 1),
       (4, 3, 'Facebook', 'https://facebook.com/cocacola', 1),
       (5, 4, 'Instagram', 'https://instagram.com/samsungmobile', 1),
       (6, 5, 'Facebook', 'https://facebook.com/starbucks', 1),
       (7, 6, 'Instagram', 'https://instagram.com/nike', 1),
       (8, 7, 'Youtube', 'https://youtube.com/disney', 1),
       (9, 8, 'Instagram', 'https://instagram.com/hyundai_worldwide', 1),
       (10, 9, 'Facebook', 'https://facebook.com/pokemon', 1),
       (11, 10, 'Facebook', 'https://facebook.com/apple', 1),
       (12, 11, 'Instagram', 'https://instagram.com/uniqlo', 1),
       (13, 12, 'Instagram', 'https://instagram.com/bts_official', 1),
       (14, 13, 'Youtube', 'https://youtube.com/netflix', 1),
       (15, 14, 'Instagram', 'https://instagram.com/kakaofriends', 1),
       (16, 15, 'Facebook', 'https://facebook.com/lg', 1),
       (17, 16, 'Instagram', 'https://instagram.com/linefriends', 1),
       (18, 17, 'Youtube', 'https://youtube.com/marvel', 1),
       (19, 18, 'Instagram', 'https://instagram.com/gucci', 1),
       (20, 19, 'Facebook', 'https://facebook.com/lotteworld', 1),
       (21, 20, 'Youtube', 'https://youtube.com/samsung', 1),
       (22, 21, 'Instagram', 'https://instagram.com/heineken', 1),
       (23, 22, 'Facebook', 'https://facebook.com/mlb', 1),
       (24, 23, 'Facebook', 'https://facebook.com/dunkindonuts', 1),
       (25, 24, 'Instagram', 'https://instagram.com/sktelecom', 1),
       (26, 25, 'Instagram', 'https://instagram.com/le_sserafim', 1),
       (27, 26, 'Youtube', 'https://youtube.com/mcdonalds', 1),
       (28, 27, 'Instagram', 'https://instagram.com/amorepacific', 1),
       (29, 28, 'Facebook', 'https://facebook.com/hyundaidept', 1),
       (30, 29, 'Instagram', 'https://instagram.com/ferrari', 1),
       (31, 30, 'Youtube', 'https://youtube.com/oliveyoung', 1);

-- ===================================
-- ✅ [DATA] popup_reviews
-- ===================================

INSERT INTO popup_reviews (id, popup_id, member_id, rating, content)
VALUES (1, 1, 1000, 4, '좋아요'),
       (2, 1, 1001, 5, '최고예요');

-- ===================================
-- ✅ [DATA] waiting
-- ===================================

INSERT INTO waitings (id, popup_id, waiting_person_name, member_id, contact_email, people_count, waiting_number, status,
                      created_at, modified_at)
VALUES (100, 1, '김테스트', 1000, 'test@test.com', 2, 0, 'WAITING', '2025-06-28T10:00:00', '2025-06-28T10:00:00'),
       (101, 1, '이테스트', 1001, 'test2@test.com', 1, 1, 'WAITING', '2025-06-28T11:00:00', '2025-06-28T11:00:00');

-- ===================================
-- ✅ [IDENTITY RESTART] prevent conflicts for auto-increment when inserting new data at runtime
-- ===================================

ALTER TABLE popups
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE popup_locations
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE popup_weekly_schedules
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE popup_images
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE popup_contents
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE popup_socials
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE popup_categories
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE categories
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE members
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE waitings
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE notifications
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE popup_reviews
    ALTER COLUMN id RESTART WITH 3000;
ALTER TABLE global_bans
    ALTER COLUMN id RESTART WITH 3000;
