update life_manager.exercise
set image_url = 'https://training.fit/wp-content/uploads/2020/03/arnold-dips.png'
where id = (select id from life_manager.exercise e where e.name = 'Tricep Dips');

update life_manager.exercise
set image_url = 'https://www.gymshark.com/_next/image?url=https%3A%2F%2Fimages.ctfassets.net%2F8urtyqugdt2l%2F56AQL6OUTCfTjfSzcmkl3r%2Fe86840c8ea82de5a79f0439fd15cff7c%2Fgoblet-squat.jpg&w=3840&q=85'
where id = (select id from life_manager.exercise e where e.name = 'Squat');

update life_manager.exercise
set image_url = 'https://hips.hearstapps.com/hmg-prod/images/running-is-one-of-the-best-ways-to-stay-fit-royalty-free-image-1036780592-1553033495.jpg?resize=640:*'
where id = (select id from life_manager.exercise e where e.name = 'Running');

update life_manager.exercise
set image_url = 'https://training.fit/wp-content/uploads/2020/02/liegestuetze.png'
where id = (select id from life_manager.exercise e where e.name = 'Push-Up');

update life_manager.exercise
set image_url = 'https://liftmanual.com/wp-content/uploads/2023/04/pull-up.jpg'
where id = (select id from life_manager.exercise e where e.name = 'Pull-Up');

update life_manager.exercise
set image_url = 'https://www.inspireusafoundation.org/file/2023/07/plank-benefits.png'
where id = (select id from life_manager.exercise e where e.name = 'Plank');

update life_manager.exercise
set image_url = 'https://liftmanual.com/wp-content/uploads/2023/04/dumbbell-standing-overhead-press.jpg'
where id = (select id from life_manager.exercise e where e.name = 'Overhead Press');

update life_manager.exercise
set image_url = 'https://training.fit/wp-content/uploads/2020/03/bergsteiger-fitnessband.png'
where id = (select id from life_manager.exercise e where e.name = 'Mountain Climbers');

update life_manager.exercise
set image_url = 'https://weighttraining.guide/wp-content/uploads/2016/10/Dumbbell-Lunge-resized-fixed.png'
where id = (select id from life_manager.exercise e where e.name = 'Lunges');

update life_manager.exercise
set image_url = 'https://spotebi.com/wp-content/uploads/2014/10/jump-rope-exercise-illustration.jpg'
where id = (select id from life_manager.exercise e where e.name = 'Jump Rope');

update life_manager.exercise
set image_url = 'https://training.fit/wp-content/uploads/2020/03/kreuzheben-gestreckte-beine.png'
where id = (select id from life_manager.exercise e where e.name = 'Deadlift');

update life_manager.exercise
set image_url = 'https://sweatcentral.com.au/cdn/shop/products/spinbike-exercisebike-C101gymbike.jpg?v=1752817289'
where id = (select id from life_manager.exercise e where e.name = 'Cycling');

update life_manager.exercise
set image_url = 'https://experiencelife.lifetime.life/wp-content/uploads/2021/02/Fitness-Fix-Burpee.jpg'
where id = (select id from life_manager.exercise e where e.name = 'Burpee');

update life_manager.exercise
set image_url = 'https://training.fit/wp-content/uploads/2018/12/bizepscurls.png'
where id = (select id from life_manager.exercise e where e.name = 'Bicep Curl');

update life_manager.exercise
set image_url = 'https://training.fit/wp-content/uploads/2018/11/bankdruecken-flachbank-langhantel.png'
where id = (select id from life_manager.exercise e where e.name = 'Bench Press');