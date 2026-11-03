
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be01d658(void)

{
  ulong uVar1;
  undefined8 uVar2;
  
  DataSynchronizationBarrier(3,3,0);
  uVar1 = UnkSytemRegRead(3,0,0xf,3,5);
  UnkSytemRegWrite(3,0,0xf,3,5,uVar1 | 0x30);
  DataSynchronizationBarrier(3,3,0);
  do {
    uVar2 = UnkSytemRegRead(3,0,0xf,3,7);
  } while ((~(uint)uVar2 & 0x30) != 0);
  _DAT_0c000060 = _DAT_0c000060 & 0xfffffff8;
  DataSynchronizationBarrier(3,3,0);
  return;
}


