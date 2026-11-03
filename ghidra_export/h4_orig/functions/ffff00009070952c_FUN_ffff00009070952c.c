// ffff00009070952c  size=240  FUN_ffff00009070952c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070952c
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined *puVar1;
  undefined8 uVar2;
  undefined8 *puVar3;
  undefined1 auVar4 [16];
  
  _DAT_ffff00009083d0d8 = param_1;
  _DAT_ffff00009083d0e0 = param_2;
  _DAT_ffff00009083d0e8 = param_3;
  _DAT_ffff00009083d0f0 = param_4;
  FUN_ffff000090718384();
  FUN_ffff00009070961c(1,0xffff);
  FUN_ffff000090707ea8();
  FUN_ffff00009071ecf8();
  FUN_ffff00009070961c(0x10000,0x1ffff);
  FUN_ffff0000907039e8();
  FUN_ffff00009070961c(0x20000,0x2ffff);
  FUN_ffff000090760294();
  FUN_ffff00009070961c(0x30000,0x3ffff);
  FUN_ffff00009077d414();
  puVar1 = PTR_PTR_ffff000090839898;
  for (puVar3 = DAT_ffff0000908398a0; puVar3 != (undefined8 *)puVar1; puVar3 = puVar3 + 1) {
    (*(code *)*puVar3)();
  }
  FUN_ffff00009070961c(0x40000,0x5ffff);
  FUN_ffff0000907176e0();
  FUN_ffff00009070961c(0x60000,0x6ffff);
  uVar2 = FUN_ffff0000907179e4(s_bootstrap2_ffff0000907fb055,FUN_ffff000090709634,0,0x10,0x2000);
  FUN_ffff000090717c08();
  FUN_ffff000090717a14(uVar2);
  auVar4 = FUN_ffff000090718490();
  FUN_ffff0000907093fc(1,auVar4._0_8_ & 0xffffffff,auVar4._8_8_ & 0xffffffff);
  return;
}


