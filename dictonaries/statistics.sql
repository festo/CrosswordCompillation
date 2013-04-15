select min(length),  max(length) from words;
select count(*) from words;
select length as hossz, count(id) as db from words group by length;