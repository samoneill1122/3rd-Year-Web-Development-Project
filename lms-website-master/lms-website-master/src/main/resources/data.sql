INSERT INTO user(id,username,firstname,lastname,email,password,librarian)
VALUES
(0,'adminUser','Bilbo','Swaggins','bigbilbo@theshire.com','password',true),
(1,'normalUser','Average','McMediocre','normie@ucd.ie','password2',false),
(2,'normalUser2','Average2','McMediocre2','normie2@ucd.ie','password3',false),
(3,'normalUser3','Average3','McMediocre3','normie3@ucd.ie','password4',false);

INSERT INTO book(id,title,author,available,reserved,userid,reservedid,duedate)
VALUES
(0,'Generic Book 1','Skippy McGee',true,false,null,null,null),
(1,'Some Other Book','Lance Butterscotch',false,true,1,2,'2020-03-25'),
(2,'Yet Another Book','Zest McLemon',false,false,2,null,'2020-03-22'),
(3,'Some Other Book 2','Lance Butterscotch',false,false,1,null,'2020-03-25'),
(4,'Random Book','Jerry Jumbo',true,false,null,null,'2020-04-22'),
(5,'Dead Calm','Dunno',false,true,3,3,'2020-03-28'),
(6,'The Astounding Tales of Propeller Man','Oscar Little',false,false,1,null,'2020-03-22'),
(7,'Turf: The Meaning of Life','Bogger OToole',true,true,null,3,'2020-03-30'),
(8,'Kevamans Guide to Being Shit at LOL','Kevin Lalor',false,true,3,2,'2020-04-04'),
(9,'Joe Sheehan: Living Life as a Loser','Joe Sheehan',false,false,2,null,'2020-03-28'),
(10,'Generic Book 2: The Return of the Book','Skippy McGee',false,false,2,null,'2020-04-12');

INSERT INTO loanhistory(rownumber, bookid, userid)
VALUES
(0, 1, 2),
(1, 1, 1),
(2, 2, 1),
(3, 2, 2),
(4, 2, 3),
(5, 3, 1),
(6, 4, 3),
(7, 4, 2),
(8, 5, 2),
(9, 6, 1),
(10, 6, 3),
(11, 7, 3),
(12, 8, 1),
(13, 8, 2),
(14, 9, 2),
(15, 10, 1);

