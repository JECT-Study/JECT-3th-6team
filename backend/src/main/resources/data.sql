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
       (1001, 'test2', 'test2@test.com', '2025-05-05T06:10:11', '2025-05-05T06:10:11');

-- ===================================
-- ✅ [DATA] popup_locations
-- ===================================

INSERT INTO popup_locations (id, address_name, region_1depth_name, region_2depth_name, region_3depth_name, longitude,
                             latitude)
VALUES (10, '경기도 성남시 분당구', '경기도', '성남시 분당구', '', 127.423084873712, 37.0789561558879);

-- ===================================
-- ✅ [DATA] popups
-- ===================================

INSERT INTO popups (id, title, popup_location_id, type, start_date, end_date)
VALUES (1, '무신사 X 나이키 팝업스토어', 10, 'EXPERIENTIAL', '2025-06-01', '2025-06-25');

-- ===================================
-- ✅ [DATA] popup_categories
-- ===================================

INSERT INTO popup_categories (id, popup_id, category_id, name)
VALUES (1, 1, 100, '패션'),
       (2, 1, 101, '예술');

-- ===================================
-- ✅ [DATA] popup_images (MAIN)
-- ===================================

INSERT INTO popup_images (id, popup_id, type, url, sort_order)
VALUES (1, 1, 'MAIN', 'https://example.com/images/popup1.jpg', 1),
       (2, 1, 'MAIN', 'https://example.com/images/popup2.jpg', 2),
       (3, 1, 'MAIN', 'https://example.com/images/popup3.jpg', 3);

-- ===================================
-- ✅ [DATA] popup_images (DESCRIPTION)
-- ===================================

INSERT INTO popup_images (id, popup_id, type, url, sort_order)
VALUES (4, 1, 'DESCRIPTION', 'http://brand-story-image.com/1.jpg', 1),
       (5, 1, 'DESCRIPTION', 'http://brand-story-image.com/2.jpg', 2),
       (6, 1, 'DESCRIPTION', 'http://brand-story-image.com/3.jpg', 3);

-- ===================================
-- ✅ [DATA] popup_weekly_schedules
-- ===================================

INSERT INTO popup_weekly_schedules (id, popup_id, day_of_week, open_time, close_time)
VALUES (1, 1, 'MONDAY', '11:00:00', '23:00:00'),
       (2, 1, 'TUESDAY', '11:00:00', '23:00:00');

-- ===================================
-- ✅ [DATA] popup_contents
-- ===================================

INSERT INTO popup_contents (id, popup_id, content_text, sort_order)
VALUES (1, 1, '설명 1', 1),
       (2, 1, '설명 2', 2);

-- ===================================
-- ✅ [DATA] popup_socials
-- ===================================

INSERT INTO popup_socials (id, popup_id, icon_url, link_url, sort_order)
VALUES (1, 1, 'http://icon.com', 'http://url.com', 1);

-- ===================================
-- ✅ [DATA] popup_reviews
-- ===================================

INSERT INTO popup_reviews (id, popup_id, member_id, rating, content)
VALUES (1, 1, 1000, 4, '좋아요'),
       (2, 1, 1001, 5, '최고예요');

-- ===================================
-- ✅ [DATA] waiting
-- ===================================

INSERT INTO waitings (id, popup_id, waiting_person_name, member_id, contact_email, people_count, waiting_number, status, created_at, modified_at)
VALUES (100, 1, '김테스트', 1000, 'test@test.com', 2, 1, 'WAITING', '2025-06-28T10:00:00', '2025-06-28T10:00:00'),
       (101, 1, '이테스트', 1001, 'test2@test.com', 1, 2, 'WAITING', '2025-06-28T11:00:00', '2025-06-28T11:00:00'),
       (102, 1, '박테스트', 1000, 'test@test.com', 3, 3, 'VISITED', '2025-06-28T09:00:00', '2025-06-28T09:00:00');

-- ===================================
-- ✅ [DATA] notifications
-- ===================================

INSERT INTO notifications (id, member_id, source_domain, source_id, event_type, content, status, read_at, created_at, modified_at)
VALUES 
-- 읽지 않은 알림들 (최신순)
(500, 1000, 'Waiting', 100, 'ENTER_NOW', '지금 매장으로 입장 부탁드립니다. 즐거운 시간 보내세요!', 'UNREAD', NULL, '2025-06-28T15:30:00', '2025-06-28T15:30:00'),
(501, 1000, 'Waiting', 100, 'ENTER_3TEAMS_BEFORE', '앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!', 'UNREAD', NULL, '2025-06-28T15:00:00', '2025-06-28T15:00:00'),
(502, 1001, 'Waiting', 101, 'WAITING_CONFIRMED', '대기 등록이 완료되었습니다. 대기번호 2번으로 확정되었습니다.', 'UNREAD', NULL, '2025-06-28T14:30:00', '2025-06-28T14:30:00'),

-- 읽은 알림들
(503, 1000, 'Waiting', 102, 'REVIEW_REQUEST', '오늘 ''무신사 X 나이키 팝업스토어'' 방문은 어떠셨나요? 간단한 리뷰만 남겨주셔도 큰 힘이 됩니다 :)', 'read', '2025-06-28T13:15:00', '2025-06-28T13:00:00', '2025-06-28T13:00:00'),
(504, 1000, 'Waiting', 100, 'WAITING_CONFIRMED', '대기 등록이 완료되었습니다. 대기번호 1번으로 확정되었습니다.', 'read', '2025-06-28T11:05:00', '2025-06-28T10:05:00', '2025-06-28T10:05:00'),
(505, 1001, 'Waiting', 101, 'ENTER_TIME_OVER', '입장 시간이 초과되어 대기가 취소되었습니다. 다음 기회에 다시 신청해주세요.', 'read', '2025-06-28T12:00:00', '2025-06-28T11:30:00', '2025-06-28T11:30:00'),

-- 추가 테스트 데이터 (페이징 테스트용)
(506, 1000, 'Waiting', 102, 'ENTER_3TEAMS_BEFORE', '입장까지 3팀 남았습니다!', 'read', '2025-06-28T09:45:00', '2025-06-28T09:30:00', '2025-06-28T09:30:00'),
(507, 1001, 'Waiting', 101, 'ENTER_NOW', '지금 입장해주세요!', 'UNREAD', NULL, '2025-06-28T08:00:00', '2025-06-28T08:00:00'),
(508, 1000, 'Waiting', 100, 'ENTER_TIME_OVER', '입장 시간이 지났습니다.', 'read', '2025-06-28T07:30:00', '2025-06-28T07:00:00', '2025-06-28T07:00:00');