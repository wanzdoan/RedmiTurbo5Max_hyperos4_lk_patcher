
/* std::ostream& std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char, std::char_traits<char>,
   std::allocator<char> >(std::ostream&, std::string const&) */

ostream * std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                    (ostream *param_1,string *param_2)

{
  ulong uVar1;
  string *psVar2;
  ostream *poVar3;

  uVar1 = *(ulong *)(param_2 + 8);
  psVar2 = *(string **)(param_2 + 0x10);
  if (((byte)*param_2 & 1) == 0) {
    psVar2 = param_2 + 1;
    uVar1 = (ulong)((byte)*param_2 >> 1);
  }
  poVar3 = __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                     (param_1,(char *)psVar2,uVar1);
  return poVar3;
}
