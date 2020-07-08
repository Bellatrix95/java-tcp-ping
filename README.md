# Java TCP Ping

Java TCP Ping is a simple application for checking network diagnostics. It can be configured to work as a `Pitcher` or a `Catcher`.
Catcher's role is to create a Socket server and wait for messages coming from Pitcher. Pitcher sends a specified number of messages in each second and later on checks network statistics for the passed time frame. 

## Common use cases

The application can be used for network diagnostics because it outputs network statistics for the one-second time frame. The output contains information like:
- second for which statistics are listed
- number of sent messages in a single time frame
- number of messages lost in a single time frame
- average time in milliseconds from Pitcher to Catcher
- average time in milliseconds from Catcher to Pitcher
- average time in milliseconds from Pitcher to Catcher and back
- maximum time in milliseconds from Pitcher to Catcher and back

## Running Java TCP ping 

The following software is required to work with the Java TCP ping application:

* Java JRE1.5+

You can verify the version is installed and running:

    $ java -version

### Running the code

The application can be started in two modes: as a Pitcher or as a Catcher. 

Arguments mandatory for starting **Pitcher** are listed in the table below:

| Parameter | Default | Example value |  Description 
| -----  | -----   | ----- | ----- 
| p | | none | Flag used for starting Pitcher
| hostname | | localhost |  Socket server port IP address
| port | | 3000 | Socket server port
| mps | 1 | 50 | Number of messages per second
| size | 300 | 50 | Message size in byte in range (50, 3000)
| numThreads | 2 | 4 | Number of threads used for sending messages

Arguments mandatory for starting **Catcher** are listed in the table below:

| Parameter | Default | Example value | Description 
| -----  | -----   | ----- 
| c | |  | Flag used for starting Catcher
| bind | |  localhost | Socket server port IP address
| port | | 3000 | Socket server port
| numThreads | 2 | 4 | Number of threads used for handling incoming messages

Example commands:

    $ java main.java.com.company.Main -c -b localhost -port 6000
    $ java main.java.com.company.Main -p -h localhost -port 6000 -mps 2 -size 50