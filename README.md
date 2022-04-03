## Crypto.com Assessment
Given [Candle Stick API](https://exchange-docs.crypto.com/#public-get-candlestick) , please design and
develop a **Java Application with JUnit5 unit test cases** to verify the consistency of the output of [Trade API](https://exchange-docs.crypto.com/#public-get-trades).

## Restriction 
Trades API size always be 200 and all are within 1 min, it's hard to test behavior by using real API
> We should design test cases to test our behavior

## Test Cases Design
1. One Trade vs One CandleStick in the same time slot
2. Two Trades vs One CandleStick in the same time slot
3. Multi-Trades vs One CandleStick in the same time slot
4. Multi-Trades vs Multi-CandleSticks in different time slots

## Solution Introduction
I created one web application(by Spring Boot) to have API connections and verification
1. API Connections:\
   Get real data from crypto.com by above two [Candle Stick API](https://exchange-docs.crypto.com/#public-get-candlestick) and [Trade API](https://exchange-docs.crypto.com/#public-get-trades)
2. Verification:\
   Repeat below steps until there is no left candleSticks or trades.\
   Since we have to compare each time slot of candleStick, so we should get begin and end time of this time slot. 
   Extract each trade time to compare the current time slot, if this trade is in this slot, we update to our tradeStick*. 
   Once there is no trade or trade time is before previous time, then end the loop. 
   Store as the resultMap** to check verified result of each time slot.
   
   > \**tradeStick* : It's used to build the temporary candle stick result from multi-trades which are in the same slot.\
   > \*\**resultMap* : key is begin time of this time slot, value is verification result (true or false)
3. Test Behavior:
   - Create Web API endpoint to quick test result and behavior
   - Create Test Cases to simulate above mentioned test cases

## Test Steps
1. unzip the file 
2. 

 


