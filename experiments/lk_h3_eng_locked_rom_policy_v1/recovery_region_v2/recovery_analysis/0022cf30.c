
/* std::string::find[abi:ne180000](char const*, unsigned long) const */

ulong __thiscall std::string::find_abi_ne180000_(string *this,char *param_1,ulong param_2)

{
  string *psVar1;
  ulong uVar2;
  string *psVar3;
  byte bVar4;
  int iVar5;
  size_t __n;
  string *psVar6;
  long lVar7;
  string *psVar8;

  uVar2 = *(ulong *)(this + 8);
  psVar3 = *(string **)(this + 0x10);
  if (((byte)*this & 1) == 0) {
    psVar3 = this + 1;
    uVar2 = (ulong)((byte)*this >> 1);
  }
  __n = strlen(param_1);
  if (uVar2 < param_2) {
    param_2 = 0xffffffffffffffff;
  }
  else if (__n != 0) {
    psVar6 = psVar3 + param_2;
    psVar1 = psVar3 + uVar2;
    lVar7 = (long)psVar1 - (long)psVar6;
    psVar8 = psVar1;
    if ((long)__n <= lVar7) {
      bVar4 = *param_1;
      do {
        psVar8 = psVar1;
        if (((0xfffffffffffffffe < lVar7 - __n) ||
            (psVar6 = memchr(psVar6,(uint)bVar4,(lVar7 - __n) + 1), psVar6 == (string *)0x0)) ||
           (iVar5 = memcmp(psVar6,param_1,__n), psVar8 = psVar6, iVar5 == 0)) break;
        psVar6 = psVar6 + 1;
        lVar7 = (long)psVar1 - (long)psVar6;
        psVar8 = psVar1;
      } while ((long)__n <= lVar7);
    }
    param_2 = (long)psVar8 - (long)psVar3;
    if (psVar8 == psVar1) {
      param_2 = 0xffffffffffffffff;
    }
  }
  return param_2;
}
