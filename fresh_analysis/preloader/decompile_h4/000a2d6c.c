// File offset entry: 0xa2d6c

undefined4 FUN_020a2c6c(void)

{
  undefined8 *puVar1;
  undefined8 *puVar2;
  undefined8 *puVar3;
  undefined4 uVar4;
  
  uVar4 = 0x10a009;
  puVar1 = (undefined8 *)func_0x020a3d40(0x20bb54f);
  if (((puVar1 != (undefined8 *)0x0) &&
      (puVar2 = (undefined8 *)func_0x020a3d40(0x20c6c10), puVar2 != (undefined8 *)0x0)) &&
     (puVar3 = (undefined8 *)func_0x020a3d40(0x20cd069), puVar3 != (undefined8 *)0x0)) {
    func_0x020a1f38(*puVar1,0x21224e8,uRam00000000021224e4,uRam0000000002122b98);
    func_0x020a1f38(*puVar2,0x21224e8,uRam00000000021224e4,uRam0000000002122b98);
    func_0x020a1f38(*puVar3,0x21224e8,uRam00000000021224e4,uRam0000000002122b98);
    uRam0000000002100e28 = *puVar1;
    func_0x020a3d50(uRam0000000002100e28,0x10000,0x36);
    uVar4 = 0;
  }
  return uVar4;
}


