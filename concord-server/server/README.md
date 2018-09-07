

##Example API calls

### Get Token
curl -v -X POST http://127.0.0.1:8080/api/sessions --header "Content-Type: application/json" --data '{"userId":"Bob","password":"secret"}'

### Get next phrase to label
curl -v -X GET http://127.0.0.1:8080/api/phrases/next --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYyOTc4MzJ9.TLF77eyg_P7KutuiJm6p7nNGzn1TWxueady8vDeckNs"

### Upload phrases
curl -v -X POST http://127.0.0.1:8080/api/phrases/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYyOTc4MzJ9.TLF77eyg_P7KutuiJm6p7nNGzn1TWxueady8vDeckNs" --data-binary '@unlabelled_phrases.csv'

### Get all labels
curl -v -X GET http://127.0.0.1:8080/api/labels --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYyOTc4MzJ9.TLF77eyg_P7KutuiJm6p7nNGzn1TWxueady8vDeckNs"

### add a label
curl -v -X POST http://127.0.0.1:8080/api/labels --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYyOTc4MzJ9.TLF77eyg_P7KutuiJm6p7nNGzn1TWxueady8vDeckNs" --data '{"label":"Test","shortDescription":"short", "longDescription":"longer description"}'

### upload csv of labels
curl -v -X POST http://127.0.0.1:8080/api/labels/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYyOTc4MzJ9.TLF77eyg_P7KutuiJm6p7nNGzn1TWxueady8vDeckNs" --data-binary '@labels.csv'

### Make vote
curl -v -X POST http://127.0.0.1:8080/api/phrases/082f2ce2d8fa15fcf60189796c126d55/votes --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb2IiLCJleHAiOjE1MjQxODgwNTl9.v1psWokees7Ykk93zHlZvcvxf-GelJ5GZxx3wLYSWPs" --data '{"label":"WhereTaxi"}'
