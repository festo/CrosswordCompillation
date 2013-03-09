select min(length),  max(length) from hun_eng;
select length as hossz, count(id) as db from hun_eng group by length;

select min(length),  max(length) from eng_hun;
select length as hossz, count(id) as db from eng_hun group by length;