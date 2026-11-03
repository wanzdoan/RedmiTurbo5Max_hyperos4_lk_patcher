
void FUN_ffff0000be0314b8(int param_1,undefined1 *param_2,uint param_3,int param_4)

{
  ulong uVar1;
  
  if (0 < (int)param_3) {
    uVar1 = (ulong)param_3;
    do {
      if (param_4 != 0) {
        FUN_ffff0000be05fd98
                  (s__s__d__Failed_to_write_0x_04X___0_ffff0000be0b0d90,
                   s_mt6379_print_write_log_ffff0000be0c6468,0x43,param_1,*param_2,param_4);
      }
      param_1 = param_1 + 1;
      param_2 = param_2 + 1;
      uVar1 = uVar1 - 1;
    } while (uVar1 != 0);
  }
  return;
}


