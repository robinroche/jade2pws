function output = open_case(file_path)

global A;

output = A.OpenCase(file_path);

%If the first cell in Output ~= '', then that means an error occurred.
if ~(strcmp(output{1},''))
    disp(output{1})
else
    %Otherwise, no errors. Perform activities.
    % disp('Open Case successful')
end

end