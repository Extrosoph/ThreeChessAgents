package threeChess.agents;

import threeChess.*;

import java.util.Random;

/**
 * An interface for AI bots to implement.
 * They are simply given a Board object indicating the positions of all pieces, 
 * the history of the game and whose turn it is, and they respond with a move, 
 * expressed as a pair of positions.
 * **/ 
public class Daniel extends Agent{
  
  private static final String name = "Daniel";
  private static final Random random = new Random();

  Position do_start = null;
  Position do_end = null;

  /**
   * A no argument constructor, 
   * required for tournament management.
   * **/
  public Daniel(){
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
  public Position[] playMove(Board board){
          //Board copyBoard = (Board)board.clone();
          int score = 0;
          Colour whom = board.getTurn();
          Position[] allPositions = board.getPositions(whom).toArray(new Position[0]);
          //System.out.println("what is the piece?");
          for(Position position : allPositions){
            Piece piece = board.getPiece(position);
            //PieceType pieceType = piece.getType();
            //System.out.println("I am: " + piece.getType().toString() + ", value is: " + piece.getType().getValue());
            Direction[][] steps = piece.getType().getSteps();
            for(Direction[] outer : steps){
              for(Direction inner : outer){
                //System.out.println("All the posibble moves:" + inner.toString());
              }          
              Position start = position;
              Position end = position;
              //System.out.println("move from: " + start.toString());         
    try{         
          if(!board.isLegalMove(start, end)){
            end = board.step(piece, outer, start, start.getColour()!=end.getColour());
            //System.out.println("move to: " + end.toString());
            if(board.isLegalMove(start, end)){
              Position[] allPositions_enemy = board.getPositions(end.getColour()).toArray(new Position[0]);
              //System.out.println("---------------- testing0 ------------------");
              for(Position position_enemy : allPositions_enemy){
                //System.out.println(position_enemy);
                if(position_enemy == end){
                  Piece piece_enemy = board.getPiece(position_enemy);
                  int value = piece_enemy.getType().getValue();
                  //System.out.println("---------------- testing ------------------");
                  if(value > score){
                    score = value;
                    do_start = start;
                    do_end = end;
                }
               
                }
              }
            }
            //System.out.println("---------------- testing1 ------------------");
          }          
        }    
        catch(Exception e){
        }
      }
    }
    if(score == 0){
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
      do_start = start;
      do_end = end;
    }
    return new Position[] {do_start, do_end};
    // Position[] pieces = board.getPositions(board.getTurn()).toArray(new Position[0]);
    // Position start = pieces[0];
    // Position end = pieces[0]; //dummy illegal move
    // while (!board.isLegalMove(start, end)){
    //   start = pieces[random.nextInt(pieces.length)];
    //   Piece mover = board.getPiece(start);
    //   Direction[][] steps = mover.getType().getSteps();
    //   Direction[] step = steps[random.nextInt(steps.length)];
    //   int reps = 1 + random.nextInt(mover.getType().getStepReps());
    //   end = start;
    //   try{
    //     for(int i = 0; i<reps; i++)
    //       end = board.step(mover, step, end, start.getColour()!=end.getColour());
    //   }catch(ImpossiblePositionException e){}
    // }
    // return new Position[] {start,end};
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


