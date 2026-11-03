// ffff0000907c0920  size=128  FUN_ffff0000907c0920

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000907c0920(long param_1)

{
  long lVar1;
  ulong uVar2;
  char *pcVar3;
  
  if (param_1 == 0) {
    pcVar3 = s__AVB__malloc_error__size_<__0_ffff0000907fe60a;
  }
  else {
    lVar1 = (_DAT_ffff00009090d830 + *DAT_ffff0000908399c8 & 0xffffffffffffffc0) + 0x40;
    uVar2 = (param_1 - *DAT_ffff0000908399c8) + lVar1;
    if (uVar2 <= *DAT_ffff000090839b20) {
      _DAT_ffff00009090d830 = uVar2;
      return lVar1;
    }
    pcVar3 = s__AVB__malloc_error__no_enough_me_ffff000090801a70;
  }
  FUN_ffff00009077de54(pcVar3);
  return 0;
}


