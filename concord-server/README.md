

##Example API calls

### Get Token
curl -v -X POST http://127.0.0.1:8080/api/sessions --header "Content-Type: application/json" --data '{"userId":"Bob","password":"secret"}'

### Get next phrase to label
curl -v -X GET http://127.0.0.1:8080/api/phrases/next --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb2IiLCJleHAiOjE1MjE3ODQxMTF9.1_8HCeb6RNAAKkjT3JSluwFG7-UPTCDUC3kRsaKMBw8"

### Upload phrases
curl -v -X POST http://127.0.0.1:8080/api/phrases/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb2IiLCJleHAiOjE1MjM1OTU5NzV9.-50EFmI800rh3mAsntKxQy8YqbTL0c4B66E_r9aMATs" --data-binary '@unlabelled_phrases.csv'

### Get all labels
curl -v -X GET http://127.0.0.1:8080/api/labels --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb2IiLCJleHAiOjE1MjM1ODY3OTB9.mPIvq5OIgxWwS7pvNXExjbq1jf1a38wqmtKuvdKKu_0"

### add a label
curl -v -X POST http://127.0.0.1:8080/api/labels --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb2IiLCJleHAiOjE1MjM1ODY3OTB9.mPIvq5OIgxWwS7pvNXExjbq1jf1a38wqmtKuvdKKu_0" --data '{"label":"Test","shortDescription":"short", "longDescription":"longer description"}'

### upload csv of labels
curl -v -X POST http://127.0.0.1:8080/api/labels/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb2IiLCJleHAiOjE1MjM1OTU5NzV9.-50EFmI800rh3mAsntKxQy8YqbTL0c4B66E_r9aMATs" --data-binary '@labels.csv'

