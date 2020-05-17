import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.2"

project {

    vcsRoot(HttpsGithubComG0t4teamcityCourseCards)

    buildType(id03Firefox)
    buildType(id01FastTest)
    buildType(DeployToStaging)
    buildType(id02Chrome)

    template(Template_1)
}

object id01FastTest : BuildType({
    templates(Template_1)
    id("01FastTest")
    name = "01.Fast Test"
    description = "Browser for this is PhantomJS"

    steps {
        script {
            name = "restore NPM package"
            id = "RUNNER_3"
            scriptContent = "npm install"
        }
    }
})

object id02Chrome : BuildType({
    templates(Template_1)
    id("02Chrome")
    name = "02. Chrome"

    params {
        param("Browsers", "Chrome")
    }

    dependencies {
        snapshot(id01FastTest) {
        }
    }
})

object id03Firefox : BuildType({
    templates(Template_1)
    id("03Firefox")
    name = "03. Firefox"

    params {
        param("Browsers", "Firefox")
    }

    dependencies {
        snapshot(id01FastTest) {
        }
    }
})

object DeployToStaging : BuildType({
    name = "Deploy to Staging"

    vcs {
        root(HttpsGithubComG0t4teamcityCourseCards)
    }

    dependencies {
        snapshot(id02Chrome) {
        }
        snapshot(id03Firefox) {
        }
    }
})

object Template_1 : Template({
    id("Template")
    name = "Template"

    params {
        param("Browsers", "PhantomJS")
    }

    vcs {
        root(HttpsGithubComG0t4teamcityCourseCards)
    }

    steps {
        script {
            name = "restore NPM package"
            id = "RUNNER_3"
            scriptContent = "npm install"
        }
        script {
            name = "Browser Tests"
            id = "RUNNER_4"
            scriptContent = "npm test -- --single-run --browsers %Browsers% --colors false --reporters teamcity"
        }
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            branchFilter = ""
        }
    }
})

object HttpsGithubComG0t4teamcityCourseCards : GitVcsRoot({
    name = "https://github.com/g0t4/teamcity-course-cards"
    url = "https://github.com/g0t4/teamcity-course-cards"
})
