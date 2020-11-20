package threeChess.agents;

import threeChess.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * An interface for AI bots to implement.
 * They are simply given a Board object indicating the positions of all pieces, 
 * the history of the game and whose turn it is, and they respond with a move, 
 * expressed as a pair of positions.
 * **/ 
public class Agent22234922 extends Agent{
  
  private static final String name = "22234922";
  private static final Random random = new Random();
  // each key in the hashmap is the index of the node 
  // each value is a list of the index of all its children
  // the index of the node in the map is the same index as all its corresponding data in the arraylists.
  private HashMap<Integer, ArrayList<Integer>> children;
  // // each key in the hashmap is the index of the node 
  // each value is its parent node
  // the index of the node in the map is the same index as all its corresponding data in the arraylists.
  private HashMap<Integer, Integer> parents;
  // an arraylist of all visited game states
  private ArrayList<Board> states;
  // an arraylist of the actions taken to get to that game state
  private ArrayList<Position[]> actions;
  // the total wins resulting from that game state
  private ArrayList<Double> wins;
  // the total times that game state has been visited
  private ArrayList<Integer> visits;


  /**
   * A no argument constructor, 
   * required for tournament management.
   * **/
  public Agent22234922(){
  }

  /** 
   * reaches as far down the tree as is expanded
   * @param parent the node it begins from
   * @return the furthest node that could be reached
  */
  public int treePolicy(int parent){
    // continues down the tree until a terminal node or a not fully expanded node
    // if not fully expanded node found, expand it
    while (!states.get(parent).gameOver()){
        if (children.get(parent).isEmpty()){
            expand(parent);
            return children.get(parent).get(0);
        }
        else {
            parent = bestChild(parent);
        }
    }
    return parent;
  }

  /**
   * expands the tree by adding leaf nodes for all possible moves 
   * to be made from the current leaf node
   * @param parent the node to be expanded
   */
  public void expand(int parent){
    int child = parent;
    // iterate through each legal action from the parent node
    for (Position[] step : allActions(states.get(parent))){
        // copy the board so actions can be tested on it
        Board stateCopy = states.get(parent);
        try{
          stateCopy = (Board) states.get(parent).clone();
        }catch (CloneNotSupportedException e){}
        try {
          // make the move on the board
          stateCopy.move(step[0], step[1]);
        }catch (ImpossiblePositionException e){}
        // adds new child to all appropriate data structures
        child = parents.size();
        parents.put(child, parent);
        children.get(parent).add(child);
        children.put(child, new ArrayList<Integer>());
        actions.add(step);
        wins.add(0.0);
        visits.add(0);
        states.add(stateCopy);
    }
  }

  /**
   * retrieved the best node to move to from the given node
   * @param parent the current node 
   * @return the index of the best node to move to
   */
  public int bestChild(int parent){
    double bestScore = 0.0;
    int bestChild = parent;
    // iterate through all children of the parent node
    for (int child : children.get(parent)){
      if (states.get(child).gameOver()){return child;}
      // calculate the UCT associated with each of the children
      if (visits.get(child) == 0){
        return child;
      }
      // values to calculate UCT function
      double exploit = wins.get(child) / visits.get(child);
      double explore = Math.sqrt(2.0) * Math.sqrt(Math.log(visits.get(parent))/visits.get(child));
      double score = exploit + explore;
      // store the best one to be returned
      if (score > bestScore) {
        bestScore = score;
        bestChild = child;
      }
    }
    return bestChild;
  }


  /**
   * runs a random move simulation from the current node
   * @param state the state of the current noe (the furthest leaf node)
   * @return the result of the simulation (1 = win, 0.5 = draw, 0 = lose)
   */
  public double defaultPolicy(Board state){
    // store what colour the agent is
    Colour current = state.getTurn();
    Board stateCopy = state;
    try{
        stateCopy = (Board) state.clone();
    }catch (CloneNotSupportedException e){}
    // continue randomised simulation until the game terminates
    while (!stateCopy.gameOver()){
        Position[] move = randomMove(stateCopy);
        try{
            stateCopy.move(move[0], move[1]);
        }catch(ImpossiblePositionException e){}
    }
    if (stateCopy.getWinner() == current){return 1.0;}
    else if (stateCopy.getLoser() == current){return 0.0;}
    else {return 0.5;}
  }

