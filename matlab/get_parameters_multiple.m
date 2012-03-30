function output = get_parameters_multiple(request)

global A;


% Parse request
contentArray = regexp(request,',','split');
type = contentArray{2};
fieldArray = contentArray(3:length(contentArray));
nbFields = length(fieldArray);

% Gets parameters for the device with the ID deviceId
simOutput = A.GetParametersMultipleElement(type, fieldArray,' ');


% Analyze the answer
if ~(strcmp(simOutput{1},''))
    % If there is an error
    disp('Error:')
    disp(simOutput{1})
    output = simOutput{1};
else
    % If the answer is correct
    paramList = transpose(simOutput{2});
    
    % Prepare answer for JADE
    output = [num2str(size(paramList{1},1)),',',...
        num2str(size(paramList,2))];
    nbElements = size(paramList{1},1);
    
    for j=1:nbElements
        for i=1:nbFields
            if(isnan(paramList{i}{j}))
                val = ' ';
            else
                val = strtrim(char(paramList{i}{j}));
            end
            output = [output,',',val];
        end
    end
    
    % disp('GetParametersMultiple succesful')
end

end


