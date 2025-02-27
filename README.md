
1. **Adhere to Planned Dates for Tech Debt and Ensured Compliance with Minimum Bar Standards**  
   - Ensured to verify 100% test coverage for the transaction component through JUnit tests using Mockito to mock dependencies, ensuring reliability and maintainability.  
   - Ensured to verify Notepad API responses against previous snapshots, detecting discrepancies and maintaining data integrity.  
   - Ensured to verify data security by validating access controls and ensuring sensitive information in transaction monitoring remains protected.  
   - Ensured to complete unit tests within the expected timeline and got them reviewed to address feedback and implement necessary changes promptly.
          
2. **Innovation & Tech Advancement**  
   - Ensured to verify and validate the working of the transaction service through tests to enhance it and in turn reduce reliance on the SAM8 UI for alert processing and to find any potential discrepancies in the results.  
   - Ensured to verify system reliability by implementing a validation mechanism for Notepad API responses, automating discrepancy detection across releases.  
   - Ensured to verify and enhance test automation by achieving 100% JUnit test coverage for the transaction component, ensuring robust and error-free deployments.  
   - Ensured to verify and support tech objectives by optimizing validation workflows, reducing manual effort, and improving data accuracy through automation.
 
3. **Proactive System Maintenance and Optimization**
- **To** ensure regular updates, patching, vulnerability management, system dependencies, and third-party risk to maintain system hygiene and stability.
- **To** set up proactive monitoring with alerts for prompt issue resolution, optimize batch processing time, improve incident management, and reduce problem records.
- **To** monitor infrastructure usage, optimize costs while ensuring performance through tuning and enhancements, and reduce the total cost of ownership.
- **To** strengthen data security by enforcing encryption, access controls, and validation mechanisms to protect sensitive information and maintain compliance.

   - Ensured to verify the robustness of the transaction component by writing JUnit tests using Mockito to simulate service interactions and validate expected behaviors under various conditions.  
   - Ensured to verify data consistency across releases by maintaining robust validation mechanisms, preventing discrepancies, and strengthening system reliability.  
   - Ensured to verify data security by implementing validation checks to prevent unauthorized access and ensuring encryption standards are maintained.  
   - Ensured to get unit tests and validation mechanisms regularly reviewed by team members, incorporating their feedback to enhance accuracy and efficiency.

  
4. To contribute to the project Ops Automation by leveraging my technical skills to improve and enhance the project.
   - **Enhanced Code Quality with JUnit Tests** – Ensured to verify close to 100% test coverage for the transaction component by writing comprehensive JUnit tests using Mockito, effectively mocking dependencies and validating expected behaviors to improve system reliability and maintainability.
   - **Strengthened System Stability** – Developed JUnit tests to cover various execution paths, edge cases, and failure scenarios in the transaction component, ensuring robust error handling and preventing potential issues before deployment.
   - **Implemented Validation Mechanisms** – Built a validation process for Notepad API responses by comparing them with previous snapshots, helping detect discrepancies and ensuring data consistency
   
5. To write high-quality, well-tested, and maintainable code that ensures reliability and efficiency in the project.
   - **Developed Comprehensive JUnit Tests** – Ensured to verify close to 100% test coverage for the transaction component using Mockito, improving code reliability and making it easier to detect and prevent potential issues.
   - **Ensured Code Maintainability** – Wrote clean, modular, and well-structured test cases, making future updates and enhancements more efficient while ensuring long-term stability of the project.
     
6.  To stay proactive in learning and applying the latest technologies to strengthen my technical expertise and contribute effectively to my team's success.
    - *Learned Spring Boot Fundamentals** – Gained an understanding of Spring Boot concepts, including dependency injection, REST API development, and application configuration, to improve backend development skills.
    - **Explored Mockito for Unit Testing** – Learned how to use Mockito for mocking dependencies in JUnit tests, enabling effective isolation of components and improving test reliability.
    
7. To embrace and apply Barclays’ values and mindsets in my daily work, ensuring collaboration, excellence, and continuous improvement in all tasks.
   - **To** integrate Respect, Integrity, Service, Excellence, and Stewardship in my work by collaborating with my team, maintaining transparency, and delivering meaningful value to stakeholders.
   - **To** embrace a challenge mindset by identifying opportunities for automation and improvement, taking ownership of tasks, and fostering a culture of empowerment through idea-sharing and team support.
  



i. ensure regular updates, patching, vulnerability management, system dependencies and TP risk

ii. proactive system maintenance - demonstrate improvement in batch time, incident management, problem records

1. Setup proactive monitoring with alerts to address system failures promptly

iii. Reduce total cost of ownership - monitor infrastructure usage and optimize costs while ensuring performance (tuning and optimization can ensure both performance and reduction costs)

iv. Strengthen data security, encryption and access controls to protect sensitive data



package com.barclays.nextgen.transaction.config;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.barclays.nextgen.utils.auth.CyberArkConfig;
import com.barclays.nextgen.utils.auth.CyberArkUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class AppCyberArkPropsTest {

    @Mock
    private CyberArkProps cyberArkProps;

    @Mock
    private TxnProperties txnProperties;

    @InjectMocks
    private AppCyberArkProps appCyberArkProps;

    @MockBean
    private CyberArkUtil cyberArkUtil;

    private CyberArkProps.DataSource dataSource;
    private CyberArkConfig nextgenConfig;
    private CyberArkConfig notificationConfig;
    private CyberArkConfig samcdConfig;
    private CyberArkConfig tiaaConfig;

    @BeforeEach
    void setUp() {
        dataSource = mock(CyberArkProps.DataSource.class);
        nextgenConfig = mock(CyberArkConfig.class);
        notificationConfig = mock(CyberArkConfig.class);
        samcdConfig = mock(CyberArkConfig.class);
        tiaaConfig = mock(CyberArkConfig.class);

        when(cyberArkProps.getDatasource()).thenReturn(dataSource);
        when(dataSource.getNextgen()).thenReturn(nextgenConfig);
        when(dataSource.getNotification()).thenReturn(notificationConfig);
        when(dataSource.getSamcd()).thenReturn(samcdConfig);
        when(cyberArkProps.getTiaa()).thenReturn(tiaaConfig);
    }

    @Test
    void testConstruct_WhenCyberArkPropsPresent() {
        when(nextgenConfig.getObject()).thenReturn("NextGenObject");
        when(notificationConfig.getObject()).thenReturn("NotificationObject");
        when(samcdConfig.getObject()).thenReturn("SamcdObject");
        when(tiaaConfig.getObject()).thenReturn("TiaaObject");

        when(CyberArkUtil.getPassword(any())).thenReturn("mockPassword");

        assertDoesNotThrow(() -> appCyberArkProps.construct());

        verify(txnProperties.getDatasource().getNextgen()).setPassword("mockPassword");
        verify(txnProperties.getDatasource().getNotification()).setPassword("mockPassword");
        verify(txnProperties.getDatasource().getSamcd()).setPassword("mockPassword");
        verify(txnProperties.getTiaa()).setPassword("mockPassword");
    }

    @Test
    void testConstruct_WhenCyberArkPropsMissing() {
        when(cyberArkProps.getDatasource()).thenReturn(null);

        assertDoesNotThrow(() -> appCyberArkProps.construct());
    }

    @Test
    void testConstruct_WhenCyberArkThrowsException() {
        when(nextgenConfig.getObject()).thenReturn("NextGenObject");
        when(CyberArkUtil.getPassword(any())).thenThrow(new RuntimeException("CyberArk failure"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> appCyberArkProps.construct());
        assertEquals("Failed to get password from CyberArk", exception.getMessage());
    }
}