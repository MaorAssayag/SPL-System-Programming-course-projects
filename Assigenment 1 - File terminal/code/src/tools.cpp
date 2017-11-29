#include "../include/tools.h"


string Tools :: deleteSpaces(string currentInput) {
    size_t first = currentInput.find_first_not_of(" ");
    size_t last = currentInput.find_last_not_of(" ");
    string ans = currentInput.substr(0,last+1);
    ans = currentInput.substr(first,last+1-first);
    return ans;
}

