
void FUN_ffff0000b8056d80(long param_1)

{
  int iVar1;
  
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0xe,0);
  if (iVar1 == 0) {
    *(undefined1 *)(param_1 + 0x170) = 1;
  }
  else {
    FUN_ffff0000b804c33c
              (s__UFS__Write_Booster__s_failed__d_ffff0000b809cf34,s_enabling_ffff0000b809b0bc,iVar1
              );
  }
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0x10,0);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c
              (s__UFS__WB_Buf_Flush_during_H8__s_f_ffff0000b809a27b,s_enabling_ffff0000b809b0bc,
               iVar1);
  }
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0xf,0);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c
              (s__UFS_dev_err_WB_Buf_Flush__s_fai_ffff0000b8096abe,s_enabling_ffff0000b809b0bc,iVar1
              );
    return;
  }
  *(undefined1 *)(param_1 + 0x171) = 1;
  return;
}


