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
stage 'Git Checkout'
node {
   git 'https://github.com/ajeshmahto/spring-jenkins.git'
       
}
stage 'Build'

node {
   sh './gradlew clean build'
   
   stash name: 'everything',
   includes: '**'
}

def notify(status){
        emailext(
            to: "testajesh@mailinator.com",
            subject: "${status}: Job '${env.JOB_NAME} [$env.BUILD_NUMBER]'",
             body: "Hello Ajesh , " + 
             "<div> Here is the build url <a href='${env.BUILD_URL}' </a>, buldNumber:'${env.BUILD_NUMBER}' </div>"
        )     
    }
node('mac') {
    sh 'ls'
    sh 'rm -rf *'
    unstash 'everything'
    sh 'ls'
}

stage 'End to End Testing'

parallel chrome: {
    runText('Chrome')
}, firefox: {
    runText('Firefox')
}, safari: {
    runText('Safari')
}

def runText(browser){
 node {    
    sh 'rm -rf *'
    unstash 'everything'
    sh "echo running text on browser - ${browser}"
  }
}
input 'Deploy to UAT'

stage 'notification'
//notify('success')
