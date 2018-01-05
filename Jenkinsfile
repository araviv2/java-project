pipeline {
  agent any

  environment {
    MAJOR_VERSION = 1
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '1'))
  }

  stages {
    stage('build') {
      steps {
        sh 'ant -f build.xml -v'
      }
    }

    stage('unit test') {
      steps {
        sh 'ant -f text.xml -v'
        junit 'reports/result.xml'
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'dist/*.jar', fingerprint: true
    }
  }
}
