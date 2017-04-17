filename = input("input file name:\n>>>")

file = open(filename, 'r')

temp=0;

data = []
settings =[]
for line in file:
    if(temp%2==0 and temp!=0):
        data.append(line)
    elif(("invalid" not in line) and temp !=0):
        settings.append(line)
    temp+=1

times=[]
for text in data:
    textData=text.split(":")
    minutes=int(textData[0])
    seconds=int(textData[1])+60*minutes
    if(seconds!=0):
        times.append(seconds)


total = 0
for time in times:
    total+=time

print("Average is "+str(total/len(times)))

splitSettings=[]
for setting in settings:
    candidateSettings = setting.split("|")
    splitSettings += candidateSettings[2::]

permutationSettings=[]
for setting in splitSettings:
    permSetting = setting.split("@")
    for i in range(1,len(permSetting)):
        permutationSettings.append(permSetting[0]+" "+permSetting[i])

allSettings=[]
for setting in permutationSettings:
    allSets = setting.split("#")
    for i in range(1, len(allSets)):
        allSettings.append(allSets[0]+" "+allSets[i])


#for i in range(len(allSettings)):
#    print(str(i+1)+". "+allSettings[i])
print(len(allSettings))
