// ffff000090741454  size=144  FUN_ffff000090741454

void FUN_ffff000090741454(int param_1,undefined1 *param_2,uint param_3,int param_4)

{
  ulong uVar1;
  
  if (0 < (int)param_3) {
    uVar1 = (ulong)param_3;
    do {
      if (param_4 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__Failed_to_write_0x_04X___0_ffff0000907e3004,
                   s_mt6379_print_write_log_ffff000090802957,0x43,param_1,*param_2,param_4);
      }
      param_1 = param_1 + 1;
      param_2 = param_2 + 1;
      uVar1 = uVar1 - 1;
    } while (uVar1 != 0);
  }
  return;
}


