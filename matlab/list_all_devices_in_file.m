% For testing communication and interaction with a file in PowerWorld

clc

global A;

%% INITIALIZATION

% Establish a connection with PowerWorld SimAuto
A = actxserver('pwrworld.SimulatorAuto');
disp('Connection with PowerWorld established')

% Open file
disp('Opening file')
file_path = 'C:\Users\rroche\code\madems\powerworld\RBTS_Bus3Dist_4dg.PWB';
output = open_case(file_path);
disp(char(output));

% Switch to edit mode
disp('Switching to edit mode')
output = edit_mode();
disp(output);

%% LIST ELEMENTS

% List loads
disp('Listing load')
output = list_devices('Load');
disp(output);

% List generators
disp('Listing generators')
output = list_devices('Gen');
disp(output);

% List branches
disp('Listing branches')
output = list_devices('Branch');
disp(output);

% List bus
disp('Listing buses')
output = list_devices('Bus');
disp(output);

%% ENDING

% Close the opened case
output = A.CloseCase;
delete(A);
