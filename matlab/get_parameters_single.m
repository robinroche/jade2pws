function output = get_parameters_single(request)

global A;


% Parse request
contentArray = regexp(request,',','split');
type = contentArray{2};
deviceId = contentArray{3};
if(strcmp(type,'Bus'))
    % For buses
    fieldArray = contentArray(4:length(contentArray));
    nbFields = length(fieldArray);
    valueList{1} = deviceId;
    for i=2:2+nbFields
        valueList{i} = '0';
    end
end
if(strcmp(type,'Gen') || strcmp(type,'Load'))
    % For loads and generators
    deviceId2 = contentArray{4};
    fieldArray = contentArray(5:length(contentArray));
    nbFields = length(fieldArray);
    valueList{1} = deviceId;
    valueList{2} = deviceId2;
    for i=3:3+nbFields
        valueList{i} = '0';
    end
end
if(strcmp(type,'Branch'))
    % For branches
    deviceId2 = contentArray{4};
    deviceId3 = contentArray{5};
    fieldArray = contentArray(6:length(contentArray));
    nbFields = length(fieldArray);
    valueList{1} = deviceId;
    valueList{2} = deviceId2;
    valueList{3} = deviceId3;
    for i=4:4+nbFields
        valueList{i} = '0';
    end
end


% Send request
simOutput = A.GetParametersSingleElement(type,fieldArray,valueList);


% Analyze the answer
if ~(strcmp(simOutput{1},''))
    % If there is an error
    disp('Error:')
    disp(simOutput{1})
    output = simOutput{1};
else
    % Puts the buses in row vector busparam
    paramList = transpose(simOutput{2});
    
    % Prepare answer for JADE
    output = [num2str(size(paramList{1},1)),',',...
        num2str(nbFields)];
    
    for i=1:nbFields
        if(isnan(paramList{i}))
            val = ' ';
        else
            val = strtrim(char(paramList{i}));
        end
        output = [output,',',val];
    end
    
    % disp('GetParametersSingle succesful')
end

end
