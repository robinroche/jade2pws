% Robin Roche, 2012
% Establishes a TCP connection between a JADE agent in Java and Matlab
% and a COM connection between Matlab and PowerWorld with SimAuto

clc
clear all

global A;


%% INITIALIZE CONNECTION

% Create TCP/IP object 't'. Specify server machine and port number.
% Open the connection with the server
t = tcpip('localhost', 1234);
set(t, 'InputBufferSize', 30000);
set(t, 'OutputBufferSize', 30000);
pause(0.1)
fopen(t);
disp('Connection with JADE established')

% Establish a connection with PowerWorld SimAuto
A = actxserver('pwrworld.SimulatorAuto');
disp('Connection with PowerWorld established')


% % Get the list of fields if needed
% type = 'Bus';
% output = A.GetFieldList(type);
% disp(output)

disp('Exchanging data...')


while(exist('t'))
    
    clear action;
    clear param1;
    clear output;
    clear val;
    clear ack;
    clear msg;
    
    %% GET MESSAGE FROM JADE
    
    % Receive a message from JADE
    val = tcp_receive_function(t);
    % disp(val)
    
    % Check if the message is valid
    if(~strcmp(val,''))
        
        % Extract the message content
        msg = val{1}{3};
        
        % Split the message content
        contentArray = regexp(msg,',','split');
        action = contentArray{1};
        if(length(contentArray)>1)
            param1 = contentArray{2};
        end
        
        %disp('-------------------------')
        %disp('Request from JADE:')
        %disp(msg)
        
        
        %% TAKE ACTIONS BASED ON THE MESSAGE CONTENT
        
        % If this is to open a case in PowerWorld
        if(strcmp(action,'open-case'))
            file_path = param1;
            output = open_case(file_path);
            tcp_send_function(t,char(output));
        end
        
        % If this is to switch to edit mode
        if(strcmp(action,'edit-mode'))
            output = edit_mode();
            tcp_send_function(t,char(output));
        end
        
        % If this is to switch to run mode
        if(strcmp(action,'run-mode'))
            output = run_mode();
            tcp_send_function(t,char(output));
        end
        
        % If this is to list the devices in the case
        if(strcmp(action,'list-devices'))
            type = param1;
            output = list_devices(type);
            tcp_send_function(t,output);
        end
        
        % If this is to get the parameters of some elements
        if(strcmp(action,'get-parameters-multiple'))
            output = get_parameters_multiple(msg);
            tcp_send_function(t,output);
        end
        
        % If this is to get the parameters of one element
        if(strcmp(action,'get-parameters-single'))
            output = get_parameters_single(msg);
            tcp_send_function(t,output);
        end
        
        % If this is to change the parameters of some elements
        if(strcmp(action,'change-parameters-multiple'))
            output = change_parameters_multiple(msg);
            tcp_send_function(t,output);
        end
        
        % If this is to change the parameters of one element
        if(strcmp(action,'change-parameters-single'))
            output = change_parameters_single(msg);
            tcp_send_function(t,output);
        end
        
        % If this is to run a power flow
        if(strcmp(action,'run-power-flow'))
            output = run_power_flow();
            tcp_send_function(t,output);
        end
        
        % If this is to save the file
        if(strcmp(action,'save-file'))
            file_path = param1;
            output = save_file(file_path);
            tcp_send_function(t,output);
        end
        
        % If this is to close the opened case
        if(strcmp(action,'close-case'))
            % Close the case in PowerWorld
            output = A.CloseCase;
            tcp_send_function(t,output{1});
        end
        
        % If this is to end the connection
        if(strcmp(action,'end-connection'))
            disp('Ending connection')
            % Close the connection with PowerWorld SimAuto
            delete(A);
            % Disconnect and clean up the TCP connection.
            fclose(t);
            delete(t);
            clear t
        end
        
    end % End message validity check
    
end % End loop

disp('Connection ended')
