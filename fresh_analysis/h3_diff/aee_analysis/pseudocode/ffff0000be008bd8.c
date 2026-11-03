
void FUN_ffff0000be008bd8
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined *puVar1;
  undefined8 uVar2;
  undefined8 *puVar3;
  undefined1 auVar4 [16];
  
  DAT_ffff0000be0f90a8 = param_1;
  DAT_ffff0000be0f90b0 = param_2;
  DAT_ffff0000be0f90b8 = param_3;
  DAT_ffff0000be0f90c0 = param_4;
  FUN_ffff0000be01b344();
  FUN_ffff0000be008cc8(1,0xffff);
  FUN_ffff0000be007560();
  FUN_ffff0000be01d730();
  FUN_ffff0000be008cc8(0x10000,0x1ffff);
  FUN_ffff0000be0036dc();
  FUN_ffff0000be008cc8(0x20000,0x2ffff);
  FUN_ffff0000be04cd40();
  FUN_ffff0000be008cc8(0x30000,0x3ffff);
  FUN_ffff0000be05f0bc();
  puVar1 = PTR_PTR_ffff0000be0f5548;
  for (puVar3 = (undefined8 *)PTR_PTR_ffff0000be0f5550; puVar3 != (undefined8 *)puVar1;
      puVar3 = puVar3 + 1) {
    (*(code *)*puVar3)();
  }
  FUN_ffff0000be008cc8(0x40000,0x5ffff);
  FUN_ffff0000be01a6d8();
  FUN_ffff0000be008cc8(0x60000,0x6ffff);
  uVar2 = FUN_ffff0000be01a9dc(s_bootstrap2_ffff0000be0c1713,FUN_ffff0000be008ce0,0,0x10,0x2000);
  FUN_ffff0000be01abc8();
  FUN_ffff0000be01aa0c(uVar2);
  auVar4 = FUN_ffff0000be01b450();
  FUN_ffff0000be008aa8(1,auVar4._0_8_ & 0xffffffff,auVar4._8_8_ & 0xffffffff);
  return;
}


