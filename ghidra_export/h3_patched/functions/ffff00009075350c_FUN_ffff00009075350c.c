// ffff00009075350c  size=104  FUN_ffff00009075350c

void FUN_ffff00009075350c(int param_1)

{
  ulong uVar1;
  
  uVar1 = FUN_ffff000090769488(s_lcm_rst_out0_gpio_ffff0000907e3186);
  if (-1 < (int)uVar1) {
    FUN_ffff000090785468(uVar1,0);
    FUN_ffff000090785354(uVar1 & 0xffffffff,1);
    FUN_ffff0000907853c4(uVar1 & 0xffffffff,param_1 != 0);
    return;
  }
  return;
}


