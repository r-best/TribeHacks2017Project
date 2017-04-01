package states;

import events.EventQueue;
import java.awt.*;
import java.util.EmptyStackException;
import java.util.Stack;

import utils.KeyManager;
import utils.Preferences;

public class StateManager {

	private static StateStack<State> states;

	public static void init(){
		states = new StateStack<>(new GameState());
	}

	public static void update(){
		if(!(states.peek() instanceof DebugConsole) && KeyManager.checkKeyAndReset(192) /*tilde*/)
			push(new DebugConsole());

		if(!EventQueue.isEmpty())
			EventQueue.update();
		else
			states.peek().update();
	}

	public static void draw(Graphics2D graphics){
		states.peek().draw(graphics);
		if(!EventQueue.isEmpty())
			EventQueue.draw(graphics);
	}

	public static void push(State state){
		states.peek().onExit();
		states.push(state);
		states.peek().onEnter();
	}

	public static void pop(){
		states.peek().onExit();
		states.pop();
		states.peek().onEnter();
	}

	public static State peek(){
		return states.peek();
	}
	public static State peek(int distanceFromTop){ return states.peek(distanceFromTop); }

	public static class StateStack<T> extends Stack<T> {

		public StateStack(T firstObject){
			super();
			push(firstObject);
		}

		/**
		 * Takes in a parameter of the distance an item is from the top of the stack and returns that item
		 * For example peek(0) will return the top item of the stack, peek(1) will return the item underneath it, etc.
		 * If the distanceFromTop results in trying to retrieve an item from below the bottom of the stack, it will retrieve the bottom item
		 * @param distanceFromTop the location of the item relative to the top of the stack
		 * @return the item at the location determined by distanceFromTop
		 */
		public T peek(int distanceFromTop){
			if(size() == 0)
				throw new EmptyStackException();
			int x = size()-1-distanceFromTop;
			if(x < 0)
				x = 0;
			return elementAt(x);
		}
	}
}