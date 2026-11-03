// ffff0000907537a4  size=104  FUN_ffff0000907537a4

void FUN_ffff0000907537a4(int param_1)

{
  ulong uVar1;
  
  uVar1 = FUN_ffff000090769750(s_lcm_rst_out0_gpio_ffff0000907e326e);
  if (-1 < (int)uVar1) {
    FUN_ffff000090785730(uVar1,0);
    FUN_ffff00009078561c(uVar1 & 0xffffffff,1);
    FUN_ffff00009078568c(uVar1 & 0xffffffff,param_1 != 0);
    return;
  }
  return;
}


