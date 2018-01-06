pipeline {
  agent none

  environment {
    MAJOR_VERSION = 1
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '1'))
  }

  stages {
    stage('build') {
      agent {
        label 'worker1'
      }
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
        label 'worker1'
      }
      steps {
        sh "cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all"
      }
    }

    stage('unit test') {
      agent {
        label 'worker1'
      }
      steps {
        sh 'ant -f test.xml -v'
        junit 'reports/result.xml'
      }
    }

    stage("Running on Debian") {
      agent {
        docker {
          image 'openjdk:8u151-jre'
          label 'worker1'
        }
      }
      steps {
        sh "wget http://192.168.33.10/rectangles/all/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
      }
    }

    stage('Promote to Green') {
      agent {
        label 'worker1'
      }
      steps {
        sh "cp /var/www/html/rectangles/all/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green"
      }
    }
  }


}
