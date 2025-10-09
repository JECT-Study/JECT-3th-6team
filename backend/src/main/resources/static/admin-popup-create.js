document.addEventListener('DOMContentLoaded', () => {
  const state = {
    openingHours: [],
    mainImageUrls: [""],        // 메인 이미지들 (필수)
    brandStoryImageUrls: [""],  // 브랜드 스토리 이미지들 (필수)
    sns: [{ iconUrl: "", linkUrl: "" }],
  };

  const qs = (sel) => document.querySelector(sel);
  const qsa = (sel) => Array.from(document.querySelectorAll(sel));
  
  // 인증 확인
  function checkAuth() {
    const token = localStorage.getItem('adminToken');
    if (!token) {
      alert('로그인이 필요합니다.');
      window.location.href = '/admin-login.html';
      return null;
    }
    return token;
  }

  // 페이지 로드 시 인증 확인
  const token = checkAuth();
  if (!token) return;
  
  // Authorization 헤더 생성 함수
  const getAuthHeaders = () => {
    const token = localStorage.getItem('adminToken');
    return {
      'X-Admin-Token': token
    };
  };
  
  // 인증 상태 업데이트
  const updateAuthStatus = () => {
    const token = localStorage.getItem('adminToken');
    const statusDiv = qs('#authStatus');
    
    if (token) {
      statusDiv.textContent = '✅ 로그인되어 있습니다. API 호출이 가능합니다.';
      statusDiv.className = 'success';
    } else {
      statusDiv.textContent = '⚠️ 로그인이 필요합니다.';
      statusDiv.className = 'error';
    }
  };

  updateAuthStatus();

  function renderOpeningHours() {
    const container = qs('#openingHoursList');
    container.innerHTML = '';
    state.openingHours.forEach((it, idx) => {
      const row = document.createElement('div');
      row.className = 'row-3';
      row.innerHTML = `
        <select data-idx="${idx}" data-key="dayOfWeek">
          ${['MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY']
            .map(d => `<option value="${d}" ${it.dayOfWeek===d?'selected':''}>${d}</option>`).join('')}
        </select>
        <input type="time" value="${it.openTime}" data-idx="${idx}" data-key="openTime" />
        <div style="display:flex; gap:8px;">
          <input type="time" value="${it.closeTime}" data-idx="${idx}" data-key="closeTime" style="flex:1;" />
          <button class="btn" data-remove-opening="${idx}">삭제</button>
        </div>
      `;
      container.appendChild(row);
    });

    container.querySelectorAll('select, input[type="time"]').forEach(el => {
      el.addEventListener('change', (e) => {
        const idx = Number(e.target.getAttribute('data-idx'));
        const key = e.target.getAttribute('data-key');
        state.openingHours[idx][key] = e.target.value;
      });
    });

    container.querySelectorAll('[data-remove-opening]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = Number(e.target.getAttribute('data-remove-opening'));
        state.openingHours.splice(idx, 1);
        renderOpeningHours();
      });
    });
  }

  function renderMainImageUrls() {
    const container = qs('#mainImageUrlList');
    container.innerHTML = '';
    state.mainImageUrls.forEach((url, idx) => {
      const row = document.createElement('div');
      row.innerHTML = `
        <div style="display:flex; gap:8px;">
          <input type="text" value="${url}" data-idx="${idx}" style="flex:1;" placeholder="https://..." />
          <button class="btn" data-remove-main-image="${idx}">삭제</button>
        </div>
      `;
      container.appendChild(row);
    });

    container.querySelectorAll('input[type="text"]').forEach(el => {
      el.addEventListener('input', (e) => {
        const idx = Number(e.target.getAttribute('data-idx'));
        state.mainImageUrls[idx] = e.target.value;
      });
    });
    container.querySelectorAll('[data-remove-main-image]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = Number(e.target.getAttribute('data-remove-main-image'));
        state.mainImageUrls.splice(idx, 1);
        renderMainImageUrls();
      });
    });
  }

  function renderBrandStoryImageUrls() {
    const container = qs('#brandStoryImageUrlList');
    container.innerHTML = '';
    state.brandStoryImageUrls.forEach((url, idx) => {
      const row = document.createElement('div');
      row.innerHTML = `
        <div style="display:flex; gap:8px;">
          <input type="text" value="${url}" data-idx="${idx}" style="flex:1;" placeholder="https://..." />
          <button class="btn" data-remove-brand-story-image="${idx}">삭제</button>
        </div>
      `;
      container.appendChild(row);
    });

    container.querySelectorAll('input[type="text"]').forEach(el => {
      el.addEventListener('input', (e) => {
        const idx = Number(e.target.getAttribute('data-idx'));
        state.brandStoryImageUrls[idx] = e.target.value;
      });
    });
    container.querySelectorAll('[data-remove-brand-story-image]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = Number(e.target.getAttribute('data-remove-brand-story-image'));
        state.brandStoryImageUrls.splice(idx, 1);
        renderBrandStoryImageUrls();
      });
    });
  }

  function renderSns() {
    const container = qs('#snsList');
    container.innerHTML = '';
    state.sns.forEach((s, idx) => {
      const row = document.createElement('div');
      row.className = 'row-3';
      row.innerHTML = `
        <input type="text" value="${s.iconUrl}" data-idx="${idx}" data-key="iconUrl" placeholder="iconUrl" />
        <input type="text" value="${s.linkUrl}" data-idx="${idx}" data-key="linkUrl" placeholder="linkUrl" />
        <button class="btn" data-remove-sns="${idx}">삭제</button>
      `;
      container.appendChild(row);
    });

    container.querySelectorAll('input').forEach(el => {
      el.addEventListener('input', (e) => {
        const idx = Number(e.target.getAttribute('data-idx'));
        const key = e.target.getAttribute('data-key');
        state.sns[idx][key] = e.target.value;
      });
    });
    container.querySelectorAll('[data-remove-sns]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = Number(e.target.getAttribute('data-remove-sns'));
        state.sns.splice(idx, 1);
        renderSns();
      });
    });
  }

  // init default items
  state.openingHours.push({ dayOfWeek: 'MONDAY', openTime: '10:00', closeTime: '20:00' });
  renderOpeningHours();
  renderMainImageUrls();
  renderBrandStoryImageUrls();
  renderSns();

  qs('#addOpeningHours').addEventListener('click', () => {
    state.openingHours.push({ dayOfWeek: 'TUESDAY', openTime: '10:00', closeTime: '20:00' });
    renderOpeningHours();
  });
  qs('#addMainImageUrl').addEventListener('click', () => {
    state.mainImageUrls.push('');
    renderMainImageUrls();
  });
  qs('#addBrandStoryImageUrl').addEventListener('click', () => {
    state.brandStoryImageUrls.push('');
    renderBrandStoryImageUrls();
  });
  qs('#addSns').addEventListener('click', () => {
    state.sns.push({ iconUrl: '', linkUrl: '' });
    renderSns();
  });

  // 메인 이미지 업로드 기능
  qs('#uploadMainImages').addEventListener('click', async () => {
    const fileInput = qs('#mainImageFile');
    const statusDiv = qs('#mainImageUploadStatus');
    
    if (!fileInput.files || fileInput.files.length === 0) {
      statusDiv.textContent = '파일을 선택해주세요.';
      statusDiv.className = 'error';
      return;
    }
    
    statusDiv.textContent = '업로드 중...';
    statusDiv.className = 'muted';
    
    try {
      // 여러 파일 업로드 처리
      for (let i = 0; i < fileInput.files.length; i++) {
        const file = fileInput.files[i];
        const formData = new FormData();
        formData.append('file', file);
        
        const response = await fetch('/api/admin/images/upload', {
          method: 'POST',
          body: formData,
          headers: getAuthHeaders(),
        });
        
        if (response.status === 401) {
          statusDiv.textContent = '세션이 만료되었습니다. 다시 로그인해주세요.';
          statusDiv.className = 'error';
          localStorage.removeItem('adminToken');
          setTimeout(() => { window.location.href = '/admin-login.html'; }, 2000);
          return;
        }

        const result = await response.json();
        
        if (!response.ok) {
          statusDiv.textContent = `업로드 실패: ${result?.message || response.status}`;
          statusDiv.className = 'error';
          return;
        }
        
        // 업로드된 이미지 URL을 메인 이미지 상태에 추가
        state.mainImageUrls.push(result.data.imageUrl);
      }
      
      renderMainImageUrls();
      
      // 파일 입력 초기화
      fileInput.value = '';
      
      statusDiv.textContent = `메인 이미지 업로드 성공! (${fileInput.files.length}개 파일)`;
      statusDiv.className = 'success';
      
    } catch (error) {
      console.error('Upload error:', error);
      statusDiv.textContent = '업로드 중 오류가 발생했습니다.';
      statusDiv.className = 'error';
    }
  });

  // 브랜드 스토리 이미지 업로드 기능
  qs('#uploadBrandStoryImages').addEventListener('click', async () => {
    const fileInput = qs('#brandStoryImageFile');
    const statusDiv = qs('#brandStoryImageUploadStatus');
    
    if (!fileInput.files || fileInput.files.length === 0) {
      statusDiv.textContent = '파일을 선택해주세요.';
      statusDiv.className = 'error';
      return;
    }
    
    statusDiv.textContent = '업로드 중...';
    statusDiv.className = 'muted';
    
    try {
      // 여러 파일 업로드 처리
      for (let i = 0; i < fileInput.files.length; i++) {
        const file = fileInput.files[i];
        const formData = new FormData();
        formData.append('file', file);
        
        const response = await fetch('/api/admin/images/upload', {
          method: 'POST',
          body: formData,
          headers: getAuthHeaders(),
        });
        
        if (response.status === 401) {
          statusDiv.textContent = '세션이 만료되었습니다. 다시 로그인해주세요.';
          statusDiv.className = 'error';
          localStorage.removeItem('adminToken');
          setTimeout(() => { window.location.href = '/admin-login.html'; }, 2000);
          return;
        }

        const result = await response.json();
        
        if (!response.ok) {
          statusDiv.textContent = `업로드 실패: ${result?.message || response.status}`;
          statusDiv.className = 'error';
          return;
        }
        
        // 업로드된 이미지 URL을 브랜드 스토리 이미지 상태에 추가
        state.brandStoryImageUrls.push(result.data.imageUrl);
      }
      
      renderBrandStoryImageUrls();
      
      // 파일 입력 초기화
      fileInput.value = '';
      
      statusDiv.textContent = `브랜드 스토리 이미지 업로드 성공! (${fileInput.files.length}개 파일)`;
      statusDiv.className = 'success';
      
    } catch (error) {
      console.error('Upload error:', error);
      statusDiv.textContent = '업로드 중 오류가 발생했습니다.';
      statusDiv.className = 'error';
    }
  });

  qs('#submit').addEventListener('click', async () => {
    const result = qs('#result');
    result.textContent = '';
    result.className = 'muted';

    const payload = {
      name: qs('#name').value.trim(),
      type: qs('#type').value,
      startDate: qs('#startDate').value,
      endDate: qs('#endDate').value,
      location: {
        addressName: qs('#addressName').value.trim(),
        region1DepthName: qs('#region1DepthName').value.trim(),
        region2DepthName: qs('#region2DepthName').value.trim(),
        region3DepthName: qs('#region3DepthName').value.trim(),
        longitude: Number(qs('#longitude').value),
        latitude: Number(qs('#latitude').value),
      },
      weeklyOpeningHours: state.openingHours,
      mainImageUrls: state.mainImageUrls.filter(Boolean),
      brandStoryImageUrls: state.brandStoryImageUrls.filter(Boolean),
      content: {
        introduction: qs('#introduction').value,
        notice: qs('#notice').value,
      },
      sns: state.sns.filter(s => s.iconUrl && s.linkUrl),
      categoryIds: qsa('#categoryList input[type="checkbox"]:checked').map(el => Number(el.value)),
    };

    if (!payload.name || !payload.startDate || !payload.endDate) {
      result.textContent = '필수값(이름/기간)을 입력하세요';
      result.className = 'error';
      return;
    }
    if (!payload.location.addressName || !payload.location.region1DepthName || !payload.location.region2DepthName) {
      result.textContent = '위치 정보를 모두 입력하세요';
      result.className = 'error';
      return;
    }
    if (payload.location.region1DepthName === '') {
      result.textContent = '시/도를 선택해주세요';
      result.className = 'error';
      return;
    }
    if (payload.mainImageUrls.length === 0) {
      result.textContent = '메인 이미지 URL을 1개 이상 입력하세요';
      result.className = 'error';
      return;
    }
    if (payload.brandStoryImageUrls.length === 0) {
      result.textContent = '브랜드 스토리 이미지 URL을 1개 이상 입력하세요';
      result.className = 'error';
      return;
    }
    if (!payload.categoryIds.length) {
      result.textContent = '카테고리를 1개 이상 선택하세요';
      result.className = 'error';
      return;
    }

    try {
      const res = await fetch('/api/admin/popups/create', {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          ...getAuthHeaders()
        },
        body: JSON.stringify(payload),
      });

      if (res.status === 401) {
        result.textContent = '세션이 만료되었습니다. 다시 로그인해주세요.';
        result.className = 'error';
        localStorage.removeItem('adminToken');
        setTimeout(() => { window.location.href = '/admin-login.html'; }, 2000);
        return;
      }

      const data = await res.json();
      if (!res.ok) {
        result.textContent = `생성 실패: ${data?.message ?? res.status}`;
        result.className = 'error';
        return;
      }
      result.textContent = `생성 성공! popupId=${data.data.popupId}`;
      result.className = 'success';
    } catch (err) {
      console.error(err);
      result.textContent = '요청 중 오류가 발생했습니다';
      result.className = 'error';
    }
  });
});


