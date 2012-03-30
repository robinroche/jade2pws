function output = list_devices(type)

global A;

%A list of branches is desired, without using any filter
DeviceType = type;
FilterName = '';

%Execute the ListOfDevices command
simOutput = A.ListOfDevices(DeviceType,FilterName);

%If the first cell in SimAutoOutput ~= '',
% then that means an error occurred.
if ~(strcmp(simOutput{1},''))
    disp(simOutput{1})
    output = simOutput{1};
else
    
    %Otherwise, no errors. Display the branch information.
    % disp('ListOfDevices successful')
    
    % FOR BRANCHES
    if(strcmp(type,'Branch'))
        %Devicelist1 contains the From: bus number
        %Devicelist2 contains the To: bus number
        %Devicelist3 contains the Bus identifier
        devicelist1 = double(transpose(simOutput{2}{1}));
        devicelist2 = double(transpose(simOutput{2}{2}));
        devicelist3 = simOutput{2}{3};
        
        % Prepare the output string
        output = sprintf('%d,%d,',size(devicelist1,1),3);
        for counter = 1:size(devicelist1,1)
            newText = sprintf('%d,%d,%s,',...
                devicelist1(counter),...
                devicelist2(counter),...
                strtrim(char(devicelist3(counter))));
            output = [output newText];
        end
    end
    
    % FOR BUSES
    if(strcmp(type,'Bus'))
        %Devicelist1 contains the bus number
        devicelist1 = transpose(simOutput{2}{1});
        
        % Prepare the output string
        output = sprintf('%d,%d,',size(devicelist1,1),1);
        for counter = 1:size(devicelist1,1)
            newText = sprintf('%d,',devicelist1(counter));
            output = [output newText];
        end
    end
    
    % FOR GENERATORS
    if(strcmp(type,'Gen'))
        %Devicelist1 contains the bus number
        %Devicelist2 contains the generator ID
        devicelist1 = transpose(simOutput{2}{1});
        devicelist2 = simOutput{2}{2};
        
        % Prepare the output string
        output = sprintf('%d,%d,',size(devicelist1,1),2);
        for counter = 1:size(devicelist1,1)
            newText = sprintf('%d,%s,',...
                devicelist1(counter),...
                strtrim(char(devicelist2(counter))));
            output = [output newText];
        end
    end
    
    % FOR LOADS
    if(strcmp(type,'Load'))
        %Devicelist1 contains the bus number
        %Devicelist2 contains the load ID
        devicelist1 = transpose(simOutput{2}{1});
        devicelist2 = simOutput{2}{2};
        
        % Prepare the output string
        output = sprintf('%d,%d,',size(devicelist1,1),2);
        for counter = 1:size(devicelist1,1)
            newText = sprintf('%d,%s,',...
                devicelist1(counter),...
                strtrim(char(devicelist2(counter))));
            output = [output newText];
        end
    end
    
    
end

end