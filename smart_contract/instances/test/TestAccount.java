/*
Copyright Soramitsu Co., Ltd. 2016 All Rights Reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package instances.test;

import repository.Repository;
import java.util.HashMap;
import java.util.Iterator;

import static repository.KeyConstants.*;

public class TestAccount {

  private static Repository repository = new Repository();

  public static void printSuccess() {
    System.out.println("==============================================");
    System.out.println("Success");
    System.out.println("==============================================");    
  }

  public static void printFail(IllegalStateException e) {
    System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    System.err.println("Fail");
    System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    System.err.println(e.getMessage() + " in " + e.getClass().getName());
  }

  public static void testAddAccount(HashMap<String, String> info, String[] assets) throws IllegalStateException {
    try {
      // Print received params
      System.out.println("----------------------------------------------");
      System.out.println("Params pubKey:      " + info.get(PublicKey));
      System.out.println("Params accountName: " + info.get(AccountName));
      for (int i = 0; i < assets.length; i++) {
        System.out.println("Params assets[" + i + "]: " + assets[i]);
      }
      System.out.println("----------------------------------------------");

      // 1. Add account.
      System.out.println("Call accountRepo.add()");

      String accountUuid = repository.accountAdd(
        info.get(PublicKey),
        info.get(AccountName),
        assets
      );

      System.out.println("----------------------------------------------");
      System.out.println("Received from C++: accountUuid: " + accountUuid);
      System.out.println("----------------------------------------------");

      // 2. Find account data by uuid.
      System.out.println("Call accountRepo.findByUuid()");
      HashMap<String, String> accountMap = repository.accountInfoFindByUuid(accountUuid);
      String[] assetsArray = repository.accountValueFindByUuid(accountUuid);

      System.out.println("----------------------------------------------");
      System.out.println("Received from C++: found pubKey:      " + accountMap.get(PublicKey));
      System.out.println("Received from C++: found accountName: " + accountMap.get(AccountName));
      for (int i = 0; i < assets.length; i++) {
        System.out.println("Received from C++: found assets[" + i + "]:   " + assetsArray[i]);
      }
      System.out.println("----------------------------------------------");

      // 3. Ensure the integrity.
      /*
      assert accountMap.get(PublicKey).equals(params.get(PublicKey));
      assert accountMap.get(AccountName).equals(params.get(AccountName));
      */
      if (! accountMap.get(PublicKey).equals(info.get(PublicKey)))
        throw new IllegalStateException("Mismatch public key");

      if (! accountMap.get(AccountName).equals(info.get(AccountName)))
        throw new IllegalStateException("Mismatch account name");

      printSuccess();
    } catch(IllegalStateException e) {
      printFail(e);
    }
  }

  public static void testUpdateAccount(String uuid, HashMap<String, String> params, String[] assets) throws IllegalStateException {
    try {
      // Print received params
      System.out.println("----------------------------------------------");
      System.out.println("Params accountName: " + params.get(AccountName));
      for (int i = 0; i < assets.length; i++) {
        System.out.println("Params assets[" + i + "]: " + assets[i]);
      }
      System.out.println("----------------------------------------------");

      // 1. Add account.
      System.out.println("Call accountRepo.add()");

      if (! repository.accountUpdate(
        uuid,
        params.get(AccountName),
        assets
      )) throw new IllegalStateException("Cannot update account");

      System.out.println("----------------------------------------------");
      System.out.println("Received from C++: accountUuid: " + uuid);
      System.out.println("----------------------------------------------");

      // 2. Find account data by uuid.
      System.out.println("Call accountRepo.findByUuid()");
      HashMap<String, String> accountMap = repository.accountInfoFindByUuid(uuid);
      String[] assetsArray = repository.accountValueFindByUuid(uuid);

      System.out.println("----------------------------------------------");
      System.out.println("Received from C++: found pubKey:      " + accountMap.get(PublicKey));
      System.out.println("Received from C++: found accountName: " + accountMap.get(AccountName));
      for (int i = 0; i < assets.length; i++) {
        System.out.println("Received from C++: found assets[" + i + "]:   " + assetsArray[i]);
      }
      System.out.println("----------------------------------------------");

      // 3. Ensure the integrity.
      
      if (! accountMap.get(AccountName).equals(params.get(AccountName)))
        throw new IllegalStateException("Mismatch account name");

      printSuccess();
    } catch(IllegalStateException e) {
      printFail(e);
    }
  }

  public static void testRemoveAccount(String uuid) throws IllegalStateException {
    try {
      // Print received params
      System.out.println("----------------------------------------------");
      System.out.println("Params uuid: " + uuid);
      System.out.println("----------------------------------------------");

      if (!repository.accountRemove(uuid))
        throw new IllegalStateException("Cannot remove account");
      
      printSuccess();
    } catch(IllegalStateException e) {
      printFail(e);
    }
  }
  /***************************************************************************************************
   * From java main function
   ***************************************************************************************************/

  // Test 'add' only. All command tests are invoked by gTest.
  private static void javaIntegrityCheckAddAccount() {
    try {
      HashMap<String, String> accountParam = new HashMap<String, String>();
      accountParam.put(PublicKey, "This is Public key.");
      accountParam.put(AccountName, "Mizuki Sonoko");
      String[] assets = { "Hoge", "Foo", "Bar" };

      testAddAccount(accountParam, assets);

      System.out.println("==============================================");
      System.out.println("Success (from Java main)");
      System.out.println("==============================================");

    } catch(IllegalStateException e) {
      System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
      System.err.println("Fail (from Java main)");
      System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
      System.err.println(e.getMessage() + " in " + e.getClass().getName());
    }
  }

  public static void main(String[] argv) {
    System.out.println("Hello in JAVA!");
    javaIntegrityCheckAddAccount();
    System.out.println("Call C++ gTest is also required.");
  }

}