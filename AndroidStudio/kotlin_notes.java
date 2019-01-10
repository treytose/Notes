----- Variables -----
val name: String = "Trey"  // use 'val' to declare a constant value
var age: Int = 22		   // use 'var' to declare a variable 

---- Classes ----

// Example Class with primary constructor
class Avatar(val name: String, var powerLevel: Int = 1)  // <---- Constructor 
{
	// body of class 
}

// Example 2 - Init Block
class Avatar(val name: String, var powerLevel: Int = 1)
{
	// An optional 'init block' to contain additional constructor code, notice 'name' is already defined 
	init { 
		println("Hello " + name + "!")  
	}
}

// Instantiate Avatar class
val avatar1 = Avatar("Bilbo", 5)
val avatar2 = Avatar("Frodo") //default value used for powerLevel

