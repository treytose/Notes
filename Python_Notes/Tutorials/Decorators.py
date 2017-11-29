#functions are objects

#functions within functions
def hello(name='Jose'):

    def greet():
        return 'The greet() function has been called!'

    def welcome():
        return 'The welcome() function has been called!'

    if name == 'Trey':
        return greet  #return the function greet not the result greet()
    else:
        return welcome

print(hello('Trey')()) #hello('Trey') returns a function


#functions as arguments

def hello():
    return 'Hi Trey!'

def other(func):
    print('Other code goes here!')
    print(func())

other(hello)


def new_decorator(func):

    def wrap_func():
        print('Code here, before executing the func!')

        func()

        print('Code here will execute after the func!')

    return wrap_func

def func_needs_decorator():
    print('This function needs a decorator!')

#set func_needs_decorator = to the wrap_func() (which contains func_needs_decorator)
func_needs_decorator = new_decorator(func_needs_decorator)

#func_needs_decorator now = the same as:
    #print('Code here, before...')
    #(func_needs_decorator code)
    #print('Code here will execute...')

#basically adding more code surrounding the original function

#write the same as above using the @ symbol

@new_decorator
def func_needs_decorator():
    print('This function needs a decorator!')

func_needs_decorator()