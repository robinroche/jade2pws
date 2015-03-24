# jade2pws

An interface between JADE and PowerWorld Simulator through Matlab.

### Licence

None. Feel free to use it as you wish.

### Context

Developing smart control strategies for power systems requires testing their behavior and performance, at first in simulation, as shown in a recent [paper](http://dx.doi.org/10.1109/DEXA.2012.9). To do that, you need a simulation tool capable of advanced analysis (e.g., power flow) and of advanced and customizable control algorithms. 

As we could not find any satisfactory existing solution, we developed an interface between two/three software to achieve it: [JADE](http://jade.tilab.com/) / [Matlab](http://www.mathworks.com/products/matlab/) / [PowerWorld Simulator](http://www.powerworld.com/).

This interface was developed in collaboration with Colorado State University's [Dr. Suryanarayanan](http://www.engr.colostate.edu/~ssuryana).

![Interface between JADE, Matlab and PowerWorld Simulator](http://robinroche.com/webpage/images/Jadepw.png)

See [mat2pws](https://github.com/robinroche/mat2pws) and [mat2jade](https://github.com/robinroche/mat2jade) for interfaces between these software.

### Interface concept

This project is an interface between JADE and PowerWorld Simulator through Matlab. 
It enables interfacing JADE agents with the power systems simulator PowerWorld.

### Code structure

- In JADE:
  - Launcher.java: A simple class that runs the two test agents.
  - MatlabComAGent.java: An agent that acts as a server and forwards information between JADE and Matlab.
  - MatlabComAGentTest.java: An agent that requests the previous agent to send some requests to PowerWorld.

- In Matlab:
  - main.m: This is the main file, that establishes the TCP connection and handles messages.
  - list_all_devices_in_file.m: This file can be used to list the devices, etc. in a PowerWorld file, directly from Matlab.    - It is easier to use than through JADE, but be careful about how data is formatted.
  - The other files are just functions used by main.m for sending and retrieving data to/from PowerWorld.

### Using the code

The following software are required:

- Matlab (tested with R2011b) with the Instrument Control Toolbox. This toolbox is only used for the TCP functions. If you find another way to use TCP communication with Matlab, you may not need this toolbox.
- PowerWorld Simulator (tested with version 15), with the SimAuto option that enables COM communication with external programs.
- JADE (tested with 4.0) libraries. 

Code use instructions are as follows:

1. Get the jade2pws files.
2. Import them to your favorite IDE, like Eclipse.
3. Get JADE jar libraries.
4. Include the libraries to the jade2pws project.
5. Edit the powerWorldFilePath variables in the MatlabComAgent class, to point to the folder where your PowerWorld file is.
6. Run the JADE program with the Launcher class.
7. In Matlab, open the main.m file and run it.
8. The communication should then be established and several requests should be sent to PowerWorld from JADE. You should see things displaying in the console. If not, well, there is a problem somewhere.

Please cite one of my papers if you use it, especially for research: http://dx.doi.org/10.1109/DEXA.2012.9

### Limitations 

- No extensive testing has been done, so use it at your own risk. 
- If you find bugs, errors, etc., or want to contribute, please let me know.
- Refer to PowerWorld's user manual for information on how data is structured.

### Contact

Robin Roche - robinroche.com
