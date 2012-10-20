function output = run_mode()

global A;

% Set script command to cause Simulator to enter Run Mode
scriptcommand = 'EnterMode(RUN)';

% Make the RunScriptCommand call
output = A.RunScriptCommand(scriptcommand);

output = output{1};

if(~strcmp(output,''))
    disp(output)
end

end

