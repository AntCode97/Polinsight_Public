import _ from 'lodash'
import http from "./http";

const emailValidator = e => {
  http.get('/user' + e.target.value)
      .then(res => {

        console.log(res)
      })
      .catch(err => {
        console.log(err)
      });
}

// NOTE 2021/06/12 : pwconfirm의 내용이 바뀔때마다 체크
const passwordChecker = e => {
  const pw = document.getElementsByName("password").value
  const cfm = e.target.value // pw confirm의 값
  const msg = document.getElementsByName("pwmsg")

  if (pw !== cfm) {
    // TODO : 다르다는 메시지 띄우기
    msg.style.visibility = visible;
  } else {
    // 띄웟던 메시지 지우기
    msg.style.visibility = hidden;
  }
}

// NOTE 2021/06/12 : 다수의 요청이 들어와도 0.2초에 한번씩만 api call이 일어남
const emailThrottle = _.debounce(emailValidator, 200);
