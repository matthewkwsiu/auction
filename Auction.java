import java.util.*; 

/**
 * A simple model of an auction.
 * The auction maintains a list of lots of arbitrary length.
 *
 * @author David J. Barnes and Michael Kolling.
 * @version 2006.03.30
 *
 * @author (of AuctionSkeleton) Lynn Marshall
 * @version 2.0
 * 
 * @author Matthew Siu
 * @version October 3, 2019
 * 
 */
public class Auction
{
    /** The list of Lots in this auction. */
    private ArrayList<Lot> lots;

    /** 
     * The number that will be given to the next lot entered
     * into this auction.  Every lot gets a new number, even if some lots have
     * been removed.  (For example, if lot number 3 has been deleted, we don't
     * reuse it.)
     */
    private int nextLotNumber;
    
    /**
     * A boolean to check if the auction is open or not
     */
    private boolean auctionOpen;
    
    /**
     * Create a new auction.
     */
    public Auction()
    {
        lots = new ArrayList<Lot>();
        nextLotNumber = 1;
        auctionOpen = true;
    }
    
    /**
     * This constructor takes
     * an Auction as a parameter.  Provided the auction parameter
     * is closed, the constructor creates a new auction containing
     * the unsold lots of the closed auction.  If the auction parameter
     * is still open or null, this constructor behaves like the
     * default constructor.
     */
    public Auction(Auction oldAuction)
    {
        //could make this more efficient,but not sure how to get around the null
        //problems
        if(oldAuction==null)
        {
            lots = new ArrayList<Lot>();
            nextLotNumber = 1;
            auctionOpen = true;
            return;
        }
        if(!oldAuction.auctionOpen) 
        // if the old auction is closed or null
        {
            //this sets the new lot to the unsold items from the old Auction
            lots = oldAuction.getNoBids();
            //This updates the nextLotNumber
            nextLotNumber = oldAuction.nextLotNumber;
        }
        else
        {
            lots = new ArrayList<Lot>();
            nextLotNumber = 1;
        }
        auctionOpen = true;
    }


    /**
     * Enter a new lot into the auction.  Returns false if the
     * auction is not open or if the description is null.
     *
     * @param description A description of the lot.
     * @return False if auction is closed or description is null. True otherwise
     */
    public boolean enterLot(String description)
    {
        if(!auctionOpen || description ==null)
        {
            //if the auction is not open or description is null, just return false
            return false;
        }
        lots.add(new Lot(nextLotNumber, description));
        nextLotNumber++;
        return true;
    }

    /**
     * Show the full list of lots in this auction.
     *
     */
    public void showLots()
    {
        System.out.println();
        
        if(lots.isEmpty())
        {
            System.out.println("There are no lots");
        }
        else
        {//should just run through and print off each lot
            for(Lot lot : lots) 
            {
                System.out.println(lot.toString());
            }
            //still need to add message to indiate no lots
        }
    }
    
    /**
     * This method attempts to bid for a lot. 
     * 
     * @param number The lot number being bid for.
     * @param bidder The person bidding for the lot.
     * @param value  The value of the bid.
     * @return false if bid is invalid, true otherwise
     */
    public boolean bidFor(int lotNumber, Person bidder, long value)
    {
          //checks if the auction is closed, if the lot doesn't exist, if the value is
          //not positive, or the bidder is null
          //note that this calls the getLot method, shown below
          if(!auctionOpen || getLot(lotNumber) == null || value < 0 || bidder == null)
          {
              return false;
          }
          //this tries to bid at the lotNumber
          if(getLot(lotNumber).bidFor(new Bid(bidder, value)))
          {
              System.out.println();
              System.out.println("Bid was successful");
              System.out.println("Lot Number: " + lotNumber);
              System.out.println("Bidder: " + bidder.getName());
              System.out.println("Value: " + value);
              return true;
          }
          //if it fails, then print this message
          System.out.println();
          System.out.println("Bid was not successful");
          System.out.println("Lot Number: " + lotNumber);
          System.out.println("Value: " + value);
          return true;
    }


    /**
     * Return the lot with the given number. 
     * Do not assume that the lots are stored in numerical order.   
     *   
     * Returns null if the lot does not exist.
     *
     * @param lotNumber The number of the lot to return.
     *
     * @return the Lot with the given number
     */
    public Lot getLot(int lotNumber)
    {
        //run through the lots, compare the ids, if there is a lot with the id, 
        //return the lot
        for(Lot lot : lots)
        {
            if(lot.getNumber() == lotNumber)
            {
                return lot;
            }
        }
        //otherwise, return null
        return null; 
    }
    
    /**
     * Closes the auction and prints information on the lots.
     * First print a blank line.  Then for each lot,
     * its number and description are printed.
     * If it did sell, the high bidder and bid value are also printed.  
     * If it didn't sell, print that it didn't sell.
     *
     * @return false if the auction is already closed, true otherwise.
     * 
     * 
     * 
     */
    public boolean close()
    {
        //checks if auction is open, returns false if it is
        if(!auctionOpen)
        {
            return false;
        }
        System.out.println();
        //runs through every
        for(Lot lot : lots)
        {
            System.out.println("Lot Number: " + lot.getNumber());
            System.out.println("Description: " + lot.getDescription());
            if(lot.getHighestBid() == null)
            {
                System.out.println("Item did not sell");
            }
            else
            {
                System.out.println("Highest bidder: " + lot.getHighestBid().getBidder().getName());
                System.out.println("Bid value: " + lot.getHighestBid().getValue());
            }
        }
        auctionOpen = false;
        return true;
    }
    
    /**
     * Returns an ArrayList containing all the items that have no bids so far.
     * (or have not sold if the auction has ended).
     * 
     * @return an ArrayList of the Lots which currently have no bids
     */
    public ArrayList<Lot> getNoBids()
    {
        ArrayList<Lot> newLot = new ArrayList<Lot>();
        for(Lot lot : lots)
        {
            //check if there was a bid placed
            if(lot.getHighestBid() == null)
            {
                //if there is no bid, then add this lot to the new ArrayList
                newLot.add(lot);
            }
        }
        //at the end, return the new lot
        return newLot;
    }
    
    /**
     * Remove the lot with the given lot number, as long as the lot has
     * no bids, and the auction is open.  
     * 
     * @param number The number of the lot to be removed.
     * @return true if successful, false otherwise (auction closed,
     * lot does not exist, or lot has a bid).
     */
    public boolean removeLot(int number)
    {
        
        Lot l;
        //checks to see if the auction is open        
        if(!auctionOpen)
        {
            return false;
        }
        //sets up an iterator object
        Iterator<Lot> iter = lots.iterator();
        //iterates over the array list
        while(iter.hasNext())
        {
            l = iter.next();
            if(l.getNumber() == number && l.getHighestBid() == null)
            {
                iter.remove();
                return true;
            }
            
        }
        
        //if the lot number doesn't exist
        return false;
    }
          
}
