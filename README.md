# SmartMeter Maven plugin

Maven plugin for running performance tests using [SmartMeter.io] (https://www.smartmeter.io).

**Note 1: SmartMeter Maven plugin 1.0.0 does not work on Windows, use version 1.0.1 or higher!**

**Note 2: SmartMeter 1.1.0 and higher is required!**

To learn more about SmartMeter.io read its [official documentation] (https://www.smartmeter.io/documentation). You can find useful knowing how to [run SmartMeter.io from command line] (https://www.smartmeter.io/documentation#toc-running-from-command-line).

##Usage
Use SmartMeter Maven plugin in 3 easy steps.

##### 1) Register in pom.xml

```
<build>
	<plugins>
		<plugin>
			<groupId>cz.etnetera</groupId>
			<artifactId>smartmeter-maven-plugin</artifactId>
			<version>1.0.0</version>
			<configuration>
				<smartMeterHome>SMARTMETER_HOME</smartMeterHome>
				<monitorPath>MONITOR_PATH</monitorPath>
				<testPath>TEST_PATH</testPath>
				<distributed>DISTRIBUTED</distributed>
				<gui>GUI</gui>
				<extraParams>EXTRA_PARAMS</extraParams>
			</configuration>
			<executions>
				<execution>
					<phase>verify</phase>
					<goals>
						<goal>runTest</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

##### 2) Configure

| Parameter | Description | Mandatory | Profi version only | Example |
| --------- | ----------- | --------- | ------------------ | ------- |
| SMARTMETER_HOME | Absolute path to SmartMeter home folder | yes | no | /home/etnetera/smartmeter |
| MONITOR_PATH | Monitor script name from *tests/monitors* folder or absolute path to monitor script | only for DISTRIBUTED tests | yes | monitor.jmx |
| TEST_PATH | Test script name from *tests* folder or absolute path to test script | yes | no | test.jmx |
| DISTRIBUTED | Run test in distributed mode | no | yes | *true* or *false* (default) |
| GUI | Run test in GUI mode | no | no | *true* (default) or *false* |
| EXTRA_PARAMS | Extra parameters for SmartMeter | no | no | -Jetn_batch_size=200 |

##### 3) Execute

```
mvn cz.etnetera:smartmeter-maven-plugin:runTest
```
Executing *runTest* goal will start a new instance of SmartMeter and immediately start the test. When the test if finished, SmartMeter will automatically generate a test report and shuts itself down.

### Overriding configuration from command line

Sometimes it is handy to override your default configuration defined in pom.xml directly from command line. If this is your case, try this configuration of plugin:
```
<configuration>
  ...
	<smartMeterHome>${smartmeter.smartMeterHome}</smartMeterHome>
	<monitorPath>${smartmeter.monitorPath}</monitorPath>
	<testPath>${smartmeter.testPath}</testPath>
	<distributed>${smartmeter.distributed}</distributed>
	<gui>${smartmeter.gui}</gui>
	<extraParams>${smartmeter.extraParams}</extraParams>
</configuration>

<properties>
	<smartmeter.smartMeterHome>SMARTMETER_HOME</smartmeter.smartMeterHome>
	<smartmeter.monitorPath>MONITOR_PATH</smartmeter.monitorPath>
	<smartmeter.testPath>TEST_PATH</smartmeter.testPath>
	<smartmeter.distributed>DISTRIBUTED</smartmeter.distributed>
	<smartmeter.gui>GUI</smartmeter.gui>
	<smartmeter.extraParams>EXTRA_PARAMS</smartmeter.extraParams>
</properties>
```

Then, you may easily override your pom.xml configuration like this:
```
mvn cz.etnetera:smartmeter-maven-plugin:runTest "-Dsmartmeter.testPath=my-test.jmx -Dsmartmeter.gui=false"
```