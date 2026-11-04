
ulong FUN_ffff0000907c5b00(void)

{
  long lVar1;
  ulong uVar2;

  DAT_ffff00009090d908 = 0x8c00000;
  lVar1 = FUN_ffff00009071931c(0x8c00000,0x10000,0xffffffffffffffff,0,0,s_mb_avb_ffff0000907f90d6);
  if (lVar1 == 0) {
    FUN_ffff00009077ea84(s__AVB__mblock_alloc_failed_ffff0000907cf3cc);
  }
  else {
    uVar2 = FUN_ffff00009077c2e4
                      (PTR_DAT_ffff000090831678,s_vm_avb_ffff0000907e7d82,DAT_ffff00009090d908,
                       &DAT_ffff00009090d910,0xc,lVar1,0,0);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    FUN_ffff00009077ea84(s__AVB__vmm_alloc_physical_failed__ffff0000907e64aa,uVar2 & 0xffffffff);
  }
  return 1;
}
