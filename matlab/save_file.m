function output = save_file(file_path)

global A;

% Setup name of PWB file to write
filenamepwb = strcat(file_path,'.pwb');

% Setup name of PTI file to write
filenamepti = strcat(file_path,'.raw');

% Make the SaveCase call for the PWB file
output1 = A.SaveCase(filenamepwb, 'PWB', true);

% Make the SaveCase call for the PTI file
output2 = A.SaveCase(filenamepti, 'PWB', true);

output = output1{1};

if(~strcmp(output,''))
    disp(output)
end

end