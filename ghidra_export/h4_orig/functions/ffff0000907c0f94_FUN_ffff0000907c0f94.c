// ffff0000907c0f94  size=128  FUN_ffff0000907c0f94

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000907c0f94(long param_1)

{
  long lVar1;
  ulong uVar2;
  char *pcVar3;
  
  if (param_1 == 0) {
    pcVar3 = s__AVB__malloc_error__size_<__0_ffff0000907fe7b2;
  }
  else {
    lVar1 = (_DAT_ffff00009090d8f0 + *DAT_ffff0000908399e8 & 0xffffffffffffffc0) + 0x40;
    uVar2 = (param_1 - *DAT_ffff0000908399e8) + lVar1;
    if (uVar2 <= *DAT_ffff000090839b40) {
      _DAT_ffff00009090d8f0 = uVar2;
      return lVar1;
    }
    pcVar3 = s__AVB__malloc_error__no_enough_me_ffff000090801c4c;
  }
  FUN_ffff00009077e11c(pcVar3);
  return 0;
}


