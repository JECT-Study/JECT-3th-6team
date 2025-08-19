import { isMobileDevice } from '@/shared/lib/cameraUtils';

/**
 * 간단한 기본 카메라 앱 열기
 * iOS/Android 기본 카메라는 QR 코드를 자동으로 인식합니다
 */
export const openNativeCamera = (): Promise<void> => {
  return new Promise((resolve, reject) => {
    if (!isMobileDevice()) {
      reject(
        new Error('모바일 디바이스에서만 기본 카메라를 실행할 수 있습니다.')
      );
      return;
    }

    const userAgent = navigator.userAgent.toLowerCase();
    const isIOS = /iphone|ipad|ipod/.test(userAgent);
    const isAndroid = /android/.test(userAgent);

    try {
      if (isIOS) {
        // iOS 카메라 앱 열기
        window.location.href = 'camera://';

        // 성공적으로 실행된 것으로 간주
        setTimeout(() => {
          alert(
            '카메라 앱에서 QR 코드를 비춰주세요. QR 코드가 인식되면 화면 상단에 링크가 나타납니다.'
          );
          resolve();
        }, 500);
      } else if (isAndroid) {
        // Android 카메라 앱 열기
        const iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        iframe.src =
          'intent://scan/#Intent;action=android.media.action.IMAGE_CAPTURE;category=android.intent.category.DEFAULT;end';
        document.body.appendChild(iframe);

        setTimeout(() => {
          if (document.body.contains(iframe)) {
            document.body.removeChild(iframe);
          }
          alert(
            '카메라 앱에서 QR 코드를 비춰주세요. QR 코드가 인식되면 자동으로 링크가 표시됩니다.'
          );
          resolve();
        }, 1000);
      } else {
        reject(new Error('지원되지 않는 플랫폼입니다.'));
      }
    } catch (error) {
      console.log(error);
      reject(
        new Error(
          '카메라 앱을 실행할 수 없습니다. 기기의 카메라 앱을 직접 열어보세요.'
        )
      );
    }
  });
};

/**
 * QR 스캔 안내 메시지 표시
 */
export const showQRScanGuide = (): Promise<boolean> => {
  return new Promise(resolve => {
    const modal = document.createElement('div');
    modal.style.position = 'fixed';
    modal.style.top = '0';
    modal.style.left = '0';
    modal.style.width = '100vw';
    modal.style.height = '100vh';
    modal.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
    modal.style.zIndex = '10001';
    modal.style.display = 'flex';
    modal.style.alignItems = 'center';
    modal.style.justifyContent = 'center';

    modal.innerHTML = `
      <div style="
        background: white;
        padding: 30px;
        border-radius: 12px;
        max-width: 90vw;
        width: 400px;
        box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
        text-align: center;
      ">
        <div style="font-size: 48px; margin-bottom: 20px;">📱</div>
        
        <h3 style="
          margin: 0 0 20px 0;
          font-size: 18px;
          font-weight: bold;
          color: #333;
        ">QR 코드 스캔 방법</h3>
        
        <div style="
          color: #374151; 
          line-height: 1.6;
          text-align: left;
          margin-bottom: 25px;
        ">
          <p style="margin: 0 0 15px 0;">
            <strong>1단계:</strong> 카메라 앱이 자동으로 실행됩니다
          </p>
          <p style="margin: 0 0 15px 0;">
            <strong>2단계:</strong> QR 코드에 카메라를 향해주세요
          </p>
          <p style="margin: 0 0 15px 0;">
            <strong>3단계:</strong> 화면 상단에 나타나는 링크를 터치하세요
          </p>
          <p style="margin: 0;">
            <strong>참고:</strong> 별도 앱 설치가 필요하지 않습니다
          </p>
        </div>
        
        <div style="display: flex; gap: 10px;">
          <button id="cancelBtn" style="
            flex: 1;
            padding: 12px;
            background: #f3f4f6;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            color: #374151;
          ">취소</button>
          
          <button id="startBtn" style="
            flex: 2;
            padding: 12px;
            background: #3b82f6;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            color: white;
          ">카메라 실행</button>
        </div>
      </div>
    `;

    document.body.appendChild(modal);

    const cancelBtn = modal.querySelector('#cancelBtn') as HTMLButtonElement;
    const startBtn = modal.querySelector('#startBtn') as HTMLButtonElement;

    const cleanup = () => {
      if (document.body.contains(modal)) {
        document.body.removeChild(modal);
      }
    };

    cancelBtn.addEventListener('click', () => {
      cleanup();
      resolve(false);
    });

    startBtn.addEventListener('click', () => {
      cleanup();
      resolve(true);
    });

    // 모달 바깥 클릭 시 닫기
    modal.addEventListener('click', e => {
      if (e.target === modal) {
        cleanup();
        resolve(false);
      }
    });
  });
};

/**
 * 간단한 QR 스캔 프로세스
 * 1. 안내 메시지 표시
 * 2. 카메라 앱 실행
 */
export const startSimpleQRScan = async (): Promise<void> => {
  const shouldStart = await showQRScanGuide();

  if (shouldStart) {
    await openNativeCamera();
  } else {
    throw new Error('사용자가 QR 스캔을 취소했습니다.');
  }
};
