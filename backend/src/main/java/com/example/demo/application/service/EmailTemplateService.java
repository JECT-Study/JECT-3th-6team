package com.example.demo.application.service;

import com.example.demo.application.dto.notification.WaitingEntryNotificationRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * 이메일 템플릿 생성 서비스
 */
@Service
public class EmailTemplateService {

    /**
     * 웨이팅 입장 알림 이메일 HTML 템플릿을 생성한다.
     *
     * @param request 입장 알림 요청 정보
     * @return HTML 이메일 템플릿
     */
    public String buildWaitingEntryTemplate(WaitingEntryNotificationRequest request) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a h:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
    
        String timeStr = request.waitingDateTime().format(timeFormatter);
        String dateStr = request.waitingDateTime().format(dateFormatter);
    
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px;">
                <!-- 제목 -->
                <div style="text-align: left; font-weight: bold; font-size: 20px; color: #333333; margin-bottom: 24px;">
                    [%s] 지금 입장해주세요!
                </div>
    
                <!-- 로고 & 텍스트 (왼쪽 정렬) -->
                <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 24px;">
                    <a href="https://imgbb.com/"><img src="https://i.ibb.co/8n08dL03/Group-2085669840.png" alt="Group-2085669840" border="0"></a>
                </div>
               

                <!-- 강조 문구 (왼쪽 정렬) -->
                <div style="font-size: 18px; margin-bottom: 30px;">
                    <span style="font-weight: bold;">스팟잇으로</span>
                    <span style="color: #FF6B35; font-weight: bold;">예약하신 내역</span>
                    <span>입니다.</span>
                </div>
    
                <div style="background: #f8f9fa; padding: 20px; border-radius: 8px; border: 1px solid #e9ecef; margin-bottom: 30px;">
                    <p style="margin: 0 0 10px;"><strong>대기자명:</strong> %s</p>
                    <p style="margin: 0 0 10px;"><strong>대기자 수:</strong> %d</p>
                    <p style="margin: 0 0 10px;"><strong>대기자 이메일:</strong> %s</p>
                    <p style="margin: 0;"><strong>대기 일자:</strong> %s • %s</p>
                </div>
    
                <!-- 안내 멘트 -->
                <div style="font-size: 14px; color: #333; margin-bottom: 20px;">
                    <p>안녕하세요, <strong>%s</strong>님!</p>
                    <p>스팟잇을 이용해 주셔서 진심으로 감사합니다.</p>
                    <p>기다려주신 <strong>%s</strong> 스토어의 입장 순서가 되었습니다! 지금 바로 입장해 주세요.</p>
                </div>
    
                <!-- 유의사항 -->
                <div style="font-size: 14px; color: #333; margin-bottom: 30px;">
                    <p>⏰ 본 이메일 알림을 받은 이후 <strong>10분 이내</strong>에 입장 부탁드립니다.</p>
                    <p>📍 매장 위치 보기: <a href="%s" style="color: #FF6B35; text-decoration: none;">(링크)</a></p>
                    <p>👣 도착하시면 직원에게 ‘스팟잇에서 입장하라고 안내받았어요’라고 말씀해 주세요.</p>
                </div>
    
                <!-- 클로징 -->
                <div style="font-size: 14px; color: #333; text-align: left; margin-bottom: 40px;">
                    <p>다음에도 스팟잇과 함께 특별한 순간을 놓치지 마세요. 감사합니다.</p>
                    <p><strong>지금, 이 순간의 핫플을, spot it!</strong><br>스팟잇</p>
                </div>
    
                <!-- 문의사항 -->
                <div style="font-size: 14px; color: #333;">
                    <p style="margin-bottom: 8px;">문의사항이 있으신가요?</p>
                    <p>📧 메일 문의가 있다면? <a href="mailto:0spotit0@gmail.com" style="color: #FF6B35; text-decoration: none;">0spotit0@gmail.com</a></p>
                    <p>💬\s
                        <a href="https://forms.gle/xHfg3yvpUSymZPu77" style="color: #FF6B35; text-decoration: none;">
                            스팟잇에게 의견 보내기
                        </a>
                    </p>
                    <p>🌐\s
                        <a href="https://www.spotit.co.kr" style="color: #FF6B35; text-decoration: none;">
                            스팟잇 더 알아보기
                        </a>
                    </p>
                </div>

            </div>
            """,
            request.storeName(),
            request.memberName(),
            request.waitingCount(),
            request.memberEmail(),
            timeStr,
            dateStr,
            request.memberName(),
            request.storeName(),
            request.storeLocation()
        );
    }
    
    
}
