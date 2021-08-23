const makePagination = (current, totalPage) => {
  let first = $(`<a><i class="fas fa-angle-double-left"></i></a>`).on('click', {page: 0}, init);
  let prev = $(`<a><i class="fas fa-angle-left"></i></a>`).on('click', {page: current - 1 > 1 ? current - 1 : 1}, init);
  let next = $(`<a><i class="fas fa-angle-right"></i></a>`).on('click', {page: current + 1 < totalPage - 1 ? current + 1 : totalPage - 1}, init);
  let last = $(`<a><i class="fas fa-angle-double-right"></i></a>`).on('click', {page: totalPage - 1}, init);

  let pageStart = current - 5 <= 0 ? 0 : current - 5;
  let pageEnd = current + 5 > totalPage ? totalPage : current + 5 < 10 ? 10 : current + 5;

  let pagination = $('#pagination').empty()
  if (current > 0)
    pagination.append(first).append(prev)
  for (let i = pageStart; i <= pageEnd; i++) {
    let btn = $(`<a>${i + 1}</a>`).on('click', {page: i}, init)
    btn.removeClass('active')
    if (current === i)
      btn.addClass('active')
    pagination.append(btn)
  }
  if (current < totalPage)
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

