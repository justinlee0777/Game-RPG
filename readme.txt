Here lays one of the greatest monuments to hubris ever: began in the summer of 2014 and continued onward to the winter, I was entirely devoted to creating a game that in the beginning showed great promise to be, if not good, then at least working. But, having stopped at precisely the winter of 2015, unless I sufficiently humiliate myself before it, this will not come to be.

This project at least advocates for three things:
1) Version Control (which is why it is being moved to GitHub)
2) Good software principles i.e encapsulation, focus on object orientation
3) To not get over one's head (not force long-lasting changes across every class, especially if the number of classes > 10)

I'm currently trying to do a rescale that will try to accomplish (2); the program I'm submitting now is broken, and yet somehow my best attempt to make it as it was when it worked. It's likely I'll try to unbreak this version before I move onto the rescale, as unwise as that is.

Right now the main problem (conceptually) with the program is that there is a Field class that swaps canvasses to draw, but what these canvasses draw upon the Field is entirely variable. The Field object is essentially the main pathway to every other object in the program, and it is coded to allow easy access to other objects. This is a very awful way of programming, is ugly, and is likely to perish before it gets any bigger.

The other main problem, a literal problem, is the fact that every object drawn is relegated to a "DrawTree", something which is not a bad idea: since the characters do not move gridpoint to gridpoint (which I should strongly consider) the DrawTree helps to find which objects should overlap which objects - by its inherent sorting it prioritizes which should be drawn first. The problem with the DrawTree, though, is that it contains leverage over the drawing of the entire program, and everything else must adapt to its prioritization.

Other issues:
- Remove dependence on switching boolean variables to control state (so the keyword 'volatile' doesn't have to appear everywhere)

To run from main directory:
javac *.java Characters/*.java Grids/*.java Tutorial/*.java
find -name "*.java" > source.txt
javac -d build @source.txt
java -cp .:build:**/*.class Runner

