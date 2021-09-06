const setDateValue = () => {
  let today = new Date();
  let year = $('select[name=year]')
  for (let i = 1930; i <= today.getFullYear(); i++) {
    year.append($("<option></option>").attr("value", i).text(i))
  }
  let month = $('select[name=month]')
  for (let i = 1; i < 13; i++) {
    month.append($("<option></option>").attr("value", i).text(i))
  }
}

const setDay = (e) => {
  const day = $('select[name=day]')
  day.empty();
  let month = $('select[name=month]').val()
  day.append($("<option selected></option>").text("선택"))
  if (month === 2) {
    for (let i = 1; i <= 28; i++) {
      day.append($("<option></option>").attr("value", i).text(i))
    }
  } else if (month === 1 || month === 3 || month === 5 || month === 7 || month === 8 || month === 10 || month === 12) {
    for (let i = 1; i <= 31; i++) {
      day.append($("<option></option>").attr("value", i).text(i))
    }
  } else {
    for (let i = 1; i <= 30; i++) {
      day.append($("<option></option>").attr("value", i).text(i))
    }
  }
}

const passwordInputValidator = pwd => {
  return !!pwd.match(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{10,16}$/);
}
const phoneNumberChecker = phone => {
  phone = phone.replaceAll("-", "")
  return !!phone.match(/^[0-9]{3}[0-9]{4}[0-9]{4}$/)
}
const emailInputValidator = email => {
  return !!email.match(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){5,20}@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i);
}

const phoneToStringConverter = phone => {
  if (phone.match(/([0-9]{3})-([0-9]{4})-([0-9]{4})/)) {
    return phone.replace(/([0-9]{3})-([0-9]{4})-([0-9]{4})/, "$1$2$3")
  } else {
    phone.substring(0, 11)
    return phone.replace(/([0-9]{3})([0-9]{4})([0-9]{4})/, "$1-$2-$3")
  }
}

const phoneAddHypen = e => {
  e.target.value = e.target.value.replace(/([0-9]{3})([0-9]{4})([0-9]{4})/, "$1-$2-$3")
}

const phoneDelHypen = e => {
  e.target.value = e.target.value.replace(/^([0-9]{3})-([0-9]{4})-([0-9]{4})$/, "$1$2$3")
}

const isNotBlank = val => {
  return !(String(val).trim())
}

function stringToEmail() {
  return arguments[0] + '@' + arguments[1]
}

const emailToString = (info) => {
  return info['account'] + '@' + info['domain']
}

function stringToPhone() {
  if (arguments.length > 2) {
    return `${arguments[0]}-${arguments[1]}-${arguments[2]}`
  } else if (arguments.length === 1 && !!arguments[0]) {
    let phone = String(arguments[0]).replaceAll('-', '')
    return `${phone.substring(0, 3)}-${phone.substring(3, 7)}-${phone.substring(7)}`
  }
}

function addressParser() {
  return `${arguments[0]} ${arguments[1]}`
}

function dateParser() {
  let year = arguments[0]
  let month = String(arguments[1]).length < 2 ? '0' + arguments[1] : arguments[1];
  let day = String(arguments[2]).length < 2 ? '0' + arguments[2] : arguments[2];
  return `${year}-${month}-${day}`
}

function checkNumber(event) {
  if (event.key >= 0 && event.key <= 9) {
    return true;
  }
  return false;
}

async function saveByExcel(e) {
  if (!e) {
    alert('에러가 발생하였습니다.\n관리자에게 문의해주세요.')
    return;
  }
  let url = e.data.url
  let params = !e.data.param ? {} : e.data.param

  const response = await http.get(url + '/count', {params: params})
  if (response.data.success) {
    if (response.data.response > 0) {
      const frm = $(`<form action="${url}" method="get"></form>`)
      $('body').append(frm)
      frm.submit()
    } else {
      alert('다운로드 받을 데이터가 없습니다.')
    }
  } else {
    alert('처리중 문제가 발생하였습니다.\n관리자에게 문의해주세요.')
  }


}

