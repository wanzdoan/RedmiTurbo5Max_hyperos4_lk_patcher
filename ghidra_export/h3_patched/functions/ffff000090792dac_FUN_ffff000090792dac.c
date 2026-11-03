// ffff000090792dac  size=220  FUN_ffff000090792dac

void FUN_ffff000090792dac(long param_1)

{
  int iVar1;
  
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0xe,0);
  if (iVar1 == 0) {
    *(undefined1 *)(param_1 + 0x170) = 1;
  }
  else {
    FUN_ffff00009077de54
              (s__UFS__Write_Booster__s_failed__d_ffff0000907fa867,s_enabling_ffff0000907f7289,iVar1
              );
  }
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0x10,0);
  if (iVar1 != 0) {
    FUN_ffff00009077de54
              (s__UFS__WB_Buf_Flush_during_H8__s_f_ffff0000907f5557,s_enabling_ffff0000907f7289,
               iVar1);
  }
  iVar1 = (**(code **)(param_1 + 0x218))(param_1,6,0xf,0);
  if (iVar1 != 0) {
    FUN_ffff00009077de54
              (s__UFS_dev_err_WB_Buf_Flush__s_fai_ffff0000907eedb9,s_enabling_ffff0000907f7289,iVar1
              );
    return;
  }
  *(undefined1 *)(param_1 + 0x171) = 1;
  return;
}


