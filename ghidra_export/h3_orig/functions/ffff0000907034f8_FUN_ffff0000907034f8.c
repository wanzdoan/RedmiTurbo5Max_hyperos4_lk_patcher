// ffff0000907034f8  size=136  FUN_ffff0000907034f8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907034f8(long param_1)

{
  undefined4 uVar1;
  
  FUN_ffff00009077e858(param_1,&DAT_ffff00009083d018,0x10);
  FUN_ffff00009077e858(param_1 + 0x10,0xffff00009083d028,0x10);
  FUN_ffff00009077e858(param_1 + 0x24,0xffff00009083d03c,0x10);
  FUN_ffff00009077e858(param_1 + 0x34,0xffff00009083d04c,0x10);
  uVar1 = _DAT_ffff00009083d05c;
  *(undefined4 *)(param_1 + 0x20) = _DAT_ffff00009083d038;
  *(undefined4 *)(param_1 + 0x44) = uVar1;
  *(undefined8 *)(param_1 + 0x48) = _DAT_ffff00009083d060;
  *(undefined4 *)(param_1 + 0x50) = _DAT_ffff00009083d068;
  return;
}


