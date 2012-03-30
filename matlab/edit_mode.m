function output = edit_mode()

global A;

% Set script command to cause Simulator to enter Edit Mode
scriptcommand = 'EnterMode(EDIT)';

% Make the RunScriptCommand call
output = A.RunScriptCommand(scriptcommand);

output = output{1};

if(~strcmp(output,''))
    disp(output)
end

end

