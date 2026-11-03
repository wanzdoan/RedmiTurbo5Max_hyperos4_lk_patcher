
void FUN_ffff0000be006870(void)

{
  int iVar1;
  ulong uVar2;
  undefined *puVar3;
  ulong uVar4;
  undefined **ppuVar5;
  
  iVar1 = FUN_ffff0000be031548();
  if (iVar1 != 0) {
    uVar4 = 0;
    ppuVar5 = &PTR_LAB_ffff0000be0d5aa0;
    puVar3 = &DAT_ffff0000be0d5978;
    do {
      iVar1 = (&DAT_ffff0000be0c7588)[uVar4];
      *(undefined **)(puVar3 + 0x20) = puVar3 + -0x40;
      *(long *)(puVar3 + 0x10) = (long)&DAT_ffff0000be0c7588 + (long)iVar1;
      *(undefined ***)(puVar3 + 0x18) = ppuVar5;
      FUN_ffff0000be01d8b8(puVar3);
      uVar4 = uVar4 + 1;
      uVar2 = FUN_ffff0000be031548();
      ppuVar5 = ppuVar5 + 9;
      puVar3 = puVar3 + 0xb8;
    } while (uVar4 < (uVar2 & 0xffffffff));
  }
  return;
}


