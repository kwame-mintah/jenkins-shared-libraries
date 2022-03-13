call(String deployedEnvironment, String deploymentType) {
  script {
    String buildDescription = "Environment = ${deployedEnvironment}\nDeployment type = ${deploymentType}"
    currentBuild.description = buildDescription
  }
}