// TODO 은행별로 나누어 검증 필요
const checkAccountNumber = account_number => {
  account_number = String(account_number).trim()
  return !!account_number.match(/([0-9,\-]{3,6}[0-9,\-]{2,6}[0-9,\-])/)
}

const userNameChecker = user_name => {
  user_name = String(user_name).trim()
  return !!user_name.match(/^[가-힣]{2,10}$/)
}

const panelInfoChecker = info => {
  let arr = ['birth', 'gender', 'birthType', 'education', 'marry', 'job', 'industry', 'favorite', 'address']
  for (let idx in arr) {
    let key = arr[idx];
    if (!info[key] || info[key].includes('선택') || info[key].includes('undefined')) {
      return false;
    }
    if (key === 'favorite') {
      if (info[key].length <= 0) return false;
    }
  }
  return true;
}

const adminPanelInfoChecker = info => {
  let arr = ['birth', 'gender', 'birthType', 'education', 'marry', 'job', 'industry', 'favorite', 'address']
  for (let idx in arr) {
    let key = arr[idx];
    if (!info[key] || info[key].includes('선택') || info[key].includes('undefined')) {
      console.log(key)
      return false;
    }
    if (key === 'favorite') {
      if (info[key].length <= 0) return false;

    }
  }
  return true;
}

// TODO : 우선은 어드민 페이지만 나중에 바꾼다
const userDtoParser = beforeInfo => {
  let returnInfo = {...beforeInfo}
  if (!!beforeInfo['state'] && !!beforeInfo['city'])
    returnInfo['address'] = addressParser(beforeInfo['state'], beforeInfo['city'])
  if (!!beforeInfo['year'] && !!beforeInfo['month'] && !!beforeInfo['day'])
    returnInfo['birth'] = dateParser(beforeInfo['year'], beforeInfo['month'], beforeInfo['day'])
  returnInfo['phone'] = returnInfo['phone'].replace(/([0-9]{3})([0-9]{4})([0-9]{4})/, "$1-$2-$3")
  if (!!beforeInfo['recommend'])
    returnInfo['recommend'] = returnInfo['recommend'].replace(/([0-9]{3})([0-9]{4})([0-9]{4})/, "$1-$2-$3")
  return returnInfo
}

const getBasicDataFromInputToJson = () => {
  let email = $('input[name=email]')
  let domain = $('input[name=domain]')
  let name = $('input[name=name]')
  let phone = $('input[name=phone]')
  let recommend = $('input[name=recommend]')
  let password = $('input[name=password]')
  let confirm = $('input[name=confirm]')

  return {
    email: !domain.val() ? email.val() : `${email.val()}@${domain.val()}`,
    name: name.val(),
    phone: phone.val(),
    recommend: !recommend.val() ? "" : recommend.val(),
    password: password.val(),
    confirm: confirm.val()
  }
}

const getPanelDataFromInputToJson = () => {
  let gender = $('input[name=gender]:checked')
  let year = $('select[name=year]')
  let month = $('select[name=month]')
  let day = $('select[name=day]')
  let birthType = $('input[name=birthType]:checked')
  let state = $('#state')
  let city = $('select[name=city]')
  let education = $('select[name=education]')
  let marry = $('input[name=marry]:checked')
  let job = $('select[name=job]')
  let industry = $('select[name=industry]')
  let favorite = $('input:checkbox[name=favorite]:checked')

  let favList = []
  favorite.each((index, value) => {
    favList.push(value.value)
  })

  return {
    gender: gender.val(),
    birth: dateParser(year.val(), month.val(), day.val()),
    birthType: birthType.val(),
    address: addressParser(state.val(), city.val()),
    education: education.val(),
    marry: marry.val(),
    job: job.val(),
    industry: industry.val(),
    favorite: favList
  }
}