



## Set up Test NLP endpoint
docker run -p 5000:5000 rasa/rasa_nlu:latest-full

curl -XPOST localhost:5000/train?project=taxibot -d @taxibotdata.json
curl localhost:5000/status

curl -XPOST localhost:5000/parse -d '{"q":"i like pink", "project": "taxibot"}'
curl -XPOST localhost:5000/parse -d '{"q":"get me my taxi", "project": "taxibot"}'
curl -XPOST localhost:5000/parse -d '{"q":"I live at 45 tinkerbell lane", "project": "taxibot"}'