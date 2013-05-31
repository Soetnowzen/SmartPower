Post Mortem Report - Andreas Arvidsson
======================================

## Processes and practices
I've used a bunch of new processes and practices during the development of SmartPower. Some which I'm familiar with, some that are completely new.

### Version control (Git)
For version control, we have used Git. I've been using it for some time now. It has mostly been private projects to keep track of the changes I've made. I did not use a cool branching model or any advance features though. This course has taught me to do so, and I'm very pleased to have increased my proficiency at using this excellent tool. In future project, git will still be my main tool for version control.

In this project, we have used the popular branching model "git flow", as described [here](http://nvie.com/posts/a-successful-git-branching-model/). 

For me, using git was really efficient time-wise, since I knew most of the commands before and it really helped a lot when tracking down bugs (just browse through the commit history). If development continued, it would be even more awesome, since we can compare releases, do nightly builds and all that good stuff. Even if I was forced to learn the git commands again, it would probably be worth it in the long run.

#### Advantage
There's a great bunch of advantages using version control and git, not only when developing as a team but also for individual use. First and foremost, it allows me to go back through all the commits to see what has been changed. This is very useful when something is not functioning correct, and you want to find out what (and who caused it, which is most of the times me). Not only this though, but you can easily keep track of all versions of the program (if using tags correctly), have access to the code base on all computers, synchronize code between collaborators, experiment with the code through branches, and much more.

The branching model (git flow) chosen gives numerous advantages, all listed on the website. My experience has been largely positive, even though we haven't used all aspects of it (hotfix-branches for example).

#### Disadvantage
There are some minor disadvantages to git obviously. The most clear is that it adds a bit of overhead timewise, since every non-trivial change should be committed. There's also some conflicts that can occur when merging and rebasing which can be a bit tedious to solve. Furthermore, it is very hard to remove a commit - so if you accidentally push your password somehow, you're basically doomed. However, these disadvantages are overshadowed by the advantages of using git. The extra time spent learning and integrating git into the workflow is well worth it, especially if problems arise later in the developmeny.

I haven't seen or experienced many disadvantages with the git flow branching model. It might be too complicated for projects of this timelength, since many features are not used. We only did one release and no hotfixes.

### Agile Development (Scrum)
I have never worked on a larger project with someone else, so learning about scrum has been low priority for me until now. It has proven to be a very neat workflow, although it could perhaps have been executed better if we had more experience with using it though. I can't really say anything about efficiency, since this is the only method I am familiar with. I can't imagine the project would've been better off using another method though.

#### Advantage
Using agile development methods like SCRUM gives you alot of flexibility while having the customer in focus. By mainly developing features that the user wants (that answers user stories), the finshed product will be done sooner and with more user-oriented features. All developers are kept up-to-date when attending the meetings; this provides a greater understanding of the project and where it's heading.

#### Disadvantage
Scrum obviously requires some extra resources to be successfully executed, not only because of the regular meetings but also since testing is involved at every step. Also, it can be difficult to organize the project if the goals are not clear and concise, and since new goals and user stories are introduced all the time, the project might never even get finished.

### Unit and Acceptance testing
Testing is something I've basically ignored in the past, but the importance of it is more clear now after this course. We did not do test-driven development, but rather constructing tests after the code was written. This was much due to that the design was not 100% clear, so the tests we wrote would have to be rewritten. Since we did not have much time due to the other course, we had to cut this overhead. It would've been interesting to try TDD though, since it seems promising.

Since this was a small project, the overhead of writing tests made it not very efficient time wise. I believe that if we had more time, then the real power of testing would shine.

#### Advantage
Using unit- and acceptance tests allowed us to be certain a part of the code worked. It took a bit of time, and the time was probably not regained in this small project, but if we were to continue the development we would be happy there are tests to rely on instead of tedious bug searching each time we change the code.

#### Disadvantage
The only obvious disadvantage is that it takes time to do. This time is probably regained when you don't have to search for weird bugs though.
In this project, I had some problems constructing unit tests for some classes which mainly consists of private methods. An example of this is GraphView, whose calculations are only shown when rendering the view.

### Documentation
We noticed that when using SCRUM, documentation was not as important since we had good communication in the group during the meetings. We did add some javadocs, as well as comments of course, when the code was largely finished.

Documentation, like testing, was not very efficient time-wise for this small project. If, however, development continues, we will be glad we have it. Documentation is for the future, not the present (unless a customer wants it, of course).

#### Advantage
Documentation is a great tool, not only when you work as a team but also when you work alone. Code is read more often than it is written, so it makes sense to make it as clear as possible. Explaining how methods work (using tools as JavaDoc) allows both you and the other collaborators to remember what the method actually does, months after if was last used. It is especially important when developing open source projects, so that other people can contribute more easily.

#### Disadvantage
Documentation takes time - a lot of time. This demands more resources, which might not be available or might be needed somewhere else. If the communication between developers are good enough, documentation should not be prioritized (especially not in the early stages of development, since it's easy to forget to change the documentation when you change the code). Sometimes, it is more important to provide a finished product quicker, unless the customer actually needs the documentation.

## Time
We have not kept track of the amount of time spent on the project, so it becomes tricky to evaluate how much people actually have spent. However, I feel that the following list represent the time distribution quite well:

1. Andreas Arvidsson (me) - 130 hours
2. Johan Bregell - 110 hours
3. Henning Phan - 100 hours
4. Sebastian Lagerman - 90 hours
5. Johan Swetzén - 80 hours

This is partly based on number of commits as well as my general impression I got at the scrum meetings. It might be totally off though, since much time might have been spent "behind the scenes" - like learning android and how git works for some people.

## My thoughts

### Project
I am very happy with my contribution to the project and the codebase, as well as the others contribution. I have made the most commits in the project (checked with the "git shortlog" command) - however, this does not necessarily mean that I was the one contributing the most. I was the one in charge of the majority of merging (mainly feature branches into develop at scrum meetings), and I usually have pretty few changes each commit (easier to keep track of things for me that way) - this naturally leads to more commits. Furthermore, I was the only one who has done extensive work on the Android platform, so I did not need to spend much time searching for fundamental aspects in this field.

I think that everyone in the project has done excellent work. The shortcomings of the project was probably that there was hard to have regular scrum meetings, since we all had bachelor's thesis to work on. Sometimes, people would come late to the meetings due to either traffic, other meetings or laziness (myself included). Relying on Skype was discouraged in our group, but we were sometimes forced to use it in order to have our daily meeting. I think we managed it OK though, but it's definitively something that could have been done better.

Most other things worked nice - everyone stuck with the git branching model we chose (even though some problems was encountered) and the project was a success.

The main annoyance was the usage of Eclipse and the weird errors that magically appeared when we used some external libraries. Switching to Android Studio helped a lot.

### Course
The course have been one of the most valuable for me personally, since as as software developer, the techniques taught will help me on a daily basis. We don't have many project courses either, and I believe that's a big mistake.

As for improvements for the course, I suggest that you have a few more lectures. It was really easy to miss the last ones since they were not every monday. There were both slides and videos (which was awesome, so keep doing that), which greatly limited the impact of missing the lectures.

Me, along with a few people in the group, switched to Android Studio half way through the course. It was a joy to work with, so I recommend you have that as an option to Eclipse (some people might prefer Eclipse though) in later courses.

## Future projects
I believe the core methodology taught in this course will be applied in future projects for me if I have a say in it. Version control (be it git or subversion) is a must of course, and I would very much prefer scrum as well. One thing I would like to try is code review - that is, someone reviews my code and I review some of the collaborators code before they get merged into the main branches. I feel like this is a nice alternative to extreme programming (where two programmers work on one computer), which I'm not a big fan of (even though I haven't tried it much). Also, the scrum meetings maybe should not be every day, at least not for a part-time project like this.

Documentation and testing was the two processes I felt was not so important in this particular project if the development is halted now after the course. This was expected, since it's just a project for a course and I have no personal attachments to it. However, the project I do participate in in the future, documentation and testing will play a key part.
