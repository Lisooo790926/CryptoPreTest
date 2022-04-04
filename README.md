## Crypto.com Assignment
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
2. Verification:
   - Since we have to compare each time slot of candleStick, so we should loop each candleSticks.
     First, get begin and end time of this time slot. 
     Extract each trade time to compare the current time slot, if this trade is in this slot, we update to our tradeStick*.
     <img src="https://user-images.githubusercontent.com/48560984/161411744-a105ca76-09bf-497f-ac69-93a6e61f3eee.png" width=600/>
   - Once there is no trade or trade time is before previous time, then end the loop. 
     Store verified result into resultMap**.
     <img src="https://user-images.githubusercontent.com/48560984/161411797-903543b0-b590-4ce5-844c-aa4ab528f9e3.png" width=600/>
     
   - Repeat above steps until there is no left candleSticks or trades, then we get below result.
     ```json
         [
            {candlestick1 begin time :result1 },
            {candlestick2 begin time :result2 },
            {candlestick3 begin time :result3 },
            {....}
         ]
     ```
   > \**tradeStick* : It's used to build the temporary candle stick result from multi-trades which are in the same slot.\
   > \*\**resultMap* : key is begin time of this time slot, value is verification result (true or false)
3. Test Behavior:
   - Create Web API endpoint to quick test result and behavior
   - Create Test Cases to simulate above mentioned test cases

## Test Steps And Results
1. unzip the file 
2. `cd CryptoPreTest`
3. `mvn install`, you also could see the test case result after installing    
4. If you want to open API to test go through this steps:
   ```bash
      cd target
      java -jar ./cryptopretest-0.0.1-SNAPSHOT.jar
      # test by using below
      curl --location --request GET 'http://localhost:8081/crypto/verify?instrumentName=BTC_USDT&timeframe=1m'
      # you will always get one slot with false, because we could not have whole one minunte data to check.
   ```
   <img src="https://user-images.githubusercontent.com/48560984/161411002-88150e98-e017-4f7f-bcf0-149771cf694d.png" width=600/>
5. Run the test case separately
   ```bash
      cd ../
      mvn test
      # you will get success result for 10 test cases (100% coverage)
   ```
   <img src="https://user-images.githubusercontent.com/48560984/161410949-ec29c82f-d34c-4027-91a0-4da60fc54daa.png" width=600/>
    

 


