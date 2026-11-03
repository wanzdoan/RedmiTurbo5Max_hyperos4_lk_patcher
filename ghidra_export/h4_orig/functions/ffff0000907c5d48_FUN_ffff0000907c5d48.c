// ffff0000907c5d48  size=124  FUN_ffff0000907c5d48

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907c5d48(void)

{
  uint uVar1;
  long lVar2;
  char *pcVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff00009077b334(_DAT_ffff00009090d9d0);
  if ((lVar2 == 0) || (uVar1 = FUN_ffff00009071a7f0(), uVar1 == 0)) {
    if (_DAT_ffff00009090d9d0 == 0) {
      return 0;
    }
    uVar4 = FUN_ffff00009077be30(&DAT_ffff00009087cef8);
    if ((int)uVar4 == 0) {
      return uVar4;
    }
    uVar4 = uVar4 & 0xffffffff;
    pcVar3 = s__AVB__vmm_free_region_failed__re_ffff0000907dbdd3;
  }
  else {
    uVar4 = (ulong)uVar1;
    pcVar3 = s__AVB__mblock_free_failed__ret_____ffff0000907d562d;
  }
  FUN_ffff00009077e11c(pcVar3,uVar4);
  return 1;
}


