
void FUN_ffff0000b8008e68
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined *puVar1;
  undefined8 uVar2;
  undefined8 *puVar3;
  undefined1 auVar4 [16];
  
  DAT_ffff0000b810c0c8 = param_1;
  DAT_ffff0000b810c0d0 = param_2;
  DAT_ffff0000b810c0d8 = param_3;
  DAT_ffff0000b810c0e0 = param_4;
  FUN_ffff0000b800b9a4();
  FUN_ffff0000b8008f58(1,0xffff);
  FUN_ffff0000b80077e4();
  FUN_ffff0000b800e810();
  FUN_ffff0000b8008f58(0x10000,0x1ffff);
  FUN_ffff0000b80039c0();
  FUN_ffff0000b8008f58(0x20000,0x2ffff);
  FUN_ffff0000b8043c98();
  FUN_ffff0000b8008f58(0x30000,0x3ffff);
  FUN_ffff0000b804bbcc();
  puVar1 = PTR_PTR_ffff0000b81086b8;
  for (puVar3 = (undefined8 *)PTR_PTR_ffff0000b81086c0; puVar3 != (undefined8 *)puVar1;
      puVar3 = puVar3 + 1) {
    (*(code *)*puVar3)();
  }
  FUN_ffff0000b8008f58(0x40000,0x5ffff);
  FUN_ffff0000b800ad38();
  FUN_ffff0000b8008f58(0x60000,0x6ffff);
  uVar2 = FUN_ffff0000b800b03c(s_bootstrap2_ffff0000b809d37a,FUN_ffff0000b8008f70,0,0x10,0x2000);
  FUN_ffff0000b800b228();
  FUN_ffff0000b800b06c(uVar2);
  auVar4 = FUN_ffff0000b800bab0();
  FUN_ffff0000b8008d38(1,auVar4._0_8_ & 0xffffffff,auVar4._8_8_ & 0xffffffff);
  return;
}


