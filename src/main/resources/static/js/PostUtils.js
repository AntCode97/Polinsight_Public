const makePagination = (current, totalPage) => {
  let first = $(`<a><i class="fas fa-angle-double-left"></i></a>`).on('click', {page: 0}, init);
  let prev = $(`<a><i class="fas fa-angle-left"></i></a>`).on('click', {page: current - 1 > 0 ? current - 1 : 0}, init);
  let next = $(`<a><i class="fas fa-angle-right"></i></a>`).on('click', {page: current + 1 < totalPage - 1 ? current + 1 : totalPage - 1}, init);
  let last = $(`<a><i class="fas fa-angle-double-right"></i></a>`).on('click', {page: totalPage - 1}, init);

  let pageStart = Math.floor(current / 10) * 10
  let pageEnd = pageStart + 10 >= totalPage ? totalPage : pageStart + 10

  let pagination = $('#pagination').empty()
  if (current > 0)
    pagination.append(first).append(prev)

  for (let i = pageStart; i < pageEnd; i++) {
    let btn = $(`<a>${i + 1}</a>`).on('click', {page: i}, init)
    btn.removeClass('active')
    if (current === i)
      btn.addClass('active')
    pagination.append(btn)
  }

  if (current < totalPage - 1)
    pagination.append(next).append(last)
}

const makePostTemplate = (post) => {
  let template = $(`<div></div>`).addClass('post-row')
  template.append(`<div>${post.id}</div>`)
  template.append(`<div>${post.title}</div>`)
  template.append(`<div>${post.name}</div>`)
  template.append(`<div>${post.registedAt}</div>`)
  template.append(`<div>${post.viewCount}</div>`)
  template.append(`<div>-</div>`)
  template.on('click', {post: post}, e => {
    location.href = `/posts/${post.id}`
  })
  return template;
}

const makeSurveyTemplate = (survey) => {
  let template = $(`<div class="post-row">`).on('click', {survey: survey}, setDataInModal);
  template.append(`<div>${survey.id}</div>`);// 번호
  template.append(`<div style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${survey.title}</div>`);// 설문 이름
  template.append(`<div>${!survey.point ? '지정 안됨' : survey.point}</div>`);// 지급 포인트
  template.append(`<div>${String(survey.createdAt).split('T')[0] !== 'null' ? String(survey.endAt).split('T')[0] : 'N/A'}</div>`); // 설문 기간
  template.append(`<div>${String(survey.endAt).split('T')[0] !== 'null' ? String(survey.endAt).split('T')[0] : 'N/A'}</div>`)
  template.append(`<div>${survey.progress === 'BEFORE' ? '등록' : survey.progress === 'ONGOING' ? '진행' : '종료'}</div>`);// 진행 상태
  template.append(`<div>${!survey.count ? 0 : survey.count}</div>`);// 참여자 수
  template.append(`<div>${survey.surveyId}</div>`);// 서베이 아이디
  return template
}


const searchMessage = (keyword, total, name) => {
  let searchBox = $('.util-search').empty()
  if (!keyword) {
    searchBox
        .append($(`<span><strong id="total_count">${total}</strong>개의 ${name} 있습니다</span>`))
  } else {
    searchBox
        .append($(`<span id="keyword"><span><strong>${keyword}</strong> 검색 결과 </span></span>`))
        .append($(`<span><strong id="total_count">${total}</strong>개 ${name} 있습니다</span>`))
  }
}