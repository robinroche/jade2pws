function output = run_power_flow()

global A;

% Set script command to cause Simulator to run a power flow
scriptcommand = 'SolvePowerFlow(RECTNEWT)';

% Make the RunScriptCommand call
output = A.RunScriptCommand(scriptcommand);

output = output{1};

if(~strcmp(output,''))
    disp(output)
end

end

