// File offset entry: 0x91190

byte * FUN_02091090(void)

{
  uint uVar1;
  uint uVar2;
  byte *pbVar3;
  ulong unaff_x30;
  undefined1 auVar4 [12];
  
  FUN_0208ada0(0x20cffc0);
  uVar1 = (uint)unaff_x30 & 0xfffffffe;
  uVar2 = uVar1 - 4;
  if ((unaff_x30 & 0xfffffffffffffffc) == 0) {
    uVar2 = uVar1;
  }
  FUN_0208ada0(0x20cbabc,uVar2);
  auVar4 = FUN_0208b224(0x20b1efb,0x23,0x20c7ab3);
  pbVar3 = auVar4._0_8_;
  while( true ) {
    if ((uint)*pbVar3 == auVar4._8_4_) {
      return pbVar3;
    }
    if (*pbVar3 == 0) break;
    pbVar3 = pbVar3 + 1;
  }
  return (byte *)0x0;
}


