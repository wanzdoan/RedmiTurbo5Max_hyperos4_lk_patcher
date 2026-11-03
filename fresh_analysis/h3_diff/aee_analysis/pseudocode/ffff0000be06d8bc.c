
void FUN_ffff0000be06d8bc(long *param_1)

{
  ulong uVar1;
  
  uVar1 = 0xfffffffffffffff6;
  *(undefined4 *)(*param_1 + 0x34) = 0;
  do {
    if ((*(uint *)(*param_1 + 0x34) & 1) == 0) {
      return;
    }
    FUN_ffff0000be05ede4(10);
    uVar1 = uVar1 + 10;
  } while (uVar1 < 0x137f);
  if ((*(uint *)(*param_1 + 0x34) & 1) != 0) {
    FUN_ffff0000be05fd98
              (s__s__Controller_disable_failed_ffff0000be0b36d3,s_ufs_hba_stop_ffff0000be0a77ab);
    FUN_ffff0000be07491c(param_1);
    FUN_ffff0000be07488c(param_1,0,0xa0);
    FUN_ffff0000be07488c(param_1,0x100,0x74);
    FUN_ffff0000be04c7b8(param_1);
  }
  return;
}


