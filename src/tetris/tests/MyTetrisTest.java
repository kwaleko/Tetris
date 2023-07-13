package tetris.tests;

import static org.junit.Assert.assertNotNull;


import org.junit.Test;

///import tetris.Implementations.BoardImplementation;
//import tetris.Implementations.PieceFactoryImplementation;

import tetris.game.MyTetrisFactory;


/**
 * Within this class and/or package you can implement your own tests.
 *
 * Note that no classes or interfaces will be available, except those initially
 * provided. Make use of {@link MyTetrisFactory} to get other factory instances.
 */
public class MyTetrisTest {

	private final static long seed = 343681;


	@Test
	public void test() {
		assertNotNull(MyTetrisFactory.createBoard(MyTetrisFactory.DEFAULT_ROWS, MyTetrisFactory.DEFAULT_COLUMNS));
	}

	
	






}
