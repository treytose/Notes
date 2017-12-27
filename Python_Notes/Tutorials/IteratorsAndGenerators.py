#generators are like functions but suspend then resume their action
#genreators use yield instead of return

def gencubes(n):
    for num in range(n):
        yield num**3

#seems like gencube returns an iterable but its actually just calling the generator over and over
for x in gencubes(10):
    print(x)

def genfibon(n):
    a = 1
    b = 1

    for i in range(n):
        yield a
        a,b = b,a+b

for num in genfibon(10):
    print(num)

print()


#next() function

def simple_gen():
    for x in range(3):
        yield x

g = simple_gen()

print(next(g)) #gets the next value from the generator

#iter() function

s = 'Hello'

#turns s into an iterator
s_iter = iter(s)

print(next(s_iter))

