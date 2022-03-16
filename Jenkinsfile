pipeline {
    agent {
        node {
          label "aws-slave"
        }
    }
    environment {
        PROJECT_NAME = "caceSDG"
        IMAGE = "570757229952.dkr.ecr.eu-west-1.amazonaws.com/datalab-scala-sbt:latest"
        REGISTRY_URL = "https://570757229952.dkr.ecr.eu-west-1.amazonaws.com"
        REGISTRY_CREDENTIALS_ID = "ecr:eu-west-1:aws-dev-account"
        HOME = "/var/lib/jenkins/workspace"
        APP_VERSION = sh(script: "sbt -Dsbt.log.noformat=true version | awk 'END {print \$NF}'", returnStdout: true).trim()
        SCALA_BINARY_VERSION = sh(script: "sbt -Dsbt.log.noformat=true scalaBinaryVersion | awk 'END {print \$NF}'", returnStdout: true).trim()
    }
    triggers {
        pollSCM('* * * * 1-5')
    }
    stages {
        stage("Build & test the project") {
            stages {
                stage("Test & Build") {
                    steps{
                        withDockerRegistry([credentialsId: "${REGISTRY_CREDENTIALS_ID}", url: "${REGISTRY_URL}"]) {
                            withDockerContainer(args: "-u root -v ${HOME}/.sbt:/root/.sbt -v ${HOME}/.ivy2:/root/.ivy2", image: "${IMAGE}") {
                                echo "Test & Build"
                                sh "sbt clean assembly"
                            }
                        }
                    }
                }
                stage("Deploy") {
                    parallel {
                        stage("Development"){
                            when {
                                branch "develop"
                            }
                            steps {
                                 withCredentials([[$class: "AmazonWebServicesCredentialsBinding", credentialsId: "mdm-developers-aws-dev-account"]]) {
                                    s3Upload(bucket: "kering-datalake-dev-data", path: "projects/mdm/${PROJECT_NAME}/", workingDir:"dataset-convertor/target/scala-${SCALA_BINARY_VERSION}", includePathPattern: "*-${APP_VERSION}.jar");
                                }
                            }
                        }
                        stage("Production"){
                            when {
                                branch "master"
                            }
                            steps {
                                withCredentials([[$class: "AmazonWebServicesCredentialsBinding", credentialsId: "mdm-developers-aws-prod-account"]]) {
                                    s3Upload(bucket: "kering-datalake-prod-data", path: "projects/mdm/${PROJECT_NAME}/", workingDir:"dataset-convertor/target/scala-${SCALA_BINARY_VERSION}", includePathPattern: "*-${APP_VERSION}.jar");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            slackSend (color: "#00FF00", message: ":heavy_check_mark: SUCCESSFUL : Job ${env.JOB_NAME} has succeed for branch: ${env.BRANCH_NAME} with Build number [${env.BUILD_NUMBER}]")
        }

        failure {
            slackSend (color: "#FF0000", message: ":interrobang: FAILED: Job ${env.JOB_NAME} has failed for branch: ${env.BRANCH_NAME} with Build number [${env.BUILD_NUMBER}]")
        }
    }
}
