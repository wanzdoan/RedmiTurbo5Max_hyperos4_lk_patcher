
void FUN_ffff0000be0634dc(void)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  
  if (DAT_ffff0000be1271ac == 0) {
    return;
  }
  lVar2 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000be05e088
                      (lVar2,&DAT_ffff0000be1271b8,
                       DAT_ffff0000be1271b0 - (ulong)DAT_ffff0000be1271ac,0x1000);
    if (iVar1 < 0) {
      FUN_ffff0000be063b40
                (s_kedump__flush_failed__d___0x_llx_ffff0000be0c688d,iVar1,DAT_ffff0000be1271b0);
      FUN_ffff0000be05fd98
                (s_kedump__flush_failed__d___0x_llx_ffff0000be0c688d,iVar1,DAT_ffff0000be1271b0);
    }
    uVar3 = (ulong)DAT_ffff0000be1271ac;
    DAT_ffff0000be1271ac = 0;
    DAT_ffff0000be1271b0 = (DAT_ffff0000be1271b0 - uVar3) + 0x1000;
    FUN_ffff0000be060ae0(&DAT_ffff0000be1271b8,0,0x1000);
    FUN_ffff0000be05def0(lVar2);
    return;
  }
  FUN_ffff0000be063b40
            (s_kedump__flush_partition___s__is_n_ffff0000be0ba49c,s_expdb_ffff0000be0a79cf);
  FUN_ffff0000be05fd98
            (s_kedump__flush_partition___s__is_n_ffff0000be0ba49c,s_expdb_ffff0000be0a79cf);
  return;
}


