function display_output(result)

contentArray = regexp(result,',','split');
nbElements = str2num(contentArray{1});
lengthOfElement = str2num(contentArray{2});

values = contentArray(3:length(contentArray));

for i=1:nbElements
    str = '';
    for j=1:lengthOfElement
        str = [str ' ' values{(i-1)*(lengthOfElement) + j}];
    end
    disp(str)
end

end

