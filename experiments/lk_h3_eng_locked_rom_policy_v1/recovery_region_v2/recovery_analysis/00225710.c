
/* std::string::__init_copy_ctor_external(char const*, unsigned long) */

FILE * __thiscall std::string::__init_copy_ctor_external(string *this,char *param_1,ulong param_2)

{
  int iVar1;
  string *__dest;
  FILE *pFVar2;
  int *piVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined1 auVar6 [16];

  if (param_2 < 0x17) {
    __dest = this + 1;
    *this = SUB41((int)param_2 << 1,0);
  }
  else {
    if (0xffffffffffffffef < param_2) {
      auVar6 = __throw_length_error_abi_ne180000_();
      piVar3 = auVar6._0_8_;
      if (PTR_android_fdsan_exchange_owner_tag_00429af0 != (undefined *)0x0) {
        iVar1 = *piVar3;
        uVar4 = android_fdsan_create_owner_tag(3,piVar3);
        uVar5 = android_fdsan_create_owner_tag(3,0);
        android_fdsan_exchange_owner_tag(iVar1,uVar4,uVar5);
      }
      iVar1 = *piVar3;
      *piVar3 = -1;
      pFVar2 = fdopen(iVar1,auVar6._8_8_);
      if (pFVar2 == (FILE *)0x0) {
        close(iVar1);
      }
      return pFVar2;
    }
    __dest = operator_new((param_2 | 0xf) + 1);
    *(ulong *)(this + 8) = param_2;
    *(string **)(this + 0x10) = __dest;
    *(ulong *)this = (param_2 | 0xf) + 2;
  }
  pFVar2 = memmove(__dest,param_1,param_2 + 1);
  return pFVar2;
}
