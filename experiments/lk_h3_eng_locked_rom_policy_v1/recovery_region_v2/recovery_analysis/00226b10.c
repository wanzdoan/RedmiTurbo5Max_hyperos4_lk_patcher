
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* std::ostream& std::__put_character_sequence[abi:ne180000]<char, std::char_traits<char>
   >(std::ostream&, char const*, unsigned long) */

ostream * std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (ostream *param_1,char *param_2,ulong param_3)

{
  ostream *poVar1;
  char *pcVar2;
  uint uVar3;
  long lVar4;
  uint uVar5;
  long *plVar6;
  long lVar7;
  undefined8 uVar8;
  undefined8 local_70;
  undefined8 uStack_68;
  id aiStack_60 [8];
  long local_58;

  lVar4 = tpidr_el0;
  local_58 = *(long *)(lVar4 + 0x28);
  local_70 = 0;
  uStack_68 = 0;
  std::ostream::sentry::sentry((sentry *)&local_70,param_1);
  if (local_70._0_1_ != (sentry)0x0) {
    poVar1 = param_1 + *(long *)(*(long *)param_1 + -0x18);
    uVar8 = *(undefined8 *)(poVar1 + 0x28);
    uVar3 = *(uint *)(poVar1 + 8);
    if (*(int *)(poVar1 + 0x90) == -1) {
      std::ios_base::getloc();
      plVar6 = (long *)std::locale::use_facet(aiStack_60);
      uVar5 = (**(code **)(*plVar6 + 0x38))(plVar6,0x20);
      std::locale::~locale((locale *)aiStack_60);
      *(uint *)(poVar1 + 0x90) = uVar5 & 0xff;
    }
    pcVar2 = param_2 + param_3;
    if ((uVar3 & 0xb0) != 0x20) {
      pcVar2 = param_2;
    }
    lVar7 = __pad_and_output_abi_ne180000_<char,std::char_traits<char>>
                      (uVar8,param_2,pcVar2,param_2 + param_3,poVar1);
    if (lVar7 == 0) {
      std::ios_base::clear((int)param_1 + (int)*(undefined8 *)(*(long *)param_1 + -0x18));
    }
  }
  std::ostream::sentry::~sentry((sentry *)&local_70);
  if (*(long *)(lVar4 + 0x28) == local_58) {
    return param_1;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
