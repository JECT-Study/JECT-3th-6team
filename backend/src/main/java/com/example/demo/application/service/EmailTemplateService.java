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
        
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="text-align: center; margin-bottom: 30px;">
                    <h2 style="color: #333; margin: 0;">[%s] 지금 입장해주세요!</h2>
                    <div style="margin: 20px 0;">
                        <div style="display: inline-block; background: linear-gradient(45deg, #FF6B35, #FFD93D); 
                                   width: 60px; height: 60px; border-radius: 50%; margin-bottom: 10px;"></div>
                        <div style="font-size: 24px; font-weight: bold; color: #333;">스팟잇</div>
                        <div style="font-size: 14px; color: #666;">spot it!</div>
                    </div>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="color: #333; margin: 0;">
                        스팟잇으로 예약하신 내역입니다.
                        <span style="color: #FF6B35; font-weight: bold;">예약하신 내역입니다.</span>
                    </p>
                </div>
                
                <div style="background: #f9f9f9; padding: 20px; border-radius: 8px; margin-bottom: 30px;">
                    <div style="margin-bottom: 10px;">
                        <strong>대기자명:</strong> %s
                    </div>
                    <div style="margin-bottom: 10px;">
                        <strong>대기자 수:</strong> %d
                    </div>
                    <div style="margin-bottom: 10px;">
                        <strong>대기자 이메일:</strong> %s
                    </div>
                    <div style="margin-bottom: 10px;">
                        <strong>대기 일자:</strong> %s • %s
                    </div>
                </div>
                
                <div style="margin-bottom: 30px;">
                    <p style="color: #333; font-size: 16px; margin-bottom: 15px;">
                        안녕하세요, <strong>%s</strong>님!
                    </p>
                    <p style="color: #333; font-size: 16px; margin-bottom: 15px;">
                        스팟잇을 이용해 주셔서 진심으로 감사합니다.
                    </p>
                    <p style="color: #333; font-size: 16px; margin-bottom: 20px;">
                        기다려주신 <strong>%s</strong> 스토어의 입장 순서가 되었습니다! 지금 바로 입장해 주세요.
                    </p>
                    
                    <div style="background: #fff3cd; border-left: 4px solid #FF6B35; padding: 15px; margin: 20px 0;">
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <span style="color: #FF6B35; font-size: 18px; margin-right: 10px;">⏰</span>
                            <span style="color: #333;">본 이메일 알림을 받은 이후 10분 이내에 입장 부탁드립니다.</span>
                        </div>
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <span style="color: #FF6B35; font-size: 18px; margin-right: 10px;">📍</span>
                            <span style="color: #333;">매장 위치 보기: <a href="%s" style="color: #FF6B35;">%s</a></span>
                        </div>
                        <div style="display: flex; align-items: center;">
                            <span style="color: #FF6B35; font-size: 18px; margin-right: 10px;">💬</span>
                            <span style="color: #333;">도착하시면 직원에게 '스팟잇에서 입장하라고 안내받았어요'라고 말씀해 주세요.</span>
                        </div>
                    </div>
                </div>
                
                <div style="text-align: center; margin-bottom: 30px;">
                    <p style="color: #333; font-size: 16px; margin-bottom: 10px;">
                        다음에도 스팟잇과 함께 특별한 순간을 놓치지 마세요. 감사합니다.
                    </p>
                    <p style="color: #333; font-size: 18px; font-weight: bold; margin-bottom: 5px;">
                        지금, 이 순간의 핫플을, spot it!
                    </p>
                    <p style="color: #666; font-size: 14px;">스팟잇</p>
                </div>
                
                <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                
                <div style="text-align: center;">
                    <p style="color: #333; font-size: 16px; margin-bottom: 20px;">문의사항이 있으신가요?</p>
                    <div style="display: flex; justify-content: center; gap: 20px; flex-wrap: wrap;">
                        <div style="text-align: center;">
                            <span style="color: #FF6B35; font-size: 18px;">📧</span>
                            <p style="color: #333; font-size: 14px; margin: 5px 0;">메일 문의가 있다면?</p>
                            <p style="color: #666; font-size: 12px;">Ospotit0@gmail.com</p>
                        </div>
                        <div style="text-align: center;">
                            <span style="color: #666; font-size: 18px;">💬</span>
                            <p style="color: #333; font-size: 14px; margin: 5px 0;">스팟잇에게 의견 보내기</p>
                            <p style="color: #666; font-size: 12px;">(링크)</p>
                        </div>
                        <div style="text-align: center;">
                            <span style="color: #666; font-size: 18px;">🌐</span>
                            <p style="color: #333; font-size: 14px; margin: 5px 0;">스팟잇 더 알아보기</p>
                            <p style="color: #666; font-size: 12px;">(링크)</p>
                        </div>
                    </div>
                </div>
            </div>
            """.formatted(
                request.storeName(),
                request.memberName(),
                request.waitingCount(),
                request.memberEmail(),
                timeStr,
                dateStr,
                request.memberName(),
                request.storeName(),
                request.storeLocation(),
                request.storeLocation()
            );
    }
}
