function output = change_parameters_multiple(request)

global A;


% Parse request
contentArray = regexp(request,',','split');
type = contentArray{2};
nbElements = str2num(contentArray{3});
nbFields = str2num(contentArray{4});
fieldArray = contentArray(5:(5+nbFields-1));
valueArray = contentArray(5+nbFields:length(contentArray));


% Make the ChangeParametersMultipleElementFlatInput call
simOutput = A.ChangeParametersMultipleElementFlatInput(...
type, fieldArray, nbElements, valueArray);


% Analyze the answer
if ~(strcmp(simOutput{1},''))
    % If there is an error
    disp('Error:')
    disp(simOutput{1})
else
    % If there is no error
    % disp('ChangeParametersMultiple succesful')
end

output = simOutput{1};

end