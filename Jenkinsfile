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
        sh "if [ ! -d '/var/www/html/rectangles/all/${env.BRANCH_NAME}' ]; then mkdir /var/www/html/rectangles/all/${env.BRANCH_NAME}; fi"
        sh "cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/${env.BRANCH_NAME}"
      }
    }

    stage('Say Hello') {
      agent {
        label 'worker1'
      }

      steps {
        sayHello 'Awesome Student!'
      }
    }

    stage('Git Information') {
      agent {
        label 'worker1'
      }

      steps {
        echo "My Branch Name: ${env.BRANCH_NAME}"

        script {
          def myLib = new linuxacademy.git.gitStuff();
          echo "My Commit: ${myLib.gitCommit("${env.WORKSPACE}/.git")}"
        }
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
        sh "wget http://192.168.33.10/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
      }
    }

    stage('Promote to Green') {
      agent {
        label 'worker1'
      }
      when {
        branch 'master'
      }
      steps {
        sh "cp /var/www/html/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green"
      }
    }

    stage('Promote Development Branch to Master') {
      agent {
        label 'worker1'
      }
      when {
        branch 'development'
      }
      steps {
        echo "Stashing any local changes"
        sh 'git stash'
        echo "Checking out Development Branch"
        sh 'git checkout development'
        sh 'git pull origin'
        echo "Checking out Master Branch"
        sh 'git checkout master'
        echo "Merging Development into Master Branch"
        sh 'git merge development'
        echo "Pusing to Origin Master"
        sh 'git push origin master'
        echo "Tagging the release"
        sh "git tag rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
        sh "git push origin rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
      }
      post {
        success {
          emailext(
            subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Development Promoted to Master",
            body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Development Promoted to Master":</p>
            <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
            to: "aravipati11@gmail.com"
          )
        }
      }
    }
  }
  post {
    failure {
      emailext(
        subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Failed!",
        body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Failed!":</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        to: "aravipati11@gmail.com"
      )
    }
  }
}
