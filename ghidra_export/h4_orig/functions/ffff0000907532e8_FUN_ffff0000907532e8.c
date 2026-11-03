// ffff0000907532e8  size=264  FUN_ffff0000907532e8

undefined8 FUN_ffff0000907532e8(int param_1,undefined8 param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  
  iVar1 = FUN_ffff000090769750(s_lcd_bias_enp0_gpio_ffff0000907fa0eb);
  iVar2 = FUN_ffff000090769750(s_lcd_bias_enn0_gpio_ffff0000907cfac1,param_2);
  uVar3 = 0xffffffffffffffff;
  if ((-1 < iVar1) && (-1 < iVar2)) {
    if (param_1 == 0) {
      FUN_ffff000090785730(iVar2,0);
      FUN_ffff00009078561c(iVar2,1);
      FUN_ffff00009078568c(iVar2,0);
      FUN_ffff00009077cea0(1000);
      FUN_ffff000090785730(iVar1,0);
      FUN_ffff00009078561c(iVar1,1);
    }
    else {
      FUN_ffff000090785730(iVar1,0);
      FUN_ffff00009078561c(iVar1,1);
      FUN_ffff00009078568c(iVar1,1);
      FUN_ffff00009077cea0(2000);
      FUN_ffff000090785730(iVar2,0);
      FUN_ffff00009078561c(iVar2,1);
      iVar1 = iVar2;
    }
    FUN_ffff00009078568c(iVar1,param_1 != 0);
    uVar3 = 0;
  }
  return uVar3;
}


