ABOUT:
	The Latin HyperCube Game is a war simulator that generates two teams
(red and blue). Each team controls a number of config file specified 
soldiers (10 as default). These soldiers move on a config file specified
playing board (the config file gives how many arbitrary units for the x 
component of the board and how many units for the y component). A team wins
if either one is able to kill all soldiers on an opposing team or capture
the base.
	All actions of the soldiers are determined based on values specified
in the config file. The values that play into the algorithms of the soldiers
 are accuracy, stealth, speed, and fear (however, factors like the x and y
components of the board and distance of the bases although do not play a 
direct role will affect the outcome of runs).

ALGORITHMS:
	Two algorithms guide the outcome for each game. The algorithms are
Soldier::shoot and Soldier::move where each of the two are ran for each 
soldier alive on the playing field. The soldiers move and then shoot (the 
order of this can be found in the main function).

Soldier::move:
	This algorithm is ran for each soldier currently alive (Field::move
 calls this function for each soldier). It takes moves based on how likely 
a soldier will survive when going to that position.The algorithm begins by 
creating a vector containing all possible moves for the current soldier (a 
move is considered to be possible if it is between the current position of 
the solider - speed of the solider and the current position + the speed).
This creates a circle of all possible moves.
	For each position in the vector of all possible moves, a temporary 
number p is created to contain: 

p= (1-accuracy*e^(-(distance traveled)^2/ (stealth)^2))^(Number of enemies)

*e is refering to the numerical constant of e
*distance traveled is the distance between the position and the soldier's
current position
*Number of enemies refers to all enemies that are present on the field at
the time

	For each p created, p is exponated by the number inputted into fear
 (a config file value) and is then subtracted from by 1 such as:
1-p^(fear)
 
	A new vector called probability(which is the total size of all 
possible moves) stores all 1-p^(fear) values.

	If a random number generated from 0 to 1 is greater than the value 
contained in probability, the value contained in probability is then added 
to a vector called possible moves 2.

	If possible moves 2 is somehow empty, the solider is considered to 
be paniced and makes no moves. However, on the likely chance that possible
moves 2 is filled with positions, a position is chosen based on which one
minimizes the distance of the soldier to his own base.


(note because this function runs in field::move, it is ran for every soldier
on the playing field)

VARIABLES AND THEIR EFFECTS:

	The soldier's use their current values for distance, accuracy and 
stealth to determine what the enemy's values for these variable are. 
Based on that, they make descision as to whether they will survive if they
move to a given position.

-increases refers to an increased chance of taking position in question
-decreases refers to a decreased chance of taking a position in question

	higher stealth-decreases
	lower stealth-increases

	higher accuracy-decreases
	lower accuracy-increases

	higher distance-increases
	lower distance-decreases

	more enemies-decreases
	less enemies-increases

	more fear-decreases
	less fear-increases


SUDO CODE:
	for( yth position-speed until yth position +speed):
		for(xth position-speed until xth position+speed):
			possible.add(x,y)	
	//first set of possible moves added to a vector possible

	for (each entry q in possible):
		p=1
		for(all enemies on the playing field):
			r2=distance(q,(the enemy's position))
			p=p*(1-accuracy*e^(-r2/stealth^(2))
		q=1
		q=p^(fear)
		probability.add(1-q)
	//probability now contains the chance for all moves to be accepted
	
	for(each move q in possible):
		if (the corresponding value x in probability< a random 
		number from 0 to 1):
			add x to a new vector possible2

	//moves have been narrowed down and put into a vector possible2

	if(possible2 is empty):
		do not move at all

	for(all moves in possible2)
		choose the move that minimizes the distance of the soldier
		to his base


Solider::shoot:
	This algorithm is ran for each soldier currently alive, Gaussian
Normal Distribution function (Field::shoot calls this function for each 
soldier). This algorithm is responsible for causing the death of all 
soldiers on the playing field.

	The algorithm is ran on a per soldier basis but considered each 
enemy soldier:

p=accuracy*e^((distance from enemy)/(stealth of enemy squared))

*e is refering to the constant e
*distance from enemy is the distance from the enemy's position to the
soldier's position
*stealth of enemy squared is the stealth of one individual enemy soldier
squared
*accuracy is the accuracy of the current soldier the function is running
for

	Using a random number generated between 0 and 1, if the random
number is greater than the number p, the value is not accepted and the
algorithm is ran for the next enemy on the list of all enemies. However,
if the value is accepted, the enemy is set to a status of dying and treated
as such.

SUDO CODE:

	for (each enemy i on the playing field):
		stealth_squared=i.stealth^2
		r2=distance((soldier's position),i.position)
		p=accuracy*e^(-r2/stealth_squared)
	if( a random number between 0 and 1 >p)
		go to next enemy in the list of all enemies
	else
		the enemy is dead
