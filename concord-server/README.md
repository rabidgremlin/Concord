

##Example API calls

### Get Token
curl -v -X POST http://127.0.0.1:8080/api/sessions --header "Content-Type: application/json" --data '{"userId":"Bob","password":"secret"}'

### Get next phrase to label
curl -v -X GET http://127.0.0.1:8080/api/phrases/next --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb2IiLCJleHAiOjE1MjExNTgyMTh9.mGXI4dPU78eHXcOzy8duoVXHQXTSTXbkTyVGYPwusic"

