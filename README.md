# OS Project 4 - FoodCraft
This project models the system put forth within the project documentation.


# Compiling
A manifest file has been provided to build a jar file, with the
appropriate main class. This can be done as follows: 

```bash
./buildJar FoodCraft.jar
```

# Running
The FoodCraft jar file can be run using
```bash
java -D<options> -jar FoodCraft.jar <time> <T/F>
```

This will begin the FoodCraft simulation, with an upper bound time of
the time argument. The T/F argument determines whether to log to log.txt.

# Options
The following options are available for this program:

| Key                     | Default Value | Usage                                                                                          |
|-------------------------|---------------|------------------------------------------------------------------------------------------------|
| messenger.show_attempts | false         | Determines whether to log all attempts to acquire resources, rather than just successful ones. |
| miner.bound             | 10000         | The maximum wait time for making and eating sandwiches.                                        |
| miner.hide_status       | false         | Determines whether to suppress messages stating that miners are ready to eat or are eating.    |

#