package kr.co.green.common.transaction;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TransactionHandler {
	private final PlatformTransactionManager transactionManager;
	
	@Autowired
	public TransactionHandler(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public HashMap<String, Object> getStatus() {
		// 트랜잭션 기본 설정
		DefaultTransactionDefinition transactionDeifinition = new DefaultTransactionDefinition();
		
		// 트랜잭션의 격리 수준을 기본으로
		transactionDeifinition.setIsolationLevel(transactionDeifinition.ISOLATION_DEFAULT);
		
		// 트랜잭션 동작 설정 (이미 존재할 경우 참여, 없으면 새로운 트랜잭션 생성)
		transactionDeifinition.setPropagationBehavior(transactionDeifinition.PROPAGATION_REQUIRED);
		
		// 트랜잭션 시작
		TransactionStatus status = transactionManager.getTransaction(transactionDeifinition);
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("status", status);
		result.put("transactionManager", transactionManager);
		return result;
	}
	
	
	
	
}
