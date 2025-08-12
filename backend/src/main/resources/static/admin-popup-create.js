document.addEventListener('DOMContentLoaded', () => {
  const state = {
    openingHours: [],
    imageUrls: [""],
    sns: [{ iconUrl: "", linkUrl: "" }],
  };

  const qs = (sel) => document.querySelector(sel);
  const qsa = (sel) => Array.from(document.querySelectorAll(sel));

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

  function renderImageUrls() {
    const container = qs('#imageUrlList');
    container.innerHTML = '';
    state.imageUrls.forEach((url, idx) => {
      const row = document.createElement('div');
      row.innerHTML = `
        <div style="display:flex; gap:8px;">
          <input type="text" value="${url}" data-idx="${idx}" style="flex:1;" placeholder="https://..." />
          <button class="btn" data-remove-image="${idx}">삭제</button>
        </div>
      `;
      container.appendChild(row);
    });

    container.querySelectorAll('input[type="text"]').forEach(el => {
      el.addEventListener('input', (e) => {
        const idx = Number(e.target.getAttribute('data-idx'));
        state.imageUrls[idx] = e.target.value;
      });
    });
    container.querySelectorAll('[data-remove-image]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = Number(e.target.getAttribute('data-remove-image'));
        state.imageUrls.splice(idx, 1);
        renderImageUrls();
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
  renderImageUrls();
  renderSns();

  qs('#addOpeningHours').addEventListener('click', () => {
    state.openingHours.push({ dayOfWeek: 'TUESDAY', openTime: '10:00', closeTime: '20:00' });
    renderOpeningHours();
  });
  qs('#addImage').addEventListener('click', () => {
    state.imageUrls.push('');
    renderImageUrls();
  });
  qs('#addSns').addEventListener('click', () => {
    state.sns.push({ iconUrl: '', linkUrl: '' });
    renderSns();
  });

  // 이미지 업로드 기능
  qs('#uploadImage').addEventListener('click', async () => {
    const fileInput = qs('#imageFile');
    const statusDiv = qs('#uploadStatus');
    
    if (!fileInput.files || !fileInput.files[0]) {
      statusDiv.textContent = '파일을 선택해주세요.';
      statusDiv.className = 'error';
      return;
    }
    
    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append('file', file);
    
    statusDiv.textContent = '업로드 중...';
    statusDiv.className = 'muted';
    
    try {
      const response = await fetch('/api/images/upload', {
        method: 'POST',
        body: formData,
        credentials: 'include',
      });
      
      const result = await response.json();
      
      if (!response.ok) {
        statusDiv.textContent = `업로드 실패: ${result?.message || response.status}`;
        statusDiv.className = 'error';
        return;
      }
      
      // 업로드된 이미지 URL을 상태에 추가
      state.imageUrls.push(result.data.imageUrl);
      renderImageUrls();
      
      // 파일 입력 초기화
      fileInput.value = '';
      
      statusDiv.textContent = `업로드 성공! (${result.data.fileName})`;
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
      imageUrls: state.imageUrls.filter(Boolean),
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
    if (payload.imageUrls.length === 0) {
      result.textContent = '이미지 URL을 1개 이상 입력하세요';
      result.className = 'error';
      return;
    }
    if (!payload.categoryIds.length) {
      result.textContent = '카테고리를 1개 이상 선택하세요';
      result.className = 'error';
      return;
    }

    try {
      const res = await fetch('/api/popups', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
        credentials: 'include',
      });
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


