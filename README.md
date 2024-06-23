# 3DMaze

This is a 3D maze that generate a random maze. The starting point is the top left of the map and the goal is at the bottom right. The player can move by w, a, s, d or up, left, right, down direction keys. The camera can be rotate by drugging with the mouse.

ToDo:
    1. [Done] Randomize Maze
        generate with maze generate algo then add wall as cells
    2. Enemy
        need open holes to have loop in order to get rid of the enemy
        runnable
        find shortest past toward player
        game over if the enemy and the player is in the same space
        maybe another project
    3. [Done] Vision by mouse
        a. use mouse position to detect if mouse is going right or left
        b. change rotation speed based on the distance of drage
    4. Main menu
        to let user pick size of the maze
    5. Mini map
        a map that record where the user went to before
