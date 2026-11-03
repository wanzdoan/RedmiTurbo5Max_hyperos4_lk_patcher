// File offset entry: 0xac040

ulong FUN_020abf40(void)

{
  undefined1 in_ZR;
  int iVar1;
  ulong uVar2;
  uint *puVar3;
  uint uVar4;
  undefined8 *extraout_x8;
  undefined1 auVar5 [16];
  undefined1 auStack_40 [24];
  undefined8 uStack_28;
  
  func_0x020ac088();
  uStack_28 = *extraout_x8;
  FUN_020915ac(auStack_40,0x20e44a4,0x18);
  func_0x020abed0();
  uVar2 = func_0x020ac068();
  if ((bool)in_ZR) {
    return uVar2;
  }
  auVar5 = FUN_02091090();
  if ((auVar5._8_8_ & 0xffff0000) == 0) {
    puVar3 = (uint *)(auVar5._0_8_ + 0xc);
    iVar1 = auVar5._8_4_ << 0x10;
    *puVar3 = *puVar3 | auVar5._8_4_;
    iVar1 = func_0x020a92b0(puVar3,iVar1,iVar1,1000);
    uVar4 = 0;
    if (iVar1 != 0) {
      uVar4 = 0x727e;
    }
    return (ulong)uVar4;
  }
  return 0x7246;
}


