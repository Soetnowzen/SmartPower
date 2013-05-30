Post Mortem Report - Andreas Arvidsson
======================================

## Processes and practices
I've used a bunch of new processes and practices during the development of SmartPower. Some which I'm familiar with, some that are completely new.

### Git
Git is a tool I've been using for some time now. It has mostly been private projects to keep track of the changes I've made. I did not use a cool branching model or any advance features though. This course has taught me to do so, and I'm very pleased to have increased my proficiency at using this excellent tool.

In this project, we have used the popular branching model "git flow", as described [here](http://nvie.com/posts/a-successful-git-branching-model/). 

#### Advantage
There's a great bunch of advantages using git, not only when developing as a team but also for individual use. First and foremost, it allows me to go back through all the commits to see what has been changed. This is very useful when something is not functioning correct, and you want to find out what (and who caused it, which is most of the times me). Not only this though, but you can easily keep track of all versions of the program (if using tags correctly), have access to the code base on all computers, synchronize code between collaborators, experiment with the code through branches, and much more.

The branching model (git flow) chosen gives numerous advantages, all listed on the website. My experience has been largely positive, even though we haven't used all aspects of it (hotfix-branches for example).

#### Disadvantage
There are some minor disadvantages to git obviously. The most clear is that it adds a bit of overhead timewise, since every non-trivial change should be committed. There's also some conflicts that can occur when merging and rebasing which can be a bit tedious to solve. Furthermore, it is very hard to remove a commit - so if you accidentally push your password somehow, you're basically doomed.

I haven't seen or experienced many disadvantages with the git flow branching model.

### Scrum
I have never worked on a larger project with someone else, so learning about scrum has been low priority for me until now. It has proven to be a very efficient tool.


### Unit and Acceptance testing
Testing is something I've also ignored in the past, but the importance of it is more clear now after this course.

### Agile Development

## Time
We have not kept track of the amount of time spent on the project, so it becomes tricky to evaluate how much people actually have spent. However, I feel that the following list represent the time distribution quite well:
1. Andreas Arvidsson (me) - 130 hours
2. Johan Bregell - 110 hours
3. Henning Phan - 100 hours
4. Sebastian Lagerman - 80 hours
5. Johan Swetzén - 70 hours

This is partly based on number of commits as well as my general impression I got at the scrum meetings.

## My thoughts
I am very happy with my contribution to the project and the codebase, as well as the others contribution. I have made the most commits in the project (checked with the "git shortlog" command) - however, this does not necessarily mean that I was the one contributing the most. I was the one in charge of the majority of merging (mainly feature branches into develop at scrum meetings), and I usually have pretty few changes each commit (easier to keep track of things for me that way).

## Future projects
I believe the core methodology taught in this course will be applied in future projects for me if I have a say in it. Version control (be it git or subversion) is a must of course, and I would very much prefer scrum as well. One thing I would like to try is code review - that is, someone reviews my code and I review some of the collaborators code before they get merged into the main branches. I feel like this is a nice alternative to extreme programming (where two programmers work on one computer), which I'm not a big fan of (even though I haven't tried it much). Also, the scrum meetings maybe should not be every day, at least not for a part-time project like this.
