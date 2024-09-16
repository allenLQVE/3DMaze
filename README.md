# 3DMaze

This is a 3D maze that generates a random maze with Eller's Algorithm. The 3D is built by Java ray-casting which display 3D by shooting 2D arrays from the position of the player.

### How To Play
The starting point is at the top left of the map and the goal is at the bottom right. The player can move by w, a, s, d, up, left, right, or down direction keys. The camera can be rotated by drugging with the mouse. The game will terminate if the player reaches the goal.

### Future Development
1. Enemy
    Have an enemy chase the player with the shortest path between them. This may require the maze to be not perfect so the player has a chance to run away from the loop.
2. Interface
    An interface allows the player to choose the size of the maze and shows to restart the game after finishes.
3. Minimap
    A map that record where the user went to before.
4. Timer
    A timer to show the time takes to complete the maze. Maybe with a database that keeps the record of the player.
