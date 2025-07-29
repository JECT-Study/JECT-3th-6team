// SSE 하트비트 테스트 JavaScript

let eventSource = null;

// DOM 요소들
const connectionStatus = document.getElementById('connection-status');
const connectBtn = document.getElementById('connect-btn');
const disconnectBtn = document.getElementById('disconnect-btn');
const logContainer = document.getElementById('log');

// SSE 연결
function connectSSE() {
    if (eventSource) {
        addLog('이미 연결되어 있습니다.', 'error');
        return;
    }

    addLog('SSE 연결을 시도합니다...', 'info');

    // 인증 없이 테스트용 연결
    eventSource = new EventSource('/api/notifications/stream-test');

    // 연결 성공 이벤트
    eventSource.addEventListener('connected', function (event) {
        addLog(`연결 성공: ${event.data}`, 'connected-event');
        updateConnectionStatus(true);
    });

    // 하트비트 ping 이벤트
    eventSource.addEventListener('ping', function (event) {
        try {
            const pingData = JSON.parse(event.data);
            const timestamp = new Date(pingData.timestamp).toLocaleTimeString();
            addLog(`💗 PING 수신 - ${timestamp} (${pingData.message})`, 'ping');
        } catch (e) {
            addLog(`💗 PING 수신 - ${event.data}`, 'ping');
        }
    });

    // 알림 이벤트
    eventSource.addEventListener('notification', function (event) {
        try {
            const notification = JSON.parse(event.data);
            addLog(`🔔 알림 수신: ${notification.message || JSON.stringify(notification)}`, 'notification');
        } catch (e) {
            addLog(`🔔 알림 수신: ${event.data}`, 'notification');
        }
    });

    // 일반 메시지 이벤트
    eventSource.onmessage = function (event) {
        addLog(`📨 메시지 수신: ${event.data}`, 'info');
    };

    // 연결 열기 이벤트
    eventSource.onopen = function (event) {
        addLog('SSE 연결이 열렸습니다.', 'connected-event');
        updateConnectionStatus(true);
    };

    // 에러 이벤트
    eventSource.onerror = function (event) {
        if (event.readyState === EventSource.CLOSED) {
            addLog('SSE 연결이 닫혔습니다.', 'error');
            updateConnectionStatus(false);
            eventSource = null;
        } else if (event.readyState === EventSource.CONNECTING) {
            addLog('SSE 재연결을 시도합니다...', 'info');
        } else {
            addLog(`SSE 연결 오류 발생: ${event.type}`, 'error');
        }
    };
}

// SSE 연결 해제
function disconnectSSE() {
    if (eventSource) {
        eventSource.close();
        eventSource = null;
        addLog('SSE 연결을 해제했습니다.', 'info');
        updateConnectionStatus(false);
    } else {
        addLog('연결되어 있지 않습니다.', 'error');
    }
}

// 연결 상태 업데이트
function updateConnectionStatus(isConnected) {
    if (isConnected) {
        connectionStatus.textContent = '연결됨';
        connectionStatus.className = 'status connected';
        connectBtn.disabled = true;
        disconnectBtn.disabled = false;
    } else {
        connectionStatus.textContent = '연결 안됨';
        connectionStatus.className = 'status disconnected';
        connectBtn.disabled = false;
        disconnectBtn.disabled = true;
    }
}

// 로그 추가
function addLog(message, type = 'info') {
    const timestamp = new Date().toLocaleTimeString();
    const logEntry = document.createElement('div');
    logEntry.className = `log-entry ${type}`;
    logEntry.textContent = `[${timestamp}] ${message}`;

    logContainer.appendChild(logEntry);
    logContainer.scrollTop = logContainer.scrollHeight;

    // 로그가 너무 많으면 오래된 것 삭제 (최대 100개)
    while (logContainer.children.length > 100) {
        logContainer.removeChild(logContainer.firstChild);
    }
}

// 로그 지우기
function clearLog() {
    logContainer.innerHTML = '<div class="log-entry">로그가 지워졌습니다.</div>';
}

// 페이지 언로드 시 연결 해제
window.addEventListener('beforeunload', function () {
    if (eventSource) {
        eventSource.close();
    }
});

// 초기 로그
addLog('SSE 하트비트 테스트 페이지가 로드되었습니다.', 'info');
addLog('※ 테스트용으로 인증 없이 사용자 ID 1로 연결됩니다.', 'info'); 