pipeline {
    agent none

    stages {
        stage('Maven Clean Install') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-17'
                }
            }
            steps {
                checkout scm
                sh 'mvn -B clean install'
            }
        }
    }

    post {
        success {
            echo 'Docker was initialized and mvn clean install was executed successfully!'
        }
        failure {
            echo 'Something was wrong!'
        }
        always {
            cleanWs()
        }
    }
}
