
void FUN_ffff0000b8008038(void)

{
  ulong uVar1;
  long lVar2;
  undefined1 auVar3 [16];
  
  lVar2 = tpidr_el1;
  DAT_ffff0000b810ff28 = 0;
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  (**(code **)(lVar2 + 0x278))(*(undefined8 *)(lVar2 + 0x280));
  auVar3 = FUN_ffff0000b800b624();
  uVar1 = cpacr_el1;
  if ((uVar1 & 0x300000) != 0) {
    FUN_ffff0000b8007ed4(auVar3._0_8_);
    cpacr_el1 = uVar1 & 0xffffffffffcfffff;
    InstructionSynchronizationBarrier();
  }
  FUN_ffff0000b8007364(auVar3._0_8_ + 0x50,*(undefined8 *)(auVar3._8_8_ + 0x50));
  return;
}


