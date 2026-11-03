// ffff00009078fadc  size=172  FUN_ffff00009078fadc

void FUN_ffff00009078fadc(long *param_1)

{
  ulong uVar1;
  
  uVar1 = 0xfffffffffffffff6;
  *(undefined4 *)(*param_1 + 0x34) = 0;
  do {
    if ((*(uint *)(*param_1 + 0x34) & 1) == 0) {
      return;
    }
    FUN_ffff00009077cbd8(10);
    uVar1 = uVar1 + 10;
  } while (uVar1 < 0x137f);
  if ((*(uint *)(*param_1 + 0x34) & 1) != 0) {
    FUN_ffff00009077de54
              (s__s__Controller_disable_failed_ffff0000907e6cf2,s_ufs_hba_stop_ffff0000907d5292);
    FUN_ffff000090796ee8(param_1);
    FUN_ffff000090796e58(param_1,0,0xa0);
    FUN_ffff000090796e58(param_1,0x100,0x74);
    FUN_ffff00009075f8b8(param_1);
  }
  return;
}


