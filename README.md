# Jenkins Shared Library

This repository contains shared libraries that can be used within my Jenkins server. This will be used across multiple projects to avoid duplication. This has been set up as per [documentation](https://www.jenkins.io/blog/2017/10/02/pipeline-templates-with-shared-libraries/). Additionally, this will ensure I am following [DRY principles](https://en.wikipedia.org/wiki/Don't_repeat_yourself).

## Development

### Dependencies
- Jenkins
- Groovy
- pre-commit

### Project structure

    jenkins-shared-library
    |____vars
    |____src
    |____resources

* /vars: Holds all global shared library code. Files in this directory end with a `.groovy` extension.
* /src: Contains custom groovy functions and can be called in the shared library code.
* /resources: All the non-groovy files that may be required for the pipelines should be managed in this folder.

## Usage

1. Login to your Jenkins instance and navigate to the following: Manage Jenkins -> Configure System
2. Find 'Global Pipeline Libraries' section complete the form with the following:
    | Field              | Value                                                    |
    | ------------------ | -------------------------------------------------------- |
    | Name               | jenkins-shared-library                                   |
    | Default version    | master                                                   |
    | Project Repository | https://github.com/kwame-mintah/jenkins-shared-libraries |
3. Save the changes made
4. Navigate to your Jenkins job and configure the Pipeline script e.g.
    ```
    @Library('jenkins-shared-library@master') _
    executeTerraformPipeline()
    ```
**NOTE:** The first build may fail as Jenkins may not have picked up the necessary build parameters.

## Environment variables
The following global environment variables will need to be set in Jenkins.

### Required
| Environment variable    | Description                                                            |
| ----------------------- | ---------------------------------------------------------------------- |
| JENKINS_GIT_CREDENTIALS | The Jenkins SSH Credentials to use when cloning your git repositories. |

### Pre-Commit hooks

Git hook scripts are very helpful for identifying simple issues before pushing any changes. Hooks will run on every commit automatically pointing out issues in the code e.g. trailing whitespace.

To help with the maintenance of these hooks, [pre-commit](https://pre-commit.com/) is used, along with [pre-commit-hooks](https://pre-commit.com/#install).

Please following [these instructions](https://pre-commit.com/#install) to install `pre-commit` locally and ensure that you have run `pre-commit install` to install the hooks for this project.

Additionally, once installed, the hooks can be updated to the latest available version with `pre-commit autoupdate`.
