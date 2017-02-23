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

#ifndef VIRTUAL_MACHINE_INTERFACE_HPP
#define VIRTUAL_MACHINE_INTERFACE_HPP

#include <string>
#include <unordered_map>

namespace virtual_machine {

void initializeVM(const std::string &packageName,
                  const std::string &contractName);
void finishVM(const std::string &packageName, const std::string &contractName);
void invokeFunction(const std::string &packageName,
                    const std::string &contractName,
                    const std::string &functionName,
                    const std::unordered_map<std::string, std::string> &params);
void invokeFunction(const std::string &packageName,
                    const std::string &contractName,
                    const std::string &functionName);
}

#endif