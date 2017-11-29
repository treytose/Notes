#Counter
from collections import Counter

l = [1,1,1,1,1,12,2,2,2,2,2,5,5,5,4,4,4,3,3,3,3]

print(Counter(l)) #counts the number of occurences

#also works for strings

#count letter
s = 'aaaaabbbccccd'

print(Counter(s))

s = "Hello World how how are are are you you you you?"
print(Counter(s.split(" ")))
