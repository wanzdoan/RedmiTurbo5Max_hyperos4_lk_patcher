// ffff000090753050  size=264  FUN_ffff000090753050

undefined8 FUN_ffff000090753050(int param_1,undefined8 param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  
  iVar1 = FUN_ffff000090769488(s_lcd_bias_enp0_gpio_ffff0000907f9f4f);
  iVar2 = FUN_ffff000090769488(s_lcd_bias_enn0_gpio_ffff0000907cfaa2,param_2);
  uVar3 = 0xffffffffffffffff;
  if ((-1 < iVar1) && (-1 < iVar2)) {
    if (param_1 == 0) {
      FUN_ffff000090785468(iVar2,0);
      FUN_ffff000090785354(iVar2,1);
      FUN_ffff0000907853c4(iVar2,0);
      FUN_ffff00009077cbd8(1000);
      FUN_ffff000090785468(iVar1,0);
      FUN_ffff000090785354(iVar1,1);
    }
    else {
      FUN_ffff000090785468(iVar1,0);
      FUN_ffff000090785354(iVar1,1);
      FUN_ffff0000907853c4(iVar1,1);
      FUN_ffff00009077cbd8(2000);
      FUN_ffff000090785468(iVar2,0);
      FUN_ffff000090785354(iVar2,1);
      iVar1 = iVar2;
    }
    FUN_ffff0000907853c4(iVar1,param_1 != 0);
    uVar3 = 0;
  }
  return uVar3;
}


