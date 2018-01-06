pipeline {
  agent none

  environment {
    MAJOR_VERSION = 1
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '1'))
  }

  stages {
    agent {
      label 'slave3'
    }
    stage('build') {
      steps {
        sh 'ant -f build.xml -v'
      }
      post {
        always {
          archiveArtifacts artifacts: 'dist/*.jar', fingerprint: true
        }
      }
    }

    stage('deploy') {
      agent {
        label 'slave3'
      }
      steps {
        sh "cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all"
      }
    }

    stage('unit test') {
      agent {
        label 'slave3'
      }
      steps {
        sh 'ant -f test.xml -v'
        junit 'reports/result.xml'
      }
    }

    stage("Running on Debian") {
      agent {
        docker 'openjdk:8u121-jre'
      }
      steps {
        sh "echo Ran on Debian"
      }
    }
  }


}
