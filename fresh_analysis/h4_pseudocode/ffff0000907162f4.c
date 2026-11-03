
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907162f4(void)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  ulong uVar4;
  undefined4 *puVar5;
  ulong uVar6;
  
  UnkSytemRegWrite(3,0,4,6,0,0xff);
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,0,0xc,0xc,4,0);
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,0,0xc,0xc,7,1);
  InstructionSynchronizationBarrier();
  _DAT_ffff00000c450080 = 0;
  _DAT_ffff00000c450d00 = 0xffffffff;
  do {
  } while (_DAT_ffff00000c400000 < 0);
  uVar2 = ((ulong)_DAT_ffff00000c400004 & 0x1f) << 5;
  if ((int)uVar2 == 0) {
    uVar4 = 0x20;
  }
  else {
    uVar6 = 0;
    uVar4 = (ulong)((int)uVar2 + 0x20);
    puVar5 = (undefined4 *)&DAT_ffff00000c400084;
    do {
      *puVar5 = 0;
      uVar6 = uVar6 + 0x20;
      puVar5[800] = 0xffffffff;
      puVar5 = puVar5 + 1;
    } while (uVar6 < uVar2);
    uVar6 = 0x20;
    puVar5 = (undefined4 *)&DAT_ffff00000c400c08;
    do {
      uVar6 = uVar6 + 0x10;
      *puVar5 = 0;
      puVar5 = puVar5 + 1;
    } while (uVar6 < uVar4);
    if ((_DAT_ffff00000c400004 & 0xe0) != 0) {
      uVar6 = 0x20;
      do {
        uVar1 = uVar6 + 4;
        *(undefined4 *)(uVar6 - 0xfffff3bff800) = *(undefined4 *)(uVar6 - 0xffff6f7fbaac);
        uVar6 = uVar1;
      } while (uVar1 < uVar4);
    }
  }
  uVar6 = 0;
  do {
    uVar1 = uVar6 + 4;
    *(undefined4 *)(uVar6 - 0xfffff3bffc00) = 0xa0a0a0a0;
    uVar6 = uVar1;
  } while (uVar1 < uVar4);
  uVar4 = 0xffffffffffffffe0;
  puVar5 = (undefined4 *)&DAT_ffff00000c400180;
  do {
    uVar4 = uVar4 + 0x20;
    *puVar5 = 0xffffffff;
    puVar5 = puVar5 + 1;
  } while (uVar4 < uVar2);
  uVar4 = 0xffffffffffffffe0;
  puVar5 = (undefined4 *)&DAT_ffff00000c400380;
  do {
    uVar4 = uVar4 + 0x20;
    *puVar5 = 0xffffffff;
    puVar5 = puVar5 + 1;
  } while (uVar4 < uVar2);
  uVar4 = 0xffffffffffffffe0;
  puVar5 = (undefined4 *)&DAT_ffff00000c400280;
  do {
    uVar4 = uVar4 + 0x20;
    *puVar5 = 0xffffffff;
    puVar5 = puVar5 + 1;
  } while (uVar4 < uVar2);
  _DAT_ffff00000c400000 = 0x37;
  FUN_ffff0000907160fc(&DAT_ffff00000c440000);
  uVar3 = _DAT_ffff00000c440014;
  _DAT_ffff00000c440014 = _DAT_ffff00000c440014 & 0xfffffffd;
  do {
  } while ((uVar3 >> 2 & 1) != 0);
  UnkSytemRegWrite(3,0,4,6,0,0xff);
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,0,0xc,0xc,4,0);
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,0,0xc,0xc,7,1);
  InstructionSynchronizationBarrier();
  _DAT_ffff00000c440000 = 1;
  _DAT_ffff00000c440004 = 0xff;
  return;
}


