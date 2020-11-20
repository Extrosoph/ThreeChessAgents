package threeChess.agents;

import threeChess.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Agent22764884 extends Agent{
	private static final String name = "Agent22764884";
	private static final Random random = new Random();
	private static Colour currentPlayer = null;
	private static Board current;
	Position initial1 = null;
	Position final_pos1 = null;
	Position intial = null;
	Position final_pos = null;
	Position i = null;
	Position f = null;
	
	
	public Agent22764884() {}
	
	/**
	 * This is a node class which represent a node in the tree. Each node holds a state with the current player, score and visit count which is in the state class.
	 * **/
	public class Node {
	    State state;
	    Node parent;
	    List<Node> childArray;
	    
	    /**
	     * Creates an empty node based on current board
	     * **/
	    public Node() {this.state = new State(current);childArray = new ArrayList<>();}

	    /**
	     * Creates a node with a given state.
	     * 
	     * @param State state to be copied
	     * **/
	    public Node(State state) {this.state = state;childArray = new ArrayList<>();}

	    /**
	     * Creates a node based on another node.
	     * 
	     * @param Node node to be copied.
	     * **/
	    public Node(Node node) throws CloneNotSupportedException {
	    	this.childArray = new ArrayList<>();
	        this.state = new State(node.getState());
	        if (node.getParent() != null)
	            this.parent = node.getParent();
	        List<Node> childArray = node.getChildArray();
	        for (Node child : childArray) {
	            this.childArray.add(new Node(child));
	        }
	    }

	    /**
	     * Method to return current state of node.
	     * 
	     * @return State state of the current node
	     * **/
	    public State getState() {return state;}

	    /**
	     * Method to get the parent of the given node.
	     * 
	     * @return Node parent of node
	     * **/
	    public Node getParent() {return parent;}

	    /**
	     * Method to set the given node to be the parent node.
	     * 
	     * @param Node node to be parent
	     * **/
	    public void setParent(Node parent) {this.parent = parent;}
	    
	    /**
	     * Method to return child array.
	     * 
	     * @return child array
	     * **/
	    public List<Node> getChildArray() {return childArray;}

	    /**
	     * Method to return a child node based on policies.
	     * 
	     * @return Node random child node
	     * **/
	    public Node getChildNode() {
	        int noOfPossibleMoves = this.childArray.size();
	        Node last = null;
	        int j = 0;
	        for(int i=0; i < noOfPossibleMoves; i++) {
	        	if(this.childArray.get(i).getState().getBoard().getWinner() == currentPlayer) {
	        		return this.childArray.get(i);
	        	}
	        	if(this.childArray.get(i).getState().getBoard().getCaptured(currentPlayer).size() > j) {
	        		j = this.childArray.get(i).getState().getBoard().getCaptured(currentPlayer).size();
	        		last = this.childArray.get(i);
	        	}
	        }
	        if(last == null) {
	        	int selectRandom = (int) (Math.random() * noOfPossibleMoves);
		        return this.childArray.get(selectRandom);
	        }
	        return last;
	    }
	    
	    /**
	     * Method to return the child with the highest score.
	     * 
	     * @return Node result with the maximum score
	     * **/
	    public Node getChildWithMaxScore() {
	    	Node result = null;
	    	int childs = this.childArray.size();
	    	double max = 0;
	    	for(Node node: this.childArray) {
	    		if (node.getState().getWinScore() > max) {
	    			max = node.getState().getWinScore();
	    			result = node;
	    		}
	    	}
	    	if(result == null) {
	    		int selectRandom = (int) (Math.random() * childs);
		        return this.childArray.get(selectRandom);
	    	}
	    	return result;
	    }

	}
	
	/**
	 * Tree class to have a tree structure with root and child
	 * **/
	public class Tree {
	    Node root;

	    /**
	     * Creates a new tree.
	     */
	    public Tree() {root = new Node();}

	    /**
	     * Method to return the current parent
	     * 
	     * @return Node parent of current node
	     */
	    public Node getRoot() {return root;}

	    /**
	     * Method to set the parent as given parent.
	     * 
	     * @param root node to be parent of current method
	     */
	    public void setRoot(Node root) {this.root = root;}

	}
	
	/**
	 * Helper class to encapsulate the state of each node.
	 * **/
	public class State {
	    private Board board;
	    private Colour player;
	    private int visitCount;
	    private double winScore;
	    private Position[] move = new Position[2];

	    /**
	     * Creates a new state based on the given state.
	     * 
	     * @param State state of the state to be copied.
	     * **/
	    public State(State state) throws CloneNotSupportedException {
	        this.board = (Board) state.getBoard().clone();
	        this.player = state.getPlayer();
	        this.visitCount = state.getVisitCount();
	        this.winScore = state.getWinScore();
	    }
	    
	    /**
	     * Creates a new state based on the given board.
	     * 
	     * @param Object board to be copied
	     * **/
	    public State(Object board) {this.board = (Board) board;}

	    /*
	     * Method to return the board of the current state.
	     * 
	     * @return Board board of current state
	     */
	    public Board getBoard() {return board;}
	    
	    /**
	     * Set the board of a state to be of the given board.
	     * 
	     * @param Object copy of board
	     * **/
	    public void setBoard(Object copy) {this.board = (Board) copy;}

	    /**
	     * Method to return the current player id.
	     * 
	     * @return Colour of player
	     * **/
	    public Colour getPlayer() {return player;}

	    /**
	     * Set the current player Colour to be of a given Colour.
	     * 
	     * @param Colour to be changed into
	     * **/
	    public void setPlayerColour(Colour player) {this.player = player;}

	    /**
	     * Method to get the opponent of the current Player.
	     * 
	     * @return Colour opponents colour
	     * **/
	    public Colour getOpponent() {
	    	if (this.player == Colour.BLUE) {
	    		return Colour.GREEN;
	    	}
	    	else if (this.player == Colour.GREEN) {
	    		return Colour.RED;
	    	}
	    	else {
	    		return Colour.BLUE;
	    	}
	    }
	    
	    /**
	     * Method to get the opponent of the current Player.
	     * 
	     * @return Colour[] opponents colours
	     * **/
	    public Colour[] getOpponents() {
	    	Colour[] colours = Colour.values();
	    	int index = 0;
			for(int i = 0; i < colours.length; i++) {
	    		if(colours[i] == this.board.getTurn()) {
	    			index = i;
	    		}
	    	}
			Colour[] last = new Colour[colours.length-1];
			for (int i = 0, k = 0; i < colours.length; i++) { 
	            if (i == index) { 
	                continue;
	            }
	            last[k++] = colours[i];
	        }
	    	return last;
	    }

	    /**
	     * Method to get the number of visit.
	     * 
	     * @return int counter of visit
	     * **/
	    public int getVisitCount() {return visitCount;}

	    /**
	     * Method to get the number of score after a simulation.
	     * 
	     * @return double score after a simulation
	     * **/
	    public double getWinScore() {return winScore;}

	    /**
	     * Method to change the score of the simulation after each back propagation.
	     * 
	     * @param int winScore to be changed
	     * **/
	    public void setWinScore(double winScore) {this.winScore = winScore;}

	    /**
	     * Method to get the next possible states by the current player and what moves are available.
	     * 
	     * @return List<State> possibleStates which are all the next possible states
	     * **/
	    public List<State> getAllPossibleStates() throws ImpossiblePositionException {
	    	Position[][] next = nextMoves(this.board);
	    	if (this.player == Colour.BLUE) {
		    	List<State> possibleStates = new ArrayList<>();
	    		for(int i=0; i < next.length; i++) {
	    			Object copy = null;
	    	    	try {
	    				copy = this.board.clone();
	    			} catch (CloneNotSupportedException e1) {
	    				//Do nothing
	    			}
		            State newState = new State(copy);
		            newState.setPlayerColour(Colour.GREEN);
		            newState.getBoard().move(next[i][0], next[i][1]);
		            newState.move[0] = next[i][0];
		            newState.move[1] = next[i][1];
		            possibleStates.add(newState);
	    		}
		        return possibleStates;
	    	}
	    	else if (this.player == Colour.GREEN) {
	    		List<State> possibleStates = new ArrayList<>();
	    		for(int i=0; i < next.length; i++) {
	    			Object copy = null;
	    	    	try {
	    				copy = this.board.clone();
	    			} catch (CloneNotSupportedException e1) {
	    				//Do nothing
	    			}
		            State newState = new State(copy);
		            newState.setPlayerColour(Colour.RED);
		            newState.getBoard().move(next[i][0], next[i][1]);
		            newState.move[0] = next[i][0];
		            newState.move[1] = next[i][1];
		            possibleStates.add(newState);
		        }
		        return possibleStates;
	    	}
	    	else {
	    		List<State> possibleStates = new ArrayList<>();
	    		for(int i=0; i < next.length; i++) {
	    			Object copy = null;
	    	    	try {
	    				copy = this.board.clone();
	    			} catch (CloneNotSupportedException e1) {
	    				//Do nothing
	    			}
		            State newState = new State(copy);
		            newState.setPlayerColour(Colour.BLUE);
		            newState.getBoard().move(next[i][0], next[i][1]);
		            newState.move[0] = next[i][0];
		            newState.move[1] = next[i][1];
		            possibleStates.add(newState);
		        }
		        return possibleStates;
	    	}
	    }

	    /**
	     * Method to increment the visit by 1.
	     * **/
	    public void incrementVisit() {this.visitCount++;}

	    /**
	     * Method to add the new score to the old score.
	     * 
	     * @param double score score to be added
	     * **/
	    public void addScore(double score) {if (this.winScore != Integer.MIN_VALUE) this.winScore += score;}

	    /**
	     * Method to simulate a play. This simulation will make each player to move the piece into the next best moves.
	     * **/
	    public void play() throws ImpossiblePositionException {
	    	int score = 0;
	        Colour whom = board.getTurn();
	        Position[] allPositions = board.getPositions(whom).toArray(new Position[0]);
	        for(Position position : allPositions){
	        	  Piece piece = board.getPiece(position);
	        	  Direction[][] steps = piece.getType().getSteps();
	        	  for(Direction[] outer : steps){
	        		  Position start = position;
	        		  Position end = position;      
	        		  try{         
	        			  if(!board.isLegalMove(start, end)){
	        				  end = board.step(piece, outer, start, start.getColour()!=end.getColour());
	        				  if(board.isLegalMove(start, end)){
	        					  Position[] allPositions_enemy = board.getPositions(end.getColour()).toArray(new Position[0]);
	        					  for(Position position_enemy : allPositions_enemy){
	        						  if(position_enemy == end){
	        							  Piece piece_enemy = board.getPiece(position_enemy);
	        							  int value = piece_enemy.getType().getValue();
	        							  if(value > score){
	        								  score = value;
	        								  intial = start;
	        								  final_pos = end;
	        							  }
	        						  }
	        					  }
	        				  }
	        			  }          
	        		  }    
	        		  catch(Exception e){
	        		  }
	        	  }
	          }
	          if(score == 0){
	        	  Position[] pieces = board.getPositions(board.getTurn()).toArray(new Position[0]);
	        	  Position start = pieces[0];
	        	  Position end = pieces[0];
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
	        		  	}
	        		  	catch(ImpossiblePositionException e){}
	        	  }
	        	  intial = start;
	        	  final_pos = end;
	          }
	       this.board.move(intial, final_pos);
	    }

	    /**
	     * Method to toggle the current player to be the next player.
	     * **/
	    public void togglePlayer() {
	    	if (this.player == Colour.BLUE) {
	    		this.player = Colour.GREEN;
	    	}
	    	else if (this.player == Colour.GREEN) {
	    		this.player =Colour.RED;
	    	}
	    	else {
	    		this.player = Colour.BLUE;
	    	}
	    }

	    /**
	     * Method to get the move done in the state.
	     * 
	     * @return Position[] the initial position which is stored in the first index and the final position which is stored in the second index
	     * **/
		public Position[] getMove() {return move;}

	}
	
	/**
	 * This is a helper function that selects the node that is the most promising value based on a UCT calculation formula.
	 * 
	 * @param Node node the node to select a promising child node
	 * 
	 * @return Node node the node selected
	 * **/
	private Node selectPromisingNode(Node rootNode) {
	    Node node = rootNode;
	    
	    //Start the selection
	    while (node.getChildArray().size() != 0) {
	        node = UCT.findBestNodeWithUCT(node);
	    }
	    return node;
	}
	
	/**
	 * This is a helper class to assist with selection
	 * **/
	public static class UCT {
		
		/**
		 * This method calculates the UCT value and returns it based on the total visit, score and total parent visit.
		 * 
		 * @param Double result which is the final UCT value
		 * **/
	    public static double uctValue(
	      int totalVisit, double nodeWinScore, int nodeVisit) {
	        if (nodeVisit == 0) {
	            return Integer.MAX_VALUE;
	        }
	        return (nodeWinScore / nodeVisit) 
	          + 2 * Math.sqrt(Math.log(totalVisit) / nodeVisit);
	    }
	    
	    /**
	     * This method finds the best node with the best UCT values.
	     * 
	     * @param Node node to compare
	     * 
	     * @return Node node to with best UCT value
	     * **/
	    public static Node findBestNodeWithUCT(Node node) {
	        int parentVisit = node.getState().getVisitCount();
	        return Collections.max(node.getChildArray(), Comparator.comparing(c -> uctValue(parentVisit, c.getState().getWinScore(), c.getState().getVisitCount())));
	    }
	}

	/**
	 * This is a helper function to expand the current node and get all the possible child node. It basically expands by creating a new node with different moves
	 * after from current board.
	 * 
	 * @param Node node the node to expand
	 * **/
    private void expandNode(Node node) throws ImpossiblePositionException {
    	//Get all the possible next state
        List<State> possibleStates = node.getState().getAllPossibleStates();
        
        possibleStates.forEach(state -> {
            Node newNode = new Node(state);
            newNode.setParent(node);
            newNode.getState().setPlayerColour(node.getState().getOpponent());
            node.getChildArray().add(newNode);
        });
    }
	
    /*
     * This is a helper function to back propagate the node and increment the visit counter and add the score of the child node to the parent node.
     * @param Node nodeToExplore the node that has been explored
     * @param int score the score needed to increment to parent after a simulation
     */
    private void backPropogation(Node nodeToExplore, int score) {
	    Node tempNode = nodeToExplore;
	    while (tempNode != null) {
	        tempNode.getState().incrementVisit();
            tempNode.getState().addScore(score);
	        tempNode = tempNode.getParent();
	    }
	}
	
    /**
     * This is a helper function to simulate a game of three player chess after a move is made and return a score.
     * This simulations assumes all the player will perform a move that will benefit them the most.
     * 
     * @param Node node the node needed to simulate
     * 
     * @return int score the score at the end of the game of the current player of the board
     * **/
    private int simulateRandomPlayout(Node node) {
        Node tempNode = null;
		try {
			tempNode = new Node(node);
		} catch (CloneNotSupportedException e1) {
			// Do nothing
		}
        State tempState = tempNode.getState();
        Colour turn = tempState.getBoard().getTurn();
        Colour[] opponents = tempState.getOpponents();
        int boardStatus = tempState.getBoard().score(turn);
        
        //If opponent set score to minimum
        if (turn == opponents[0]) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return boardStatus;
        }
        
        //If opponent set score to minimum
        if (turn == opponents[1]) {
        	tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
        	return boardStatus;
        }
        
        while (!tempState.getBoard().gameOver()) {
            tempState.togglePlayer();
            try {
				tempState.play();
			} catch (ImpossiblePositionException e) {
				//Do nothing
			}
        }
        
        //Reward 500 if the agent win
        if(tempState.getBoard().getWinner() == currentPlayer) {
        	return 500+tempState.getBoard().score(currentPlayer);
        }
        
        //Reward negative value if the agent lose
        else if(tempState.getBoard().getLoser() == currentPlayer) {
        	return (0-tempState.getBoard().score(currentPlayer));
        }
        else {
        	return 0;
        }
    }
	   
    /**
     * This method returns all the next possible moves.
     * @param board
     * @return
     * **/
    private Position[][] nextMoves(Board board) {
    	// Find all of our piece positions and all of the board nextPositions
    	Position[] pieces = board.getPositions(board.getTurn()).toArray(new Position[0]);
    	Position[] next = Position.values();
    	ArrayList<Position[]> nextMoves = new ArrayList<>();
    	for (Position piece : pieces) {
    	for (Position positions : next) {
    		// Move piece to position and test if legal
    		Position[] move = new Position[] {piece, positions};
        	if (board.isLegalMove(piece, positions) && !nextMoves.contains(move)) nextMoves.add(move);
      		}
    	}
    	return nextMoves.toArray(new Position[0][0]);
    }
	
	/**
	   * Play a move in the game. 
	   * The agent is given a Board Object representing the position of all pieces, the history of the game and whose turn it is. 
	   * They respond with a move represented by a pair (two element array) of positions: [start, end]
	   * @param board The representation of the game state.
	   * @return a two element array of Position objects, where the first element is the current position of the piece to be moved,
	   * and the second element is the position to move that piece to.
	   */
	@Override
	public Position[] playMove(Board board) {
		
		Object copy = null;
		try {
			copy = board.clone();
		} catch (CloneNotSupportedException e) {
			//Do nothing
		}
		
		//Test if the next moves is a win or not
		Position[][] moves = nextMoves(board);

		for(int i=0; i < moves.length; i++) {
			Object copy1 = null;
			try {
				copy1 = board.clone();
			} catch (CloneNotSupportedException e) {
			}
			try {
				((Board) copy1).move(moves[i][0], moves[i][1]);
			} catch (ImpossiblePositionException e) {
				//Do nothing
			}
			if (((Board) copy1).gameOver() == true) {
				return new Position[] {moves[i][0], moves[i][1]};
			}
		}
		
		currentPlayer = board.getTurn();    
		
		//Set Root
		Tree tree = new Tree();
		Node rootNode = tree.getRoot();
		rootNode.getState().setBoard(copy);
		if (((Board) copy).getTurn() == Colour.BLUE) {
			rootNode.getState().setPlayerColour(Colour.GREEN);
		}
		if (((Board) copy).getTurn() == Colour.GREEN) {
			rootNode.getState().setPlayerColour(Colour.RED);
		}
		if (((Board) copy).getTurn() == Colour.RED) {
			rootNode.getState().setPlayerColour(Colour.BLUE);
		}
		
		//Set the most optimal level
		int level = 15;
		
		//Perform MCTS
		while (level > 0) {
			//Selection
			Node promisingNode = selectPromisingNode(rootNode);
	        
			//Expansion
			if (promisingNode.getState().getBoard().gameOver() == false) {
				try {
					expandNode(promisingNode);
				} catch (ImpossiblePositionException e) {
					//Do nothing
				}
			}
			Node nodeToExplore = promisingNode;
            
			//Check if it is slowing the program or not
			if (promisingNode.getChildArray().size() > 0) {
                nodeToExplore = promisingNode.getChildNode();
            }
			
			//Simulation
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            //Back propagation
            backPropogation(nodeToExplore, playoutResult);
            level--;
        }
		
		//Get the final node and return the move
        Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
    	return winnerNode.getState().getMove(); 
		
	}
	
	/**
	 * @return the Agent's name, for annotating game description.
	 */
	public String toString() {return name;}

	/**
	   * Displays the final board position to the agent, 
	   * if required for learning purposes. 
	   * Other a default implementation may be given.
	   * @param finalBoard the end position of the board
	   * **/
	public void finalBoard(Board finalBoard) {}	

}
