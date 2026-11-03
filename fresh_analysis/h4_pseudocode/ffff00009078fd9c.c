
void FUN_ffff00009078fd9c(long *param_1)

{
  ulong uVar1;
  
  uVar1 = 0xfffffffffffffff6;
  *(undefined4 *)(*param_1 + 0x34) = 0;
  do {
    if ((*(uint *)(*param_1 + 0x34) & 1) == 0) {
      return;
    }
    FUN_ffff00009077cea0(10);
    uVar1 = uVar1 + 10;
  } while (uVar1 < 0x137f);
  if ((*(uint *)(*param_1 + 0x34) & 1) != 0) {
    FUN_ffff00009077e11c
              (s__s__Controller_disable_failed_ffff0000907e6e4d,s_ufs_hba_stop_ffff0000907d52cf);
    FUN_ffff0000907971a8(param_1);
    FUN_ffff000090797118(param_1,0,0xa0);
    FUN_ffff000090797118(param_1,0x100,0x74);
    FUN_ffff00009075fb50(param_1);
  }
  return;
}


