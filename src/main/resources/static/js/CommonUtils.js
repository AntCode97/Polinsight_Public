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
  return !!pwd.match(/[a-zA-Z0-9~!@#$%^&*()_+-|<>?:;`,{}\]\[/\'\"\\\']{10,16}/);
}
const phoneInputValidator = phone => {
  return !!phone.match(/([0-9]{3})-([0-9]{4})-([0-9]{4})/) || phone.match(/[0-9]{3}[0-9]{4}[0-9]{4}/)
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
  e.target.value = e.target.value.replace(/([0-9]{3})-([0-9]{4})-([0-9]{4})/, "$1$2$3")
}

const blankOrNullChecker = val => {
  return !(val.trim())
}

function stringToEmail() {
  if (arguments.length > 1) {
    return {account: arguments[0], domain: arguments[1]}
  } else if (!!arguments[0]) {
    return {account: arguments[0].split('@')[0], domain: arguments[0].split('@')[1]}
  }
}

function stringToPhone() {
  if (arguments.length > 2) {
    return {first: arguments[0], second: arguments[1], third: arguments[2]}
  } else if (arguments.length === 1 && !!arguments[0]) {
    let phone = arguments[0].replaceAll('-', '')
    return {first: phone.substring(0, 3), second: phone.substring(3, 7), third: phone.substring(7)}
  }
}