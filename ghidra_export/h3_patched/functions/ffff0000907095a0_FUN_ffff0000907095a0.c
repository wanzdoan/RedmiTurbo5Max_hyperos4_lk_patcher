// ffff0000907095a0  size=240  FUN_ffff0000907095a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907095a0
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined *puVar1;
  undefined8 uVar2;
  undefined8 *puVar3;
  undefined1 auVar4 [16];
  
  _DAT_ffff00009083d138 = param_1;
  _DAT_ffff00009083d140 = param_2;
  _DAT_ffff00009083d148 = param_3;
  _DAT_ffff00009083d150 = param_4;
  FUN_ffff000090718240();
  FUN_ffff000090709690(1,0xffff);
  FUN_ffff000090707f1c();
  FUN_ffff00009071eb70();
  FUN_ffff000090709690(0x10000,0x1ffff);
  FUN_ffff000090703a70();
  FUN_ffff000090709690(0x20000,0x2ffff);
  FUN_ffff00009075fffc();
  FUN_ffff000090709690(0x30000,0x3ffff);
  FUN_ffff00009077d14c();
  puVar1 = PTR_PTR_ffff000090839878;
  for (puVar3 = DAT_ffff000090839880; puVar3 != (undefined8 *)puVar1; puVar3 = puVar3 + 1) {
    (*(code *)*puVar3)();
  }
  FUN_ffff000090709690(0x40000,0x5ffff);
  FUN_ffff00009071759c();
  FUN_ffff000090709690(0x60000,0x6ffff);
  uVar2 = FUN_ffff0000907178a0(s_bootstrap2_ffff0000907faeb9,FUN_ffff0000907096a8,0,0x10,0x2000);
  FUN_ffff000090717ac4();
  FUN_ffff0000907178d0(uVar2);
  auVar4 = FUN_ffff00009071834c();
  FUN_ffff000090709470(1,auVar4._0_8_ & 0xffffffff,auVar4._8_8_ & 0xffffffff);
  return;
}


