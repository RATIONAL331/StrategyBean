# StrategyBean

Spring Boot 환경에서 **동적 전략 패턴(Dynamic Strategy Pattern)** 을 간편하게 구현할 수 있는 프레임워크입니다.  
런타임에 비즈니스 로직을 결정하는 상황에서 복잡한 if-else 분기문을 제거하고, 확장 가능한 아키텍처를 제공합니다.

## 🎯 핵심 기능

- **어노테이션 기반 설정**: `@StrategyBean`, `@StrategyKey`로 간단한 설정
- **SpEL 지원**: Spring Expression Language를 통한 유연한 키 매핑
- **Fallback 처리**: 매칭되지 않는 키에 대한 기본 전략 지원
- **Type Safe**: 컴파일 타임 타입 안정성 보장

## 🔄 AS-IS vs TO-BE

### AS-IS: 기존 전략 패턴 구현 방식

기존에는 전략 패턴을 구현하기 위해 복잡한 Factory 패턴이나 긴 if-else 분기문을 사용해야 했습니다.

```java
// 기존 방식: Factory Pattern + if-else 분기
@Service
public class PaymentServiceFactory {
    
    @Autowired
    private CardPaymentHandler cardHandler;
    
    @Autowired 
    private CashPaymentHandler cashHandler;
    
    @Autowired
    private DefaultPaymentHandler defaultHandler;
    
    public PaymentHandler getHandler(PayType payType) {
        if (payType == PayType.CARD) {
            return cardHandler;
        } else if (payType == PayType.CASH) {
            return cashHandler;
        } else {
            return defaultHandler;  // fallback
        }
    }
}

@Service 
public class PaymentService {
    
    @Autowired
    private PaymentServiceFactory factory;
    
    public String processPayment(PaymentRequest request) {
        PaymentHandler handler = factory.getHandler(request.getType());
        return handler.handle(request);
    }
}
```

**문제점:**
- 복잡한 분기 로직으로 인한 가독성 저하
- 런타임 에러 가능성 (null 반환, 잘못된 타입 캐스팅)
- 보일러플레이트 코드 증가

### TO-BE: StrategyBean 사용 방식

StrategyBean을 사용하면 어노테이션 기반으로 간단하고 명확하게 전략 패턴을 구현할 수 있습니다.

```java
@StrategyBean(
    value = {
        @StrategyBean.Mapping(mappingKey = "CARD", targetClass = CardPaymentHandler.class),
        @StrategyBean.Mapping(mappingKey = "CASH", targetClass = CashPaymentHandler.class)
    },
    orElse = @StrategyBean.Otherwise(targetClass = DefaultPaymentHandler.class)
)
public interface PaymentService {
    // Enum 파라미터 직접 사용
    String handle(@StrategyKey PayType type);
    
    // 객체 내부 필드 참조 (SpEL)
    String handleRequest(@StrategyKey("#root.type") PaymentRequest request);
    
    // 짧은 SpEL 표현식
    String handleRequestShort(@StrategyKey("type") PaymentRequest request);
}

// 실제 구현체들
@Component
public class CardPaymentHandler implements PaymentService {
    public String handle(PayType type) { return "CARD 결제 처리"; }
    public String handleRequest(PaymentRequest req) { return "CARD 결제 처리"; }
    public String handleRequestShort(PaymentRequest req) { return "CARD 결제 처리"; }
}

@Component  
public class CashPaymentHandler implements PaymentService {
    public String handle(PayType type) { return "CASH 결제 처리"; }
    public String handleRequest(PaymentRequest req) { return "CASH 결제 처리"; }
    public String handleRequestShort(PaymentRequest req) { return "CASH 결제 처리"; }
}
```

