package za.co.enl.acc_num_gen.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.enl.acc_num_gen.models.*;

import za.co.enl.acc_num_gen.models.responses.*;
import za.co.enl.acc_num_gen.repository.AccountNumberGenRepo;
import za.co.enl.acc_num_gen.util.AccGenUtil;
import za.co.enl.acc_num_gen.util.AccountNumberGenerator;
import za.co.enl.acc_num_gen.util.AppUtil;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AccountNumberGeneratorService {

    @Value("${egroupx.numberOfThreads}")
    private int numOfThreads;

    @Value("${egroupx.processTimeoutInSec}")
    private int processTimeoutInSec;

    @Value("${egroupx.accountWeightings}")
    private String accountWeightings;

    @Value("${egroupx.accounts.current.mobi.accNumbersTobeReserved}")
    private int mobiAccNumbersTobeReserved;
    @Value("${egroupx.accounts.current.mobi.accNumberPrefix}")
    private String mobiAccNumberPrefix;

    @Value("${egroupx.accounts.current.ezi.accNumbersTobeReserved}")
    private int eziAccNumbersTobeReserved;
    @Value("${egroupx.accounts.current.ezi.accNumberPrefix}")
    private String eziAccNumberPrefix;

    @Value("${egroupx.accounts.current.biz.accNumbersTobeReserved}")
    private int bizAccNumbersTobeReserved;
    @Value("${egroupx.accounts.current.biz.accNumberPrefix}")
    private String bizAccNumberPrefix;

    @Value("${egroupx.accounts.savings.sevi.accNumbersTobeReserved}")
    private int seviAccNumbersTobeReserved;
    @Value("${egroupx.accounts.savings.sevi.accNumberPrefix}")
    private String seviAccNumberPrefix;

    @Value("${egroupx.accounts.loan.loan.accNumbersTobeReserved}")
    private int loanAccNumbersTobeReserved;
    @Value("${egroupx.accounts.loan.loan.accNumberPrefix}")
    private String loanAccNumberPrefix;

    @Value("${egroupx.accounts.partners.shembe.accNumbersTobeReserved}")
    private int shembeAccNumbersTobeReserved;
    @Value("${egroupx.accounts.partners.shembe.accNumberPrefix}")
    private String shembeAccNumberPrefix;

    @Value("${egroupx.accounts.partners.doe.accNumbersTobeReserved}")
    private int doeAccNumbersTobeReserved;
    @Value("${egroupx.accounts.partners.doe.accNumberPrefix}")
    private String doeAccNumberPrefix;

    @Value("${egroupx.accounts.other.other.accNumbersTobeReserved}")
    private int otherAccNumbersTobeReserved;
    @Value("${egroupx.accounts.other.other.accNumberPrefix}")
    private String otherAccNumberPrefix;

    private final String TIMEOUT_ERROR = "Process timeout some accounts will not be processed.";

    private final Logger LOG = LoggerFactory.getLogger(AccountNumberGeneratorService.class);

    @Autowired
    private final AccountNumberGenRepo iAccountNumberRepository;

    public AccountNumberGeneratorService(AccountNumberGenRepo iAccountNumberRepository) {
        this.iAccountNumberRepository = iAccountNumberRepository;
    }

    public AccountNumber saveAccountNumber(AccountNumber accNumber){
        return iAccountNumberRepository.save(accNumber);
    }

    public ResponseEntity<APIResponse> getAvailableAccountNumbers(){
        List<AvailableAccNumResponses> availableResponses= iAccountNumberRepository.getAvailableAccountNumbers();

        AvailableAccNumGenResponse accNumGenResponse = new AvailableAccNumGenResponse();
        accNumGenResponse.setAccTypes(new ArrayList<>());

        Map<String, List<AvailableAccNumResponses>> groupedAccTypes = availableResponses.stream().collect(Collectors.groupingBy(AvailableAccNumResponses::getAccountType));

        groupedAccTypes.forEach((accountType, accList)->{

            List<AvailableAccNumResponses>  formmatedResponses = new ArrayList<>();

            AvailableAccountTypes availableAccType = new AvailableAccountTypes();

            long totalAvailableAccountNumbersByAccountType = accList.stream().mapToLong(AvailableAccNumResponses::getAvailableAccountNumbers).sum();
            availableAccType.setAccType(accountType.toUpperCase() + " - available account numbers is "+ totalAvailableAccountNumbersByAccountType );

            for (AvailableAccNumResponses accTypeRes : accList) {


                AvailableAccNumResponses availableAcc = new AvailableAccNumResponses(accTypeRes.getAccountType(), accTypeRes.getAccountSubType(), accTypeRes.getAvailableAccountNumbers(), 0L);
                AccountNames accName = AccGenUtil.getAccountNameFromAccName(accTypeRes.getAccountSubType().toLowerCase());

                switch (Objects.requireNonNull(accName)) {
                    case mobi:
                        availableAcc.setMinAccNumToBeReserved((long) mobiAccNumbersTobeReserved);
                        break;
                    case ezi:
                        availableAcc.setMinAccNumToBeReserved((long) eziAccNumbersTobeReserved);
                        break;
                    case biz:
                        availableAcc.setMinAccNumToBeReserved((long) bizAccNumbersTobeReserved);
                        break;
                    case sevi:
                        availableAcc.setMinAccNumToBeReserved((long) seviAccNumbersTobeReserved);
                        break;
                    case shembe:
                        availableAcc.setMinAccNumToBeReserved((long) shembeAccNumbersTobeReserved);
                        break;
                    case doe:
                        availableAcc.setMinAccNumToBeReserved((long) doeAccNumbersTobeReserved);
                        break;
                    case loan:
                        availableAcc.setMinAccNumToBeReserved((long) loanAccNumbersTobeReserved);
                        break;
                    case other:
                        availableAcc.setMinAccNumToBeReserved((long) otherAccNumbersTobeReserved);
                        break;
                    default:
                        break;
                }
                formmatedResponses.add(availableAcc);
            }
            availableAccType.setAccTypes(formmatedResponses);
            accNumGenResponse.getAccTypes().add(availableAccType);

        });

       /* for (AvailableAccNumResponses accTypeRes : availableResponses) {
            AvailableAccNumResponses availableAcc = new AvailableAccNumResponses(accTypeRes.getAccountType(),accTypeRes.getAccountSubType(), accTypeRes.getAvailableAccountNumbers(), 0L);
            AccountNames accName = AccGenUtil.getAccountNameFromAccName(accTypeRes.getAccountSubType().toLowerCase());
            switch (Objects.requireNonNull(accName)) {
                case mobi:
                    availableAcc.setMinAccNumToBeReserved((long) mobiAccNumbersTobeReserved);
                    break;
                case ezi:
                    availableAcc.setMinAccNumToBeReserved((long) eziAccNumbersTobeReserved);
                    break;
                case biz:
                    availableAcc.setMinAccNumToBeReserved((long) bizAccNumbersTobeReserved);
                    break;
                case sevi:
                    availableAcc.setMinAccNumToBeReserved((long) seviAccNumbersTobeReserved);
                    break;
                case shembe:
                    availableAcc.setMinAccNumToBeReserved((long) shembeAccNumbersTobeReserved);
                    break;
                case doe:
                    availableAcc.setMinAccNumToBeReserved((long) doeAccNumbersTobeReserved);
                    break;
                case loan:
                    availableAcc.setMinAccNumToBeReserved((long) loanAccNumbersTobeReserved);
                    break;
                case other:
                    availableAcc.setMinAccNumToBeReserved((long) otherAccNumbersTobeReserved);
                    break;
                default:
                    break;
            }
           // formmatedResponses.add(availableAcc);

        }*/
        //long totalAccountNumbersTobeReserved = accNumGenResponse..stream().mapToLong(AvailableAccNumResponses::getMinAccNumToBeReserved).sum();
        long totalAvailableAccountNumbers = availableResponses.stream().mapToLong(AvailableAccNumResponses::getAvailableAccountNumbers).sum();



       accNumGenResponse.setMessage("Total available account numbers is "+ totalAvailableAccountNumbers);
        //accNumGenResponse.setAccTypes(formmatedResponses);
        return ResponseEntity.ok(new APIResponse(200,"Request completed",accNumGenResponse,Instant.now()));
    }

    public ResponseEntity<APIResponse> generateAccountNumbers() {

        List<AccountTypeDetails> accountTypeList = buildAccountTypes();
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        long startTime = System.nanoTime();

        try{
            outerLoop:
            for (AccountTypeDetails accountTypeDetails : accountTypeList) {

                final int availableAccountNumbers = iAccountNumberRepository.getAvailableAccountNumbersByAccountType(accountTypeDetails.getAccType().toUpperCase());
                final int accountNumbersToBeGenerated = accountTypeDetails.getAccTobeReserved() - availableAccountNumbers;

                if (accountNumbersToBeGenerated > 0) {

                    AtomicInteger remainingTasks = new AtomicInteger(accountNumbersToBeGenerated);

                    //weightings = 1234567890; account number = 8641975320;
                    AccountNumberGenerator accountNumberGenerator = new AccountNumberGenerator(accountWeightings, 11);
                    // Create a fixed thread pool with 5 threads
                    // Create 5 tasks for the threads
                    for (int i = 1; i <= numOfThreads; i++) {
                        if (processTimeoutInSec > ((System.nanoTime() - startTime) / 1_000_000_000.0)) {
                            executor.submit(() -> {
                                while (remainingTasks.get() > 0) {
                                    if (processTimeoutInSec <= ((System.nanoTime() - startTime) / 1_000_000_000.0)) {
                                        LOG.error(TIMEOUT_ERROR);
                                        executor.shutdownNow();
                                        throw new RuntimeException("Process timeout exception");
                                    }
                                    try {
                                        final String accNumber = accountNumberGenerator.generate(accountTypeDetails.getAccPrefix());
                                        final AccountNumber accountNumber = new AccountNumber(null, accNumber, accountTypeDetails.getAccType().toUpperCase(), accountTypeDetails.getAccSubType(), true);
                                        saveAccountNumber(accountNumber);
                                        remainingTasks.getAndDecrement();

                                    } catch (Exception ex) {
                                        LOG.error(MessageFormat.format("Failed to save account {0}", ex.getMessage()));
                                    }
                                }
                            });
                        } else {
                            LOG.error(TIMEOUT_ERROR);
                        }
                    }
                } else {
                    LOG.info(MessageFormat.format("{0} - There are enough account numbers available in the reserves.", accountTypeDetails.getAccSubType().toUpperCase()));
                }
            }
        }catch(Exception ex){
                LOG.error("Process error "+ ex.getMessage());
        }

        // Shutdown the executor
        executor.shutdown();

        List<AccountTypeResponse> accountTypeResponses = new ArrayList<>();

        accountTypeResponses.add(new AccountTypeResponse(AccountNames.mobi.name(),mobiAccNumbersTobeReserved + " mobi account numbers will be available in the reserves"));
        accountTypeResponses.add(new AccountTypeResponse(AccountNames.ezi.name(),eziAccNumbersTobeReserved + " ezi account numbers will be available in the reserves"));
        accountTypeResponses.add(new AccountTypeResponse(AccountNames.biz.name(),bizAccNumbersTobeReserved + " biz account numbers will be available in the reserves"));
        accountTypeResponses.add(new AccountTypeResponse(AccountNames.sevi.name(),seviAccNumbersTobeReserved + " sevi account numbers will be available in the reserves"));
        accountTypeResponses.add(new AccountTypeResponse(AccountNames.loan.name(),loanAccNumbersTobeReserved + " loan account numbers will be available in the reserves"));
        accountTypeResponses.add(new AccountTypeResponse(AccountNames.shembe.name(),shembeAccNumbersTobeReserved + " shembe account numbers will be available in the reserves"));
        accountTypeResponses.add(new AccountTypeResponse(AccountNames.doe.name(),doeAccNumbersTobeReserved + " doe account numbers will be available in the reserves"));
        accountTypeResponses.add(new AccountTypeResponse(AccountTypes.other.name(),otherAccNumbersTobeReserved + "  account numbers will be available in the reserves"));

        AccGenResponse accGenResponse = new AccGenResponse(
                "The below accounts type reserves will be filled up if not full. Use endpoint GET /enl/account-num/generator/availableAccountNumbersCount to check account types in the reserves",
                accountTypeResponses
        );
        return ResponseEntity.ok().body(new APIResponse(200,"Request is in progress",accGenResponse, Instant.now()));

    }


    public ResponseEntity<APIResponse> generateAccountNumber(AccountNumberDTO accountNumberDTO) {
        try{
            AccountNumberGenerator accountNumberGenerator = new AccountNumberGenerator(accountWeightings, 11);
            AccountNumber createdAccNumber = null;
            boolean isAccountCreated = false;
            while(!isAccountCreated){
                try {
                    final String accNumber = accountNumberGenerator.generate(accountNumberDTO.getAccNumPrefix());
                    final AccountNumber accountNumModel = new AccountNumber(null, accNumber, accountNumberDTO.getAccountType().name().toUpperCase(), accountNumberDTO.getAccountSubType().name().toUpperCase(), false);
                    createdAccNumber = saveAccountNumber(accountNumModel);
                    isAccountCreated = true;
                }catch(Exception ex){
                    LOG.error( MessageFormat.format("Account number could not be created {0}", ex.getMessage()));
                }
            }
            AccountNumberResponse accountNumberResponse = AppUtil.entityToDto(createdAccNumber,new AccountNumberResponse());
            return ResponseEntity.ok().body(new APIResponse(200,"Account number created",accountNumberResponse,Instant.now()));
        }catch(Exception ex){
            LOG.error(MessageFormat.format("Account number could not be saved {0}", ex.getMessage()));
            return ResponseEntity.badRequest().body(new APIResponse(400,"An error occurred " + ex.getMessage(),"Account number could be created", Instant.now()));
        }


    }



    public List<AccountTypeDetails> buildAccountTypes(){
        List<AccountTypeDetails> accountTypeList = new ArrayList<>();

        for(AccountNames accountNames: AccountNames.values()){
            switch (accountNames){
                case mobi:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.mobi.name().toUpperCase(),AccountTypes.current.name().toUpperCase(), mobiAccNumberPrefix,mobiAccNumbersTobeReserved));
                    break;
                case ezi:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.ezi.name().toUpperCase(),AccountTypes.current.name().toUpperCase(),eziAccNumberPrefix,eziAccNumbersTobeReserved));
                    break;
                case biz:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.biz.name().toUpperCase(),AccountTypes.current.name(),bizAccNumberPrefix,bizAccNumbersTobeReserved));
                    break;
                case sevi:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.sevi.name().toUpperCase(),AccountTypes.savings.name(),seviAccNumberPrefix,seviAccNumbersTobeReserved));
                    break;
                case loan:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.loan.name().toUpperCase(),AccountTypes.loan.name(),loanAccNumberPrefix,loanAccNumbersTobeReserved));
                    break;
                case shembe:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.shembe.name().toUpperCase(),AccountTypes.partners.name(),shembeAccNumberPrefix,shembeAccNumbersTobeReserved));
                    break;
                case doe:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.doe.name().toUpperCase(),AccountTypes.partners.name(),doeAccNumberPrefix,doeAccNumbersTobeReserved));
                    break;
                case other:
                    accountTypeList.add(new AccountTypeDetails(AccountNames.other.name().toUpperCase(),AccountTypes.other.name(),otherAccNumberPrefix,otherAccNumbersTobeReserved));
                    break;
                default:
                    break;

            }
        }
        return accountTypeList;
    }

}
