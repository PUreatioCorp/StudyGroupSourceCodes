document.querySelector("#translate").addEventListener('click', translateSentence)

function translateSentence() {

  axios.post('modify your api-gateway url', {
    text: btoa(document.querySelector("#sourceArea").value)
  }).then(res => document.querySelector("#destArea").value = decodeURIComponent(escape(atob(res.data))))
}