**장점:**
- ✅ **확장성**: 새로운 결제 유형 추가 시 구현체만 추가하면 됨 (OCP 준수)
- ✅ **가독성**: 선언적 어노테이션으로 의도가 명확함
- ✅ **유지보수성**: Factory 클래스나 분기 로직 불필요
- ✅ **SpEL 지원**: 복잡한 객체 구조에서도 유연한 키 추출

## 🚀 빠른 시작

### 1. 프로젝트 설정

```java
@SpringBootApplication
@EnableStrategyBeanScan  // StrategyBean 스캐닝 활성화
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 2. StrategyBean 인터페이스 정의

```java
@StrategyBean(
    value = {
        @StrategyBean.Mapping(mappingKey = "CARD", targetClass = CardPaymentHandler.class),
        @StrategyBean.Mapping(mappingKey = "CASH", targetClass = CashPaymentHandler.class)
    },
    orElse = @StrategyBean.Otherwise(targetClass = DefaultPaymentHandler.class)
)
public interface PaymentService {
    String processPayment(@StrategyKey PayType payType);
}
```

### 3. 구현체 작성

```java
@Component
public class CardPaymentHandler implements PaymentService {
    @Override
    public String processPayment(PayType payType) {
        return "카드 결제를 처리합니다.";
    }
}

@Component
public class CashPaymentHandler implements PaymentService {
    @Override
    public String processPayment(PayType payType) {
        return "현금 결제를 처리합니다.";
    }
}

@Component
public class DefaultPaymentHandler implements PaymentService {
    @Override
    public String processPayment(PayType payType) {
        return "지원하지 않는 결제 방식입니다.";
    }
}
```

### 4. 사용

```java
@RestController
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;  // 프록시가 주입됨
    
    @PostMapping("/payment")
    public String payment(@RequestBody PaymentRequest request) {
        // 런타임에 PayType에 따라 적절한 구현체가 선택됨
        return paymentService.processPayment(request.getType());
    }
}
```

## 📚 상세 기능

### @StrategyKey SpEL 표현식

다양한 방식으로 전략 키를 추출할 수 있습니다:

```java
@StrategyBean(
    value = {
        @StrategyBean.Mapping(mappingKey = "CARD", targetClass = CardPaymentHandler.class),
        @StrategyBean.Mapping(mappingKey = "CASH", targetClass = CashPaymentHandler.class)
    }
)
public interface PaymentService {
    // 1. 직접 파라미터 사용
    String handle(@StrategyKey PayType type);
    
    // 2. 객체 필드 참조 (전체 SpEL)
    String handle(@StrategyKey("#root.paymentType") PaymentRequest request);
    
    // 3. 짧은 SpEL (root 생략)
    String handle(@StrategyKey("paymentType") PaymentRequest request);
    
    // 4. 메소드 레벨에서 파라미터 인덱스 사용
    @StrategyKey("#root[0]")  // 첫 번째 파라미터 사용
    String handle(PayType type, String additionalInfo);
    
    // 5. 단순 String 사용
    String handle(@StrategyKey String string);
}
```

### Fallback 처리

매칭되지 않는 키에 대한 기본 처리를 설정할 수 있습니다:

```java
@StrategyBean(
    value = { 
        @StrategyBean.Mapping(mappingKey = "CARD", targetClass = CardPaymentHandler.class)
    },
    orElse = @StrategyBean.Otherwise(targetClass = DefaultHandler.class)
)
public interface MyService {
    // POINT 타입은 매핑되지 않았으므로 DefaultHandler가 처리함
}
```

## 🏗️ 아키텍처

StrategyBean은 Spring의 BeanPostProcessor와 ProxyFactory를 활용하여 구현됩니다:

1. **스캐닝**: `@StrategyBean` 어노테이션이 붙은 인터페이스 탐지
2. **프록시 생성**: 인터페이스에 대한 동적 프록시 생성
3. **라우팅**: 런타임에 `@StrategyKey` 값에 따라 적절한 구현체로 라우팅
4. **Fallback**: 매칭되지 않는 경우 기본 구현체 사용
