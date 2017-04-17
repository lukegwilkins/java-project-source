def alphabetCoverage(text):
    alphabet=['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']

    coverage=[]
    for i in range(len(text)):
        if((text[i] in alphabet) and not(text[i] in coverage)):
            coverage.append(text[i])
    
    return coverage
