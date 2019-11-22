# GraphTraversalAlgorithms
This is a collections of algorithms for analysing graphs. 

## getShortestPath
The method used to find the shortest path was Dijkstra’s algorithm implemented for a directed unweighted graph. This works by finding the unvisited adjacent nodes which are at a minimum distance from the start point and working through the graph until a minimum path is found to the finish point. The number of links in the path is returned.

## getHamiltonianPath
The method used to find the Hamiltonian path was backtracking by using a depth first recursive search. The Hamiltonian path was found by checking every path in the graph starting from every point and checking to see if it is a valid Hamiltonian path by making sure it is of the correct length and that no node is repeated.

## getStronglyConnectedComponent
The method used to find the strongly connected components of the graph was Kosaraju’s algorithm. Kosaraju’s algorithm works by doing a depth first traversal and putting the results onto a stack. It then does another depth first traversal but uses the elements from the stack instead of from the graph. This effectively reverses the order of the vertices.

## getCentres
This method is used to find the vertex or vertices that have the shortest path to all other vertices in the graph. It works by creating a matrix of shortest paths then removing all nodes that can’t reach every other node. The remaining nodes are scanned to find out how long it takes to get to the node furthest away and then shortest of these distances are returned.
