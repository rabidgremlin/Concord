## Concord Server

### Example API calls

#### Get Auth Token
```
curl -v -X POST http://127.0.0.1:9000/api/sessions --header "Content-Type: application/json" --data '{"userId":"Bob","password":"secret"}'
```

***Extract token from response and use in place of ```XXXXXX``` in the following commands.***

#### Get next phrase to label
```
curl -v -X GET http://127.0.0.1:9000/api/phrases/next --header "Authorization: Bearer XXXXXX"
```

#### Upload phrases
```
curl -v -X POST http://127.0.0.1:9000/api/phrases/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer XXXXXX" --data-binary '@unlabelled_phrases.csv'
```

#### Get all labels
```
curl -v -X GET http://127.0.0.1:9000/api/labels --header "Authorization: Bearer XXXXXX"
```

#### add a label
```
curl -v -X POST http://127.0.0.1:9000/api/labels --header "Content-Type: application/json" --header "Authorization: Bearer XXXXXX" --data '{"label":"Test","shortDescription":"short", "longDescription":"longer description"}'
```

#### upload csv of labels
```
curl -v -X POST http://127.0.0.1:9000/api/labels/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer XXXXXX" --data-binary '@labels.csv'
```

#### Make vote
```
curl -v -X POST http://127.0.0.1:9000/api/phrases/082f2ce2d8fa15fcf60189796c126d55/votes --header "Content-Type: application/json" --header "Authorization: Bearer XXXXXX" --data '{"label":"WhereTaxi"}'
```

#### Get all completed votes
```
curl -v -X GET http://127.0.0.1:9000/api/phrases/completed --header "Authorization: Bearer XXXXXX"
```

#### Get user stats
```
curl -v -X GET http://127.0.0.1:9000/api/stats/user --header "Authorization: Bearer XXXXXX"
```