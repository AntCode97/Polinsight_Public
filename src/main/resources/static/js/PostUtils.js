const makePagination = (current, totalPage) => {
  let first = $(`<a><i class="fas fa-angle-double-left"></i></a>`).on(
    "click",
    { page: 0 },
    init
  );
  let prev = $(`<a><i class="fas fa-angle-left"></i></a>`).on(
    "click",
    { page: current - 1 > 0 ? current - 1 : 0 },
    init
  );
  let next = $(`<a><i class="fas fa-angle-right"></i></a>`).on(
    "click",
    { page: current + 1 < totalPage - 1 ? current + 1 : totalPage - 1 },
    init
  );
  let last = $(`<a><i class="fas fa-angle-double-right"></i></a>`).on(
    "click",
    { page: totalPage - 1 },
    init
  );

  let pageStart = Math.floor(current / 10) * 10;
  let pageEnd = pageStart + 10 >= totalPage ? totalPage : pageStart + 10;

  let pagination = $("#pagination").empty();
  if (current > 0) pagination.append(first).append(prev);

  for (let i = pageStart; i < pageEnd; i++) {
    let btn = $(`<a>${i + 1}</a>`).on("click", { page: i }, init);
    btn.removeClass("active");
    if (current === i) btn.addClass("active");
    pagination.append(btn);
  }

  if (current < totalPage - 1) pagination.append(next).append(last);
};

const makePostTemplate = (post) => {
  let template = $(`<div></div>`).addClass("post-row");
  template.append(`<div>${post.id}</div>`);
  template.append(`<div>${post.title}</div>`);
  template.append(`<div>${post.name}</div>`);
  template.append(`<div>${post.registedAt}</div>`);
  template.append(`<div>${post.viewCount}</div>`);
  template.append(`<div>-</div>`);
  template.on("click", { post: post }, (e) => {
    location.assign(`/posts/${post.id}`);
  });
  return template;
};

const makeNoticeTemplate = (post, current) => {
  let template = $(`<div></div>`).addClass("post-row");
  template.append(`<div>${post.id}</div>`);
  template.append(`<div>${post.title}</div>`);
  template.append(`<div>${post.name}</div>`);
  template.append(`<div>${post.registedAt}</div>`);
  template.append(`<div>${post.viewCount}</div>`);

  if(post.attaches.length ===0)   template.append(`  <div>
                <i class="fas fa-paperclip file_icon_no"></i>
              </div>`)
  else   template.append(`  <div>
                <i class="fas fa-paperclip file_icon_yes"></i>
              </div>`)

  template.on("click", { post: post }, (e) => {
    asynMakeBackUrl(current);
    location.assign(`/posts/${post.id}`);
  });
  return template;
};

const searchMessage = (keyword, total, name) => {
  let searchBox = $(".util-search").empty();
  if (!keyword) {
    searchBox.append(
      $(
        `<span><strong id="total_count">${total}</strong>개의 ${name} 있습니다</span>`
      )
    );
  } else {
    searchBox
      .append(
        $(
          `<span id="keyword"><span><strong>${keyword}</strong> 검색 결과 </span></span>`
        )
      )
      .append(
        $(
          `<span><strong id="total_count">${total}</strong>개 ${name} 있습니다</span>`
        )
      );
  }
};

const getCookie = function (name) {
  let value = document.cookie.match("(^|;) ?" + name + "=([^;]*)(;|$)");
  return value ? value[2] : null;
};

const setCookie = function (name, value, exp) {
  const date = new Date();
  date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
  document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
}


const asynMakeBackUrl = (current) => {

  let regex = String($('input[name=board_search]').val()).trim();
  console.log(location.href + "?page="+current +"&keyword="+regex);
  let next ="/posts?page="+current +"&keyword="+regex;
  setCookie("listBtnUrl",next, 1)
}


