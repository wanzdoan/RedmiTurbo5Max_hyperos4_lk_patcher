// ffff00009079306c  size=220  FUN_ffff00009079306c

void FUN_ffff00009079306c(long param_1)

{
  int iVar1;
  
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0xe,0);
  if (iVar1 == 0) {
    *(undefined1 *)(param_1 + 0x170) = 1;
  }
  else {
    FUN_ffff00009077e11c
              (s__UFS__Write_Booster__s_failed__d_ffff0000907faa03,s_enabling_ffff0000907f7413,iVar1
              );
  }
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0x10,0);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c
              (s__UFS__WB_Buf_Flush_during_H8__s_f_ffff0000907f56db,s_enabling_ffff0000907f7413,
               iVar1);
  }
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0xf,0);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c
              (s__UFS_dev_err_WB_Buf_Flush__s_fai_ffff0000907eef41,s_enabling_ffff0000907f7413,iVar1
              );
    return;
  }
  *(undefined1 *)(param_1 + 0x171) = 1;
  return;
}


