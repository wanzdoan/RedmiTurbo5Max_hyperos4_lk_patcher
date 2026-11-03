
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8009970(void)

{
  ulong uVar1;
  bool bVar2;
  int iVar3;
  
  uVar1 = UnkSytemRegRead(3,6,0xc,0xc,5);
  UnkSytemRegWrite(3,6,0xc,0xc,5,uVar1 | 6);
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,0,0xc,0xc,6,0);
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,6,0xc,0xc,7,0);
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,0,0xc,0xc,7,0);
  InstructionSynchronizationBarrier();
  iVar3 = -999999;
  _DAT_0c440014 = _DAT_0c440014 | 2;
  InstructionSynchronizationBarrier();
  do {
    if ((_DAT_0c440014 >> 2 & 1) != 0) {
      return;
    }
    FUN_ffff0000b804b704(1);
    bVar2 = iVar3 != -1;
    iVar3 = iVar3 + 1;
  } while (bVar2);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}


