using System.Drawing;
using Engine.Game.Screens;

namespace HeartsGame
{
	/// <summary>
	/// A rules window for the game of hearts.
	/// </summary>
	public partial class HeartsRules : RuleScreen
	{
		/// <summary>
		/// Creates a new hearts rules window.
		/// </summary>
		public HeartsRules() : base("The Rules of Hearts")
		{return;}

		/// <summary>
		/// Called when all other initialization logic is finished to provided extra initialization.
		/// </summary>
		protected override void Initialize()
		{
			AppendString("\tThe game of hearts is a trick taking game played with four people. The goal is to have the least number of points when another player reaches 100 points.",FontStyle.Regular,Color.Black);
			
			AppendString("\n\nDealing The Cards",FontStyle.Bold);
			AppendString("\n\tThirteen cards are dealt to each player.",FontStyle.Regular);

			AppendString("\n\nPassing Cards",FontStyle.Bold);
			AppendString("\n\tAfter the cards are dealt for a round players begin passing cards. The first round each player chooses three cards and passes them to the player on their left.",FontStyle.Regular);
			AppendString(" The second round each player passes instead to their right. The third round, across the table. The fourth round no passing occurs.");
			AppendString("\n\tThis repeats in four round cycles until the game ends.");

			AppendString("\n\nGameplay",FontStyle.Bold);
			AppendString("\n\tAfter passing is over, the round begins. The player holding the two of clubs leads the first trick with the two of clubs.",FontStyle.Regular);
			AppendString("\n\tPlay then proceeds to the leader's left until play returns to the leader. Each player plays a card from their hand. Players must follow suit if able.");
			AppendString(" Following suit entails playing a card of the same suit lead. If a player is not able to follow suit then they may play any card.");
			AppendString("\n\tOnce every player has played a card, the player who played the highest valued card on suit takes the trick (all the played cards). This player then leads the next trick with any card in their hand.");
			AppendString(" The round continues until all players have exhausted their hands.");
			AppendString("\n\tThere are some exceptions to what cards can be played.\n\tOn the first trick no point cards may be played until a player's hand consists of 13 point cards.");
			AppendString("\n\tA player can not lead a heart until they have been broken unless their hand consists entirely of hearts. Hearts are considered broken if a heart has been played in a previous trick.");

			AppendString("\n\tScoring",FontStyle.Bold);
			AppendString("\n\tAfter a round ends, players earn a point for each heart in a trick they took during the round. Additionally the player that took the queen of spades earns an extra 13 points. Thus there are 26 points available each round.");
			AppendString("\n\tIf a single player takes all 14 point cards (for a total of 26 points) that player instead earns 0 points and all other players earn 26 points. This is known as shooting or shooting the moon.");
			AppendString("\n\tThe game ends when a player reaches 100 points or more. The winner(s) is the player with the lowest score at this time.");
			AppendString("\n\tIf the option is enabled then if a player attains exactly 100 points their score is reset to 0 points and play continues.");

			return;
		}
	}
}
