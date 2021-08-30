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
  return !!phone.match(/^([0-9]{3})-([0-9]{4})-([0-9]{4})$/) || phone.match(/^[0-9]{3}[0-9]{4}[0-9]{4}$/)
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
  // if (arguments.length > 1) {
  //   return {account: arguments[0], domain: arguments[1]}
  // } else if (!!arguments[0]) {
  //   return {account: arguments[0].split('@')[0], domain: arguments[0].split('@')[1]}
  // }
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

const saveByExcel = e => {
  if (!e) {
    alert('에러가 발생하였습니다.\n관리자에게 문의해주세요.')
    return;
  }
  let url = e.data.url
  const frm = $(`<form action="${url}" method="get"></form>`)
  $('body').append(frm)
  frm.submit()
}

const checkAccountNumber = account_number => {
  account_number = String(account_number).trim()
  return !!account_number.match(/([0-9,\-]{3,6}\-[0-9,\-]{2,6}\-[0-9,\-])/)
}

const userNameChecker = user_name => {
  user_name = String(user_name).trim()
  return !!user_name.match(/^[가-힣]{2,10}$/)
}

const panelInfoChecker = info => {
  let arr = ['birth', 'gender', 'birthType', 'education', 'marry', 'job', 'industry', 'address', 'city', 'state', 'isEmailReceive', 'isSmsReceive', 'year', 'month', 'day']
  for (let idx in arr) {
    if (!info[key] || info[key].includes('선택')) {
      return false;
    }
  }
  return true
}