  /**
   * performs a random move, used to simulate 
   * @param board the state of the current node
   * @return the move chosen by random selection
   */
  public Position[] randomMove(Board board){
    //taken from random agent
    Position[] pieces = board.getPositions(board.getTurn()).toArray(new Position[0]);
    Position start = pieces[0];
    Position end = pieces[0]; //dummy illegal move
    while (!board.isLegalMove(start, end)){
      start = pieces[random.nextInt(pieces.length)];
      Piece mover = board.getPiece(start);
      Direction[][] steps = mover.getType().getSteps();
      Direction[] step = steps[random.nextInt(steps.length)];
      int reps = 1 + random.nextInt(mover.getType().getStepReps());
      end = start;
      try{
        for(int i = 0; i<reps; i++)
          end = board.step(mover, step, end, start.getColour()!=end.getColour());
      }catch(ImpossiblePositionException e){}
    }
    return new Position[] {start,end};
  }

  /**
   * updates all nodes that were a part of the simulation
   * @param child the node from which the random simulation stems from
   * @param result the result of the game (1 = win, 0.5 = draw, 0 = loss)
   */
  public void backpropagate(int child, double result){
    // continues up the tree until it reaches the root node
    while(child > -1){
      // updates the visits and the wins counts
      wins.set(child, wins.get(child) + result);
      visits.set(child, visits.get(child) + 1);
      child = parents.get(child);
    }
  }

  /**
   * creates a list of all possible actions that can be made from the current node
   * sourced from the random agent provided
   * @param state the current node state
   * @return the list of all possible actions
   */
  public ArrayList<Position[]> allActions(Board state){
    ArrayList<Position[]> allPossible = new ArrayList<Position[]>();
    Position[] places = state.getPositions(state.getTurn()).toArray(new Position[0]);
      for (Position place : places){
        Board stateCopy = state;
        try{
            stateCopy = (Board) state.clone();
        }catch (CloneNotSupportedException e){}
          Position start = place;
          Position end = start;//dummy illegal move
          Piece piece = stateCopy.getPiece(place);
          PieceType type = piece.getType();
          Direction[][] steps = type.getSteps();
          int maxReps = type.getStepReps();
          for (Direction[] step : steps) {
              for (int i = 1; i < 1 + maxReps; i ++) {
                for (int j = 1; j < 1 + i; j ++) {
                  try{
                      end = stateCopy.step(piece, step, start, start.getColour()!=end.getColour());
                  }catch(ImpossiblePositionException e){}
                }
                Position[] possible = {start, end};
                if (state.isLegalMove(possible[0], possible[1])){
                  allPossible.add(possible);
                }
              }
          }
      }
      return allPossible;
  }

  /**
   * Play a move in the game. 
   * The agent is given a Board Object representing the position of all pieces, 
   * the history of the game and whose turn it is. 
   * They respond with a move represented by a pair (two element array) of positions: 
   * the start and the end position of the move.
   * @param board The representation of the game state.
   * @return a two element array of Position objects, where the first element is the 
   * current position of the piece to be moved, and the second element is the 
   * position to move that piece to.
   * **/
  public Position[] playMove(Board state){
    children = new HashMap<Integer, ArrayList<Integer>>();
    parents = new HashMap<Integer, Integer>();
    states = new ArrayList<Board>();
    actions = new ArrayList<Position[]>();
    wins = new ArrayList<Double>();
    visits = new ArrayList<Integer>();
    // adding the root node to data structures
    int root = 0;
    parents.put(root, -1);
    children.put(root, new ArrayList<Integer>());
    states.add(state);
    wins.add(0.0);
    visits.add(0);
    actions.add(new Position[] {null, null});
    expand(root);
    long timeLeft = state.getTimeLeft(state.getTurn())/(1 + Math.abs(150 - state.getMoveCount()));
    long startTime = System.currentTimeMillis();
    while ((System.currentTimeMillis()-startTime)<timeLeft) {
        int child = treePolicy(0);
        double result = defaultPolicy(states.get(child));
        backpropagate(child, result);
    }
    int chosen = bestChild(0);
    return actions.get(chosen);
  }

  /**
   * @return the Agent's name, for annotating game description.
   * **/ 
  public String toString(){return name;}

  /**
   * Displays the final board position to the agent, 
   * if required for learning purposes. 
   * Other a default implementation may be given.
   * @param finalBoard the end position of the board
   * **/
  public void finalBoard(Board finalBoard){}

}


