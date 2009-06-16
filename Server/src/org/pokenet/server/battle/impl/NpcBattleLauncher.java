package org.pokenet.server.battle.impl;

import org.pokenet.server.backend.entity.NonPlayerChar;
import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.backend.entity.Positionable.Direction;
import org.pokenet.server.battle.DataService;

/**
 * When the player is challenged by an NPC, this class moves the player to the NPC and launches a battle
 * @author shadowkanji
 *
 */
public class NpcBattleLauncher implements Runnable {
	private NonPlayerChar m_npc;
	private PlayerChar m_player;
	
	/**
	 * Constructor
	 * @param n
	 * @param p
	 */
	public NpcBattleLauncher(NonPlayerChar n, PlayerChar p) {
		m_npc = n;
		m_player = p;
	}
	
	/**
	 * Moves the player to the npc and starts the battle
	 */
	public void run() {
		try {
			/*
			 * Set that the player is battling so we can control their movement.
			 * A movement request from the client could mess things up.
			 */
			m_player.setBattling(true);
			m_npc.talkToPlayer(m_player);
			/* Sleep for a moment */
			Thread.sleep(1000);
			/* Make the player face the npc */
			switch(m_npc.getFacing()) {
			case Up:
				m_player.setFacing(Direction.Down);
				break;
			case Down:
				m_player.setFacing(Direction.Down);
				break;
			case Left:
				m_player.setFacing(Direction.Down);
				break;
			case Right:
				m_player.setFacing(Direction.Down);
				break;
			}
			/* While the player isn't blocked, move the player towards the npc */
			while(m_player.forceMove()) {
				Thread.sleep(250);
			}
			/* Start the NPC battle */
			m_player.ensureHealthyPokemon();
			m_player.getSession().write("bi0");
			m_player.setBattleField(new NpcBattleField(DataService.getBattleMechanics(), m_player, m_npc));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the battle launcher
	 */
	public void start() {
		new Thread(this).start();
	}
}