pipeline {
    agent any

    triggers {
        githubPush()
    }

    tools {
        maven 'mvn'
        jdk 'jdk21'
    }

    environment {
        PROJECT_DIR = 'C:\\Users\\mukes\\eclipse-workspace\\LambdaTest'
        REPORT_DIR = 'C:\\Users\\mukes\\eclipse-workspace\\LambdaTest\\target\\surefire-reports'
        REPORT_FILE = 'ExtentReport.html'
        ZIP_FILE = 'ExtentReport.zip'
        RECIPIENTS = 'mukeshbalodi5@gmail.com,balodimukesh38@gmail.com'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/mukeshbalodi/LambdaTest.git'
            }
        }

        stage('Build & Test') {
            steps {
                dir("${env.PROJECT_DIR}") {
                    bat 'mvn clean test'
                }
            }
        }

        stage('Zip Report') {
            steps {
                dir("${env.PROJECT_DIR}") {
                    bat """
                    powershell -Command "Compress-Archive -Path '${env.REPORT_DIR}\\${env.REPORT_FILE}' -DestinationPath '${env.REPORT_DIR}\\${env.ZIP_FILE}' -Force"
                    """
                }
            }
        }
    }

    post {
        always {
            dir("${env.PROJECT_DIR}") {
                bat "echo === Showing contents of ${env.REPORT_DIR} ==="
                bat "dir ${env.REPORT_DIR}"

                junit allowEmptyResults: true, testResults: '**/TEST-*.xml'

                publishHTML(target: [
                    reportDir: "${env.REPORT_DIR}",
                    reportFiles: "${env.REPORT_FILE}",
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])

                bat """
                if exist "${env.REPORT_DIR}\\${env.ZIP_FILE}" (
                    if not exist "%WORKSPACE%" mkdir "%WORKSPACE%"
                    copy "${env.REPORT_DIR}\\${env.ZIP_FILE}" "%WORKSPACE%\\${env.ZIP_FILE}"
                ) else (
                    echo ❌ ZIP file not found. Skipping copy.
                )
                """
            }

            archiveArtifacts artifacts: 'ExtentReport.zip', onlyIfSuccessful: false

            script {
                emailext(
                    subject: "Automation Test Report - Build #${env.BUILD_NUMBER}",
                    body: """<p>Dear Stakeholders,</p>

                             <p>The automation test suite for the latest build has completed.</p>

                             <ul>
                               <li><strong>View Online:</strong> <a href="${env.BUILD_URL}Extent_20Report/">Extent Report on Jenkins</a></li>
                               <li><strong>Download:</strong> Attached ZIP file for local viewing.</li>
                             </ul>

                             <p>Regards,<br/>QA Automation Team</p>""",
                    to: "${env.RECIPIENTS}",
                    mimeType: 'text/html',
                    attachmentsPattern: 'ExtentReport.zip',
                    replyTo: 'mukeshbalodi5@gmail.com',
                    from: 'jenkins@gmail.com'
                )
            }
        }
    }
}