# The Silver Manager

----

## Adding items for which classes already exist

The silver manager allows someone to quickly and easily add new items to the currency system. For items that already have classes, like titles, you can just add new instances of the object with different bits of data in them. This allows for certain items to perform actions upon being purchased, like changing a player's title, without adding more and more logic that repeats itself.

Generic types that do nothing also exist.

1. Add an enum constant to the ItemType constant that clearly defines what the item is. While it would in theory be better to define the item name in the enum itself, rather than just defining it in the definition of the item later, this is more of a TODO item than anything else.

2. In the method setupItems(), add the new item to the items ArrayList. An example format is below. The ArrayList itself takes a Item object. Most constructors have appropriately named variables, and any extra data is always added to the end. You can refer to the Item class for a basic constructor to use when defining items.



		NameChangeItem item = new NameChangeItem(ItemType.NAME_CHANGE, //Argument 1: The ItemType
		 "Name Change", //The pretty name displayed in game
		 ShopType.MISC, //A shop type selected from the ShopType enum.
		 false, //A simple boolean that indicates if worlds following the Pure guidelines are to show this item or not.
		 "Changes your name to a new one.", //A one line description of the item to convey what it is and what it does.
		 50000); //An integer representing the amount of currency to charge.
		items.add(new NameChangeItem(ItemType.NAME_CHANGE));



----

## Adding items for which a class doesn't exist or that requires extra logic

The silver manager takes care of adding items with little trouble, but can sometimes require extra logic. If an item needs to do something special to a player or an ingame function upon being purchased, this is when a new class is needed.

The class should, even if it only has one actual purchasable item associated with it, not define any constants inside itself. It should be constructible with all data values from the constructor, per convention. It should __extend__ Item to be considered valid.

Each Item object contains two methods:


	public abstract void runExtraData(ConversationContext context,
			Registration plugin);

	public abstract void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin);


The first method is used when the item is __either__ used or purchased. This is an executor that will change a player's data in some way or another.

The second method is used to notify players of the purchase. While this might seem redundant at first, it's designed to be as flexible as possible. What if lightning needs to go off?