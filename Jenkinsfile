pipeline {
  agent {
    label 'master'
  }

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

    stage('deploy') {
      steps {
        sh 'cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all'
      }
    }

    stage('unit test') {
      steps {
        sh 'ant -f test.xml -v'
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
