
long FUN_ffff0000be096508(long param_1)

{
  long lVar1;
  ulong uVar2;
  char *pcVar3;
  
  if (param_1 == 0) {
    pcVar3 = s__AVB__malloc_error__size_<__0_ffff0000be0c396e;
  }
  else {
    lVar1 = (DAT_ffff0000be1b7dc0 + *(long *)PTR_DAT_ffff0000be0f5680 & 0xffffffffffffffc0) + 0x40;
    uVar2 = (param_1 - *(long *)PTR_DAT_ffff0000be0f5680) + lVar1;
    if (uVar2 <= *(ulong *)PTR_DAT_ffff0000be0f57a8) {
      DAT_ffff0000be1b7dc0 = uVar2;
      return lVar1;
    }
    pcVar3 = s__AVB__malloc_error__no_enough_me_ffff0000be0c5ca0;
  }
  FUN_ffff0000be05fd98(pcVar3);
  return 0;
}


