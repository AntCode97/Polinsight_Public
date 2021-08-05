import http from "./http";

const boardSearch = (e) => {
  // console.log(e)
  // e.preventDefault();
  // alert('dkfjdkfjdkj')
  console.log("thymeleaf javascript test")
  http.get('/post/search?boardType=NOTICE&searchType=TITLE&searchValue=asd')
      .then(res => console.log(res))
      .catch(err => console.log(err))

}