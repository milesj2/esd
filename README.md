# ESD
The below is guidance for various aspects of the project

# code style
For consistency it's important we all follow the same code style, this involves formatting code in a certain way, here is a link to googles Java code style which we will follow throughout the project https://google.github.io/styleguide/javaguide.html pay particular attention to package naming, class naming, function names etc. 

# Derby DB
## creating users
Creating a database user is done in 2 steps execute the 2 below commands as root user to create a new user:

CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user.\<username\>','\<password\>');
create schema <USERNAME>

when creating the schema it needs to be in caps to work no matter username, the 2 above commands should look like below

CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user.admin','pw');
create schema ADMIN;

## merging of work
Branches should not be merged to the main branch until everyone in the group has read over the content of the merge work and has confirmed they agree and like they changes, this achieves 2 things, Stops bad code entering the code base, allows everyone to know how everything is working
## Naming Standard
### Branch naming standard
Naming of branches will be based on the issue number assigned to the work you are doing, and the label on this work. It's important to note branch names shouldn't be too long and spaces should not be used a - will replace a space. The format will be:
\<label\>/\<issuenumber\>-short-desc-of-issue 

IE: feature/7-initilise-basic-web-code

### Commit naming standard
Commit's will follow a similar method, to ensure commits can easily be traced back to an issue in the future all commit's will be tagged by the issue number followed by a space and then a short description IE:
"7 create basic template"

Doing the above will allow easy maintainance and a consistent look throughout the whole project.

### Coding guidlines
This hasn't yet been established

### Project structure
The core structure of the project will be based in com.esd further packages will be created for elements such as controllers, model and sub packages within as seen fitting.

## Coding help

### Protecting pages
It's import to remember about protecting pages from user's that shouldn't have access. The basic principal is to grab the user from the session (See the index.jsp or dashboard.jsp) pages that are only visible to certain user groups need to check the usergroup of the user, this will just be an extra if statement saying 
if user.usergroup != UserGroup.DOCTOR{
    //redirect here
    return;
}

The return statement above is important as it prevent further jsp script execution, these sort of checks should be the first thing within the body tag of the page
