function output = change_parameters_single(request)

global A;


% Parse request
contentArray = regexp(request,',','split');
type = contentArray{2};
nbFields = str2num(contentArray{3});
fieldArray = contentArray(4:4+nbFields-1);
valueArray = contentArray(4+nbFields:length(contentArray));


% Make the ChangeParametersSingleElement call
simOutput = A.ChangeParametersSingleElement(...
    type, fieldArray, valueArray);


% Analyze the answer
if ~(strcmp(simOutput{1},''))
    % If there is an error
    disp('Error:')
    disp(simOutput{1})
else
    % If there is no error
    % disp('ChangeParametersSingle succesful')
end

output = simOutput{1};

end