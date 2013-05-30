Post mortem - Henning Phan
==========================
*	[processes and practices]
*	[Time spent]
*	[For each technique and practice we used]
	*	[Advantage]
	*	[Disadvantage]
	*	[Efficency given time]
	*	[Will I use it in future projects]
	*	[using vs not using]
*	[What worked well in the project]
*	[What did not work well in the project]
*	[How was teamwork]
*	[What would we do different]

* * *

<h2 id="1">Which processes and practices did you use in your project?</h2>
git flow, version control, scrum, documentation, empirical process control, agile, refactoring, collective code ownership, simple design, design improvement, testing and user stories. Some are subset of others but I wanted to highlight them specially.

<h2 id="2">Approximatey, how much time was spent (in total and by each group member) on the steps/activities involved as well as for the project as a whole?</h2>

| Name    | Git	|Pivot|Scrum	| Develop	| Research|error|
|:-------:|:---:|:---:|:-------:|:--------:|:-------:|:---:|
|Andreas  	|4	|3	|8		|100		|4		|5	|
|Bregell	|4	|3	|8		|60		|8		|20	|
|Lagerman	|5	|3	|8		|55		|5		|15	|
|Swetzén	|4	|5	|10		|30		|6		|15	|
|Henning	|4	|2	|8		|50		|20		|14	|
|Total	|21	|15	|42		|295		|43		|69	|
The sum of total becomes 485 hours.
It's hard to estimate as most worked independently at home and we were all doing different tasks, same had more experience in that field and some had easier tasks.

<h2 id="3">For each of the techniques and practices used in your project you should answer all the questions</h2>

Questions:

1 What was the advantage of this technique based on your experience in this assignment?  

2 What was the disadvantage of this technique based on your experience in this assignment?  

3 How efficient was the technique given the time it took to use?  

4 In which situations would you use this technique in a future project?  

5 In which situations would you not use this technique in a future project?  

6 If you had the practice/technique in a part of the project and not the entire project, how was using it compared to not using it?  

###Git version control###
1 The advantages is many, multiple developers can contribute at the same time, can work at different geographical locations. Branching, everyone can code without interfering each other. Working offline, that you can revert to an arbitrary commit if you messed up. We used [a succesful git branching model](http://nvie.com/posts/a-successful-git-branching-model/ "git") and after reading it we enforced this model to our other course. It was enlightening and works well with the scrum model to always have a working application which is our develop branch  
2 The disadvantages are nothing compared to what you gain but I find it time consuming to commit every step and that people push bad commits. Lastly is merging and rebasing, not always but some can be difficult to clean up  
3 Very efficient,  quite easy to use and it goes fast to learn the basics, from there you can expand your knowledge little by little  
4 Every project  
5 The exception would be small projects that only have one functionality  
6 To be without version control is like not having electricity

###Scrum###
1 Advantage is that we have a pool we can choose our work from. Also because all our ideas was summarized on the pivotaltracker it helped me visualize the product, it's also a consistent way to get information. I also like that we are helping the customer to give them what they doesn't know they want yet. It's smart to have a working application after every sprint. It gives a feeling that we are building with Lego and adding features on top which is quite convenient with Git and branches.  
2 Didn't really understand the point system at first. The daily meetings I was forced to participate \*early\* in the morning, three meetings a week would have been sufficient. Short meetings, only 15 minutes they said, someone always had to pull a joke.  
3 It was an efficient way to keep track of tasks and keep our priorities straight. In my other course that's where we failed a little. It gave us a slow start because some user stories distributed were dependent on others not yet implemented. Was not hard to grasp the concept  
4 Every project that has more than a few tasks. My favorite way to work is to write down all the tasks and then erase them one after another when finished. Scrum is all that  
5 Depends on my teammates and how efficient the meetings would be. Also Im not to keen to have multiple website to visit to keep track with one project. Google drive, github, pivotaltracker  
6 In the end we worked so close to each other that we didnt use pivotal tracker instead we just requested each other directly in a loose form of scrum. Believe this was possible because of the small team size  

###Agile###
1 Advantages that I noticed is the immediate feedback, we can handle problems early instead of letting it hide in the code. By having more quality assurance the finished product will have less bugs 
2 As said in scrum, if a user story is dependent on another I cant work. There is almost no point in documentation as you might refactor the code at any moment. As the code is only written with the short vision of only solving the problem at hands Im having a hard time to see the code grow resulting in unnecessary refactoring, though I doubt a project with such complexity is encountered often.  
3 Can't say there was a learning curve to this because I have mostly coded alone on my own projects giving me the role of decisionmaker  
4 My own private projects  
5 I believe projects without significant complexity can be done equally as good or better with a non agile method. Also I wouldn't want to work agile if the team doesn't have the agile mindset.  
6  No comment  

###Unit Testing###
1 Too find errors in code, especially if someone changes a particular function  
2 It's impossible to completely test a program. Time consuming  
3 We were 5 so we hade the manpower to build some testcases  
4 I always underestimate testing because I always assume I can write good enough code. Should be done in all projects  
5 No comments  
6 Had a bug that a constructor was misbuilt. An easy unit test would surely have caught it in this case

<h2 id="4">What worked well in how you worked in this project?</h2>
Conversation, we knew each other from before and we have a "smsgrupp". Work distribution was great with scrum and the close interactions. It also didn't take long time until a requested method would be implemented. Git is also a hightlight in this project.  

<h2 id="5">What did not work well in how you worked in this project?</h2>
We had problems with scrum in the beginning because some user stories was dependent on other tasks which someone else was developing, this lead to a slow start. Some minor information losses that could save time e.g. Coding a request that was already implemented in the API.  
Too many scrum meetings and too early in the morning. Didn't understand why we persisted with this time as 1-3 members daily didn't show up to the meeting place in time, the late members would participate using skype.  

Lastly is the task to implement user stories that depend on unimplemented functions someone else is developing. Maybe they should have coded the user story together, extreme programming, or one should have taken care of both tasks. Im also not so keen an being dependent on a server that's under development.

<h2 id="6">6. How did you work together as a group in the project? What worked and not in your interaction(s)?</h2>
We knew eachother from before and overall it worked well, we even requested methods to be implemented to another. Some miscommunication happened that happened because of a request to read the API which I believe no one did.

<h2 id="seven">What would you do differently in a future but similar project?</h2>
7. I did like the concept of scrum and pivotal tracker but I want the feature to have user stories locked if they depend on a yet unimplemented task. So the only user stories are those we can work on in parallell.

I have been thinking of the idea of having code roles, letting one developer be in charge of a group of classes.

Lastly I been thinking of merging waterfall with agile. Think waterfall with 3 iterations. First we create a prototyp to learn as much as possible of all the problems we didn't think of, then we start over but learn from our mistakes. Lastly if needed redesign it again for finishing touch.

I would like to mention that I have been reminded once again of the importance of unit testing. Had good experiences with Git and bad moments with Eclipse. But what I believe is the best is the experience, I have learned the most by experiencing it here in this course and not from reading it

Author: Henning