
-- ===================================
-- ✅ [DATA] categories
-- ===================================

INSERT INTO categories (id, name)
VALUES (100, '패션'),
       (101, '예술');

-- ===================================
-- ✅ [DATA] members
-- ===================================

INSERT INTO members (id)
VALUES (1000),
       (1001);

-- ===================================
-- ✅ [DATA] popup_locations
-- ===================================

INSERT INTO popup_locations (id, address_name, region_1depth_name, region_2depth_name, region_3depth_name, longitude, latitude)
VALUES (10, '경기도 성남시 분당구', '경기도', '성남시 분당구', '', 127.423084873712, 37.0789561558879);

-- ===================================
-- ✅ [DATA] popups
-- ===================================

INSERT INTO popups (id, title, popup_location_id, slot_interval_minutes, type, start_date, end_date)
VALUES (1, '무신사 X 나이키 팝업스토어', 10, 60, 'EXPERIENTIAL', '2025-06-01', '2025-06-25');

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
