String terraformDirectory = 'terraform-repository'
String gitCredentials = env.JENKINS_GIT_CREDENTIALS

pipeline {
    agent any

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: 'Development\nStaging\nProduction',
            description: 'Which environment to apply Terraform changes'
        )
        choice(
            name: 'DEPLOYMENT_TYPE',
            choices: 'DEPLOY\nDESTORY',
            description: 'Deployment type against environment'
        )
        string(
            name: 'GIT_BRANCH',
            defaultValue: 'master',
            description: 'Git branch to use for Terraform changes'
        )
        string(
          name: 'JENKINS_GIT_CREDENTIALS',
          defaultValue: gitCredentials,
          description: 'Git credentials to use when accessing repository'
        )
        string(
            name: 'TERRAFORM_GIT_REPO',
            defaultValue: 'git@github.com:kwame-mintah/terraform-digitalocean.git',
            description: 'Git repository containing Terraform files'
        )
    }

    stages {
        stage('Checkout Terraform') {
            steps {
                dir(terraformDirectory) {
                    git credentialsId: gitCredentials, url: "${params.TERRAFORM_GIT_REPO}", branch: "${params.GIT_BRANCH}"
                }
            }
        }

        stage('Terraform Plan') {
            when {
                expression {
                    params.DEPLOYMENT_TYPE == 'DEPLOY'
                }
            }
            steps {
                dir(terraformDirectory) {
                    echo('Running terraform plan')
                }
            }
        }

        stage('Review changes') {
            steps {
                timeout(time: 300, unit: 'SECONDS') {
                    script {
                        env.DEPLOYMENT_APPLY = input message: 'Apply changes?',
                    parameters: [choice(
                        name: 'Apply changes to environment?',
                        description: 'Continue Jenkins job to apply Terraform changes',
                        choices: 'NO\nYES')]
                    }
                }
            }
        }

        stage('Terraform Apply') {
            when {
                expression {
                    env.DEPLOYMENT_APPLY == 'YES'
                }
            }
            steps {
                dir(terraformDirectory) {
                    echo('Running terraform apply')
                }
            }
        }

        stage('Terraform Destory') {
            when {
                expression {
                    params.DEPLOYMENT_TYPE == 'DESTORY'
                }
            }
            steps {
                dir(terraformDirectory) {
                    echo('Running terraform destory')
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            script {
                String buildDescription = "Environment = ${params.ENVIRONMENT}\nDeployment type = ${params.DEPLOYMENT_TYPE}"
                currentBuild.description = buildDescription
            }
        }
    }
}
