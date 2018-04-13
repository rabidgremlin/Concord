# Concord

https://codex.happyfuncorp.com/styling-and-theming-with-rmwc-react-material-design-d260bec3b644

https://medium.freecodecamp.org/beginners-guide-to-react-router-4-8959ceb3ad58

Simple redux with APIs..
https://www.sohamkamani.com/blog/2016/06/05/redux-apis/

Redux tutorial
https://redux.js.org/basics/example-todo-list



# Start up
docker run --rm -it -p 3306:3306 -e MYSQL_DATABASE=concorddb -e MYSQL_USER=concorduser -e MYSQL_PASSWORD=concordpwd -e MYSQL_ROOT_PASSWORD=password123 -d mysql:5.7

docker exec -it blissful_knuth bash
mysql -u concorduser -p


use concorddb;

select p.phraseId, p.text from phrases p LEFT OUTER JOIN votes v on p.phraseId = v.phraseId WHERE v.userId NOT IN ('Bob');

select * from phrases p LEFT OUTER JOIN votes v on p.phraseId = v.phraseId WHERE p.completed = false AND (v.userId IS NULL OR v.userId <> 'Bob') limit 1;

INSERT into votes values ('38cf518a1dc66eba104eaec4e5e21fa6','Help','Alice');
INSERT into votes values ('38cf518a1dc66eba104eaec4e5e21fa6','Help','Bob');

update phrases set completed = true where phraseId = '082f2ce2d8fa15fcf60189796c126d55';


select p.phraseId, p.text, COUNT(v.userId) AS voteCount from phrases p LEFT OUTER JOIN votes v on p.phraseId = v.phraseId WHERE p.completed = false GROUP BY p.phraseId, p.text;