#map(), applies a function to each element in an array, returns an iterable
lst = [1,2,3,4,5,6]

print("test for even # using map(): ",list(map(lambda x: x%2==0, lst)))
print()

#--------------------------------------------------------------


#reduce(), takes 2 items in the list and applies a function
from functools import reduce

print("find max # using reduce():",reduce(lambda x,y: x if x > y else y, lst)) #returns the max element
print()

#--------------------------------------------------------------


#filter(), applies func that returns a bool, if false removes element from the list

print("Remove uneven numbers using filter():",list(filter(lambda x: x%2==0, lst)))
print()

#--------------------------------------------------------------


#zip(), combines two iterables

a = (2,3,6,7)
b = (2,4,6,8)

print("zip(", a, ",",b,") = ", list(zip(a,b)))
print()

#--------------------------------------------------------------


#switch dictionary values to = another dictionaries values
d1 = {'a': 5, 'b': 10, 'c': 15}
d2 = {'z': "Bob", 'x': 'Sally', 'w': 'jim'}


def switcharoo(d1, d2):
    for k,v in zip(d1, d2.values()):
        d1[k] = v

print("D1 before switcharoo: ", d1)
switcharoo(d1, d2)
print("D1 after switcharoo: ", d1, "\n")

#--------------------------------------------------------------

print("combine a and b remove all pairs whose sum is not an even number")
print(list(filter(lambda x: (x[0] + x[1]) % 2 == 0, zip(a,b))))
print()

print("multiply each set together")
print(list(map(lambda x: x[0] * x[1], zip(a,b))))
print()

#-------------------------------------------------------------------

print("print the tuple with the largest multiple")
max = 0
tup = ()
for t in zip(a,b):
    if t[0]*t[1] > max:
        max = t[0]*t[1]
        tup = t
print(tup)
print()

#---------enumerate()-----------------------------------------------------

lst = ['a','b','c']
print("enumerate() through list:", lst, "(keeps count of the iteration)")

for count, item in enumerate(lst):
    print(count, item)
print()

#---------all() and any()-------------------------------------------------
lst = [True, False, False, False]
lst2 = [True, True, True, True]

print("all(", lst, ")", all(lst))
print("all(", lst2, ")", all(lst2))

print()

print("any(", lst, ")", any(lst))
print("any(", lst2, ")", any(lst2))
print()

#------------complex()------------------------------------------------
#useful for imaginary numbers

print(complex(2,3))
print()

print('--------------built in functions test------------------------')
#complete the problems in a single line

#1. from a string of words create a list with the length of each word
word = "How long are the words in this phrase"

lst = list(map(lambda word: len(word), word.split(" ")))
print(lst, '\n')

#2 take a list of digits and return the number they correspond to
digits = [3,4,3,2,1]
print(reduce(lambda x,y: x * 10 + y, digits), '\n')

#3 take a  list of words and return a list with words that start with the target letter
words = ['hello', 'are', 'cat', 'dog', 'ham', 'hi', 'go', 'to', 'heart']
letter = 'h'

lst = list(filter(lambda word: word[0] == letter, words))
print(lst,'\n')

#4 create a function that turns ['A', 'B'] ['a', 'b'] '-' into ['A-a', 'B-b']
connector = '-'
lst1 = ['A','B']
lst2 = ['a','b']

word = [word1 + connector + word2 for word1, word2 in zip(lst1, lst2)]

print(word, '\n')

#5 take a list and create a dictionary with the list elements as the key and their index as the values
lst = ['a','b','c']
dic = {}

for i, element in enumerate(lst): dic[element] = i

print(dic,'\n')

#6 count the number of matching indexes in a list
lst = [0, 2, 2, 1, 5, 5, 6, 10]
#count = reduce(lambda x,y: x + y, map(lambda e: 1 if e[0] == e[1] else 0, enumerate(lst)))
count = len([num for count,num in enumerate(lst) if num == count])


print(count)

print([num for num in lst if num % 2 == 0])