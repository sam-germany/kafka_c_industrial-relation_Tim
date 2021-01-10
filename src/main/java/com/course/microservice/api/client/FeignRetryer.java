package com.course.microservice.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.RetryableException;
import feign.Retryer;

public class FeignRetryer implements Retryer {

	private static final Logger LOG = LoggerFactory.getLogger(FeignRetryer.class);
	private long additionalBackoffInMillis;                  // 3sec
	int attempt = 0;

	private int maxAttempts;                        // 5

	public FeignRetryer() {
		this(3000, 5);          //(1)     this()   is called at the time of starting this application and it call
//		System.out.println("---------11");                                 // the argument consutructor  see nr (2)
	}

	private FeignRetryer(long additionalBackoffInMillis, int maxAttempts) {  // this constructor will be called by the no argument constructor and
//		System.out.println("-----22");                                        // the values as arguments will be passed to both  the arguments
		this.additionalBackoffInMillis = additionalBackoffInMillis;           // additionalBackoffInMillis,  maxAttampts
		this.maxAttempts = maxAttempts;
	}

	@Override
	public Retryer clone() {
//		System.out.println("-----33");
		return new FeignRetryer(additionalBackoffInMillis, maxAttempts);
	}

	@Override
	public void continueOrPropagate(RetryableException e) {
//		System.out.println("-------55");

		if (attempt++ >= maxAttempts) {
//			System.out.println("--------66");
			throw e;  }

		LOG.debug("Retrying feign, attempt {} of {}", attempt, maxAttempts);

		try {
//			System.out.println("-----44");
			Thread.sleep(additionalBackoffInMillis);
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}
}
/*
just run it one time easy to understand see down the output comes like this.

(1)
when we call the FeignClient()  to fetch the data then  .clone()  method will be called  and it create a new Instance
of this  custom FeignRetryer class  by calling the argument constructor. if the call is successfull in the first
attempt then no other method will be called by the  "Retryer interface"
(2)
But if the call does not successed then their is a infinite loop will be created till we call is successfull or we will
throw a exception, for this always   "continueOrPropagate(RetryableException e)"  this method is called infinetly
but as we put the logic of  "maxAttemps" then only for those times this method will be called and after that we
throw a Exception
(3)
Thread.sleep(additionalBackoffInMillis)       <-- on every call we want to wait for the few seconds then here the Thread
                                                will sleep before trying next time



 */