# darkProbe



```text

(\,--------'()'--o
 (_    ___    /~"
  (_)_)  (_)_)
~~~  Mama never worry about your study! 
                           ~~~~~~~~~~~~ DARK_PROBE
```


DarkProbe is a javaAgent for inspecting method invoking chains in GLOBAL VIEWs, 
you can use it in learning principle of java middleware,   
such as ZooKeeper, ElasticSearch, RocketMQ etc.  




## usage:
add to VM options:
>  -javaagent:{YOU AGENT JAR PATH NAME}/dark-probe.jar  -DinspectPackage={PACKAGE NAMES YOU MONITOR} 
 
## example:
 
 we can show you how this javaAgent affects ZooKeeper.
 
**step 1: add to zookeeper VM startup parameters.**
 >  -javaagent:/home/work/??/dark-probe.jar
    -DinspectPackage=org.apache.zookeeper.server
    
 **step 2: run the ZooKeeper source code :**
 ```java
    // RUN org.apache.zookeeper.server.quorum.QuorumPeerMain#main
    public static void main(String[] args) {
        QuorumPeerMain main = new QuorumPeerMain();
        
        main.initializeAndRun(args);
        // ...
            
        
    }

```
 **step 3: console will output (from zookeeper)：**
```text
↗---------↘↙---↖
[main]go↑↑ org.apache.zookeeper.server.quorum.QuorumPeerMain#main(String[], 
[main]--------> org.apache.zookeeper.server.quorum.QuorumPeerMain#initializeAndRun(String[], 
[main]----------> org.apache.zookeeper.server.quorum.QuorumPeerConfig#parse(String, 
[main]------------> org.apache.zookeeper.server.util.VerifyingFileFactory#create(String, 
[main]--------------> org.apache.zookeeper.server.util.VerifyingFileFactory#validate(File, 
[main]----------------> org.apache.zookeeper.server.util.VerifyingFileFactory#doWarnForRelativePath(File, 
[main]----------------> org.apache.zookeeper.server.util.VerifyingFileFactory#doFailForNonExistingPath(File, 
[main]------------> org.apache.zookeeper.server.quorum.QuorumPeerConfig#parseProperties(Properties, 
[main]--------------> org.apache.zookeeper.server.util.VerifyingFileFactory#create(String, 
[main]----------------> org.apache.zookeeper.server.util.VerifyingFileFactory#validate(File, 
[main]------------------> org.apache.zookeeper.server.util.VerifyingFileFactory#doWarnForRelativePath(File, 
```

every meaning a new root method invoked (may from a new Thread),  
the template is:
```text
"↗---------↘↙---↖" 
[{thread name}]go↑↑ {1st root Class and method}
[{thread name}]--------> {2th Class and method}
[{thread name}]------------> {3rd Class and method}
[{thread name}]------------> {3rd Class and method}
[{thread name}]--------------> {4th Class and method}
[{thread name}]--------> {2th Class and method}
```


if you are boring to see  'org.apache.zookeeper.server.metric.AvgMinMaxCounter' 
and 'org.apache.zookeeper.server.metric.AvgMinMaxPercentileCounter'   
(**or some Class cause circulation deadly** )
,just add VM options:
```text
-DignorePackage=org.apache.zookeeper.server.metric.AvgMinMaxCounter,org.apache.zookeeper.server.metric.AvgMinMaxPercentileCounter

```



--


