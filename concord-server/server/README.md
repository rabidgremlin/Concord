## Example API calls

### Get Token
curl -v -X POST http://127.0.0.1:8080/api/sessions --header "Content-Type: application/json" --data '{"userId":"Bob","password":"secret"}'

### Get next phrase to label
curl -v -X GET http://127.0.0.1:8080/api/phrases/next --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYzMDI5MjZ9.SGIrgw6py4RDd6v2dI5sP_l3N5ajzP5IMoBt_N6uoOA"

### Upload phrases
curl -v -X POST http://127.0.0.1:8080/api/phrases/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYzMDI5MjZ9.SGIrgw6py4RDd6v2dI5sP_l3N5ajzP5IMoBt_N6uoOA" --data-binary '@unlabelled_phrases.csv'

### Get all labels
curl -v -X GET http://127.0.0.1:8080/api/labels --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYzMDI5MjZ9.SGIrgw6py4RDd6v2dI5sP_l3N5ajzP5IMoBt_N6uoOA"

### add a label
curl -v -X POST http://127.0.0.1:8080/api/labels --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYzMDI5MjZ9.SGIrgw6py4RDd6v2dI5sP_l3N5ajzP5IMoBt_N6uoOA" --data '{"label":"Test","shortDescription":"short", "longDescription":"longer description"}'

### upload csv of labels
curl -v -X POST http://127.0.0.1:8080/api/labels/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYzMDI5MjZ9.SGIrgw6py4RDd6v2dI5sP_l3N5ajzP5IMoBt_N6uoOA" --data-binary '@labels.csv'

### Make vote
curl -v -X POST http://127.0.0.1:8080/api/phrases/082f2ce2d8fa15fcf60189796c126d55/votes --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYzMDI5MjZ9.SGIrgw6py4RDd6v2dI5sP_l3N5ajzP5IMoBt_N6uoOA" --data '{"label":"WhereTaxi"}'

### Get all completed votes
curl -v -X GET http://127.0.0.1:8080/api/phrases/completed --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJleHAiOjE1MzYzMDI5MjZ9.SGIrgw6py4RDd6v2dI5sP_l3N5ajzP5IMoBt_N6uoOA"