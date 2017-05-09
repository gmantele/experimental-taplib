# experimental-taplib
Experimental restructuration of the taplib repository.
# What is Gradle?

[Gradle](https://gradle.org) is a powerful and flexible build tool.
On the contrary to [Apache Ant](https://ant.apache.org/) but similarly as
[Maven](https://maven.apache.org/), this tool is able to manage all kind of
dependencies. Basically, you just have to specify which libraries your project
does need and it will fetch them for you.

_For a better description of Gradle and examples, please check out the following
URL: https://gradle.org/_

# How to use it?

## Installation

On Debian:

    sudo apt-get install gradle


For other operating system or for a manual installation, you should
consult the official Gradle webpage:
[Gradle Installation](https://gradle.org/install).

## In command line

All task to execute is performed by suffixing the command `gradle`.

Here are some useful and common tasks:

* `gradle help` : get an help about the `gradle` command.

* `gradle projects` : list and describe this project and its available
                      sub-projects if there are any.

* `gradle tasks` : display and describe all the available tasks in the current
                   project/directory.

* `gradle build` : build the whole project (and sub-projects). In a Java
                   project, this builds all the classes and generate a JAR with
                   all the built classes of this project.

* `gradle clean` : remove all files and directories created by the
                   `gradle build` command.

* `gradle test` : execute all test classes (e.g. JUnit test cases) defined in
                  this project (and sub-projects).

* `gradle eclipse` : for projects applying the Eclipse plugin (which is the case
                     in this project), this task configure the project as an
                     Eclipse project. Concretely, at least a `.classpath` file
                     and a `.project/` directory are created.

* `gradle cleanEclipse` : remove all files and directories created by the
                          `gradle eclipse` command.

## In Eclipse

You have at least two options.

The first one is to generate the Eclipse project using the `gradle eclipse`
command and then to import this project in your Eclipse project
(i.e. File -> Import... -> Existing Projects into Workspace). Theoretically,
the dependencies are already resolved and the classpath is already set
correctly. So, you can now work on the code, but to compile the project you
will have to go back on command line and execute `gradle build`.

The second option is to import the project directly as a Gradle project. Thus,
you can execute any task you want directly from Eclipse using suitable views.
For that, you must install an additional plugin:
  - Go to the menu Help -> Eclipse Marketplace....
  - Search for 'gradle'
  - You must install 'Buildship Gradle Integration' and optionally
    'Minimalist Gradle Editor' for a nicer build file editor.

You may have to restart your Eclipse. Once done, select
File -> Import... -> Gradle/Existing Gradle Project. You will be asked for a
root directory. This could be the directory of the main project or of one of its
sub-project. If you select the main project, all sub-projects will be imported.
If you select just a sub-project, you should know that other dependent
sub-projects will be also imported. When going to the next step, you are asked
for a Gradle distribution: the default one ('Gradle wrapper') should be enough.
Go to the next step and you should see all (sub-)projects detected by Eclipse
and that will be imported if you click on Finish. Once done, you should now have
additional projects in your workspace.

'Buildship Gradle Integration' provides two additional Eclipse views:
'Gradle Tasks' and 'Gradle Execution'. The first one is equivalent to the
command `gradle tasks`. The second one is only displayed while executing a task.
You should also notice the addition of an item in the contextual menu of you
Gradle project(s): 'Gradle'/'Refresh Gradle project'. This is particularly
useful when you updated the Gradle build file to update the 'Gradle Tasks' view
; there is no automatic update from Eclipse.

_For more information and documentation about 'Buildship Gradle Integration',
check out this link: https://projects.eclipse.org/projects/tools.buildship._






































