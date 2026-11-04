
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* bool std::operator==[abi:nn180000]<char, std::char_traits<char>, std::allocator<char>
   >(std::string const&, char const*) */

bool std::operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
               (string *param_1,char *param_2)

{
  size_t __n;
  string sVar1;
  bool bVar2;
  int iVar3;
  size_t sVar4;
  string *__s1;

  sVar1 = *param_1;
  __n = (ulong)((byte)sVar1 >> 1);
  if (((byte)sVar1 & 1) != 0) {
    __n = *(size_t *)(param_1 + 8);
  }
  sVar4 = strlen(param_2);
  if (__n == sVar4) {
    __s1 = *(string **)(param_1 + 0x10);
    if (((byte)sVar1 & 1) == 0) {
      __s1 = param_1 + 1;
    }
    iVar3 = memcmp(__s1,param_2,__n);
    bVar2 = iVar3 == 0;
  }
  else {
    bVar2 = false;
  }
  return bVar2;
}
