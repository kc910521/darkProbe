# darkAgent
java agent for method invoke inspection

usage:
 -javaagent:{YOU PATH}/my-agent.jar

output (from zookeeper)：
> [main=1]--> protected void org.apache.zookeeper.server.quorum.QuorumPeerMain.initializeAndRun(java.lang.String[]) throws org.apache.zookeeper.server.quorum.QuorumPeerConfig$ConfigException,java.io.IOException,org.apache.zookeeper.server.admin.AdminServer$AdminServerException 
|-[1] ------> public void org.apache.zookeeper.server.quorum.QuorumPeerConfig.parse(java.lang.String) throws org.apache.zookeeper.server.quorum.QuorumPeerConfig$ConfigException 
|-[1] --------> public org.apache.zookeeper.server.util.VerifyingFileFactory$Builder org.apache.zookeeper.server.util.VerifyingFileFactory$Builder.warnForRelativePath() 
|-[1] --------> public org.apache.zookeeper.server.util.VerifyingFileFactory$Builder org.apache.zookeeper.server.util.VerifyingFileFactory$Builder.failForNonExistingPath() 
|-[1] --------> public org.apache.zookeeper.server.util.VerifyingFileFactory org.apache.zookeeper.server.util.VerifyingFileFactory$Builder.build() 
|-[1] --------> public java.io.File org.apache.zookeeper.server.util.VerifyingFileFactory.create(java.lang.String) 
|-[1] ----------> public java.io.File org.apache.zookeeper.server.util.VerifyingFileFactory.validate(java.io.File) 
|-[1] ------------> private void org.apache.zookeeper.server.util.VerifyingFileFactory.doWarnForRelativePath(java.io.File) 
|-[1] ------------> private void org.apache.zookeeper.server.util.VerifyingFileFactory.doFailForNonExistingPath(java.io.File) 
|-[1] --------> public void org.apache.zookeeper.server.quorum.QuorumPeerConfig.parseProperties(java.util.Properties) throws java.io.IOException,org.apache.zookeeper.server.quorum.QuorumPeerConfig$ConfigException 
|-[1] ----------> public org.apache.zookeeper.server.util.VerifyingFileFactory$Builder org.apache.zookeeper.server.util.VerifyingFileFactor