
void FUN_ffff0000b8053b30(long *param_1)

{
  ulong uVar1;
  
  uVar1 = 0xfffffffffffffff6;
  *(undefined4 *)(*param_1 + 0x34) = 0;
  do {
    if ((*(uint *)(*param_1 + 0x34) & 1) == 0) {
      return;
    }
    FUN_ffff0000b804b704(10);
    uVar1 = uVar1 + 10;
  } while (uVar1 < 0x137f);
  if ((*(uint *)(*param_1 + 0x34) & 1) != 0) {
    FUN_ffff0000b804c33c
              (s__s__Controller_disable_failed_ffff0000b8091eab,s_ufs_hba_stop_ffff0000b8088803);
    FUN_ffff0000b805a58c(param_1);
    FUN_ffff0000b805a4fc(param_1,0,0xa0);
    FUN_ffff0000b805a4fc(param_1,0x100,0x74);
    FUN_ffff0000b8043710(param_1);
  }
  return;
}


