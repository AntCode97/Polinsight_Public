const isValidPassword = (passwordValue) => {
  return (/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{10,16}$/).test(passwordValue)
}

const isValidEmail = (emailValue) => {
  return (/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){5,20}@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i).test(emailValue)
}

const isValidPhone = (phoneValue) => {
  return (/^[0-9]{3}[0-9]{4}[0-9]{4}$/).test(phoneValue)
}

const isValidName = (nameValue) => {
  return (/^[가-힣]{2,10}$/).test(nameValue)
}

const emailValidateMessage = (emailValue, msg) => {
  if (emailValue.length > 0) {
    if (!isValidEmail(emailValue)) {
      msg.text('올바른 이메일을 입력해주세요').show()
    } else {
      msg.text('').hide()
      return true;
    }
  } else {
    msg.text('').hide()
  }
  return false;
}
const passwordValidateMessage = (passwordValue, msg) => {
  if (passwordValue.length > 0) {
    if (!isValidPassword(passwordValue)) {
      msg.text('올바른 패스워드를 입력해주세요.').show();
    } else {
      msg.text('').hide();
      return true;
    }
  } else {
    msg.text('').hide();
  }
  return false;
}
const confirmValidateMessage = (confirmValue, passwordValue, msg) => {
  if (confirmValue.length > 0) {
    if (!isValidPassword(confirmValue)) {
      msg.text('올바른 패스워드를 입력해주세요.').show();
    } else if (confirmValue !== passwordValue && isValidPassword(confirmValue) && isValidPassword(passwordValue)) {
      msg.text('비밀번호가 일치하지 않습니다.').show();
    } else if (confirmValue === passwordValue && isValidPassword(confirmValue) && isValidPassword(passwordValue)) {
      msg.text('').hide();
      return true;
    }
  } else {
    msg.text('').hide();
  }
  return false;
}
const phoneValidateMessage = (phoneValue, msg) => {
  if (phoneValue.length > 0) {
    if (!isValidPhone(phoneValue)) {
      msg.text('올바른 핸드폰 번호를 입력해주세요').show()
    } else {
      msg.text('').hide();
      return true;
    }
  } else {
    msg.text('').hide();
  }
  return false;
}
const nameValidateMessage = (nameValue, msg) => {
  if (nameValue.length > 0) {
    if (!isValidName(nameValue)) {
      msg.text('올바른 이름을 입력해주세요').show()
    } else {
      msg.text('').hide();
      return true
    }
  } else {
    msg.text('').hide();
  }
  return false;
}

// email, phone, name, password
const isValidBasicInfo = (emailValue, phoneValue, nameValue, passwordValue) => {
  return isValidName(emailValue) && isValidPhone(phoneValue) && isValidName(nameValue) && isValidPassword(passwordValue)
}

const isValidNameAndPhone = (nameValue, phoneValue) => {
  return isValidName(nameValue) && isValidPhone(phoneValue)
}

// post_password, new_password, new_password_confirm 패스워드 변경시에만 사용
const isValidPasswordForJoin = (password, confirm) => {
  return isValidPassword(password) && isValidPassword(confirm)
}
const isValidPasswordForChange = (before, password, confirm) => {
  return isValidPassword(before) && isValidPassword(password) && isValidPassword(confirm)
}

// gender, birth(yaer, month, day), birthType, address(state, city), education, marry, job, industry, favorite
// 추가 정보 검증시에 사용
const isValidPanelInfo = info => {
  let names = ['birth', 'gender', 'birthType', 'education', 'marry', 'job', 'industry', 'favorite', 'address']
  for (name of names) {
    if (info[name].length <= 0 || !info[name]) {
      return false;
    }
    if (name !== 'favorite') {
      if (info[name].includes('선택') || info[name].includes('undefined')) {
        return false;
      }
    }
  }
  return true;
}
