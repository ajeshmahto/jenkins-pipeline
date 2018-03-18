stage 'Git download'
node {
   git branch: 'spring-boot-app',
       url: 'https://github.com/ajeshmahto/spring-jenkins.git'
       
}
stage 'Build'

node {
   sh 'gradlew clean build'
}

def notify(status) {
  emailtest (
   to :'ajeshjenkins@mailinator.com'
   subject: "${status} : Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' " , 
   body : "check console output at <a href ='${env.BUILD_URL}'> ${env.JOB_NAME} [$env.BUILD_NUMBER}] </a>"
 )
}

stage 'notification'
notify('success')
